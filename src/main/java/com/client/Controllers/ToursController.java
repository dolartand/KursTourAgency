package com.client.Controllers;

import com.client.Service.TourService;
import com.client.SessionHolder;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.*;
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
import java.time.LocalDate;
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
    private TableColumn<TourDTO, String> country_column;
    @FXML
    private TableColumn<TourDTO, LocalDate> startDate_column;
    @FXML
    private TableColumn<TourDTO, Double> nights_column;
    @FXML
    private TableColumn<TourDTO, String> food_column;
    @FXML
    private TableColumn<TourDTO, Double> price_column;
    @FXML
    private TableColumn<TourDTO, Void> action_column;

    private TourService tourService = new TourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        country_column.setCellValueFactory(new PropertyValueFactory<>("country"));
        startDate_column.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        nights_column.setCellValueFactory(new PropertyValueFactory<>("nights"));
        food_column.setCellValueFactory(new PropertyValueFactory<>("food"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));

        addButtonToTable();

        food_box.getItems().addAll("Завтрак", "Обед", "Ужин", "Все включено");
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
                            String sessionId = SessionHolder.getSessionId();
                            BookTourRequest req = new BookTourRequest(tourDTO.getId(), sessionId);
                            BookTourResponse resp = tourService.bookTour(req);
                            if (resp.isSuccess()) {
                                AlertFactory.showInfoAlert(resp.getMessage()).showAndWait();
                            } else {
                                AlertFactory.showErrorAlert(resp.getMessage()).showAndWait();
                            }
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
                String country = country_txt.getText().trim();
                LocalDate startDate = tour_date.getValue();
                Integer nights = (nights_txt.getText().trim().isEmpty()) ? null : Integer.parseInt(nights_txt.getText().trim());
                Double minPrice = (min_price_txt.getText().trim().isEmpty()) ? null : Double.parseDouble(min_price_txt.getText().trim());
                Double maxPrice = (max_price_txt.getText().trim().isEmpty()) ? null : Double.parseDouble(max_price_txt.getText().trim());
                String food = food_box.getValue();

                TourRequest req = new TourRequest();
                req.setCountry(country);
                req.setStartDate(startDate);
                req.setNights(nights);
                req.setMinPrice(minPrice);
                req.setMaxPrice(maxPrice);
                req.setFood(food);

                TourResponse resp = tourService.fetchTours(req);
                if (resp.isSuccess()) {
                    return resp.getTours();
                } else throw new Exception(resp.getMessage());
            }
        };
        task.setOnSucceeded(event -> {
            ObservableList<TourDTO> tours = FXCollections.observableArrayList(task.getValue());
            table_tours.setItems(tours);
        });
        task.setOnFailed(event -> {
            Platform.runLater(() -> {AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait();});
            System.out.println(task.getException().getMessage());

        });
        new Thread(task).start();
    }
}
