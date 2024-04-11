package com.example.tmawarehouse.Data;

public class Item {
    private int itemID;
    private String itemName;
    private String groupName;
    private String unitOfMeasurement;
    private int quantity;
    private double priceWithoutVAT;
    private String status;
    private String storageLocation;
    private String contactPerson;

    public Item(int itemID, String itemName, String groupName, String unitOfMeasurement, int quantity, double priceWithoutVAT,
                String status, String storageLocation, String contactPerson) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.groupName = groupName;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
        this.priceWithoutVAT = priceWithoutVAT;
        this.status = status;
        this.storageLocation = storageLocation;
        this.contactPerson = contactPerson;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPriceWithoutVAT() {
        return priceWithoutVAT;
    }

    public void setPriceWithoutVAT(double priceWithoutVAT) {
        this.priceWithoutVAT = priceWithoutVAT;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}