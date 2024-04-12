package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.Data.UnitOfMeasurements;
import com.example.tmawarehouse.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

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
    Statement statement;
    PreparedStatement preparedStatement;
    ObservableList<Item> itemObservableList;
    ObservableList<TMA_Requests> requestObservableList;
    ArrayList<UnitOfMeasurements> unitOfMeasurementsList = new ArrayList<>();

    @FXML
    private void initialize() {
        itemObservableList = FXCollections.observableArrayList();
        MainApplication.loadItems(itemObservableList);
        setItemTableColumns();
        setupItemTableView(itemObservableList);

        requestObservableList = FXCollections.observableArrayList();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setRequestTableColumns();
        setupRequestTableView(requestObservableList);

        MainApplication.loadUnitOfMeasurements(unitOfMeasurementsList);
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

    protected static class DialogData {
        public Dialog<Pair<String, String>> dialog;
        public TextField itemIDField;
        public TextField itemNameField;
        public TextField groupNameField;
        public ComboBox unitOfMeasurementComboBox;
        public TextField quantityField;
        public TextField priceWithoutVATField;
        public TextField statusField;
        public TextField storageLocationField;
        public TextField contactPersonField;

        public DialogData(Dialog<Pair<String, String>> dialog, TextField itemNameField, TextField groupNameField,
                          ComboBox unitOfMeasurementComboBox, TextField quantityField, TextField priceWithoutVATField,
                          TextField statusField, TextField storageLocationField, TextField contactPersonField) {
            this.dialog = dialog;
            this.itemNameField = itemNameField;
            this.groupNameField = groupNameField;
            this.unitOfMeasurementComboBox = unitOfMeasurementComboBox;
            this.quantityField = quantityField;
            this.priceWithoutVATField = priceWithoutVATField;
            this.statusField = statusField;
            this.storageLocationField = storageLocationField;
            this.contactPersonField = contactPersonField;
        }
    }
    private DialogData createItemDialog(String title, String headerText) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");
        ComboBox<String> unitOfMeasurementComboBox = new ComboBox<>();
        ObservableList<String> unitNames = FXCollections.observableArrayList(
                unitOfMeasurementsList.stream().map(UnitOfMeasurements::getUnitName).collect(Collectors.toList())
        );
        unitOfMeasurementComboBox.setItems(unitNames);
        unitOfMeasurementComboBox.setPromptText("Unit of Measurement");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField priceWithoutVATField = new TextField();
        priceWithoutVATField.setPromptText("Price Without VAT");
        TextField statusField = new TextField();
        statusField.setPromptText("Status");
        TextField storageLocationField = new TextField();
        storageLocationField.setPromptText("Storage Location");
        TextField contactPersonField = new TextField();
        contactPersonField.setPromptText("Contact Person");

        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(itemNameField, 1, 0);
        grid.add(new Label("Group Name:"), 0, 1);
        grid.add(groupNameField, 1, 1);
        grid.add(new Label("Unit of Measurement:"), 0, 2);
        grid.add(unitOfMeasurementComboBox, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Price Without VAT:"), 0, 4);
        grid.add(priceWithoutVATField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusField, 1, 5);
        grid.add(new Label("Storage Location:"), 0, 6);
        grid.add(storageLocationField, 1, 6);
        grid.add(new Label("Contact Person:"), 0, 7);
        grid.add(contactPersonField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        return new DialogData(dialog, itemNameField, groupNameField, unitOfMeasurementComboBox, quantityField, priceWithoutVATField,
                statusField, storageLocationField, contactPersonField);
    }
    @FXML
    public void handleUpdateButtonAction() {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            DialogData dialogData = createItemDialog("Update Item", "Update item details:");
            dialogData.dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Update", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

            dialogData.itemNameField.setText(itemTableView.getSelectionModel().getSelectedItem().getItemName());
            dialogData.groupNameField.setText(itemTableView.getSelectionModel().getSelectedItem().getGroupName());
            dialogData.unitOfMeasurementComboBox.setValue(selectedItem.getUnitOfMeasurement());
            dialogData.quantityField.setText(String.valueOf(itemTableView.getSelectionModel().getSelectedItem().getQuantity()));
            dialogData.priceWithoutVATField.setText(String.valueOf(itemTableView.getSelectionModel().getSelectedItem().getPriceWithoutVAT()));
            dialogData.statusField.setText(itemTableView.getSelectionModel().getSelectedItem().getStatus());
            dialogData.storageLocationField.setText(itemTableView.getSelectionModel().getSelectedItem().getStorageLocation());
            dialogData.contactPersonField.setText(itemTableView.getSelectionModel().getSelectedItem().getContactPerson());

            dialogData.dialog.setResultConverter(dialogButton -> {
                if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (!dialogData.itemNameField.getText().isEmpty() && !dialogData.groupNameField.getText().isEmpty() &&
                            !String.valueOf(dialogData.unitOfMeasurementComboBox.getValue()).isEmpty() && !dialogData.quantityField.getText().isEmpty() &&
                            !dialogData.priceWithoutVATField.getText().isEmpty()){
                        try {
                            String query = "UPDATE Items SET ItemName = ?, ItemGroup = ?, UnitOfMeasurement = ?, Quantity = ?, " +
                                    "PriceWithoutVAT = ?, Status = ?, StorageLocation = ?, ContactPerson = ? " +
                                    "WHERE ITEMID = ?";

                            fillQuery(dialogData, query);
                            int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();
                            preparedStatement.setInt(9, index);

                            preparedStatement.executeUpdate();
                            preparedStatement.close();

                            refreshItemsTable();
                        } catch (SQLException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(e.getSQLState());
                            alert.setHeaderText(null);
                            alert.setContentText(e.getLocalizedMessage());
                            alert.showAndWait();
                        }
                    }else showAlert("updat");

                }
                return null;
            });
            dialogData.dialog.showAndWait();

        }else{
            showAlert("Invalid Operation", "No items selected or request already processed.");
        }
    }

    @FXML
    public void handleRemoveButtonAction() {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            try {
                String query = "DELETE FROM Items WHERE ITEMID = ?";
                preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
                int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();
                preparedStatement.setInt(1, index);

                preparedStatement.execute();
                preparedStatement.close();

                itemObservableList.remove(itemObservableList.get(itemTableView.getSelectionModel().getSelectedIndex()));
                setupItemTableView(itemObservableList);
            } catch (SQLException e) {
                showAlert("remov");
            }
        }else {
            showAlert("Invalid Operation", "No request selected or request already processed.");
        }
    }

    public void fillQuery(DialogData dialogData, String query){
        try {
            preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
            preparedStatement.setString(1, dialogData.itemNameField.getText());
            preparedStatement.setString(2, dialogData.groupNameField.getText());
            preparedStatement.setString(3, String.valueOf(dialogData.unitOfMeasurementComboBox.getValue()));
            preparedStatement.setInt(4, Integer.parseInt(dialogData.quantityField.getText()));
            preparedStatement.setDouble(5, Double.parseDouble(dialogData.priceWithoutVATField.getText()));
            preparedStatement.setString(6, dialogData.statusField.getText());
            preparedStatement.setString(7, dialogData.storageLocationField.getText());
            preparedStatement.setString(8, dialogData.contactPersonField.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void showAlert(String action){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Item wasn't " + action + "d");
        alert.setHeaderText(null);
        if (!action.equalsIgnoreCase("remov")) alert.setContentText("Fill all necessary fields");
        alert.showAndWait();
    }

    @FXML
    public void handleAddButtonAction() {
        DialogData dialogData = createItemDialog("Add Item", "Add item details:");
        dialogData.dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Add", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

        dialogData.dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (!dialogData.itemNameField.getText().isEmpty() && !dialogData.groupNameField.getText().isEmpty() &&
                        !String.valueOf(dialogData.unitOfMeasurementComboBox.getValue()).isEmpty() && !dialogData.quantityField.getText().isEmpty() &&
                        !dialogData.priceWithoutVATField.getText().isEmpty()){
                    try {
                        String query = "INSERT INTO Items(ItemName, ItemGroup, UnitOfMeasurement, Quantity, PriceWithoutVAT, Status, StorageLocation, ContactPerson) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                        fillQuery(dialogData, query);

                        preparedStatement.execute();

                        preparedStatement.close();

                        query = "SELECT MAX(ItemID) FROM ITEMS";
                        statement = MainApplication.getDBHelper().getConnection().createStatement();
                        ResultSet rs = statement.executeQuery(query);
                        int itemID = 0;
                        while (rs.next()){
                            itemID = rs.getInt(1);
                        }
                        statement.close();

                        itemObservableList.add(new Item(itemID, dialogData.itemNameField.getText(), dialogData.groupNameField.getText(),
                                String.valueOf(dialogData.unitOfMeasurementComboBox.getValue()), Integer.parseInt(dialogData.quantityField.getText()),
                                Double.parseDouble(dialogData.priceWithoutVATField.getText()), dialogData.statusField.getText(),
                                dialogData.storageLocationField.getText(), dialogData.contactPersonField.getText()));

                        setupItemTableView(itemObservableList);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(e.getSQLState());
                        alert.setHeaderText(null);
                        alert.setContentText(e.getLocalizedMessage());
                        alert.showAndWait();
                    }
                }else showAlert("add");
            }
            return null;
        });

        dialogData.dialog.showAndWait();
    }

    @FXML
    public void handleOpenRequestAction() {
        TMA_Requests selectedRequest = requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            openRequestDialog(selectedRequest);
        } else {
            showAlert("No Request Selected", "Please select a request to view details.");
        }
    }

    private void openRequestDialog(TMA_Requests request) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Purchase Request Details");
        dialog.setHeaderText("Details for Request ID: " + request.getRequestID());

        Item item = MainApplication.getItemDetails(request.getItemID());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER_LEFT);

        TextField itemNameField = new TextField(item.getItemName());
        itemNameField.setEditable(false);
        TextField groupNameField = new TextField(item.getGroupName());
        groupNameField.setEditable(false);
        TextField unitOfMeasurementField = new TextField(item.getUnitOfMeasurement());
        unitOfMeasurementField.setEditable(false);
        TextField quantityField = new TextField(String.valueOf(item.getQuantity()));
        quantityField.setEditable(false);
        TextField priceField = new TextField(String.format("%.2f", item.getPriceWithoutVAT()));
        priceField.setEditable(false);
        TextField statusField = new TextField(item.getStatus());
        statusField.setEditable(false);
        TextField storageLocationField = new TextField(item.getStorageLocation());
        storageLocationField.setEditable(false);
        TextField contactPersonField = new TextField(item.getContactPerson());
        contactPersonField.setEditable(false);

        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(itemNameField, 1, 0);
        grid.add(new Label("Group Name:"), 0, 1);
        grid.add(groupNameField, 1, 1);
        grid.add(new Label("Unit of Measurement:"), 0, 2);
        grid.add(unitOfMeasurementField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Price Without VAT:"), 0, 4);
        grid.add(priceField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusField, 1, 5);
        grid.add(new Label("Storage Location:"), 0, 6);
        grid.add(storageLocationField, 1, 6);
        grid.add(new Label("Contact Person:"), 0, 7);
        grid.add(contactPersonField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void confirmRequestAction() {
        TMA_Requests selectedRequest = requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null && selectedRequest.getStatus().equalsIgnoreCase("New")) {
            try {
                String query = "UPDATE Items SET Quantity = Quantity - ? WHERE ITEMID = ?";
                preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
                preparedStatement.setInt(1, selectedRequest.getQuantity());
                preparedStatement.setInt(2, selectedRequest.getItemID());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                String updateRequestQuery = "UPDATE TMA_REQUESTS SET Status = 'Approved' WHERE RequestID = ?";
                preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(updateRequestQuery);
                preparedStatement.setInt(1, selectedRequest.getRequestID());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                refreshTables();
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to update the item quantity: " + e.getLocalizedMessage());
            }
        } else {
            showAlert("Invalid Operation", "No request selected or request already processed.");
        }
    }

    @FXML
    public void rejectRequestAction() {
        TMA_Requests selectedRequest = requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null && selectedRequest.getStatus().equalsIgnoreCase("New")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Reject Request");
            dialog.setHeaderText("Rejecting Request ID: " + selectedRequest.getRequestID());
            dialog.setContentText("Please enter the reason for rejection:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(reason -> {
                try {
                    String updateRequestQuery = "UPDATE TMA_REQUESTS SET Status = 'Rejected', \"COMMENT\" = ? WHERE RequestID = ?";
                    preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(updateRequestQuery);
                    preparedStatement.setString(1, reason);
                    preparedStatement.setInt(2, selectedRequest.getRequestID());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    refreshRequestsTable();
                } catch (SQLException e) {
                    showAlert("Database Error", "Failed to reject the request: " + e.getLocalizedMessage());
                }
            });
        } else {
            showAlert("Invalid Operation", "No request selected or request already processed.");
        }
    }

    private void refreshTables() {
        itemObservableList.clear();
        requestObservableList.clear();
        MainApplication.loadItems(itemObservableList);
        MainApplication.loadPurchaseRequests(requestObservableList);
        setupItemTableView(itemObservableList);
        setupRequestTableView(requestObservableList);
    }

    private void refreshRequestsTable() {
        requestObservableList.clear();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setupRequestTableView(requestObservableList);
    }

    private void refreshItemsTable() {
        requestObservableList.clear();
        MainApplication.loadPurchaseRequests(requestObservableList);
        setupRequestTableView(requestObservableList);
    }
}