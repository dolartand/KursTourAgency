module com.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.client.Controllers to javafx.fxml;
    exports com.client;
    exports com.client.Controllers;
}