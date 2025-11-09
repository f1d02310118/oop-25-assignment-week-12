package dao;
import model.Student;
import java.sql.*;
import java.util.*;

public class StudentDAO {

    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data: " + e.getMessage());
        }
        return list;
    }

    public void insert(Student s) {
        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.executeUpdate();
            System.out.println("Data mahasiswa berhasil ditambahkan.");
        } catch (SQLException e) {
            System.err.println("Error saat insert: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Data mahasiswa berhasil dihapus (id=" + id + ")");
            else
                System.out.println("Tidak ada data dengan id: " + id);
        } catch (SQLException e) {
            System.err.println("Error saat delete: " + e.getMessage());
        }
    }
}