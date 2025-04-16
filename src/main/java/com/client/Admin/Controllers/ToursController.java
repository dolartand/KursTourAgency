package com.client.Admin.Controllers;

import com.client.Admin.Service.AdminService;
import com.client.Admin.Service.AdminTourService;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.AdminDTOs.DeleteTourResponse;
import com.kurs.dto.TourDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ToursController implements Initializable {
    @FXML
    private TableView<TourDTO> tours_table;
    @FXML
    private TableColumn<TourDTO, String> title_column;
    @FXML
    private TableColumn<TourDTO, String> description_column;
    @FXML
    private TableColumn<TourDTO, String> country_column;
    @FXML
    private TableColumn<TourDTO, LocalDate> startDate_column;
    @FXML
    private TableColumn<TourDTO, Integer> nights_column;
    @FXML
    private TableColumn<TourDTO, Double> price_column;
    @FXML
    private TableColumn<TourDTO, String> food_column;
    @FXML
    private TableColumn<TourDTO, Integer> capacity_column;

    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button delete_btn;

    private AdminTourService adminTourService = new AdminTourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        country_column.setCellValueFactory(new PropertyValueFactory<>("country"));
        startDate_column.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        nights_column.setCellValueFactory(new PropertyValueFactory<>("nights"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        food_column.setCellValueFactory(new PropertyValueFactory<>("food"));
        capacity_column.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        loadTours();
    }

    private void loadTours() {
        Task<List<TourDTO>> task = new Task<>() {
            @Override
            protected List<TourDTO> call() throws Exception {
                return adminTourService.fetchTours();
            }
        };

        task.setOnSucceeded(event -> {
            ObservableList<TourDTO> tours = FXCollections.observableArrayList(task.getValue());
            tours_table.setItems(tours);
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait();
            });
        });
        new Thread(task).start();
    }

    @FXML
    private void handleAdd(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/AdminViews/TourView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.showAndWait();
        loadTours();
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) throws IOException {
        TourDTO selected = tours_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertFactory.showInfoAlert("Необходимо выбрать тур для редактирования").showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/client/AdminViews/TourView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ((TourController) loader.getController()).initData(selected);
        stage.showAndWait();
        loadTours();
    }

    @FXML
    private void handleDelete(ActionEvent actionEvent) {
        TourDTO selected = tours_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertFactory.showInfoAlert("Необходимо выбрать тур для редактирования").showAndWait();
            return;
        }

        boolean confirmed = AlertFactory.showConfirmationAlert("Удалить тур \"" + selected.getTitle() + "\"?");
        if (!confirmed) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                DeleteTourResponse resp = adminTourService.deleteTour(selected.getId());
                if (!resp.isSuccess()) {
                    throw new Exception(resp.getMessage());
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> { AlertFactory.showInfoAlert("Тур удален").showAndWait(); });
            loadTours();
        });
        task.setOnFailed(event -> {
            Platform.runLater(() -> { AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait(); });
        });
        new Thread(task).start();
    }
}
