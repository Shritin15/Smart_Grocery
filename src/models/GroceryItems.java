package models;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class GroceryItems {
    private int id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    private String username; // new field to hold the creator's username


    // Constructor with ID and username (used when loading from DB)
    public GroceryItems(int id, String name, int quantity, LocalDate expiryDate, String username) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.username = username;
    }


    // Constructor without ID or username (used when inserting new items)
    public GroceryItems(String name, int quantity, LocalDate expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public int getQuantity() {
        return quantity;
    }


    public LocalDate getExpiryDate() {
        return expiryDate;
    }


    public String getUsername() {
        return username;
    }


    public int getDaysLeft() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }


    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }


    public String getStatus() {
        if (isExpired()) {
            return "Expired";
        } else if (getDaysLeft() <= 3) {
            return "Expiring Soon";
        } else {
            return "Fresh";
        }
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}





