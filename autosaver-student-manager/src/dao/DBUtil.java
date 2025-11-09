package dao;
import java.sql.*;
import javax.swing.JOptionPane;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/belajar";
    private static final String USER = "postgres";
    private static final String PASS = "";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver loaded.");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "PostgreSQL JDBC Driver tidak ditemukan!\n" + e.getMessage(),
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal terhubung ke database!\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Database connection failed", e);
        }
    }
}