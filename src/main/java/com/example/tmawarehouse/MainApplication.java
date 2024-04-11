package com.example.tmawarehouse;

import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.Model.DBHelper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import javafx.scene.Parent;

public class MainApplication extends Application {

    static DBHelper dbHelper;
    static Statement statement;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/tmawarehouse/main-view.fxml"));
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
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
                requestList.add(new TMA_Requests(rs.getInt("RequestID"), rs.getInt("EmployeeID"), rs.getInt("ItemID"),
                        rs.getString("UnitOfMeasurement"), rs.getInt("Quantity"), rs.getDouble("PriceWithoutVAT"),
                        rs.getString("COMMENT"), rs.getString("Status")));
            }
//            TODO: Get price from item * quantity
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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