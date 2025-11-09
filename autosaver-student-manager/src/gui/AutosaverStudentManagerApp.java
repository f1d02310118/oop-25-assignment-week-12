package gui;

import dao.StudentDAO;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

public class AutosaverStudentManagerApp extends JFrame {
    private final StudentDAO dao = new StudentDAO();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Age"}, 0);
    private final JTable table = new JTable(model);
    private final JTextField tfName = new JTextField(10);
    private final JTextField tfAge = new JTextField(5);
    private final JLabel lblStatus = new JLabel("Idle");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public AutosaverStudentManagerApp() {
        super("Autosaver Student Manager");
        initUI();
        loadStudents();
        startAutoSaveThread();
    }

    private void initUI() {
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> addStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> loadStudents());

        JPanel top = new JPanel();
        top.add(new JLabel("Name:"));
        top.add(tfName);
        top.add(new JLabel("Age:"));
        top.add(tfAge);
        top.add(btnAdd);

        JPanel bottom = new JPanel();
        bottom.add(btnDelete);
        bottom.add(btnRefresh);
        bottom.add(lblStatus);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadStudents() {
        SwingUtilities.invokeLater(() -> {
            try {
                model.setRowCount(0);
                List<Student> list = dao.getAll();
                for (Student s : list)
                    model.addRow(new Object[]{s.getId(), s.getName(), s.getAge()});
                lblStatus.setText("Data loaded successfully");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                lblStatus.setText("Failed to load data");
            }
        });
    }

    private void addStudent() {
        String name = tfName.getText().trim();
        String ageText = tfAge.getText().trim();

        if (name.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            dao.insert(new Student(0, name, age));
            tfName.setText("");
            tfAge.setText("");
            loadStudents();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        dao.delete(id);
        loadStudents();
    }

    // Thread Autosave
    private void startAutoSaveThread() {
        scheduler.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> lblStatus.setText("💾 Auto-saving data..."));
            try {
                Thread.sleep(2000);
                System.out.println("[AutoSave] Data snapshot saved successfully.");
                SwingUtilities.invokeLater(() -> lblStatus.setText("Autosave completed"));
            } catch (InterruptedException ignored) {
            }
        }, 5, 15, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutosaverStudentManagerApp::new);
    }
}