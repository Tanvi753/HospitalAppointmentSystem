package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class BookAppointmentFrame extends JFrame {

    private final int userId;
    private final int doctorId;
    private final String doctorName;
    private final String specialization;

    private JTextField dateField;
    private JComboBox<String> timeBox;
    private JTextArea reasonArea;

    public BookAppointmentFrame(
            int userId,
            int doctorId,
            String doctorName,
            String specialization
    ) {

        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {
        }

        setTitle("CareConnect | Book Appointment");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(
                new BoxLayout(
                        mainPanel,
                        BoxLayout.Y_AXIS
                )
        );

        mainPanel.setBackground(
                new Color(248, 250, 252)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 30, 40
                )
        );

        // ================= HEADER =================

        JLabel title =
                new JLabel("Book Appointment");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(title);

        mainPanel.add(
                Box.createVerticalStrut(8)
        );

        JLabel subtitle =
                new JLabel(
                        "Schedule your visit with your selected doctor."
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

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(subtitle);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // ================= DOCTOR CARD =================

        JPanel doctorCard = new JPanel();

        doctorCard.setLayout(
                new BoxLayout(
                        doctorCard,
                        BoxLayout.Y_AXIS
                )
        );

        doctorCard.setBackground(Color.WHITE);

        doctorCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 20, 18, 20
                        )
                )
        );

        doctorCard.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        doctorCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        JLabel doctorLabel =
                new JLabel(
                        "Doctor: " + doctorName
                );

        doctorLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        JLabel specializationLabel =
                new JLabel(
                        "Specialization: " + specialization
                );

        specializationLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        specializationLabel.setForeground(
                new Color(90, 90, 90)
        );

        doctorCard.add(doctorLabel);

        doctorCard.add(
                Box.createVerticalStrut(8)
        );

        doctorCard.add(specializationLabel);

        mainPanel.add(doctorCard);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // ================= DATE =================

        JLabel dateLabel =
                new JLabel("Appointment Date");

        dateLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        dateLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(dateLabel);

        mainPanel.add(
                Box.createVerticalStrut(7)
        );

        dateField =
                new JTextField();

        dateField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        dateField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        dateField.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        dateField.setToolTipText(
                "Format: YYYY-MM-DD"
        );

        mainPanel.add(dateField);

        mainPanel.add(
                Box.createVerticalStrut(5)
        );

        JLabel dateHint =
                new JLabel(
                        "Enter date in YYYY-MM-DD format"
                );

        dateHint.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        dateHint.setForeground(
                new Color(120, 120, 120)
        );

        dateHint.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(dateHint);

        mainPanel.add(
                Box.createVerticalStrut(20)
        );

        // ================= TIME =================

        JLabel timeLabel =
                new JLabel("Appointment Time");

        timeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        timeLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(timeLabel);

        mainPanel.add(
                Box.createVerticalStrut(7)
        );

        timeBox = new JComboBox<>();

        timeBox.addItem("09:00");
        timeBox.addItem("09:30");
        timeBox.addItem("10:00");
        timeBox.addItem("10:30");
        timeBox.addItem("11:00");
        timeBox.addItem("11:30");
        timeBox.addItem("12:00");
        timeBox.addItem("12:30");
        timeBox.addItem("14:00");
        timeBox.addItem("14:30");
        timeBox.addItem("15:00");
        timeBox.addItem("15:30");
        timeBox.addItem("16:00");
        timeBox.addItem("16:30");
        timeBox.addItem("17:00");

        timeBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        timeBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(timeBox);

        mainPanel.add(
                Box.createVerticalStrut(20)
        );

        // ================= REASON =================

        JLabel reasonLabel =
                new JLabel("Reason for Visit");

        reasonLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        reasonLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(reasonLabel);

        mainPanel.add(
                Box.createVerticalStrut(7)
        );

        reasonArea =
                new JTextArea(5, 30);

        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);

        reasonArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JScrollPane reasonScroll =
                new JScrollPane(reasonArea);

        reasonScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        reasonScroll.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        mainPanel.add(reasonScroll);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // ================= BUTTONS =================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton cancelButton =
                new JButton("Cancel");

        JButton confirmButton =
                new JButton("Confirm Appointment");

        cancelButton.setPreferredSize(
                new Dimension(100, 40)
        );

        confirmButton.setPreferredSize(
                new Dimension(190, 40)
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        buttonPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        mainPanel.add(buttonPanel);

        // ================= ACTIONS =================

        cancelButton.addActionListener(
                e -> dispose()
        );

        confirmButton.addActionListener(
                e -> bookAppointment()
        );

        add(mainPanel);
    }

    // =================================================
    // BOOK APPOINTMENT
    // =================================================

    private void bookAppointment() {

        String dateText =
                dateField.getText().trim();

        String timeText =
                timeBox.getSelectedItem().toString();

        String reason =
                reasonArea.getText().trim();

        // ================= CHECK DATE =================

        if (dateText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an appointment date.",
                    "Missing Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate appointmentDate;

        try {

            appointmentDate =
                    LocalDate.parse(dateText);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n\n"
                            + "Please use YYYY-MM-DD.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ================= CHECK PAST DATE =================

        if (appointmentDate.isBefore(
                LocalDate.now()
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "You cannot book an appointment for a past date.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ================= SQL =================

        String patientSql =
                "SELECT id FROM patients WHERE patient_code = ?";

        String checkSql =
                "SELECT id FROM appointments "
                        + "WHERE doctor_id = ? "
                        + "AND appointment_date = ? "
                        + "AND appointment_time = ? "
                        + "AND status <> 'CANCELLED'";

        String insertSql =
                "INSERT INTO appointments "
                        + "(patient_id, doctor_id, "
                        + "appointment_date, appointment_time, "
                        + "reason, status) "
                        + "VALUES (?, ?, ?, ?, ?, 'PENDING')";

        try (
                Connection conn =
                        DBConnection.getConnection()
        ) {

            // =================================================
            // FIND PATIENT
            // =================================================

            int patientId = -1;

            // Example:
            // User ID 9 → PAT0009
            String patientCode =
                    "PAT" + String.format(
                            "%04d",
                            userId
                    );

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    patientSql
                            )
            ) {

                stmt.setString(
                        1,
                        patientCode
                );

                ResultSet rs =
                        stmt.executeQuery();

                if (rs.next()) {

                    patientId =
                            rs.getInt("id");
                }
            }

            // Patient not found
            if (patientId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Patient record not found.\n\n"
                                + "Patient Code: "
                                + patientCode,
                        "Booking Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // =================================================
            // CHECK WHETHER SLOT IS ALREADY BOOKED
            // =================================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    checkSql
                            )
            ) {

                stmt.setInt(
                        1,
                        doctorId
                );

                stmt.setDate(
                        2,
                        Date.valueOf(
                                appointmentDate
                        )
                );

                stmt.setTime(
                        3,
                        Time.valueOf(
                                timeText + ":00"
                        )
                );

                ResultSet rs =
                        stmt.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "This time slot is already booked.\n\n"
                                    + "Please select another time.",
                            "Slot Unavailable",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            // =================================================
            // INSERT APPOINTMENT
            // =================================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    insertSql
                            )
            ) {

                stmt.setInt(
                        1,
                        patientId
                );

                stmt.setInt(
                        2,
                        doctorId
                );

                stmt.setDate(
                        3,
                        Date.valueOf(
                                appointmentDate
                        )
                );

                stmt.setTime(
                        4,
                        Time.valueOf(
                                timeText + ":00"
                        )
                );

                if (reason.isEmpty()) {

                    stmt.setNull(
                            5,
                            Types.LONGVARCHAR
                    );

                } else {

                    stmt.setString(
                            5,
                            reason
                    );
                }

                int rows =
                        stmt.executeUpdate();

                if (rows > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment booked successfully! 🎉\n\n"
                                    + "Doctor: "
                                    + doctorName
                                    + "\nSpecialization: "
                                    + specialization
                                    + "\nDate: "
                                    + dateText
                                    + "\nTime: "
                                    + timeText
                                    + "\nStatus: PENDING",
                            "Appointment Confirmed",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to book appointment.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}