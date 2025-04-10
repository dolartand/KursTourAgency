package com.client.Controllers;

import com.client.Service.TourService;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.TourDTO;
import com.kurs.dto.TourRequest;
import com.kurs.dto.TourResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ToursController implements Initializable {
    @FXML
    private TextField country_txt;
    @FXML
    private DatePicker tour_date;
    @FXML
    private TextField nights_txt;
    @FXML
    private TextField amount_txt;
    @FXML
    private TextField min_price_txt;
    @FXML
    private TextField max_price_txt;
    @FXML
    private ChoiceBox<String> food_box;
    @FXML
    private Button find_btn;
    @FXML
    private TableView<TourDTO> table_tours;
    @FXML
    private TableColumn<TourDTO, String> title_column;
    @FXML
    private TableColumn<TourDTO, Double> price_column;
    @FXML
    private TableColumn<TourDTO, Void> action_column;

    private TourService tourService = new TourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));

        addButtonToTable();

        food_box.getItems().addAll("Завтрак", "Обед", "Ужин", "Все включено");
        food_box.setValue("Все включено");
    }

    private void addButtonToTable() {
        Callback<TableColumn<TourDTO, Void>, TableCell<TourDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<TourDTO, Void> call(final TableColumn<TourDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Забронировать");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TourDTO tourDTO = getTableView().getItems().get(getIndex());
                            AlertFactory.showInfoAlert("ЗАММЕНИТЬ ЭТОТ КОД НА ВЫЗОВ МЕТОДА БРОНИРОВАНИЯ");
                            // bookTour(tour.getId());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        action_column.setCellFactory(cellFactory);
    }

    @FXML
    private void findTours() {
        Task<List<TourDTO>> task = new Task<>() {
            @Override
            protected List<TourDTO> call() throws Exception {
                String country = country_txt.getText();
                double minPrice = Double.parseDouble(min_price_txt.getText());
                double maxPrice = Double.parseDouble(max_price_txt.getText());
                TourRequest req = new TourRequest(country, minPrice, maxPrice);
                TourResponse resp = tourService.fetchTours(req);
                if (resp.isSuccess()) {
                    return resp.getTours();
                } else {
                    throw new Exception(resp.getMessage());
                }
            }
        };
        task.setOnSucceeded(event -> {
            ObservableList<TourDTO> list = FXCollections.observableArrayList(task.getValue());
            table_tours.setItems(list);
        });
        task.setOnFailed(event -> {
            Platform.runLater(() -> {AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait();});
            System.out.println(task.getException().getMessage());

        });
        new Thread(task).start();
    }
}
