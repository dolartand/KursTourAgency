package com.client.Admin.Controllers;

import com.client.Admin.Service.AdminService;
import com.kurs.alerts.AlertFactory;
import com.kurs.dto.AdminDTOs.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    @FXML
    private TableView<UserDTO> users_table;
    @FXML
    private TableColumn<UserDTO, Integer> id_column;
    @FXML
    private TableColumn<UserDTO, String> name_column;
    @FXML
    private TableColumn<UserDTO, String> first_name_column;
    @FXML
    private TableColumn<UserDTO, String> surname_column;
    @FXML
    private TableColumn<UserDTO, String> email_column;
    @FXML
    private TableColumn<UserDTO, String> phone_column;
    @FXML
    private TableColumn<UserDTO, String> role_column;
    @FXML
    private TableColumn<UserDTO, LocalDate> birth_date_column;
    @FXML
    private TableColumn<UserDTO, Void> delete_column;
    @FXML
    private TableColumn<UserDTO, Void> promote_column;

    private AdminService adminService = new AdminService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        first_name_column.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("surname"));
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
        role_column.setCellValueFactory(new PropertyValueFactory<>("role"));
        birth_date_column.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        addDeleteButtonToTable();
        addPromoteButtonToTable();
        loadUsers();
    }

    private void loadUsers() {
        Task<List<UserDTO>> task = new Task<>() {
            @Override
            protected List<UserDTO> call() throws Exception {
                return adminService.getUsers();
            }
        };

        task.setOnSucceeded(event -> {
            ObservableList<UserDTO> users = FXCollections.observableArrayList(task.getValue());
            users_table.setItems(users);
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                AlertFactory.showErrorAlert(task.getException().getMessage()).showAndWait();
            });
        });
        new Thread(task).start();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<UserDTO, Void>, TableCell<UserDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<UserDTO, Void> call(final TableColumn<UserDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Удалить");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            UserDTO user = getTableView().getItems().get(getIndex());
                            boolean confirmed = AlertFactory.showConfirmationAlert("Удалить пользователя " + user.getName() + " " + user.getFirstName() + "?");
                            if (confirmed) {
                                boolean success = adminService.deleteUser(user.getId());
                                if (success) {
                                    AlertFactory.showInfoAlert("Пользователь удалён.").showAndWait();
                                    loadUsers();
                                } else {
                                    AlertFactory.showErrorAlert("Не удалось удалить пользователя.").showAndWait();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
        delete_column.setCellFactory(cellFactory);
    }

    private void addPromoteButtonToTable() {
        Callback<TableColumn<UserDTO, Void>, TableCell<UserDTO, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<UserDTO, Void> call(final TableColumn<UserDTO, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Сделать администратором");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            UserDTO user = getTableView().getItems().get(getIndex());
                            boolean confirmed = AlertFactory.showConfirmationAlert("Сделать пользователя " + user.getName() + " " + user.getSurname() + " администратором?");
                            if (confirmed) {
                                boolean success = adminService.promoteToAdmin(user.getId());
                                if (success) {
                                    AlertFactory.showInfoAlert("Пользователь теперь администратор.").showAndWait();
                                    loadUsers();
                                } else {
                                    AlertFactory.showErrorAlert("Не удалось изменить роль пользователя.").showAndWait();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
        promote_column.setCellFactory(cellFactory);
    }
}
