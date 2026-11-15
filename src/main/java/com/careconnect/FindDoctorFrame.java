package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FindDoctorFrame extends JFrame {

    private JTextField searchField;
    private JComboBox<String> specializationBox;
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    // Logged-in user's ID
    private final int userId;

    public FindDoctorFrame(int userId) {

        this.userId = userId;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {
        }

        setTitle("CareConnect - Find a Doctor");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadDoctors();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );
        mainPanel.setBackground(new Color(248, 250, 252));

        // ================= HEADER =================

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Find a Doctor");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "Search and choose the right doctor for your appointment"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // ================= SEARCH PANEL =================

        JPanel searchPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 10, 8, 10
                        )
                )
        );

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        searchField = new JTextField(20);
        searchField.setPreferredSize(
                new Dimension(230, 35)
        );

        specializationBox = new JComboBox<>();
        specializationBox.setPreferredSize(
                new Dimension(190, 35)
        );

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(
                new Dimension(100, 35)
        );

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(
                new Dimension(100, 35)
        );

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Specialization:"));
        searchPanel.add(specializationBox);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        searchButton.addActionListener(e -> searchDoctors());

        resetButton.addActionListener(e -> {
            searchField.setText("");
            specializationBox.setSelectedIndex(0);
            loadDoctors();
        });

        searchField.addActionListener(e -> searchDoctors());

        specializationBox.addActionListener(e -> {

            if (specializationBox.getSelectedIndex() > 0) {
                searchDoctors();
            }
        });

        // ================= TABLE =================

        String[] columns = {
                "ID",
                "Doctor Code",
                "Doctor Name",
                "Specialization",
                "Department",
                "Phone",
                "Email",
                "Fee"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);

        doctorTable.setRowHeight(35);

        doctorTable.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        doctorTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        doctorTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(doctorTable);

        // ================= BOOK BUTTON =================

        JButton bookButton =
                new JButton("Book Appointment");

        bookButton.setPreferredSize(
                new Dimension(180, 40)
        );

        bookButton.addActionListener(e -> {

            int selectedRow =
                    doctorTable.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a doctor first.",
                        "No Doctor Selected",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Get doctor ID from first column
            int doctorId =
                    Integer.parseInt(
                            doctorTable
                                    .getValueAt(selectedRow, 0)
                                    .toString()
                    );

            String doctorName =
                    doctorTable
                            .getValueAt(selectedRow, 2)
                            .toString();

            String specialization =
                    doctorTable
                            .getValueAt(selectedRow, 3)
                            .toString();

            // Open actual booking screen
            BookAppointmentFrame bookingFrame =
                    new BookAppointmentFrame(
                            userId,
                            doctorId,
                            doctorName,
                            specialization
                    );

            bookingFrame.setVisible(true);

            // Close Find Doctor screen
            dispose();
        });

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.setOpaque(false);
        bottomPanel.add(bookButton);

        // ================= ADD EVERYTHING =================

        JPanel topPanel =
                new JPanel(
                        new BorderLayout(0, 15)
                );

        topPanel.setOpaque(false);

        topPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        topPanel.add(
                searchPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);
    }

    // ================= LOAD ALL DOCTORS =================

    private void loadDoctors() {

        tableModel.setRowCount(0);

        specializationBox.removeAllItems();

        specializationBox.addItem(
                "All Specializations"
        );

        String sql =
                "SELECT id, doctor_code, full_name, "
                        + "specialization, department, phone, "
                        + "email, consultation_fee "
                        + "FROM doctors "
                        + "ORDER BY full_name";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                tableModel.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("doctor_code"),
                                rs.getString("full_name"),
                                rs.getString("specialization"),
                                rs.getString("department"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                "₹" + rs.getBigDecimal(
                                        "consultation_fee"
                                )
                        }
                );
            }

            loadSpecializations();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load doctors.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= LOAD SPECIALIZATIONS =================

    private void loadSpecializations() {

        String sql =
                "SELECT DISTINCT specialization "
                        + "FROM doctors "
                        + "ORDER BY specialization";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                specializationBox.addItem(
                        rs.getString("specialization")
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load specializations.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= SEARCH DOCTORS =================

    private void searchDoctors() {

        tableModel.setRowCount(0);

        String search =
                searchField.getText().trim();

        String specialization =
                specializationBox.getSelectedItem() == null
                        ? "All Specializations"
                        : specializationBox
                        .getSelectedItem()
                        .toString();

        String sql =
                "SELECT id, doctor_code, full_name, "
                        + "specialization, department, phone, "
                        + "email, consultation_fee "
                        + "FROM doctors "
                        + "WHERE (full_name LIKE ? "
                        + "OR specialization LIKE ? "
                        + "OR department LIKE ?) ";

        if (!specialization.equals(
                "All Specializations")) {

            sql += "AND specialization = ? ";
        }

        sql += "ORDER BY full_name";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            String keyword =
                    "%" + search + "%";

            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);

            if (!specialization.equals(
                    "All Specializations")) {

                stmt.setString(
                        4,
                        specialization
                );
            }

            ResultSet rs =
                    stmt.executeQuery();

            int count = 0;

            while (rs.next()) {

                tableModel.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("doctor_code"),
                                rs.getString("full_name"),
                                rs.getString("specialization"),
                                rs.getString("department"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                "₹" + rs.getBigDecimal(
                                        "consultation_fee"
                                )
                        }
                );

                count++;
            }

            if (count == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "No doctors found matching your search.",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}