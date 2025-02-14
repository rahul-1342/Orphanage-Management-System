package com.example.orphanage;

public class InventoryItem {
    private String id;
    private String name;
    private int quantity;

    // Default constructor (required for Firebase)
    public InventoryItem() {
    }

    // Parameterized constructor
    public InventoryItem(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters (optional, if you need to update data)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
