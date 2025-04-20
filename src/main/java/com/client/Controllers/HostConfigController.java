package com.client.Controllers;

import com.client.AppConfig;
import com.kurs.alerts.AlertFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HostConfigController {
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;

    @FXML
    private void handleOK(ActionEvent actionEvent) {
        String h = hostField.getText().trim();
        String pText = portField.getText().trim();
        try {
            int p = Integer.parseInt(pText);
            AppConfig.setHost(h);
            AppConfig.setPort(p);
            Stage cfgStage = (Stage) hostField.getScene().getWindow();
            cfgStage.setTitle("Login");
            cfgStage.close();
        } catch (NumberFormatException e) {
            AlertFactory.showErrorAlert("Порт должен быть числом").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
