package com.example.mini_project;

public class Reservation {
    private String userName;
    private String roomName;
    private String day;

    public Reservation(String userName, String roomName, String day) {
        this.userName = userName;
        this.roomName = roomName;
        this.day = day;
    }

    public String getUserName() { return userName; }
    public String getRoomName() { return roomName; }
    public String getDay() { return day; }
}
