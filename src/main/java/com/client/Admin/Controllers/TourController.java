package com.client.Admin.Controllers;

import com.client.Admin.Service.AdminTourService;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.AdminDTOs.AddTourRequest;
import com.kurs.dto.AdminDTOs.AddTourResponse;
import com.kurs.dto.AdminDTOs.UpdateTourRequest;
import com.kurs.dto.AdminDTOs.UpdateTourResponse;
import com.kurs.dto.TourDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TourController implements Initializable {
    @FXML
    private Button save_btn;
    @FXML
    private TextField title_txt;
    @FXML
    private TextField description_txt;
    @FXML
    private TextField country_txt;
    @FXML
    private DatePicker startDate;
    @FXML
    private TextField nights_txt;
    @FXML
    private TextField price_txt;
    @FXML
    private ComboBox<String> food_cb;
    @FXML
    private TextField capacity_txt;

    private AdminTourService adminTourService = new AdminTourService();
    private TourDTO currentTour = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        food_cb.getItems().addAll("Без питания", "Завтрак", "Обед", "Ужин", "Все включено");
        food_cb.setValue("Завтрак");
    }

    public void initData(TourDTO tour) {
        this.currentTour = tour;
        title_txt.setText(tour.getTitle());
        description_txt.setText(tour.getDescription());
        country_txt.setText(tour.getCountry());
        startDate.setValue(tour.getStartDate());
        nights_txt.setText(String.valueOf(tour.getNights()));
        price_txt.setText(String.valueOf(tour.getPrice()));
        food_cb.setValue(tour.getFood());
        capacity_txt.setText(String.valueOf(tour.getCapacity()));
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try {
            String title = title_txt.getText();
            String description = description_txt.getText();
            String country = country_txt.getText();
            LocalDate date = startDate.getValue();
            int nights = Integer.parseInt(nights_txt.getText());
            double price = Double.parseDouble(price_txt.getText());
            String food = food_cb.getValue();
            int capacity = Integer.parseInt(capacity_txt.getText());

            if (currentTour == null) {
                AddTourRequest req = new AddTourRequest();
                req.setTitle(title);
                req.setDescription(description);
                req.setCountry(country);
                req.setStartDate(date);
                req.setNights(nights);
                req.setPrice(price);
                req.setFood(food);
                req.setCapacity(capacity);

                AddTourResponse resp = adminTourService.addTour(req);
                if (resp.isSuccess()) {
                    AlertFactory.showInfoAlert("Тур успешно добавлен").showAndWait();
                    closeWindow();
                } else AlertFactory.showErrorAlert(resp.getMessage()).showAndWait();
            } else {
                UpdateTourRequest req = new UpdateTourRequest();
                req.setId(currentTour.getId());
                req.setTitle(title);
                req.setDescription(description);
                req.setCountry(country);
                req.setStartDate(date);
                req.setNights(nights);
                req.setPrice(price);
                req.setFood(food);
                req.setCapacity(capacity);

                UpdateTourResponse resp = adminTourService.updateTour(req);
                if (resp.isSuccess()) {
                    AlertFactory.showInfoAlert("Тур успешно обновлен").showAndWait();
                    closeWindow();
                } else AlertFactory.showErrorAlert(resp.getMessage()).showAndWait();
            }
        } catch (Exception e) {
            AlertFactory.showErrorAlert("Ошибка ввода данных: " + e.getMessage()).showAndWait();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }
}
