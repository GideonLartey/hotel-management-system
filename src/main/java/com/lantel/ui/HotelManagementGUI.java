package com.lantel.ui;

import com.lantel.admin.StaffManager;
import com.lantel.room.RoomType;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Hotel Management System - JavaFX GUI Application
 */
public class HotelManagementGUI extends Application {
    
    private Stage primaryStage;
    private StaffManager staffManager;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        // Initialize business logic
        this.staffManager = new StaffManager();
        
        // Set window icon
        try {
            Image icon = new Image(
                getClass().getResourceAsStream("/images/app-icon.png")
            );
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + e.getMessage());
        }
        
        primaryStage.setTitle("Hotel Management System");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        
        showMainMenu();
        
        primaryStage.show();
    }
    
    private void showMainMenu() {
        // Root container with background support
        StackPane root = new StackPane();
        root.setPrefSize(900, 700);

        // Main content container
        VBox contentBox = new VBox(20);
        applyBackground(root);
        contentBox.setPadding(new Insets(40));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-font-size: 14;");
       
         
        // Background Image
        try {
            Image bgImage = new Image(
                getClass().getResource("/images/hotel-sunset.png").toExternalForm()
            );
            
            BackgroundImage backgroundImage = new BackgroundImage(
                bgImage, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                new BackgroundSize(
                    100,
                    100,
                    true,
                    true,
                    false,
                    true   
                )
            );

            root.setBackground(new Background(backgroundImage));
            
        } catch (Exception e) {
            System.err.println("Background image not found: " + e.getMessage());
        }

               
        // Logo and Header
        VBox logoBox = new VBox(10);
        logoBox.setAlignment(Pos.CENTER);
        
        try {
            Image logoImage = new Image(
                getClass().getResourceAsStream("/images/logo.png")
            );

            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(130);
            logoView.setFitHeight(130);
            logoView.setPreserveRatio(true);
            logoView.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.45), 20, 0.6, 0, 0), " +
                "dropshadow(gaussian, rgba(0,0,0,0.65), 10, 0.35, 0, 0);"
            );
            logoBox.getChildren().add(logoView);
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }
        
        // Title
        Label title = new Label("HOTEL MANAGEMENT SYSTEM");
        title.setStyle(
            "-fx-font-size: 32;" + 
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );
        
        Label subtitle = new Label("Welcome to Lantel Hotel");
        subtitle.setStyle(
            "-fx-font-size: 20;" + 
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #F5F5F5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 6, 0.5, 2, 2);"
        );
        
        // Buttons
        Button guestPortalBtn = createButton("Guest Portal", 200, 60);
        guestPortalBtn.setOnAction(e -> showGuestPortal());
        
        Button adminDashboardBtn = createButton("Admin Dashboard", 200, 60);
        adminDashboardBtn.setOnAction(e -> showAdminLogin());
        
        Button exitBtn = createButton("Exit", 200, 60);
        exitBtn.setStyle(exitBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #cc0000;");
        exitBtn.setOnAction(e -> System.exit(0));
        
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
            guestPortalBtn, 
            adminDashboardBtn, 
            exitBtn
        );
        
        contentBox.getChildren().addAll(
            logoBox, 
            title, 
            subtitle, 
            buttonBox
        );

        Label footer = new Label("© 2025 Lantel Hotel Management Suite/DeonLondn");
        footer.setStyle(
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
        );

        StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);
        footer.setTranslateY(-25);
        
        root.getChildren().addAll(contentBox, footer);

        contentBox.setOpacity(0);

        FadeTransition fade = new FadeTransition(
            Duration.seconds(1.2),
            contentBox
        );

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showGuestPortal() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        // Header
        HBox header = createHeader("Guest Portal");
        
        // Menu
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_CENTER);
        
        Label menuLabel = new Label("Guest Menu");
        menuLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 20;" +
            "-fx-font-weight: bold;"
        );
        
        Button searchRoomsBtn = createButton("Search Available Rooms", 250, 50);
        searchRoomsBtn.setOnAction(e -> showSearchRooms());
        
        Button viewBookingsBtn = createButton("View Bookings", 250, 50);
        viewBookingsBtn.setOnAction(e -> showGuestBookings());
        
        Button checkinBtn = createButton("Check-in", 250, 50);
        checkinBtn.setOnAction(e -> showCheckIn());
        
        Button checkoutBtn = createButton("Check-out", 250, 50);
        checkoutBtn.setOnAction(e -> showCheckOut());
        
        Button backBtn = createButton("Back to Main Menu", 250, 50);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showMainMenu());
        
        menuBox.getChildren().addAll(
            menuLabel, 
            searchRoomsBtn, 
            viewBookingsBtn, 
            checkinBtn, 
            checkoutBtn, 
            backBtn
        );
        
        root.getChildren().addAll(header, menuBox);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showSearchRooms() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Search Available Rooms");
        root.getChildren().add(header);
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));
        
        Label checkInLabel = new Label("Check-in Date (YYYY-MM-DD):");
        styleLabel(checkInLabel);
        TextField checkInField = new TextField();
        form.add(checkInLabel, 0, 0);
        form.add(checkInField, 1, 0);
        
        Label checkOutLabel = new Label("Check-out Date (YYYY-MM-DD):");
        styleLabel(checkOutLabel);
        TextField checkOutField = new TextField();
        form.add(checkOutLabel, 0, 1);
        form.add(checkOutField, 1, 1);
        
        Label roomTypeLabel = new Label("Room Type:");
        styleLabel(roomTypeLabel);
        ComboBox<RoomType> roomTypeCombo = new ComboBox<>();
        roomTypeCombo.getItems().addAll(RoomType.values());
        form.add(roomTypeLabel, 0, 2);
        form.add(roomTypeCombo, 1, 2);
        
        TextArea resultsArea = new TextArea();
        resultsArea.setWrapText(true);
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(250);
        
        Button searchBtn = createButton("Search", 100, 40);
        searchBtn.setOnAction(e -> {
            String result = "Searching for rooms...\n";
            result += "Check-in: " + checkInField.getText() + "\n";
            result += "Check-out: " + checkOutField.getText() + "\n";
            result += "Room Type: " + (roomTypeCombo.getValue() != null ? roomTypeCombo.getValue() : "Not selected");
            resultsArea.setText(result);
        });
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showGuestPortal());
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(searchBtn, backBtn);

        Label resultsLabel = new Label("Search Results:");
        styleLabel(resultsLabel);

        root.getChildren().addAll(
            form, 
            resultsLabel, 
            resultsArea, 
            buttonBox
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showGuestBookings() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("View Bookings");
        root.getChildren().add(header);
        
        TextArea bookingsArea = new TextArea();
        bookingsArea.setText("No bookings found.\n\nMake a reservation to see your bookings here.");
        bookingsArea.setWrapText(true);
        bookingsArea.setEditable(false);
        bookingsArea.setPrefHeight(300);
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showGuestPortal());
        
        root.getChildren().addAll(bookingsArea, backBtn);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showCheckIn() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Check-in");
        root.getChildren().add(header);
        
        Label infoLabel = new Label("Check-in Time: 2:00 PM");
        styleLabel(infoLabel);
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));
        
        Label bookingIdLabel = new Label("Booking ID:");
        styleLabel(bookingIdLabel);
        TextField bookingIdField = new TextField();
        form.add(bookingIdLabel, 0, 0);
        form.add(bookingIdField, 1, 0);
        
        Label nameLabel = new Label("Guest Name:");
        styleLabel(nameLabel);
        TextField nameField = new TextField();
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);
        
        Button checkInBtn = createButton("Check-in", 100, 40);
        checkInBtn.setOnAction(e -> showAlert("Check-in", "Guest checked in successfully!"));
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showGuestPortal());
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(checkInBtn, backBtn);
        
        root.getChildren().addAll(
            infoLabel, 
            form, 
            buttonBox
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showCheckOut() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Check-out");
        root.getChildren().add(header);
        
        Label infoLabel = new Label("Check-out Time: 11:00 AM");
        styleLabel(infoLabel);
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));
        
        Label bookingIdLabel = new Label("Booking ID:");
        styleLabel(bookingIdLabel);
        TextField bookingIdField = new TextField();
        form.add(bookingIdLabel, 0, 0);
        form.add(bookingIdField, 1, 0);
        
        Button checkOutBtn = createButton("Check-out", 100, 40);
        checkOutBtn.setOnAction(e -> showAlert("Check-out", "Thank you for staying with us!"));
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showGuestPortal());
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(checkOutBtn, backBtn);
        
        root.getChildren().addAll(
            infoLabel,  
            form, 
            buttonBox
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showAdminLogin() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        
        Label title = new Label("Admin Dashboard Login");
        title.setStyle(
            "-fx-font-size: 24;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" 
            );

        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);
        
        Label usernameLabel = new Label("Username:");
        styleLabel(usernameLabel);
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(250);
        form.add(usernameLabel, 0, 0);
        form.add(usernameField, 1, 0);
        
        Label passwordLabel = new Label("Password:");
        styleLabel(passwordLabel);
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(250);
        form.add(passwordLabel, 0, 1);
        form.add(passwordField, 1, 1);
        
        Label infoLabel = new Label("Demo: Username-admin / Pass-admin123");
        styleLabel(infoLabel);
        form.add(infoLabel, 1, 2);
        
        Button loginBtn = createButton("Login", 100, 40);
        loginBtn.setDefaultButton(true);
        loginBtn.setOnAction(e -> {
            if (staffManager.login(usernameField.getText(), passwordField.getText())) {
                showAdminDashboard();
            } else {
                showAlert("Login Failed", "Invalid username or password!");
                passwordField.clear();
            }
        });
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showMainMenu());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, backBtn);
        
        root.getChildren().addAll(
            title,  
            form, 
            buttonBox
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showAdminDashboard() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Admin Dashboard - " + staffManager.getLoggedInUser().getFullName());
        root.getChildren().add(header);
        
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setMaxWidth(Double.MAX_VALUE);

        // Glass Effect Panel
        menuBox.setStyle(
            "-fx-background-color: rgba(0,0,0,0.05);" +
            "-fx-background-radius: 35;" +
            "-fx-padding: 22;" 
        );
        
        Label menuLabel = new Label("Admin Menu");
        menuLabel.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 20;" +
            "-fx-font-weight: bold;"
        );
        
        Button staffMgmtBtn = createButton("Staff Management", 250, 50);
        staffMgmtBtn.setOnAction(e -> showStaffManagement());
        
        Button roomMgmtBtn = createButton("Room Management", 250, 50);
        roomMgmtBtn.setOnAction(e -> showRoomManagement());
        
        Button financialBtn = createButton("Financial Reports", 250, 50);
        financialBtn.setOnAction(e -> showFinancialReports());
        
        Button housekeepingBtn = createButton("Housekeeping", 250, 50);
        housekeepingBtn.setOnAction(e -> showHousekeeping());
        
        Button logoutBtn = createButton("Logout", 250, 50);
        logoutBtn.setStyle(logoutBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #cc0000;");
        logoutBtn.setOnAction(e -> {
            staffManager.logout();
            showMainMenu();
        });
        
        menuBox.getChildren().addAll(
            menuLabel,
            staffMgmtBtn,
            roomMgmtBtn,
            financialBtn,
            housekeepingBtn,
            logoutBtn
        );
        
        ScrollPane scrollPane = new ScrollPane(menuBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );

        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {

                scrollPane.lookup(".viewport").setStyle(
                    "-fx-background-color: transparent;"
                );
            }
        });

        root.getChildren().add(scrollPane);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showStaffManagement() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Staff Management");
        root.getChildren().add(header);
        
        TextArea staffArea = new TextArea();
        staffArea.setEditable(false);
        staffArea.setWrapText(true);
        staffArea.setPrefHeight(300);
        
        StringBuilder sb = new StringBuilder();
        sb.append("ID\t\tUsername\t\tFull Name\t\t\tRole\n");
        sb.append("================================================================================\n");
        
        for (com.lantel.admin.AdminUser user : staffManager.getStaffByRole("Manager")) {
            sb.append(String.format("%s\t%s\t%s\t%s\n", 
                user.getAdminID(), user.getUsername(), user.getFullName(), user.getRole()));
        }
        
        staffArea.setText(sb.toString());

        // Management Buttons
        Button addStaffBtn = createButton("Add Staff", 200, 40);
        addStaffBtn.setOnAction(e ->
            showAlert("Add Staff", "This feature is under development.")
        );

        Button assignRoleBtn = createButton("Assign Roles", 200, 40);
        assignRoleBtn.setOnAction(e ->
            showAlert("Assign Roles", "This feature is under development.")
        );

        Button attendanceBtn = createButton("Attendance Tracking", 200, 40);
        attendanceBtn.setOnAction(e ->
            showAlert("Attendance Tracking", "This feature is under development.")
        );

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);

        actionBox.getChildren().addAll(
            addStaffBtn,
            assignRoleBtn,
            attendanceBtn
        );
        
        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showAdminDashboard());
        
        Label staffLabel = new Label("Current Staff:");
        styleLabel(staffLabel);
        root.getChildren().addAll(
            staffLabel, 
            staffArea, 
            actionBox,
            backBtn
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showRoomManagement() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Room Management");
        root.getChildren().add(header);
        
        TextArea roomArea = new TextArea();
        roomArea.setText("Room Management\n\nManage all rooms and their availability status.");
        roomArea.setWrapText(true);
        roomArea.setEditable(false);
        roomArea.setPrefHeight(300);

        // Room Management Buttons
        Button addRoomBtn = createButton("Add Room", 200, 40);
        addRoomBtn.setOnAction(e ->
            showAlert("Add Room", "This feature is under development.")
        );

        Button availableRoomsBtn = createButton("Available Rooms", 200, 40);
        availableRoomsBtn.setOnAction(e ->
            showAlert("Available Rooms", "This feature is under development.")
        );

        Button maintenanceBtn = createButton("Maintenance Mode", 200, 40);
        maintenanceBtn.setOnAction(e ->
            showAlert("Maintenance Mode", "This feature is under development.")
        );

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);

        actionBox.getChildren().addAll(
            addRoomBtn,
            availableRoomsBtn,
            maintenanceBtn
        );

        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showAdminDashboard());
        
        root.getChildren().addAll(
            roomArea, 
            actionBox, 
            backBtn
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    

    private void showFinancialReports() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Financial Reports");
        root.getChildren().add(header);
        
        TextArea reportArea = new TextArea();
        reportArea.setText("Financial Reports\n\nTotal Revenue: $0\nTotal Expenses: $0\nNet Profit: $0");
        reportArea.setWrapText(true);
        reportArea.setEditable(false);
        reportArea.setPrefHeight(300);

        // Financial Report Buttons
        Button revenueBtn = createButton("Revenue Summary", 200, 40);
        revenueBtn.setOnAction(e ->
            showAlert("Revenue Summary", "This feature is under development.")
        );

        Button occupancyBtn = createButton("Occupancy Report", 200, 40);
        occupancyBtn.setOnAction(e ->
            showAlert("Occupancy Report", "This feature is under development.")
        );

        Button exportBtn = createButton("Export Report", 200, 40);
        exportBtn.setOnAction(e ->
            showAlert("Export Report", "This feature is under development.")
        );

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.getChildren().addAll(
            revenueBtn, 
            occupancyBtn, 
            exportBtn
        );

        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showAdminDashboard());
        
        root.getChildren().addAll(
            reportArea, 
            actionBox, 
            backBtn
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void showHousekeeping() {
        VBox root = new VBox(15);
        applyBackground(root);
        root.setPadding(new Insets(20));
        
        HBox header = createHeader("Housekeeping");
        root.getChildren().add(header);
        
        TextArea housekeepingArea = new TextArea();
        housekeepingArea.setText("Housekeeping Tasks\n\nNo pending tasks.");
        housekeepingArea.setWrapText(true);
        housekeepingArea.setEditable(false);
        housekeepingArea.setPrefHeight(300);

        // Housekeeping Buttons
        Button createTaskBtn = createButton("Create Cleaning Task", 220, 40);
        createTaskBtn.setOnAction(e -> 
            showAlert("Create Task", "This feature is under development.")
        );

        Button roomStatusBtn = createButton("Room Status", 220, 40);
        roomStatusBtn.setOnAction(e -> 
            showAlert("Room Status", "This feature is under development.")
        );

        Button maintenanceBtn = createButton("Maintenance Requests", 220, 40);
        maintenanceBtn.setOnAction(e -> 
            showAlert("Maintenance Requests", "This feature is under development.")
        );

        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER);

        actionBox.getChildren().addAll(
            createTaskBtn,
            roomStatusBtn,
            maintenanceBtn
        );

        Button backBtn = createButton("Back", 100, 40);
        backBtn.setStyle(backBtn.getStyle() + "; -fx-text-fill: white; -fx-base: #666;");
        backBtn.setOnAction(e -> showAdminDashboard());
        
        root.getChildren().addAll(
            housekeepingArea, 
            actionBox, 
            backBtn
        );
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    // Helper methods
    private HBox createHeader(String title) {
        HBox header = new HBox();
        header.setStyle(              
            "-fx-padding: 10 10 25 10;"
        );
        header.setPadding(new Insets(10));
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 20;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 5, 0.3, 1, 1);"
        );
        
        header.getChildren().add(titleLabel);
        
        return header;
    }


    private void styleLabel(Label label) {

        label.setStyle(
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 4, 0.3, 1, 1);"
        );
    }

    private void applyBackground(Pane pane) {

        try {
            Image bgImage = new Image(
                getClass().getResource("/images/hotel-sunset.png").toExternalForm()
            );

            BackgroundImage backgroundImage = new BackgroundImage(
                bgImage, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                new BackgroundSize(
                    100,
                    100,
                    true,
                    true,
                    false,
                    true   
                )
            );

            pane.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Background image not found: " + e.getMessage());
        }

    }

      
    private Button createButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(height);

        // Executive style
        button.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0,102,204,0.92);" +
            "-fx-background-radius: 14;" +
            "-fx-border-radius: 14;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 8, 0.3, 0, 3);"
                       
        );

        // Hover Effect
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.04);
            button.setScaleY(1.04);

            button.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: rgba(30,144,255,1);" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.25), 12, 0.4, 0, 0);"
            );
        });

        // Mouse Exit
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);

            button.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: rgba(0,102,204,0.92);" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 8, 0.3, 0, 3);"
            );
        });
        return button;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
