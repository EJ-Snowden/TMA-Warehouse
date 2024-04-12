package com.example.tmawarehouse.Data;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class DialogData {
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
