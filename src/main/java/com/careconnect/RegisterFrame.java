package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class RegisterFrame extends JFrame {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;

    public RegisterFrame() {

        FlatLightLaf.setup();

        setTitle("CareConnect | Create Account");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(
                new BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        );

        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 65, 35, 65
                )
        );

        // =========================
        // TITLE
        // =========================

        JLabel title = new JLabel("Create your account");

        title.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        title.setForeground(
                new Color(35, 35, 35)
        );

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Register as a patient to book appointments"
        );

        subtitle.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        subtitle.setForeground(
                new Color(100, 100, 100)
        );

        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(
                Box.createVerticalStrut(5)
        );
        mainPanel.add(subtitle);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // =========================
        // FULL NAME
        // =========================

        addLabel(mainPanel, "Full Name");

        fullNameField = createTextField();

        mainPanel.add(fullNameField);

        mainPanel.add(
                Box.createVerticalStrut(14)
        );

        // =========================
        // USERNAME
        // =========================

        addLabel(mainPanel, "Username");

        usernameField = createTextField();

        mainPanel.add(usernameField);

        mainPanel.add(
                Box.createVerticalStrut(14)
        );

        // =========================
        // EMAIL
        // =========================

        addLabel(mainPanel, "Email");

        emailField = createTextField();

        mainPanel.add(emailField);

        mainPanel.add(
                Box.createVerticalStrut(14)
        );

        // =========================
        // PHONE
        // =========================

        addLabel(mainPanel, "Phone Number");

        phoneField = createTextField();

        mainPanel.add(phoneField);

        mainPanel.add(
                Box.createVerticalStrut(14)
        );

        // =========================
        // PASSWORD
        // =========================

        addLabel(mainPanel, "Password");

        passwordField = new JPasswordField();

        passwordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        passwordField.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        mainPanel.add(passwordField);

        // =========================
        // SHOW PASSWORD
        // =========================

        JCheckBox showPassword =
                new JCheckBox("Show password");

        showPassword.setBackground(Color.WHITE);

        showPassword.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        showPassword.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        showPassword.addActionListener(e -> {

            if (showPassword.isSelected()) {

                passwordField.setEchoChar((char) 0);

            } else {

                passwordField.setEchoChar('•');
            }
        });

        mainPanel.add(showPassword);

        mainPanel.add(
                Box.createVerticalStrut(20)
        );

        // =========================
        // CREATE ACCOUNT BUTTON
        // =========================

        JButton registerButton =
                new JButton("CREATE ACCOUNT");

        registerButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        registerButton.setForeground(Color.WHITE);

        registerButton.setBackground(
                new Color(30, 136, 229)
        );

        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);

        registerButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        registerButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        registerButton.addActionListener(
                e -> registerPatient()
        );

        mainPanel.add(registerButton);

        add(mainPanel);
    }

    // =========================
    // ADD LABEL
    // =========================

    private void addLabel(
            JPanel panel,
            String text
    ) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5)
        );
    }

    // =========================
    // CREATE TEXT FIELD
    // =========================

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        return field;
    }

    // =========================
    // REGISTER PATIENT
    // =========================

    private void registerPatient() {

        String fullName =
                fullNameField.getText().trim();

        String username =
                usernameField.getText().trim();

        String email =
                emailField.getText().trim();

        String phone =
                phoneField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // =========================
        // VALIDATION
        // =========================

        if (fullName.isEmpty()
                || username.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!email.contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!phone.matches("\\d{10}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Invalid Phone",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (password.length() < 4) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 4 characters.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Connection connection = null;

        try {

            // =========================
            // CONNECT TO MYSQL
            // =========================

            connection =
                    DBConnection.getConnection();

            connection.setAutoCommit(false);

            // =========================
            // CHECK USERNAME
            // =========================

            String checkSQL =
                    "SELECT id FROM users WHERE username = ?";

            PreparedStatement checkStatement =
                    connection.prepareStatement(
                            checkSQL
                    );

            checkStatement.setString(
                    1,
                    username
            );

            ResultSet checkResult =
                    checkStatement.executeQuery();

            if (checkResult.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username already exists.\nPlease choose another username.",
                        "Registration Failed",
                        JOptionPane.WARNING_MESSAGE
                );

                checkResult.close();
                checkStatement.close();

                return;
            }

            checkResult.close();
            checkStatement.close();

            // =========================
            // HASH PASSWORD
            // =========================

            String hashedPassword =
                    hashPassword(password);

            // =========================
            // INSERT INTO USERS
            // =========================

            String userSQL =
                    "INSERT INTO users " +
                            "(username, password_hash, role) " +
                            "VALUES (?, ?, 'USER')";

            PreparedStatement userStatement =
                    connection.prepareStatement(
                            userSQL,
                            Statement.RETURN_GENERATED_KEYS
                    );

            userStatement.setString(
                    1,
                    username
            );

            userStatement.setString(
                    2,
                    hashedPassword
            );

            userStatement.executeUpdate();

            ResultSet keys =
                    userStatement.getGeneratedKeys();

            int userId = 0;

            if (keys.next()) {

                userId =
                        keys.getInt(1);
            }

            keys.close();
            userStatement.close();

            if (userId == 0) {

                throw new SQLException(
                        "Could not generate user ID."
                );
            }

            // =========================
            // GENERATE PATIENT ID
            // =========================

            String patientCode =
                    "PAT" +
                            String.format(
                                    "%04d",
                                    userId
                            );

            // =========================
            // INSERT INTO PATIENTS
            // =========================

            String patientSQL =
                    "INSERT INTO patients " +
                            "(patient_code, full_name, gender, dob, phone, email, address) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement patientStatement =
                    connection.prepareStatement(
                            patientSQL
                    );

            patientStatement.setString(
                    1,
                    patientCode
            );

            patientStatement.setString(
                    2,
                    fullName
            );

            patientStatement.setString(
                    3,
                    "Not Specified"
            );

            patientStatement.setDate(
                    4,
                    Date.valueOf("2000-01-01")
            );

            patientStatement.setString(
                    5,
                    phone
            );

            patientStatement.setString(
                    6,
                    email
            );

            patientStatement.setString(
                    7,
                    ""
            );

            patientStatement.executeUpdate();

            patientStatement.close();

            // =========================
            // SAVE EVERYTHING
            // =========================

            connection.commit();

            // =========================
            // SUCCESS MESSAGE
            // =========================

            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!\n\n"
                            + "Patient ID: "
                            + patientCode
                            + "\n\n"
                            + "You can now sign in.",
                    "Welcome to CareConnect",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Close registration window
            dispose();

        } catch (Exception ex) {

            // =========================
            // ROLLBACK IF ERROR
            // =========================

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception ignored) {
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed:\n\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();

        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception ignored) {
            }
        }
    }

    // =========================
    // SHA-256 PASSWORD HASHING
    // =========================

    private String hashPassword(
            String password
    ) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance(
                        "SHA-256"
                );

        byte[] hash =
                digest.digest(
                        password.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder hex =
                new StringBuilder();

        for (byte b : hash) {

            String h =
                    Integer.toHexString(
                            0xff & b
                    );

            if (h.length() == 1) {
                hex.append('0');
            }

            hex.append(h);
        }

        return hex.toString();
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            RegisterFrame frame =
                    new RegisterFrame();

            frame.setVisible(true);
        });
    }
}