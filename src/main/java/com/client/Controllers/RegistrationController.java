package com.client.Controllers;

import com.client.Service.RegistrationService;
import com.client.SessionHolder;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.RegistrationResponse;
import com.kurs.exceptions.BlankFieldsException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField name_txt;
    @FXML
    private TextField login_txt;
    @FXML
    private PasswordField password_txt;
    @FXML
    private Button singIn_btn;
    @FXML
    private Label logIn_lbl;

    @FXML
    public void handleLogin(MouseEvent mouseEvent) {
        openLoginWindow();
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFields() {
        if (name_txt.getText().isEmpty() || login_txt.getText().isEmpty() || password_txt.getText().isEmpty()) {
            throw new BlankFieldsException();
        }
    }

    @FXML
    private void handleRegistration(ActionEvent actionEvent) {
        try {
            checkFields();
        } catch (BlankFieldsException e) {
            Alert alert = AlertFactory.showErrorAlert(e.getMessage());
            alert.showAndWait();
        }

        Task<RegistrationResponse> registrationTask = new Task<>() {
            @Override
            protected RegistrationResponse call() throws Exception {
                RegistrationService reg = new RegistrationService();
                return reg.registration(getName(), getLogin(), getPassword());
            }
        };

        registrationTask.setOnSucceeded(event -> {
           RegistrationResponse response = registrationTask.getValue();
           if (response.isSuccess()) {
               String sessionId = response.getMessage();
               SessionHolder.setSessionId(sessionId);
               try {
                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Main.fxml"));
                   Stage stage = new Stage();
                   stage.setScene(new Scene(fxmlLoader.load()));
                   stage.show();

                   Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                   currentStage.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           } else {
               Alert alert = AlertFactory.showErrorAlert("Недопустимые данные для регистрации!");
               alert.showAndWait();
           }
        });
        registrationTask.setOnFailed(event -> {
            Alert alert = AlertFactory.showErrorAlert("Соединение с сервером не установлено");
            alert.showAndWait();
        });

        new Thread(registrationTask).start();
    }

    private String getName() {
        return name_txt.getText();
    }

    private String getLogin() {
        return login_txt.getText();
    }

    private String getPassword() {
        return password_txt.getText();
    }

    @FXML
    private void handleHostConfig(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/client/UserViews/HostConfigView.fxml")
            );
            Parent root = loader.load();
            Stage owner = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Stage cfgStage = new Stage();
            cfgStage.setTitle("Настройки соединения");
            cfgStage.initOwner(owner);
            cfgStage.initModality(Modality.APPLICATION_MODAL);
            cfgStage.setResizable(false);
            cfgStage.setScene(new Scene(root));
            cfgStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
