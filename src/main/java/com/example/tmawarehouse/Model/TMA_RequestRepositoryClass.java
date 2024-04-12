package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.Item;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TMA_RequestRepositoryClass implements TMA_RequestRepository{
    private final DBHelper dbHelper;

    public TMA_RequestRepositoryClass(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public PreparedStatement insertNewRequest(PreparedStatement preparedStatement, Item selectedItem, int id, String unit,
                                              int quantity, double price, String comment) {
        try {
            String query = "INSERT INTO TMA_REQUESTS(EmployeeID, ItemID, UnitOfMeasurement, Quantity, PriceWithoutVAT, \"COMMENT\", Status) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = dbHelper.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, selectedItem.getItemID());
            preparedStatement.setString(3, unit);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, comment);
            preparedStatement.setString(7, "New");
            return preparedStatement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
