package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        FlatLightLaf.setup();

        setTitle("CareConnect | Hospital Appointment System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);

        // =========================
        // LEFT SIDE
        // =========================

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(30, 136, 229));
        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(80, 55, 50, 55)
        );

        JLabel logo = new JLabel("✚");
        logo.setFont(new Font("Arial", Font.BOLD, 55));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("CareConnect");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "<html>Better care starts<br>with the right doctor.</html>"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 22));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html><br>Book appointments easily.<br>" +
                        "Find the right specialist.<br>" +
                        "Manage your healthcare in one place.</html>"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 15));
        description.setForeground(new Color(230, 245, 255));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(logo);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalStrut(25));
        leftPanel.add(description);

        // =========================
        // RIGHT SIDE
        // =========================

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(
                new BoxLayout(rightPanel, BoxLayout.Y_AXIS)
        );
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(
                BorderFactory.createEmptyBorder(90, 75, 70, 75)
        );

        JLabel welcome = new JLabel("Welcome back!");
        welcome.setFont(new Font("Arial", Font.BOLD, 32));
        welcome.setForeground(new Color(35, 35, 35));
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginText = new JLabel(
                "Sign in to your CareConnect account"
        );
        loginText.setFont(new Font("Arial", Font.PLAIN, 15));
        loginText.setForeground(new Color(110, 110, 110));
        loginText.setAlignmentX(Component.LEFT_ALIGNMENT);

        // USERNAME
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );
        usernameField.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        // PASSWORD
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );
        passwordField.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        // SHOW PASSWORD
        JCheckBox showPassword =
                new JCheckBox("Show password");

        showPassword.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );
        showPassword.setBackground(Color.WHITE);
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

        // SIGN IN BUTTON
        JButton loginButton =
                new JButton("SIGN IN");

        loginButton.setFont(
                new Font("Arial", Font.BOLD, 15)
        );
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(
                new Color(30, 136, 229)
        );
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
        );
        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =========================
        // REAL DATABASE LOGIN
        // =========================

        loginButton.addActionListener(e -> {

            String username =
                    usernameField.getText().trim();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            if (username.isEmpty()
                    || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter username and password.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String sql =
                    "SELECT id, password_hash, role " +
                            "FROM users WHERE username = ?";

            try (
                    Connection connection =
                            DBConnection.getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(1, username);

                ResultSet result =
                        statement.executeQuery();

                if (result.next()) {

                    int userId =
                            result.getInt("id");

                    String storedHash =
                            result.getString("password_hash");

                    String role =
                            result.getString("role");

                    String enteredHash =
                            hashPassword(password);

                    if (enteredHash.equals(storedHash)) {

                        // Open patient dashboard
                        PatientDashboard dashboard =
                                new PatientDashboard(username, userId);

                        dashboard.setVisible(true);

                        // Close login window
                        dispose();
                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Incorrect username or password.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Incorrect username or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

                result.close();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database connection error:\n\n"
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // REGISTER BUTTON
        JButton registerButton =
                new JButton("Create a new account");

        registerButton.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        registerButton.setForeground(
                new Color(30, 136, 229)
        );
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        registerButton.addActionListener(e -> {

            RegisterFrame registerFrame =
                    new RegisterFrame();

            registerFrame.setVisible(true);
        });

        // ADD COMPONENTS
        rightPanel.add(welcome);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(loginText);

        rightPanel.add(Box.createVerticalStrut(35));

        rightPanel.add(usernameLabel);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(usernameField);

        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(passwordLabel);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(passwordField);

        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(showPassword);

        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(loginButton);

        rightPanel.add(Box.createVerticalStrut(12));
        rightPanel.add(registerButton);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    // =========================
    // SHA-256 HASH
    // =========================

    private static String hashPassword(
            String password
    ) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

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

            LoginFrame frame =
                    new LoginFrame();

            frame.setVisible(true);
        });
    }
}