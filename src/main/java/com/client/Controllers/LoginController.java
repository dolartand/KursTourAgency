package com.client.Controllers;

import com.client.Service.AuthService;
import com.client.SessionHolder;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.LoginResponse;
import com.kurs.exceptions.BlankFieldsException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountType_box.getItems().addAll("Админ", "Клиент");
        accountType_box.setValue("Клиент");
    }

    @FXML
    private void handleRegistration(MouseEvent actionEvent) {
        openRegistrationWindow();
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void checkFields() {
        if (login_txt.getText().isEmpty() || password_txt.getText().isEmpty()) {
            throw new BlankFieldsException();
        }
    }

    private void openRegistrationWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/UserViews/Registration.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Регистрация");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        try {
            checkFields();
        } catch (BlankFieldsException e) {
            Alert alert = AlertFactory.showErrorAlert(e.getMessage());
            alert.showAndWait();
        }

        Task<LoginResponse> loginTask = new Task<>() {
          @Override
          protected LoginResponse call() throws Exception {
              AuthService authService = new AuthService();
              return authService.login(getLogin(), getPassword(), getAccountType());
          }
        };

        loginTask.setOnSucceeded(e -> {
            LoginResponse resp = loginTask.getValue();
            if (resp.isSuccess()) {
                String sessionId = resp.getMessage();
                SessionHolder.setSessionId(sessionId);
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
                    stage.setTitle("Турагенство");
                    stage.setResizable(false);
                    stage.show();

                    Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = AlertFactory.showErrorAlert("Неверный логин или пароль");
                alert.showAndWait();
            }
        });
        loginTask.setOnFailed(e -> {
            Alert alert = AlertFactory.showErrorAlert("Соединение с сервером не установлено");
            alert.showAndWait();
        });

        new Thread(loginTask).start();
    }

    public String getLogin() {
        return login_txt.getText();
    }

    public String getPassword() {
        return password_txt.getText();
    }

    public String getAccountType() {
        return accountType_box.getValue();
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
