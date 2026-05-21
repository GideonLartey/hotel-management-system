package com.lantel.admin;

/**
 * Manages hotel inventory (supplies, linens, toiletries)
 */
public class RoomInventory {
    
    private int linens;
    private int toiletries;
    private int supplies;
    private int towels;
    
    public RoomInventory(int initialLinens, int initialToiletries, int initialSupplies, int initialTowels) {
        this.linens = initialLinens;
        this.toiletries = initialToiletries;
        this.supplies = initialSupplies;
        this.towels = initialTowels;
    }

    public void addLinens(int quantity) {
        linens += quantity;
        System.out.println("Added " + quantity + " linens. Total: " + linens);
    }

    public void useLinens(int quantity) {
        if (linens >= quantity) {
            linens -= quantity;
            System.out.println("Used " + quantity + " linens. Remaining: " + linens);
        } else {
            System.out.println("Insufficient linens!");
        }
    }

    public void addToiletries(int quantity) {
        toiletries += quantity;
        System.out.println("Added " + quantity + " toiletries. Total: " + toiletries);
    }

    public void useToiletries(int quantity) {
        if (toiletries >= quantity) {
            toiletries -= quantity;
            System.out.println("Used " + quantity + " toiletries. Remaining: " + toiletries);
        } else {
            System.out.println("Insufficient toiletries!");
        }
    }

    public void addSupplies(int quantity) {
        supplies += quantity;
        System.out.println("Added " + quantity + " supplies. Total: " + supplies);
    }

    public void useSupplies(int quantity) {
        if (supplies >= quantity) {
            supplies -= quantity;
            System.out.println("Used " + quantity + " supplies. Remaining: " + supplies);
        } else {
            System.out.println("Insufficient supplies!");
        }
    }

    public void addTowels(int quantity) {
        towels += quantity;
        System.out.println("Added " + quantity + " towels. Total: " + towels);
    }

    public void useTowels(int quantity) {
        if (towels >= quantity) {
            towels -= quantity;
            System.out.println("Used " + quantity + " towels. Remaining: " + towels);
        } else {
            System.out.println("Insufficient towels!");
        }
    }

    public void displayInventory() {
        System.out.println("\n========== INVENTORY SUMMARY ==========");
        System.out.printf("Linens: %d units%n", linens);
        System.out.printf("Toiletries: %d units%n", toiletries);
        System.out.printf("Supplies: %d units%n", supplies);
        System.out.printf("Towels: %d units%n", towels);
        System.out.println("=====================================");
        
        // Low stock warnings
        if (linens < 50) System.out.println("⚠ Low stock: Linens");
        if (toiletries < 30) System.out.println("⚠ Low stock: Toiletries");
        if (supplies < 40) System.out.println("⚠ Low stock: Supplies");
        if (towels < 60) System.out.println("⚠ Low stock: Towels");
    }

    public int getLinens() { return linens; }
    public int getToiletries() { return toiletries; }
    public int getSupplies() { return supplies; }
    public int getTowels() { return towels; }
}
