package com.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ChoiceBox<String> accountType_box;
    @FXML
    private TextField login_txt;
    @FXML
    private PasswordField password_txt;
    @FXML
    private Button logIn_btn;
    @FXML
    private Label registration_lbl;

    @FXML
    private void handleRegistration(MouseEvent actionEvent) {
        openRegistrationWindow();
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void openRegistrationWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Registration.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader;
            if (accountType_box.getSelectionModel().getSelectedItem().equals("Админ")) {
                loader = new FXMLLoader(getClass().getResource("/com/client/AdminViews/Main.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Main.fxml"));

            }

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountType_box.getItems().addAll("Админ", "Клиент");
    }
}
