package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.DialogData;
import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.Data.UnitOfMeasurements;
import com.example.tmawarehouse.MainApplication;
import com.example.tmawarehouse.Model.ItemRepositoryClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.sql.PreparedStatement;
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
            itemStatusColumn, itemStorageLocationColumn, itemContactPersonColumn, requestIDColumn, requestEmpNameColumn,
            requestItemNameColumn, requestUnitOfMeasurements, requestQuantityColumn, requestPriceWithoutVATColumn, requestCommentColumn,
            requestStatusColumn;

    @FXML
    private Button addButton, updateButton, removeButton, openRequestButton, confirmRequestButton, rejectRequestButton;
    Statement statement;
    PreparedStatement preparedStatement;
    ObservableList<Item> itemObservableList;
    ObservableList<TMA_Requests> requestObservableList;
    ArrayList<UnitOfMeasurements> unitOfMeasurementsList = new ArrayList<>();
    ItemRepositoryClass itemRepositoryClass;

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

        itemRepositoryClass = new ItemRepositoryClass(MainApplication.getDBHelper());
    }

    private void setupItemTableView(ObservableList<Item> itemObservableList) {
        EmployeeController.SetUpItemView(itemObservableList, itemSearchField, itemTableView);
    }

    private void setupRequestTableView(ObservableList<TMA_Requests> requestObservableList) {
        EmployeeController.SetUpRequestView(requestObservableList, requestSearchField, requestTableView);
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
                            preparedStatement = itemRepositoryClass.insertNewItem(dialogData, preparedStatement);
                            int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();
                            preparedStatement.setInt(9, index);

                            itemRepositoryClass.execStatement(preparedStatement);

                            refreshItemsTable();
                        } catch (SQLException e) {
                            showAlert(e.getSQLState(), e.getLocalizedMessage());
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
            int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();

            itemRepositoryClass.delete(index, preparedStatement, itemTableView);

            itemObservableList.remove(itemObservableList.get(itemTableView.getSelectionModel().getSelectedIndex()));
            setupItemTableView(itemObservableList);
        }else {
            showAlert("Invalid Operation", "No request selected or request already processed.");
        }
    }

    @FXML
    public void handleAddButtonAction() {
        DialogData dialogData = createItemDialog("Add Item", "Add item details:");
        dialogData.dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Add", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

        dialogData.dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                int itemId = itemRepositoryClass.selectItemId(dialogData, statement);
                String itemName = dialogData.itemNameField.getText();
                String groupName = dialogData.groupNameField.getText();
                String unitOfMeasurement = String.valueOf(dialogData.unitOfMeasurementComboBox.getValue());
                int quantity;
                try {
                    quantity = Integer.parseInt(dialogData.quantityField.getText());
                } catch (NumberFormatException e) {
                    quantity = 0;
                }
                double priceWithoutVAT;
                try {
                    priceWithoutVAT = Double.parseDouble(dialogData.priceWithoutVATField.getText());
                } catch (NumberFormatException e) {
                    priceWithoutVAT = 0;
                }
                String status = dialogData.statusField.getText();
                String storageLocation = dialogData.storageLocationField.getText();
                String contactPerson = dialogData.contactPersonField.getText();
                if (!itemName.isEmpty() && !groupName.isEmpty() && !String.valueOf(unitOfMeasurement).isEmpty()
                        && quantity != 0 && priceWithoutVAT != 0){
                    preparedStatement = itemRepositoryClass.insertNewItem(dialogData, preparedStatement);

                    itemRepositoryClass.execStatement(preparedStatement);

                    itemObservableList.add(new Item(itemId, itemName, groupName, unitOfMeasurement, quantity, priceWithoutVAT,
                            status, storageLocation, contactPerson));

                    setupItemTableView(itemObservableList);
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
    public void showAlert(String action){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Item wasn't " + action + "d");
        alert.setHeaderText(null);
        if (!action.equalsIgnoreCase("remov")) alert.setContentText("Fill all necessary fields");
        alert.showAndWait();
    }
    @FXML
    public void confirmRequestAction() {
        TMA_Requests selectedRequest = requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null && selectedRequest.getStatus().equalsIgnoreCase("New")) {
            itemRepositoryClass.updateQuantity(preparedStatement, selectedRequest);
            refreshTables();
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
                itemRepositoryClass.updateRejectStatus(preparedStatement, selectedRequest ,reason);
                refreshRequestsTable();
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