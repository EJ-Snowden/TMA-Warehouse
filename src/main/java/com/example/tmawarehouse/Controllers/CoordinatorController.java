package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
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

    @FXML
    private TextField itemSearchField, requestSearchField;
    @FXML
    private TableView<Item> itemTableView;
    @FXML
    private TableView<TMA_Requests> requestTableView;
    @FXML
    private TableColumn<Item, Integer> itemIDColumn;
    @FXML
    private TableColumn<Item, String> itemNameColumn, groupNameColumn;
    @FXML
    public TableColumn itemUnitOfMeasurementColumn, itemQuantityColumn, itemPriceWithoutVATColumn,
            itemStatusColumn, itemStorageLocationColumn, itemContactPersonColumn;
    @FXML
    public TableColumn requestIDColumn, requestEmpNameColumn, requestItemNameColumn, requestUnitOfMeasurements,
            requestQuantityColumn, requestPriceWithoutVATColumn, requestCommentColumn, requestStatusColumn;

    @FXML
    private Button addButton, updateButton, removeButton, openRequestButton, confirmRequestButton, rejectRequestButton;

    @FXML
    private void initialize() {
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        MainApplication.loadItems(itemObservableList);
        setItemTableColumns();
        setupItemTableView(itemObservableList);

        ObservableList<TMA_Requests> requestObservableList = FXCollections.observableArrayList();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setRequestTableColumns();
        setupRequestTableView(requestObservableList);
    }

    private void setupItemTableView(ObservableList<Item> itemObservableList) {
        FilteredList<Item> filteredData = new FilteredList<>(itemObservableList, p -> true);
        itemSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getItemName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTableView.comparatorProperty());
        itemTableView.setItems(sortedData);
    }

    private void setupRequestTableView(ObservableList<TMA_Requests> requestObservableList) {
        FilteredList<TMA_Requests> filteredRequests = new FilteredList<>(requestObservableList, p -> true);
        requestSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRequests.setPredicate(request -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.valueOf(request.getRequestID()).contains(newValue);
            });
        });

        SortedList<TMA_Requests> sortedRequests = new SortedList<>(filteredRequests);
        sortedRequests.comparatorProperty().bind(requestTableView.comparatorProperty());
        requestTableView.setItems(sortedRequests);
    }

    public void setItemTableColumns(){
        itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        itemUnitOfMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasurement"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemPriceWithoutVATColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutVAT"));
        itemStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        itemStorageLocationColumn.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        itemContactPersonColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
    }
    public void setRequestTableColumns(){
        requestIDColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        requestEmpNameColumn.setCellValueFactory(new PropertyValueFactory<>("empName"));
        requestItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        requestUnitOfMeasurements.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasurements"));
        requestQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        requestPriceWithoutVATColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutVAT"));
        requestCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
}
