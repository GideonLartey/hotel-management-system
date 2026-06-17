package com.lantel.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.lantel.admin.AdminUser;
import com.lantel.admin.FinancialReport;
import com.lantel.admin.HousekeepingTracker;
import com.lantel.admin.RoomInventory;
import com.lantel.admin.RoomRateManager;
import com.lantel.admin.StaffManager;
import com.lantel.booking.Booking;
import com.lantel.booking.BookingList;
import com.lantel.booking.BookingManager;
import com.lantel.guest.Guest;
import com.lantel.guest.GuestBook;
import com.lantel.room.GuestRoom;
import com.lantel.room.GuestRoomStatus;
import com.lantel.room.GuestType;
import com.lantel.room.RoomType;
import com.lantel.room.SeasonType;

public class HotelManagementCLI {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Scanner scanner = new Scanner(System.in);

    private static final GuestBook guestBook = new GuestBook();
    private static final BookingList bookingList = new BookingList(guestBook, 0.0, 0.0);
    private static final List<GuestRoom> rooms = new ArrayList<>();
    private static final StaffManager staffManager = new StaffManager();
    private static final RoomRateManager rateManager = new RoomRateManager();
    private static final HousekeepingTracker housekeepingTracker = new HousekeepingTracker();
    private static final RoomInventory inventory = new RoomInventory(200, 150, 100, 250);
    private static AdminUser currentAdmin = null;

