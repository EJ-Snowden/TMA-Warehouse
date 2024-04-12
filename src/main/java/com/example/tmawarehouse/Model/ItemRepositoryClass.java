package com.example.tmawarehouse.Model;

import com.example.tmawarehouse.Data.DialogData;
import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import javafx.scene.control.TableView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemRepositoryClass implements ItemRepository {
    private final DBHelper dbHelper;

    public ItemRepositoryClass(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public PreparedStatement insertNewItem(DialogData dialogData, PreparedStatement preparedStatement) {
        String query = "INSERT INTO Items(ItemName, ItemGroup, UnitOfMeasurement, Quantity, PriceWithoutVAT, Status, StorageLocation, ContactPerson) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = dbHelper.getConnection().prepareStatement(query);
            preparedStatement.setString(1, dialogData.itemNameField.getText());
            preparedStatement.setString(2, dialogData.groupNameField.getText());
            preparedStatement.setString(3, String.valueOf(dialogData.unitOfMeasurementComboBox.getValue()));
            preparedStatement.setInt(4, Integer.parseInt(dialogData.quantityField.getText()));
            preparedStatement.setDouble(5, Double.parseDouble(dialogData.priceWithoutVATField.getText()));
            preparedStatement.setString(6, dialogData.statusField.getText());
            preparedStatement.setString(7, dialogData.storageLocationField.getText());
            preparedStatement.setString(8, dialogData.contactPersonField.getText());
            return preparedStatement;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return preparedStatement;
    }

    public void execStatement(PreparedStatement preparedStatement){
        try {
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int selectItemId(DialogData dialogData, Statement statement) {
        try {
            String query = "SELECT MAX(ItemID) FROM ITEMS";
            statement = dbHelper.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);
            int itemID = 0;
            while (rs.next()){
                itemID = rs.getInt(1);
            }
            rs.close();
            statement.close();
            return itemID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateQuantity(PreparedStatement preparedStatement, TMA_Requests selectedRequest) {
        try {
            String query = "UPDATE Items SET Quantity = Quantity - ? WHERE ITEMID = ?";
            preparedStatement = dbHelper.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, selectedRequest.getQuantity());
            preparedStatement.setInt(2, selectedRequest.getItemID());
            execStatement(preparedStatement);

            String updateRequestQuery = "UPDATE TMA_REQUESTS SET Status = 'Approved' WHERE RequestID = ?";
            preparedStatement = dbHelper.getConnection().prepareStatement(updateRequestQuery);
            preparedStatement.setInt(1, selectedRequest.getRequestID());
            execStatement(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRejectStatus(PreparedStatement preparedStatement, TMA_Requests selectedRequest, String reason) {
        try {
            String updateRequestQuery = "UPDATE TMA_REQUESTS SET Status = 'Rejected', \"COMMENT\" = ? WHERE RequestID = ?";
            preparedStatement = dbHelper.getConnection().prepareStatement(updateRequestQuery);
            preparedStatement.setString(1, reason);
            preparedStatement.setInt(2, selectedRequest.getRequestID());
            execStatement(preparedStatement);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int itemId, PreparedStatement preparedStatement, TableView<Item> itemTableView) {
        try {
            String query = "DELETE FROM Items WHERE ITEMID = ?";
            preparedStatement = dbHelper.getConnection().prepareStatement(query);
            int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();
            preparedStatement.setInt(1, index);

            execStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
