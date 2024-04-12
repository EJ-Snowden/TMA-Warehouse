package com.example.tmawarehouse;

import com.example.tmawarehouse.Controllers.MainController;
import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.Data.UnitOfMeasurements;
import com.example.tmawarehouse.Model.DBHelper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javafx.scene.Parent;

public class MainApplication extends Application {

    static DBHelper dbHelper;
    static Statement statement;
    static PreparedStatement preparedStatement;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tmawarehouse/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();

        MainController controller = loader.getController();
        controller.setStage(stage);
    }
    public static void loadItems(ObservableList<Item> itemList) {
        try {
            statement = dbHelper.getConnection().createStatement();

            String query = "SELECT * FROM Items";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                itemList.add(new Item(rs.getInt("ItemID"), rs.getString("ItemName"), rs.getString("ItemGroup"),
                        rs.getString("UnitOfMeasurement"), rs.getInt("Quantity"), rs.getDouble("PriceWithoutVAT"),
                        rs.getString("Status"), rs.getString("StorageLocation"), rs.getString("ContactPerson")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadPurchaseRequests(ObservableList<TMA_Requests> requestList) {
        try {
            statement = dbHelper.getConnection().createStatement();

            String query = "SELECT * FROM TMA_REQUESTS";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){

                Item item = getItemDetails(rs.getInt("ItemID"));

                requestList.add(new TMA_Requests(rs.getInt("RequestID"), rs.getInt("EmployeeID"), rs.getInt("ItemID"),
                        rs.getString("UnitOfMeasurement"), rs.getInt("Quantity"), rs.getInt("Quantity") * item.getPriceWithoutVAT(),
                        rs.getString("COMMENT"), rs.getString("Status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadUnitOfMeasurements(ArrayList<UnitOfMeasurements> unitOfMeasurementsList) {
        try {
            String query = "SELECT * FROM UNITOFMEASUREMENTS";
            statement = MainApplication.getDBHelper().getConnection().createStatement();

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                unitOfMeasurementsList.add(new UnitOfMeasurements(rs.getInt("UnitID"), rs.getString("UnitName")));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static Item getItemDetails(int itemID){
        Item item = null;
        try {
            String query = "SELECT * FROM ITEMS WHERE ITEMID = ?";
            preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
            preparedStatement.setInt(1, itemID);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                item = new Item(itemID, rs.getString("ItemName"), rs.getString("ItemGroup"),
                        rs.getString("UnitOfMeasurement"), rs.getInt("Quantity"),
                        rs.getDouble("PriceWithoutVAT"), rs.getString("Status"),
                        rs.getString("StorageLocation"), rs.getString("ContactPerson"));
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return item;
    }


    public static void main(String[] args) {
        try {
            dbHelper = new DBHelper();
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        launch(args);
    }
    public static DBHelper getDBHelper(){
        return dbHelper;
    }
}