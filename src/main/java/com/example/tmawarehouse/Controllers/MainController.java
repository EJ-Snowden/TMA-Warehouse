package com.example.tmawarehouse.Controllers;

import com.example.tmawarehouse.Model.AuthenticationClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainController {
    private Stage stage;

    @FXML
    private void handleCoordinatorAction() {
        showPasswordWindow("Coordinator");
    }

    @FXML
    private void handleEmployeeAction() {
        showPasswordWindow("Employee");
    }

    @FXML
    private void handleAdministratorAction() {
        showPasswordWindow("Administrator");
    }

    private void showPasswordWindow(String role) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your credentials for " + role);

        ButtonType loginButton = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButton, ButtonType.CANCEL);

        TextField nameF = new TextField();
        nameF.setPromptText("Username");
        PasswordField passF = new PasswordField();
        passF.setPromptText("Password");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Username:"), nameF, new Label("Password:"), passF);
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButton) {
                return new Pair<>(nameF.getText(), passF.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(usernamePassword -> {
            String name = usernamePassword.getKey();
            String pass = usernamePassword.getValue();

            if (new AuthenticationClass().authenticate(role, name, pass)) {
                if ("Coordinator".equals(role)) {
                    openController("/com/example/tmawarehouse/coordinator-view.fxml", "Coordinator Window");
                } else if ("Employee".equals(role)) {
//                    openController("/com/example/tmawarehouse/coordinator-view.fxml", "Coordinator Window");
                } else if ("Administrator".equals(role)){
//                    openController("/com/example/tmawarehouse/coordinator-view.fxml", "Coordinator Window");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Authentication Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid user or password, please try again.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void openController(String resourceName, String windowName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(windowName);
            stage.setScene(new Scene(root));
            stage.show();

            this.stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
