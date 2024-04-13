package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.Emp;
import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.Data.UnitOfMeasurements;
import com.example.tmawarehouse.MainApplication;
import com.example.tmawarehouse.Model.TMA_RequestRepositoryClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class EmployeeController {

    @FXML
    public TableView<Item> itemsTable;
    @FXML
    public TableView<TMA_Requests> requestsTable;
    @FXML
    public TableColumn requestIDColumn, rUnitOfMeasurementColumn, groupNameColumn, rQuantityColumn, rPriceColumn, rCommentColumn, rStatusColumn, ItemNameColumn;
    @FXML
    public TextField requestSearchField, itemsSearchField;
    @FXML
    private TableColumn<Item, String> itemNameColumn, unitOfMeasurementColumn;
    @FXML
    private TableColumn<Item, Integer> quantityColumn;
    @FXML
    private TableColumn<Item, Double> priceColumn;
    private TextField itemNameField, unitField, quantityField, priceField, commentField;

    ObservableList<Item> itemObservableList;
    ObservableList<TMA_Requests> requestObservableList;
    ArrayList<UnitOfMeasurements> unitOfMeasurementsList = new ArrayList<>();
    Item selectedItem;
    PreparedStatement preparedStatement;
    Emp emp;
    TMA_RequestRepositoryClass tmaRequestRepositoryClass;


    @FXML
    private void initialize() {
        itemObservableList = FXCollections.observableArrayList();
        MainApplication.loadItems(itemObservableList);
        setItemTableColumns();
        setUpItemTableView(itemObservableList);

        requestObservableList = FXCollections.observableArrayList();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setRequestTableColumns();
        setupRequestTableView(requestObservableList);

        MainApplication.loadUnitOfMeasurements(unitOfMeasurementsList);

        tmaRequestRepositoryClass = new TMA_RequestRepositoryClass(MainApplication.getDBHelper());
    }

    public void setEmp(Emp emp){
        this.emp = emp;
    }

    @FXML
    private void handleOrderButton() {
        selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            createAndShowOrderForm();
        } else showAlert("Invalid Operation", "No items selected or request already processed.");
    }

    private void createAndShowOrderForm() {
        Dialog<Object> orderFormDialog = new Dialog<>();
        orderFormDialog.setTitle("Order Form");

        orderFormDialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        itemNameField = new TextField(selectedItem.getItemName());
        itemNameField.setEditable(false);
        unitField = new TextField(selectedItem.getUnitOfMeasurement());
        quantityField = new TextField(String.valueOf(selectedItem.getQuantity()));
        priceField = new TextField(String.format("%.2f", selectedItem.getPriceWithoutVAT() * selectedItem.getQuantity()));
        commentField = new TextField();

        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(itemNameField, 1, 0);
        grid.add(new Label("Unit of Measurement:"), 0, 1);
        grid.add(unitField, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantityField, 1, 2);
        grid.add(new Label("Price Without VAT (UAH):"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Comment (optional):"), 0, 4);
        grid.add(commentField, 1, 4);

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            updatePriceBasedOnQuantity();
        });

        orderFormDialog.getDialogPane().setContent(grid);

        orderFormDialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                handleFormSubmission();
            }
            return null;
        });

        orderFormDialog.showAndWait();
    }
    private void updatePriceBasedOnQuantity() {
        if (!quantityField.getText().isEmpty()) {
            int quantity = Integer.parseInt(quantityField.getText());
            double price = quantity * selectedItem.getPriceWithoutVAT();
            priceField.setText(String.format("%.2f", price));
        } else {
            priceField.setText("0.00");
        }
    }
    private void refreshRequestsTable() {
        requestObservableList.clear();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setupRequestTableView(requestObservableList);
    }

    private void handleFormSubmission() {
        if (!unitField.getText().isEmpty() && !priceField.getText().isEmpty() && !quantityField.getText().isEmpty()){
            preparedStatement = tmaRequestRepositoryClass.insertNewRequest(preparedStatement, selectedItem, emp.getEmpId(), unitField.getText(),
                    Integer.parseInt(quantityField.getText()), Double.parseDouble(priceField.getText().replace(",", ".")),
                    commentField.getText());

            tmaRequestRepositoryClass.execStatement(preparedStatement);
            refreshRequestsTable();
        }else showAlert("Request wasn't added", "Fill all necessary fields");
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void setItemTableColumns(){
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        unitOfMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasurement"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutVAT"));
    }
    public void setRequestTableColumns(){
        requestIDColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        rUnitOfMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("unitOfMeasurements"));
        rQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        rPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutVAT"));
        rCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        rStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void setUpItemTableView(ObservableList<Item> itemObservableList) {
        SetUpItemView(itemObservableList, itemsSearchField, itemsTable);
    }

    static void SetUpItemView(ObservableList<Item> itemObservableList, TextField itemsSearchField, TableView<Item> itemsTable) {
        FilteredList<Item> filteredData = new FilteredList<>(itemObservableList, p -> true);
        itemsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getItemName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemsTable.comparatorProperty());
        itemsTable.setItems(sortedData);
    }

    private void setupRequestTableView(ObservableList<TMA_Requests> requestObservableList) {
        SetUpRequestView(requestObservableList, requestSearchField, requestsTable);
    }

    static void SetUpRequestView(ObservableList<TMA_Requests> requestObservableList, TextField requestSearchField, TableView<TMA_Requests> requestsTable) {
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
        sortedRequests.comparatorProperty().bind(requestsTable.comparatorProperty());
        requestsTable.setItems(sortedRequests);
    }
}