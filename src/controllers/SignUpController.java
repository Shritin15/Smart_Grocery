package controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import utils.DBConnection;
import utils.PasswordHashingUtils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SignUpController {


    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Button signUpButton;
    @FXML private Button backButton;


    @FXML
    public void initialize() {
        signUpButton.setOnAction(e -> handleSignUp());
        backButton.setOnAction(e -> goBackToLogin());
    }


    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm = confirmField.getText().trim();


        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }


        if (!password.equals(confirm)) {
            showAlert("Passwords do not match.");
            return;
        }


        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users_sm WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                showAlert("Username already exists.");
                return;
            }


            String hashedPassword = PasswordHashingUtils.hashPassword(password);


            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO users_sm (username, password, role) VALUES (?, ?, ?)");
            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            insertStmt.setString(3, "user");
            insertStmt.executeUpdate();


            showAlert("Account created successfully. Please log in.");
            goBackToLogin();


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error creating account.");
        }
    }


    private void goBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/views/style.css").toExternalForm());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SmartGrocery - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Could not load login screen.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SmartGrocery");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}





