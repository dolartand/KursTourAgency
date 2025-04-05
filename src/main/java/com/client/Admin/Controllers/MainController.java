package com.client.Admin.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Pane contentArea;
    @FXML
    private Button tours_btn;
    @FXML
    private Button bookings_btn;
    @FXML
    private Button users_btn;
    @FXML
    private Button logOut_btn;

    @FXML
    private void handleTours(ActionEvent actionEvent) {
        loadView("ToursView.fxml");
    }

    @FXML
    private void handleBookings(ActionEvent actionEvent) {
        loadView("BookingView.fxml");
    }

    @FXML
    private void handleUsers(ActionEvent actionEvent) {
        loadView("UsersView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadView("ToursView.fxml");
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/AdminViews/" + fxml));
            Pane newView = fxmlLoader.load();
            contentArea.getChildren().setAll(newView);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
