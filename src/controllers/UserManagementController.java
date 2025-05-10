package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.User;
import utils.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserManagementController {


    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private Button deleteUserButton;
    @FXML private Button backButton;


    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private String currentAdminUsername;


    public void setCurrentAdminUsername(String username) {
        this.currentAdminUsername = username;
    }


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));


        userTable.setItems(userList);
        loadUsers();


        deleteUserButton.setOnAction(e -> handleDeleteUser());
        backButton.setOnAction(e -> goBackToMain());
    }


    private void loadUsers() {
        userList.clear();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users_sm");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userList.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Please select a user to delete.");
            return;
        }


        if (selectedUser.getUsername().equals(currentAdminUsername)) {
            showAlert("You cannot delete your own admin account.");
            return;
        }


        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users_sm WHERE id = ?");
            stmt.setInt(1, selectedUser.getId());
            stmt.executeUpdate();
            showAlert("User deleted successfully.");
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error deleting user.");
        }
    }


    private void goBackToMain() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close(); // or switch to main view if needed
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}