    public static void main(String[] args) {
        initializeRooms();
        System.out.println("==========================================");
        System.out.println("   Welcome to Lantel Hotel Management     ");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            if (currentAdmin == null) {
                System.out.println("\n========== LOGIN MENU ==========");
                System.out.println("  1. Guest Portal");
                System.out.println("  2. Admin Login");
                System.out.println("  0. Exit");
                System.out.println("================================");
                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1 -> guestPortal();
                    case 2 -> adminLogin();
                    case 0 -> {
                        System.out.println("Thank you for using Lantel HMS. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } else {
                printMainMenu();
                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1 -> adminDashboard();
                    case 2 -> staffManagement();
                    case 3 -> roomManagement();
                    case 4 -> financialReports();
                    case 5 -> housekeepingSection();
                    case 6 -> inventoryManagement();
                    case 7 -> guestPortal();
                    case 0 -> {
                        System.out.println("Logging out...");
                        staffManager.logout();
                        currentAdmin = null;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private static void adminLogin() {
        System.out.println("\n========== ADMIN LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (staffManager.login(username, password)) {
            currentAdmin = staffManager.getLoggedInUser();
            System.out.println("\n✔ Login successful! Welcome, " + currentAdmin.getFullName());
        } else {
            System.out.println("\n✗ Invalid credentials. Please try again.");
        }
    }

    private static void guestPortal() {
        System.out.println("\n========== GUEST PORTAL ==========");
        boolean inPortal = true;
        while (inPortal) {
            System.out.println("\n========== GUEST MENU ==========");
            System.out.println("  1. Make a Reservation");
            System.out.println("  2. View All Bookings");
            System.out.println("  3. Search Available Rooms");
            System.out.println("  4. View Guest Book");
            System.out.println("  5. Check Out");
            System.out.println("  0. Back");
            System.out.println("================================");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> makeReservation();
                case 2 -> viewAllBookings();
                case 3 -> searchAvailableRooms();
                case 4 -> viewGuestBook();
                case 5 -> checkOutGuest();
                case 0 -> inPortal = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void adminDashboard() {
        System.out.println("\n========== ADMIN DASHBOARD ==========");
        System.out.printf("Logged in as: %s (%s)%n", currentAdmin.getFullName(), currentAdmin.getRole());
        System.out.printf("Total Bookings: %d%n", bookingList.getBookingList().size());
        System.out.printf("Total Guests: %d%n", guestBook.size());
        System.out.printf("Total Rooms: %d%n", rooms.size());
        System.out.printf("Available Rooms: %d%n", (int) rooms.stream().filter(r -> r.getRoomStatus() == GuestRoomStatus.AVAILABLE).count());
    }

    private static void staffManagement() {
        if (!currentAdmin.getRole().equals("Manager")) {
            System.out.println("✗ You don't have permission to manage staff.");
            return;
        }
        
        boolean inStaffMenu = true;
        while (inStaffMenu) {
            System.out.println("\n========== STAFF MANAGEMENT ==========");
            System.out.println("  1. View All Staff");
            System.out.println("  2. Add New Staff");
            System.out.println("  3. Deactivate Staff");
            System.out.println("  4. Activate Staff");
            System.out.println("  0. Back");
            System.out.println("======================================");
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1 -> staffManager.displayAllStaff();
                case 2 -> {
                    System.out.println("\n--- Add New Staff ---");
                    System.out.print("Admin ID: ");
                    String id = scanner.nextLine().trim();
                    System.out.print("Username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();
                    System.out.print("Full Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.println("Role: 1=Manager  2=Receptionist  3=Housekeeper");
                    int roleChoice = readInt("Select: ");
                    String role = switch (roleChoice) {
                        case 1 -> "Manager";
                        case 2 -> "Receptionist";
                        default -> "Housekeeper";
                    };
                    
                    if (staffManager.addStaff(id, username, password, name, role)) {
                        System.out.println("✔ Staff added successfully!");
                    } else {
                        System.out.println("✗ Username already exists.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Admin ID to deactivate: ");
                    String deactId = scanner.nextLine().trim();
                    staffManager.deactivateStaff(deactId);
                }
                case 4 -> {
                    System.out.print("Enter Admin ID to activate: ");
                    String actId = scanner.nextLine().trim();
                    staffManager.activateStaff(actId);
                }
                case 0 -> inStaffMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void roomManagement() {
        boolean inRoomMenu = true;
        while (inRoomMenu) {
            System.out.println("\n========== ROOM MANAGEMENT ==========");
            System.out.println("  1. View All Rooms");
            System.out.println("  2. View Room Rates");
            System.out.println("  3. Update Room Rate");
            System.out.println("  4. Add New Room");
            System.out.println("  5. View Room Status");
            System.out.println("  0. Back");
            System.out.println("====================================");
            int choice = readInt("Enter choice: ");
            RoomType[] types = RoomType.values();
            
            switch (choice) {
                case 1 -> displayAllRooms();
                case 2 -> rateManager.displayAllRates();
                case 3 -> {
                    System.out.println("Room Types:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("  %d. %s%n", i + 1, types[i]);
                    }
                    int typeChoice = readInt("Select Room Type: ") - 1;
                    if (typeChoice < 0 || typeChoice >= types.length) typeChoice = 0;
                    RoomType roomType = types[typeChoice];
                    
                    System.out.println("Season: 1=HIGH_DEMAND  2=SLOW_BUSINESS  3=SPECIAL");
                    int seasonChoice = readInt("Select Season: ");
                    SeasonType season = seasonChoice == 2 ? SeasonType.SLOW_BUSINESS_DAYS : (seasonChoice == 3 ? SeasonType.SPECIAL : SeasonType.HIGH_DEMAND);
                    
                    double newRate = readDouble("Enter new rate: ");
                    rateManager.updateRate(roomType, season, newRate);
                }
                case 4 -> {
                    System.out.println("Room Types:");
                    for (int i = 0; i < types.length; i++) {
                        System.out.printf("  %d. %s%n", i + 1, types[i]);
                    }
                    int typeChoice = readInt("Select Room Type: ") - 1;
                    if (typeChoice < 0 || typeChoice >= types.length) typeChoice = 0;
                    int newRoomId = (rooms.isEmpty() ? 101 : rooms.get(rooms.size() - 1).getRoomID() + 1);
                    rooms.add(new GuestRoom(newRoomId, types[typeChoice], GuestRoomStatus.AVAILABLE));
                    System.out.println("✔ Room #" + newRoomId + " added successfully!");
                }
                case 5 -> displayRoomStatus();
                case 0 -> inRoomMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void financialReports() {
        System.out.println("\n========== FINANCIAL REPORTS ==========");
        FinancialReport report = new FinancialReport(bookingList.getBookingList());
        report.displayFinancialSummary(rooms.size());
    }

    private static void housekeepingSection() {
        if (!currentAdmin.getRole().equals("Housekeeper") && !currentAdmin.getRole().equals("Manager")) {
            System.out.println("✗ You don't have permission to access housekeeping.");
            return;
        }
        
        boolean inHKMenu = true;
        while (inHKMenu) {
            System.out.println("\n========== HOUSEKEEPING ==========");
            System.out.println("  1. Record Cleaning");
            System.out.println("  2. View Cleaning Log");
            System.out.println("  3. Mark Room for Cleaning");
            System.out.println("  0. Back");
            System.out.println("==================================");
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Room ID: ");
                    int roomId = readInt("");
                    System.out.print("Housekeeper Name: ");
                    String hkName = scanner.nextLine().trim();
                    System.out.print("Notes: ");
                    String notes = scanner.nextLine().trim();
                    housekeepingTracker.recordCleaning(roomId, hkName, notes);
                }
                case 2 -> housekeepingTracker.displayCleaningLog();
                case 3 -> {
                    System.out.print("Enter Room ID: ");
                    int markId = readInt("");
                    housekeepingTracker.markAsNeedsCleaning(markId);
                }
                case 0 -> inHKMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void inventoryManagement() {
        if (!currentAdmin.getRole().equals("Manager")) {
            System.out.println("✗ You don't have permission to manage inventory.");
            return;
        }
        
        boolean inInventoryMenu = true;
        while (inInventoryMenu) {
            System.out.println("\n========== INVENTORY MANAGEMENT ==========");
            System.out.println("  1. View Inventory");
            System.out.println("  2. Add Linens");
            System.out.println("  3. Add Toiletries");
            System.out.println("  4. Add Supplies");
            System.out.println("  5. Add Towels");
            System.out.println("  0. Back");
            System.out.println("==========================================");
            int choice = readInt("Enter choice: ");
            
            switch (choice) {
                case 1 -> inventory.displayInventory();
                case 2 -> {
                    int linens = readInt("Enter quantity of linens: ");
                    inventory.addLinens(linens);
                }
                case 3 -> {
                    int toiletries = readInt("Enter quantity of toiletries: ");
                    inventory.addToiletries(toiletries);
                }
                case 4 -> {
                    int supplies = readInt("Enter quantity of supplies: ");
                    inventory.addSupplies(supplies);
                }
                case 5 -> {
                    int towels = readInt("Enter quantity of towels: ");
                    inventory.addTowels(towels);
                }
                case 0 -> inInventoryMenu = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void displayAllRooms() {
        System.out.println("\n========== ALL ROOMS ==========");
        for (GuestRoom room : rooms) {
            System.out.printf("Room #%d | Type: %-20s | Status: %s%n",
                room.getRoomID(), room.getRoomType(), room.getRoomStatus());
        }
    }

    private static void displayRoomStatus() {
        System.out.println("\n========== ROOM STATUS SUMMARY ==========");
        long available = rooms.stream().filter(r -> r.getRoomStatus() == GuestRoomStatus.AVAILABLE).count();
        long occupied = rooms.stream().filter(r -> r.getRoomStatus() == GuestRoomStatus.OCCUPIED).count();
        long maintenance = rooms.stream().filter(r -> r.getRoomStatus() == GuestRoomStatus.UNDER_MAINTENANCE).count();
        
        System.out.printf("Available: %d%n", available);
        System.out.printf("Occupied: %d%n", occupied);
        System.out.printf("Maintenance: %d%n", maintenance);
        System.out.printf("Total: %d%n", rooms.size());
    }

    private static void printMainMenu() {
        System.out.println("\n========== ADMIN MENU ==========");
        System.out.println("  1. Admin Dashboard");
        System.out.println("  2. Staff Management");
        System.out.println("  3. Room Management");
        System.out.println("  4. Financial Reports");
        System.out.println("  5. Housekeeping");
        System.out.println("  6. Inventory Management");
        System.out.println("  7. Guest Portal");
        System.out.println("  0. Logout");
        System.out.println("================================");
    }

    private static void makeReservation() {
        System.out.println("\n--- New Reservation ---");
        System.out.print("Guest Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Guest ID (passport/national ID): ");
        String guestID = scanner.nextLine().trim();

        int age = readInt("Guest Age: ");
        long phone = readLong("Guest Phone Number: ");

        System.out.println("Guest Type: 1=REGULAR  2=VVIP");
        GuestType guestType = readInt("Choose: ") == 2 ? GuestType.VVIP_GUEST : GuestType.REGULAR_GUEST;

        RoomType roomType = selectRoomType();
        SeasonType season = selectSeasonType();

        LocalDate checkIn = readDate("Check-In Date (yyyy-MM-dd): ");
        LocalDate checkOut = readDate("Check-Out Date (yyyy-MM-dd): ");

        String receptionistID = readLine("Receptionist ID: ");

        try {
            BookingManager manager = new BookingManager(bookingList, receptionistID, null, roomType, 0.15, 20.0, 0.0, 0.0);
            manager.calculateAmountToPay(checkIn, checkOut, guestType, season);
            double due = manager.getAmountToPay();

            System.out.printf("%nTotal Amount Due: GH₵ %.2f%n", due);
            double payment = readDouble("Enter Payment Amount: ");

            manager.verifyIdentification(new Booking(guestID, age, phone, name, checkIn, checkOut,
                java.time.LocalTime.of(14, 0), java.time.LocalTime.of(11, 0),
                due, 0, (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut),
                guestType, season, roomType, GuestRoomStatus.AVAILABLE));

            manager.makePayments(payment);
            manager.checkInConfirmation();

            if (manager.isAddedToBookingList()) {
                manager.createANewReservation(name, age, phone, guestID, checkIn, checkOut, due, 0,
                    (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut),
                    guestType, season, roomType, GuestRoomStatus.AVAILABLE);

                guestBook.addGuest(new Guest(guestID, name, age, phone));
                System.out.println("\n✔ Reservation confirmed! Booking #: " + manager.getBookingNumber());
            }

        } catch (Exception e) {
            System.err.println("Reservation failed: " + e.getMessage());
        }
    }

    private static void viewAllBookings() {
        List<Booking> list = bookingList.getBookingList();
        if (list.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }
        bookingList.displayBookings(list);
    }

    private static void searchAvailableRooms() {
        System.out.println("\n--- Search Available Rooms ---");
        RoomType roomType = selectRoomType();
        LocalDate checkIn = readDate("Desired Check-In (yyyy-MM-dd): ");
        LocalDate checkOut = readDate("Desired Check-Out (yyyy-MM-dd): ");

        List<GuestRoom> available = BookingManager.findAvailableRooms(
            rooms, bookingList.getBookingList(), roomType, GuestRoomStatus.AVAILABLE, checkIn, checkOut);

        if (available.isEmpty()) {
            System.out.println("No available rooms found for the selected dates and type.");
        } else {
            System.out.println("\nAvailable Rooms:");
            for (GuestRoom r : available) {
                System.out.printf("  Room #%d | Type: %-20s | Status: %s%n",
                    r.getRoomID(), r.getRoomType(), r.getRoomStatus());
            }
        }
    }

    private static void viewGuestBook() {
        guestBook.displayGuests();
    }

    private static void checkOutGuest() {
        System.out.println("\n--- Guest Check-Out ---");
        System.out.print("Enter Guest ID to check out: ");
        String guestID = scanner.nextLine().trim();

        if (guestBook.hasGuest(guestID)) {
            // Mark the booking as checked out so revenue reports are accurate
            for (Booking b : bookingList.getBookingList()) {
                if (b.getGuestID().equals(guestID) && !b.hasCheckedOut()) {
                    b.setCheckedOut(true);
                    break;
                }
            }
            guestBook.removeGuest(guestID);
            System.out.println("Guest " + guestID + " has been successfully checked out. Thank you for staying!");
        } else {
            System.out.println("Guest ID not found in the system.");
        }
    }

    // ---- Helpers ----

    private static RoomType selectRoomType() {
        System.out.println("Room Types:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d. %-25s GH₵ %.2f/night%n", i + 1, types[i], types[i].getCostPerNight());
        }
        int choice = readInt("Select Room Type: ") - 1;
        if (choice < 0 || choice >= types.length) choice = 0;
        return types[choice];
    }

    private static SeasonType selectSeasonType() {
        System.out.println("Season: 1=HIGH_DEMAND  2=SLOW_BUSINESS  3=SPECIAL");
        int choice = readInt("Select Season: ");
        return switch (choice) {
            case 2 -> SeasonType.SLOW_BUSINESS_DAYS;
            case 3 -> SeasonType.SPECIAL;
            default -> SeasonType.HIGH_DEMAND;
        };
    }

    private static void initializeRooms() {
        int id = 101;
        for (RoomType type : RoomType.values()) {
            for (int i = 0; i < 5; i++) {
                rooms.add(new GuestRoom(id++, type, GuestRoomStatus.AVAILABLE));
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid phone number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use yyyy-MM-dd (e.g. 2025-12-25).");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
