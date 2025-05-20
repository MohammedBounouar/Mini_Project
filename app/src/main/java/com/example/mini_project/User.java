package com.example.mini_project;

public class User {
    private int id;
    private String name;
    private String email;
    //private String phone;
    private String password;
    private String role;

    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        //this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // getters...
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    //public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
