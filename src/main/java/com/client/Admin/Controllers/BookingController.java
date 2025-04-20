package com.client.Admin.Controllers;

import com.client.Admin.Service.AdminBookingService;
import com.kurs.dto.AdminDTOs.AdminBookingDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
    @FXML
    private TableView<AdminBookingDTO> bookings_table;
    @FXML
    private TableColumn<AdminBookingDTO, Integer> colId, colUserId, colTourId;
    @FXML
    private TableColumn<AdminBookingDTO, LocalDate> colDate;
    @FXML
    private TableColumn<AdminBookingDTO, String> colStatus;
    @FXML
    private TableColumn<AdminBookingDTO, String> colUserName;
    @FXML
    private TableColumn<AdminBookingDTO, String> colTourName;
    @FXML
    private Button approve_btn, reject_btn;

    private AdminBookingService adminBookingService = new AdminBookingService();

    @FXML
    private void handleApprove(ActionEvent actionEvent) {
        AdminBookingDTO selected = bookings_table.getSelectionModel().getSelectedItem();
        if (selected != null && adminBookingService.approve(selected.getId())) {
            loadBookings();
        }
    }

    @FXML
    private void handleReject(ActionEvent actionEvent) {
        AdminBookingDTO selected = bookings_table.getSelectionModel().getSelectedItem();
        if (selected != null && adminBookingService.reject(selected.getId())) {
            loadBookings();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colTourId.setCellValueFactory(new PropertyValueFactory<>("tourId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colTourName.setCellValueFactory(new PropertyValueFactory<>("tourName"));

        loadBookings();
    }

    private void loadBookings() {
        List<AdminBookingDTO> list = adminBookingService.fetchAll();
        bookings_table.setItems(FXCollections.observableArrayList(list));
    }
}
