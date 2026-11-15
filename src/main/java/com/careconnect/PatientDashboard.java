package com.careconnect;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JFrame {

    private final String username;
    private final int userId;

    public PatientDashboard(String username, int userId) {

        this.username = username;
        this.userId = userId;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {
        }

        setTitle("CareConnect | Patient Dashboard");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        createUI();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(248, 250, 252)
        );

        // =====================================================
        // SIDEBAR
        // =====================================================

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(240, 750)
        );

        sidebar.setBackground(
                new Color(24, 39, 56)
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 20, 25, 20
                )
        );

        // Logo

        JLabel logo =
                new JLabel("CareConnect");

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        logo.setForeground(Color.WHITE);

        logo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        sidebar.add(logo);

        sidebar.add(
                Box.createVerticalStrut(8)
        );

        JLabel patientText =
                new JLabel("Patient Portal");

        patientText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        patientText.setForeground(
                new Color(190, 200, 210)
        );

        patientText.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        sidebar.add(patientText);

        sidebar.add(
                Box.createVerticalStrut(40)
        );

        // =====================================================
        // SIDEBAR BUTTONS
        // =====================================================

        JButton dashboardButton =
                createSidebarButton(
                        "Dashboard"
                );

        JButton doctorsButton =
                createSidebarButton(
                        "Find a Doctor"
                );

        JButton appointmentsButton =
                createSidebarButton(
                        "My Appointments"
                );

        JButton profileButton =
                createSidebarButton(
                        "My Profile"
                );

        sidebar.add(dashboardButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(doctorsButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(appointmentsButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(profileButton);

        sidebar.add(
                Box.createVerticalGlue()
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        JButton logoutButton =
                createSidebarButton(
                        "Logout"
                );

        sidebar.add(logoutButton);

        // =====================================================
        // SIDEBAR ACTIONS
        // =====================================================

        dashboardButton.addActionListener(
                e -> {
                    // Already on dashboard
                }
        );

        doctorsButton.addActionListener(
                e -> {
                    new FindDoctorFrame(userId)
                            .setVisible(true);
                }
        );

        appointmentsButton.addActionListener(
                e -> {
                    new MyAppointmentsFrame(userId)
                            .setVisible(true);
                }
        );

        profileButton.addActionListener(
                e -> {
                    new MyProfileFrame(userId)
                            .setVisible(true);
                }
        );

        logoutButton.addActionListener(
                e -> {

                    int choice =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Are you sure you want to logout?",
                                    "Logout",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (choice ==
                            JOptionPane.YES_OPTION) {

                        dispose();

                        new LoginFrame()
                                .setVisible(true);
                    }
                }
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                new Color(248, 250, 252)
        );

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        45, 45, 40, 45
                )
        );

        // =====================================================
        // WELCOME
        // =====================================================

        JLabel welcome =
                new JLabel(
                        "Welcome, " + username + " 👋"
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        welcome.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(welcome);

        content.add(
                Box.createVerticalStrut(8)
        );

        JLabel subtitle =
                new JLabel(
                        "Manage your healthcare appointments easily."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(100, 105, 110)
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(subtitle);

        content.add(
                Box.createVerticalStrut(40)
        );

        // =====================================================
        // QUICK ACTION TITLE
        // =====================================================

        JLabel quickTitle =
                new JLabel("Quick Actions");

        quickTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        quickTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(quickTitle);

        content.add(
                Box.createVerticalStrut(20)
        );

        // =====================================================
        // CARDS
        // =====================================================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        cardsPanel.setOpaque(false);

        cardsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        cardsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        // Find Doctor

        JPanel doctorCard =
                createCard(
                        "Find a Doctor",
                        "Search doctors by name, specialization and department."
                );

        doctorCard.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        doctorCard.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        new FindDoctorFrame(userId)
                                .setVisible(true);
                    }
                }
        );

        // Book Appointment

        JPanel bookCard =
                createCard(
                        "Book Appointment",
                        "Choose a doctor and schedule your hospital visit."
                );

        bookCard.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        bookCard.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        new FindDoctorFrame(userId)
                                .setVisible(true);
                    }
                }
        );

        // My Appointments

        JPanel appointmentCard =
                createCard(
                        "My Appointments",
                        "View your upcoming and previous appointments."
                );

        appointmentCard.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        appointmentCard.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        new MyAppointmentsFrame(userId)
                                .setVisible(true);
                    }
                }
        );

        cardsPanel.add(doctorCard);
        cardsPanel.add(bookCard);
        cardsPanel.add(appointmentCard);

        content.add(cardsPanel);

        content.add(
                Box.createVerticalStrut(40)
        );

        // =====================================================
        // INFORMATION CARD
        // =====================================================

        JPanel infoCard =
                new JPanel(
                        new BorderLayout()
                );

        infoCard.setBackground(Color.WHITE);

        infoCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                28,
                                25,
                                28
                        )
                )
        );

        infoCard.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        infoCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        JLabel infoTitle =
                new JLabel(
                        "Your Healthcare, Simplified"
                );

        infoTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        JLabel infoText =
                new JLabel(
                        "<html>"
                                + "Find trusted doctors, book appointments and "
                                + "manage your visits from one place."
                                + "</html>"
                );

        infoText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        infoText.setForeground(
                new Color(
                        100,
                        105,
                        110
                )
        );

        JPanel infoTextPanel =
                new JPanel();

        infoTextPanel.setLayout(
                new BoxLayout(
                        infoTextPanel,
                        BoxLayout.Y_AXIS
                )
        );

        infoTextPanel.setOpaque(false);

        infoTextPanel.add(infoTitle);

        infoTextPanel.add(
                Box.createVerticalStrut(10)
        );

        infoTextPanel.add(infoText);

        infoCard.add(
                infoTextPanel,
                BorderLayout.CENTER
        );

        content.add(infoCard);

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        mainPanel.add(
                sidebar,
                BorderLayout.WEST
        );

        mainPanel.add(
                content,
                BorderLayout.CENTER
        );

        add(mainPanel);
    }

    // =========================================================
    // SIDEBAR BUTTON CREATOR
    // =========================================================

    private JButton createSidebarButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(
                        24,
                        39,
                        56
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFocusPainted(false);

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        return button;
    }

    // =========================================================
    // CARD CREATOR
    // =========================================================

    private JPanel createCard(
            String title,
            String description
    ) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        230,
                                        235
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                22,
                                25,
                                22
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + "<div style='width:200px;'>"
                                + description
                                + "</div>"
                                + "</html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        descriptionLabel.setForeground(
                new Color(
                        100,
                        105,
                        110
                )
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(descriptionLabel);

        return card;
    }
}