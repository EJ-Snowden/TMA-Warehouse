package com.example.tmawarehouse;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.*;

public class HelloApplication extends Application implements AuthenticationService{

    static DBHelper dbHelper;
    Statement statement;

    @Override
    public void start(Stage stage) {
        Text welcomeText = new Text("Welcome! Please choose your role:");
        welcomeText.setFont(new Font(14));

        Button coordinatorBtn = new Button("Coordinator");
        coordinatorBtn.setPrefWidth(200);
        coordinatorBtn.setStyle("-fx-font-size: 14; -fx-base: #ADD8E6;");

        Button employeeBtn = new Button("Employee");
        employeeBtn.setPrefWidth(200);
        employeeBtn.setStyle("-fx-font-size: 14; -fx-base: #ADD8E6;");

        Button administratorBtn = new Button("Administrator");
        administratorBtn.setPrefWidth(200);
        administratorBtn.setStyle("-fx-font-size: 14; -fx-base: #ADD8E6;");

        coordinatorBtn.setOnAction(e -> showPasswordWindow("Coordinator", stage));
        employeeBtn.setOnAction(e -> showPasswordWindow("Employee", stage));
        administratorBtn.setOnAction(e -> showPasswordWindow("Administrator", stage));

        VBox layout = new VBox(20);
        layout.getChildren().addAll(welcomeText, coordinatorBtn, employeeBtn ,administratorBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25, 50, 25, 50));

        Scene scene = new Scene(layout, 400, 200);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void showPasswordWindow(String role, Stage stage) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.initOwner(stage);
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

            if (authenticate(role, name, pass)) {
                if ("Coordinator".equals(role)) {
                    showCoordinatorWindow();
                } else if ("Employee".equals(role)) {
                    showEmployeeWindow();
                } else if ("Administrator".equals(role)){
                    showAdministratorWindow();
                }
            } else {
                showAlert();
            }
        });
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Authentication Failed");
        alert.setHeaderText(null);
        alert.setContentText("Invalid user or password, please try again.");
        alert.showAndWait();
    }
    @Override
    public boolean authenticate(String role, String name, String password){
        try {
            if (dbHelper.conn != null) {
                statement = dbHelper.conn.createStatement();
                String query = "SELECT EmpName, EmpRole, Password FROM Emp";
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()){
                    if (name.equalsIgnoreCase(rs.getString("EmpName")) && role.equalsIgnoreCase(rs.getString("EmpRole")))
                        return password.equalsIgnoreCase(rs.getString("Password"));
                    }
                statement.close();
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private void showCoordinatorWindow() {

    }
    private void showAdministratorWindow() {

    }
    private void showEmployeeWindow() {

    }

    public static void main(String[] args) {
        try {
            dbHelper = new DBHelper();
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        launch(args);
    }
}