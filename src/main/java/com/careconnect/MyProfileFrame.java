package com.careconnect;

import com.careconnect.db.DBConnection;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyProfileFrame extends JFrame {

    private final int userId;

    private JLabel patientIdLabel;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel dobLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel addressLabel;

    public MyProfileFrame(int userId) {

        this.userId = userId;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {
        }

        setTitle("CareConnect | My Profile");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();
        loadProfile();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel();

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
                new JLabel("My Profile");

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
                        "View your registered personal information."
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

        // ================= PROFILE CARD =================

        JPanel profileCard =
                new JPanel();

        profileCard.setLayout(
                new GridLayout(
                        7,
                        2,
                        15,
                        15
                )
        );

        profileCard.setBackground(
                Color.WHITE
        );

        profileCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)
                        ),
                        BorderFactory.createEmptyBorder(
                                25, 25, 25, 25
                        )
                )
        );

        profileCard.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        profileCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        400
                )
        );

        // Labels

        profileCard.add(
                createTitleLabel("Patient ID")
        );

        patientIdLabel =
                createValueLabel();

        profileCard.add(
                patientIdLabel
        );

        profileCard.add(
                createTitleLabel("Full Name")
        );

        nameLabel =
                createValueLabel();

        profileCard.add(
                nameLabel
        );

        profileCard.add(
                createTitleLabel("Gender")
        );

        genderLabel =
                createValueLabel();

        profileCard.add(
                genderLabel
        );

        profileCard.add(
                createTitleLabel("Date of Birth")
        );

        dobLabel =
                createValueLabel();

        profileCard.add(
                dobLabel
        );

        profileCard.add(
                createTitleLabel("Phone")
        );

        phoneLabel =
                createValueLabel();

        profileCard.add(
                phoneLabel
        );

        profileCard.add(
                createTitleLabel("Email")
        );

        emailLabel =
                createValueLabel();

        profileCard.add(
                emailLabel
        );

        profileCard.add(
                createTitleLabel("Address")
        );

        addressLabel =
                createValueLabel();

        profileCard.add(
                addressLabel
        );

        mainPanel.add(profileCard);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // ================= CLOSE BUTTON =================

        JButton closeButton =
                new JButton("Close");

        closeButton.setPreferredSize(
                new Dimension(
                        100,
                        40
                )
        );

        closeButton.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        mainPanel.add(closeButton);

        add(mainPanel);
    }

    // =========================================================
    // LABEL HELPERS
    // =========================================================

    private JLabel createTitleLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                new Color(70, 70, 70)
        );

        return label;
    }

    private JLabel createValueLabel() {

        JLabel label =
                new JLabel("-");

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        label.setForeground(
                new Color(30, 30, 30)
        );

        return label;
    }

    // =========================================================
    // LOAD PROFILE
    // =========================================================

    private void loadProfile() {

        String patientCode =
                "PAT" + String.format(
                        "%04d",
                        userId
                );

        String sql =
                "SELECT "
                        + "patient_code, "
                        + "full_name, "
                        + "gender, "
                        + "dob, "
                        + "phone, "
                        + "email, "
                        + "address "
                        + "FROM patients "
                        + "WHERE patient_code = ?";

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

            if (rs.next()) {

                patientIdLabel.setText(
                        rs.getString(
                                "patient_code"
                        )
                );

                nameLabel.setText(
                        rs.getString(
                                "full_name"
                        )
                );

                genderLabel.setText(
                        rs.getString(
                                "gender"
                        )
                );

                Date dob =
                        rs.getDate("dob");

                if (dob != null) {

                    dobLabel.setText(
                            dob.toString()
                    );

                } else {

                    dobLabel.setText("-");
                }

                phoneLabel.setText(
                        rs.getString("phone")
                );

                emailLabel.setText(
                        rs.getString("email")
                );

                String address =
                        rs.getString("address");

                if (address == null ||
                        address.trim().isEmpty()) {

                    address = "Not provided";
                }

                addressLabel.setText(
                        address
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Patient profile not found.",
                        "Profile Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load profile.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}