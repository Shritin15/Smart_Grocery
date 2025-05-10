package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.PasswordHashingUtils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoginController {


    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signUpRedirectButton;


    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter both username and password.");
            return;
        }


        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users_sm WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (PasswordHashingUtils.verifyPassword(password, hashedPassword)) {
                    String role = rs.getString("role");


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                    Parent root = loader.load();


                    MainController mainController = loader.getController();
                    mainController.setLoggedInUser(username, role);
                    mainController.loadItemsFromDatabase();


                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/views/style.css").toExternalForm());
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("SmartGrocery");
                    stage.show();
                } else {
                    showAlert("Incorrect password. Please try again.");
                }
            } else {
                showAlert("User does not exist.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred during login.");
        }
    }


    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUpView.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/views/style.css").toExternalForm());
            Stage stage = (Stage) signUpRedirectButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SmartGrocery - Sign Up");
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Unable to load sign-up screen.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}





