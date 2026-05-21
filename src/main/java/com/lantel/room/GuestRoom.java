package com.lantel.room;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model for a hotel room. Holds per-room state such as id, status, type and
 * last cleaned time.
 */
public class GuestRoom {
    
    private final int roomID;
    private GuestRoomStatus roomStatus;
    private final LocalDateTime lastCleaned;
    private boolean isRoomAvailableCurrently;
    private final RoomType roomType;

    public GuestRoom(int roomID, RoomType roomType, GuestRoomStatus roomStatus) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.roomStatus = roomStatus == null ? GuestRoomStatus.AVAILABLE : roomStatus;
        this.lastCleaned = LocalDateTime.now();
        this.isRoomAvailableCurrently = this.roomStatus == GuestRoomStatus.AVAILABLE;
    }

    public int getRoomID() {
        return roomID;
    }

    public GuestRoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(GuestRoomStatus roomStatus) {
        this.roomStatus = roomStatus;
        this.isRoomAvailableCurrently = roomStatus == GuestRoomStatus.AVAILABLE;
    }

    public LocalDateTime getLastCleaned() {
        return lastCleaned;
    }

    public boolean isRoomAvailableCurrently() {
        return isRoomAvailableCurrently;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, roomType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GuestRoom other = (GuestRoom) obj;
        return roomID == other.roomID;
    }

    @Override
    public String toString() {
        return "GuestRoom{" +
                "roomID=" + roomID +
                ", roomStatus=" + roomStatus +
                ", roomType=" + roomType +
                '}';
    }
}