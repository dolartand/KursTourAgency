package com.client.Controllers;

import com.client.Service.ProfileService;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.ProfileResponse;
import com.kurs.dto.UserProfile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private TextField name_txt;
    @FXML
    private TextField surname_txt;
    @FXML
    private TextField otch_txt;
    @FXML
    private TextField email_txt;
    @FXML
    private TextField phoneNumber_txt;
    @FXML
    private Button edit_btn;
    @FXML
    private Button save_btn;
    @FXML
    private DatePicker birthDate;

    private UserProfile currentUserProfile;
    private ProfileService profileService = new ProfileService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUserProfile();
        setFieldsEditable(false);
        save_btn.setDisable(false);
    }

    private void loadUserProfile() {
        new Thread(() -> {
            try {
                ProfileResponse resp = profileService.fetchProfile();
                if (resp.isSuccess() && resp.getUserProfile() != null) {
                    currentUserProfile = resp.getUserProfile();
                    Platform.runLater(() -> {
                        name_txt.setText(currentUserProfile.getName());
                        surname_txt.setText(currentUserProfile.getLastName());
                        otch_txt.setText(currentUserProfile.getSurname());
                        email_txt.setText(currentUserProfile.getEmail());
                        phoneNumber_txt.setText(currentUserProfile.getPhone());
                        birthDate.setValue(currentUserProfile.getBirthDate());
                    });
                } else {
                    Platform.runLater(() -> {AlertFactory.showErrorAlert(resp.getMessage());});
                }
            } catch (Exception e) {
                AlertFactory.showErrorAlert("Невозможно загрузить профиль пользователя").showAndWait();
            }
        }).start();
    }

    private void setFieldsEditable(boolean editable) {
        name_txt.setEditable(editable);
        surname_txt.setEditable(editable);
        otch_txt.setEditable(editable);
        email_txt.setEditable(editable);
        phoneNumber_txt.setEditable(editable);
        birthDate.setDisable(!editable);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        setFieldsEditable(true);
        edit_btn.setDisable(true);
        save_btn.setDisable(false);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        UserProfile updated = new UserProfile();
        updated.setId(currentUserProfile.getId());
        updated.setName(name_txt.getText());
        updated.setLastName(surname_txt.getText());
        updated.setSurname(otch_txt.getText());
        updated.setEmail(email_txt.getText());
        updated.setPhone(phoneNumber_txt.getText());
        updated.setBirthDate(birthDate.getValue());

        new Thread(() -> {
            try {
                ProfileResponse resp = profileService.updateProfile(updated);
                if (resp.isSuccess()) {
                    currentUserProfile = resp.getUserProfile();
                    Platform.runLater(() -> {
                        setFieldsEditable(false);
                        edit_btn.setDisable(false);
                        save_btn.setDisable(true);
                        Platform.runLater(() -> {AlertFactory.showInfoAlert(resp.getMessage()).showAndWait();});
                    });
                } else {
                    Platform.runLater(() -> { AlertFactory.showErrorAlert(resp.getMessage()); });
                }
            } catch (Exception e) {
                Platform.runLater(() -> { AlertFactory.showErrorAlert("Невозможно сохранить изменения").showAndWait();});
            }
        }).start();
    }
}
