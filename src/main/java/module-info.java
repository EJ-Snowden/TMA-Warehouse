module com.example.tmawarehouse {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tmawarehouse to javafx.fxml;
    exports com.example.tmawarehouse;
}