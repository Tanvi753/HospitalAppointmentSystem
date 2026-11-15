package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyAppointmentsFrame extends JFrame {

    private final int userId;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public MyAppointmentsFrame(int userId) {

        this.userId = userId;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {
        }

        setTitle("CareConnect | My Appointments");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadAppointments();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 30, 25, 30
                )
        );

        // ================= HEADER =================

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );
        headerPanel.setOpaque(false);

        JLabel title =
                new JLabel("My Appointments");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "View and manage your upcoming appointments."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(100, 100, 100)
        );

        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(7)
        );

        headerPanel.add(subtitle);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // ================= TABLE =================

        String[] columns = {
                "ID",
                "Doctor",
                "Specialization",
                "Date",
                "Time",
                "Reason",
                "Status"
        };

        tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        appointmentTable =
                new JTable(tableModel);

        appointmentTable.setRowHeight(38);

        appointmentTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        appointmentTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        appointmentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        appointmentTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane =
                new JScrollPane(
                        appointmentTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)
                )
        );

        JPanel tablePanel =
                new JPanel(new BorderLayout());

        tablePanel.setOpaque(false);

        tablePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 0, 20, 0
                )
        );

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                tablePanel,
                BorderLayout.CENTER
        );

        // ================= BUTTONS =================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        bottomPanel.setOpaque(false);

        JButton refreshButton =
                new JButton("Refresh");

        JButton cancelButton =
                new JButton("Cancel Appointment");

        JButton closeButton =
                new JButton("Close");

        refreshButton.setPreferredSize(
                new Dimension(100, 40)
        );

        cancelButton.setPreferredSize(
                new Dimension(180, 40)
        );

        closeButton.setPreferredSize(
                new Dimension(90, 40)
        );

        bottomPanel.add(refreshButton);
        bottomPanel.add(cancelButton);
        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // ================= ACTIONS =================

        refreshButton.addActionListener(
                e -> loadAppointments()
        );

        cancelButton.addActionListener(
                e -> cancelAppointment()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);
    }

    // =========================================================
    // LOAD APPOINTMENTS
    // =========================================================

    private void loadAppointments() {

        tableModel.setRowCount(0);

        String patientCode =
                "PAT" + String.format(
                        "%04d",
                        userId
                );

        String sql =
                "SELECT "
                        + "a.id, "
                        + "d.full_name AS doctor_name, "
                        + "d.specialization, "
                        + "a.appointment_date, "
                        + "a.appointment_time, "
                        + "a.reason, "
                        + "a.status "
                        + "FROM appointments a "
                        + "JOIN patients p "
                        + "ON a.patient_id = p.id "
                        + "JOIN doctors d "
                        + "ON a.doctor_id = d.id "
                        + "WHERE p.patient_code = ? "
                        + "ORDER BY "
                        + "a.appointment_date ASC, "
                        + "a.appointment_time ASC";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    patientCode
            );

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                int id =
                        rs.getInt("id");

                String doctor =
                        rs.getString(
                                "doctor_name"
                        );

                String specialization =
                        rs.getString(
                                "specialization"
                        );

                String date =
                        rs.getDate(
                                "appointment_date"
                        ).toString();

                String time =
                        rs.getTime(
                                "appointment_time"
                        ).toString();

                String reason =
                        rs.getString("reason");

                if (reason == null ||
                        reason.isEmpty()) {

                    reason = "-";
                }

                String status =
                        rs.getString("status");

                tableModel.addRow(
                        new Object[]{
                                id,
                                doctor,
                                specialization,
                                date,
                                time,
                                reason,
                                status
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load appointments.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    private void cancelAppointment() {

        int selectedRow =
                appointmentTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an appointment first.",
                    "No Appointment Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                appointmentTable.convertRowIndexToModel(
                        selectedRow
                );

        int appointmentId =
                (int) tableModel.getValueAt(
                        modelRow,
                        0
                );

        String status =
                tableModel.getValueAt(
                        modelRow,
                        6
                ).toString();

        // ================= ALREADY CANCELLED =================

        if (status.equalsIgnoreCase(
                "CANCELLED"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "This appointment is already cancelled.",
                    "Already Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // ================= CONFIRM =================

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this appointment?",
                        "Cancel Appointment",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // ================= UPDATE DATABASE =================

        String sql =
                "UPDATE appointments "
                        + "SET status = 'CANCELLED' "
                        + "WHERE id = ?";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    appointmentId
            );

            int rows =
                    stmt.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Appointment cancelled successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadAppointments();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to cancel appointment.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}