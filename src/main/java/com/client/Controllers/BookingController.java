package com.client.Controllers;

import com.client.Service.BookingService;
import com.client.SessionHolder;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.BookingDTO;
import com.kurs.dto.BookingResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
    @FXML
    private TableView<BookingDTO> booking_table;
    @FXML
    private TableColumn<BookingDTO, String> title_column;
    @FXML
    private TableColumn<BookingDTO, String> country_column;
    @FXML
    private TableColumn<BookingDTO, LocalDate> startDate_column;
    @FXML
    private TableColumn<BookingDTO, Integer> nights_column;
    @FXML
    private TableColumn<BookingDTO, String> food_column;
    @FXML
    private TableColumn<BookingDTO, Double> price_column;
    @FXML
    private TableColumn<BookingDTO, LocalDate> bookingDate_column;

    private BookingService bookingService = new BookingService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        country_column.setCellValueFactory(new PropertyValueFactory<>("country"));
        startDate_column.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        nights_column.setCellValueFactory(new PropertyValueFactory<>("nights"));
        food_column.setCellValueFactory(new PropertyValueFactory<>("food"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        bookingDate_column.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

        loadBookings();
    }

    private void loadBookings() {
        Task<List<BookingDTO>> task = new Task() {
            @Override
            protected List<BookingDTO> call() throws Exception {
                String sessionId = SessionHolder.getSessionId();
                if (sessionId == null) {
                    throw new Exception("SessionId не найден.");
                }

                BookingResponse resp = bookingService.fetchBookings(sessionId);
                if (resp.isSuccess()) {
                    return resp.getBookings();
                } else throw new Exception(resp.getMessage());
            }
        };

        task.setOnSucceeded(e -> {
            ObservableList<BookingDTO> list = FXCollections.observableArrayList(task.getValue());
            booking_table.setItems(list);
        });

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait();
            });
        });

        new Thread(task).start();
    }
}
