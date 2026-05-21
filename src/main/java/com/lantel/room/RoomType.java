package com.lantel.room;

// adding random prices for each room type. These can be adjusted as needed
public enum RoomType {
    STANDARD(80.0),
    DELUXE(150.0),
    EXECUTIVE_SUITE(300.0),
    VIP_SUITE(500.0),
    VVIP_PENTHOUSE_SUITE(1000.0);

    private double costPerNight;

    RoomType(double costPerNight) {
        this.costPerNight = costPerNight;
    }

    public double getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(double costPerNight) {
        if (costPerNight < 0) throw new IllegalArgumentException("Cost per night cannot be negative.");
        this.costPerNight = costPerNight;
    }
}
