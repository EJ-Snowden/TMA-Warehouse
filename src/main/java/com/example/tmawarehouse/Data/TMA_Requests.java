package com.example.tmawarehouse.Data;


import com.example.tmawarehouse.MainApplication;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TMA_Requests {
    int requestID;
    int employeeID;
    int itemID;
    String unitOfMeasurements;
    int quantity;
    double priceWithoutVAT;
    String comment;
    String status;
    String itemName;
    String empName;

    public TMA_Requests(int requestID, int employeeID, int itemID, String unitOfMeasurements, int quantity,
                        double priceWithoutVAT, String comment, String status) {
        this.requestID = requestID;
        this.employeeID = employeeID;
        this.itemID = itemID;
        this.unitOfMeasurements = unitOfMeasurements;
        this.quantity = quantity;
        this.priceWithoutVAT = priceWithoutVAT;
        this.comment = comment;
        this.status = status;

        setItemName();
        setEmpName();
    }

    public void setItemName(){
        try {
            if (MainApplication.getDBHelper().getConnection() != null) {
                String query = "SELECT ITEMNAME FROM ITEMS WHERE ITEMID = ?";
                PreparedStatement statement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
                statement.setInt(1, this.itemID);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.itemName = rs.getString("ITEMNAME");
                }

                statement.close();
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEmpName(){
        try {
            if (MainApplication.getDBHelper().getConnection() != null) {
                String query = "SELECT EMPNAME FROM EMP WHERE EMPID = ?";
                PreparedStatement statement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
                statement.setInt(1, this.employeeID);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.empName = rs.getString("EMPNAME");
                }

                statement.close();
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getUnitOfMeasurements() {
        return unitOfMeasurements;
    }

    public void setUnitOfMeasurements(String unitOfMeasurements) {
        this.unitOfMeasurements = unitOfMeasurements;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceWithoutVAT() {
        return priceWithoutVAT;
    }

    public void setPriceWithoutVAT(double priceWithoutVAT) {
        this.priceWithoutVAT = priceWithoutVAT;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public String getEmpName() {
        return empName;
    }
}
