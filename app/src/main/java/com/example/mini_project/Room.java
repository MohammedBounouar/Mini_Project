package com.example.mini_project;

import java.util.List;

public class Room {
    private int id;
    private String name;
    private String size;
    private int capacity;
    private double price;
    private boolean isBooked;
    private List<String> features;

    private static int idCounter = 1;

    // Constructor without ID (auto-assigned for new entries before DB insert)
    public Room(String name, String size, int capacity, double price, boolean isBooked, List<String> features) {
        this.id = idCounter++;
        this.name = name;
        this.size = size;
        this.capacity = capacity;
        this.price = price;
        this.isBooked = isBooked;
        this.features = features;
    }


    public Room(int id, String name, String size, int capacity, double price, boolean isBooked, List<String> features) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.capacity = capacity;
        this.price = price;
        this.isBooked = isBooked;
        this.features = features;
    }


    public int getId() { return id; }

    public String getName() { return name; }

    public String getSize() { return size; }

    public int getCapacity() { return capacity; }

    public double getPrice() { return price; }

    public boolean isBooked() { return isBooked; }

    public List<String> getFeatures() { return features; }

    public void setName(String name) { this.name = name; }

    public void setSize(String size) { this.size = size; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void setPrice(double price) { this.price = price; }

    public void setBooked(boolean booked) { isBooked = booked; }

    public void setFeatures(List<String> features) { this.features = features; }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", isBooked=" + isBooked +
                ", features=" + features +
                '}';
    }
}
