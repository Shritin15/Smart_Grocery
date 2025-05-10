package models;

import java.sql.Connection;
import utils.DBConnection;

public class DBTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Connected to database!");
            }
        } catch (Exception e) {
            System.out.println("Failed to connect.");
            e.printStackTrace();
        }
    }
}
