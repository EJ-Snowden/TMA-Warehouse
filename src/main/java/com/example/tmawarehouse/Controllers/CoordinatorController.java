package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CoordinatorController {

    public TableColumn unitOfMeasurementColumn;
    public TableColumn quantityColumn;
    public TableColumn priceWithoutVATColumn;
    public TableColumn statusColumn;
    public TableColumn storageLocationColumn;
    public TableColumn contactPersonColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Item> itemTableView;
    @FXML
    private TableColumn<Item, Integer> idColumn;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, String> groupNameColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;

    @FXML
    private void initialize() {
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        MainApplication.loadItems(itemObservableList);
        setItemTableColumns();
        itemTableView.setItems(itemObservableList);

        FilteredList<Item> filteredData = new FilteredList<>(itemObservableList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(item -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return item.getItemName().toLowerCase().contains(lowerCaseFilter);
        }));

        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTableView.comparatorProperty());
        itemTableView.setItems(sortedData);
    }
        public void setItemTableColumns(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        unitOfMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasurement"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceWithoutVATColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutVAT"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        storageLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        contactPersonColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
    }
}
