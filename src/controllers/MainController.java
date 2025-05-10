package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import models.GroceryItems;
import utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;


public class MainController {
    @FXML private TableView<GroceryItems> tableView;
    @FXML private TableColumn<GroceryItems, String> nameColumn;
    @FXML private TableColumn<GroceryItems, Integer> quantityColumn;
    @FXML private TableColumn<GroceryItems, LocalDate> expiryColumn;
    @FXML private TableColumn<GroceryItems, Long> daysLeftColumn;
    @FXML private TableColumn<GroceryItems, String> statusColumn;
    @FXML private TableColumn<GroceryItems, String> usernameColumn;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private DatePicker expiryPicker;
    @FXML private Label welcomeLabel;
    @FXML private Button deleteButton;
    @FXML private Button insertButton;
    @FXML private Button manageUsersButton;
    @FXML private Button logoutButton;


    private final ObservableList<GroceryItems> groceryList = FXCollections.observableArrayList();
    private String loggedInUser;
    private String userRole;


    public void setLoggedInUser(String username, String role) {
        this.loggedInUser = username;
        this.userRole = role;
        welcomeLabel.setText("Welcome, " + username + "!");
        if (!"admin".equals(role)) {
            deleteButton.setVisible(false);
            manageUsersButton.setVisible(false);
            usernameColumn.setVisible(false);
        }
        loadItemsFromDatabase();
    }


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        expiryColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        daysLeftColumn.setCellValueFactory(new PropertyValueFactory<>("daysLeft"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableView.setItems(groceryList);
        tableView.setOnMouseClicked(this::handleRowClick);
    }


    private void handleRowClick(MouseEvent event) {
        GroceryItems selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nameField.setText(selected.getName());
            quantityField.setText(String.valueOf(selected.getQuantity()));
            expiryPicker.setValue(selected.getExpiryDate());
        }
    }


    @FXML
    public void handleAddItem() {
        String name = nameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        LocalDate expiry = expiryPicker.getValue();
        if (name.isEmpty() || quantityText.isEmpty() || expiry == null) {
            showAlert("Please fill all fields.");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Invalid quantity.");
            return;
        }


        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO items_sm (name, quantity, expiry_date, status, added_by) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDate(3, Date.valueOf(expiry));
            stmt.setString(4, calculateStatus(expiry));
            stmt.setString(5, loggedInUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        clearFields();
        loadItemsFromDatabase();
    }


    @FXML
    public void handleDeleteItem() {
        GroceryItems selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an item to delete.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM items_sm WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadItemsFromDatabase();
    }


    @FXML
    public void handleEditItem() {
        GroceryItems selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an item to edit.");
            return;
        }


        String name = nameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        LocalDate expiry = expiryPicker.getValue();


        if (name.isEmpty() || quantityText.isEmpty() || expiry == null) {
            showAlert("Please fill all fields.");
            return;
        }


        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Invalid quantity.");
            return;
        }


        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE items_sm SET name = ?, quantity = ?, expiry_date = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDate(3, Date.valueOf(expiry));
            stmt.setString(4, calculateStatus(expiry));
            stmt.setInt(5, selected.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        clearFields();
        loadItemsFromDatabase();
    }


    public void loadItemsFromDatabase() {
        groceryList.clear();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM items_sm";
            if ("user".equals(userRole)) {
                sql += " WHERE added_by = ?";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            if ("user".equals(userRole)) {
                stmt.setString(1, loggedInUser);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                groceryList.add(new GroceryItems(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getString("added_by")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String calculateStatus(LocalDate expiryDate) {
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        if (daysLeft < 0) return "Expired";
        else if (daysLeft <= 3) return "Expiring Soon";
        else return "Fresh";
    }


    private void clearFields() {
        nameField.clear();
        quantityField.clear();
        expiryPicker.setValue(null);
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    @FXML
    private void openUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserManagementView.fxml"));
            Parent root = loader.load();
            UserManagementController controller = loader.getController();
            controller.setCurrentAdminUsername(loggedInUser);
            Stage stage = new Stage();
            stage.setTitle("User Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





