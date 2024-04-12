package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Data.Item;
import com.example.tmawarehouse.Data.TMA_Requests;
import com.example.tmawarehouse.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        public TextField unitOfMeasurementField;
        public TextField quantityField;
        public TextField priceWithoutVATField;
        public TextField statusField;
        public TextField storageLocationField;
        public TextField contactPersonField;

        public DialogData(Dialog<Pair<String, String>> dialog, TextField itemNameField, TextField groupNameField,
                          TextField unitOfMeasurementField, TextField quantityField, TextField priceWithoutVATField,
                          TextField statusField, TextField storageLocationField, TextField contactPersonField) {
            this.dialog = dialog;
            this.itemNameField = itemNameField;
            this.groupNameField = groupNameField;
            this.unitOfMeasurementField = unitOfMeasurementField;
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
        TextField unitOfMeasurementField = new TextField();
        unitOfMeasurementField.setPromptText("Unit of Measurement");
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
        grid.add(unitOfMeasurementField, 1, 2);
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

        return new DialogData(dialog, itemNameField, groupNameField, unitOfMeasurementField, quantityField, priceWithoutVATField,
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
            dialogData.unitOfMeasurementField.setText(itemTableView.getSelectionModel().getSelectedItem().getUnitOfMeasurement());
            dialogData.quantityField.setText(String.valueOf(itemTableView.getSelectionModel().getSelectedItem().getQuantity()));
            dialogData.priceWithoutVATField.setText(String.valueOf(itemTableView.getSelectionModel().getSelectedItem().getPriceWithoutVAT()));
            dialogData.statusField.setText(itemTableView.getSelectionModel().getSelectedItem().getStatus());
            dialogData.storageLocationField.setText(itemTableView.getSelectionModel().getSelectedItem().getStorageLocation());
            dialogData.contactPersonField.setText(itemTableView.getSelectionModel().getSelectedItem().getContactPerson());

            dialogData.dialog.setResultConverter(dialogButton -> {
                if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (!dialogData.itemNameField.getText().isEmpty() && !dialogData.groupNameField.getText().isEmpty() &&
                            !dialogData.unitOfMeasurementField.getText().isEmpty() && !dialogData.quantityField.getText().isEmpty() &&
                            !dialogData.priceWithoutVATField.getText().isEmpty()){
                        try {
                            String query = "UPDATE Items SET ItemName = ?, ItemGroup = ?, UnitOfMeasurement = ?, Quantity = ?, " +
                                    "PriceWithoutVAT = ?, Status = ?, StorageLocation = ?, ContactPerson = ? " +
                                    "WHERE ITEMID = ?";

                            fillQuery(dialogData, query);
                            int index = itemTableView.getSelectionModel().getSelectedItem().getItemID();
                            preparedStatement.setInt(9, index);

                            preparedStatement.execute();
                            preparedStatement.close();

                            itemObservableList.clear();
                           MainApplication.loadItems(itemObservableList);
                            setupItemTableView(itemObservableList);
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(e.getSQLState());
                alert.setHeaderText(null);
                alert.setContentText(e.getLocalizedMessage());
                alert.showAndWait();
            }
        }else showAlert("remov");
    }

    public void fillQuery(DialogData dialogData, String query){
        try {
            preparedStatement = MainApplication.getDBHelper().getConnection().prepareStatement(query);
            preparedStatement.setString(1, dialogData.itemNameField.getText());
            preparedStatement.setString(2, dialogData.groupNameField.getText());
            preparedStatement.setString(3, dialogData.unitOfMeasurementField.getText());
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
                        !dialogData.unitOfMeasurementField.getText().isEmpty() && !dialogData.quantityField.getText().isEmpty() &&
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
                                dialogData.unitOfMeasurementField.getText(), Integer.parseInt(dialogData.quantityField.getText()),
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
}
