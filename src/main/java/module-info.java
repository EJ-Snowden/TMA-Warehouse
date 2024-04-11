module com.example.tmawarehouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.tmawarehouse to javafx.fxml;
    exports com.example.tmawarehouse;
    exports com.example.tmawarehouse.Controllers;
    opens com.example.tmawarehouse.Controllers to javafx.fxml;
    exports com.example.tmawarehouse.Model;
    opens com.example.tmawarehouse.Model to javafx.fxml;
    exports com.example.tmawarehouse.Data;
    opens com.example.tmawarehouse.Data to javafx.fxml;
}