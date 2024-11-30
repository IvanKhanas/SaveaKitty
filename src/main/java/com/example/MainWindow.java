package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javazoom.jl.player.Player;
import lombok.SneakyThrows;

public class MainWindow {

    private final JFrame frame1;

    public MainWindow() {
        frame1 = new JFrame("SaveTheKitty");

        // Create a panel with a background image
        JPanel backgroundPanel = new BackgroundPanel("/fon.jpg"); // Use resource path relative to resources
        backgroundPanel.setLayout(null);

        // Create the "Play" button
        JButton button = new JButton("Play");
        button.setBounds(700, 300, 100, 50); // Set button position and size
        backgroundPanel.add(button); // Add the button to the background panel

        // Add the panel with the background to the JFrame
        frame1.setContentPane(backgroundPanel);

        ActionListener buttonListener = new ButtonListener();
        button.addActionListener(buttonListener);
    }

    @SneakyThrows
    void PlayMP3() {
        // Load the MP3 file as a resource from the JAR
        InputStream fis = getClass().getResourceAsStream("/oan1.mp3");
        if (fis == null) {
            throw new RuntimeException("MP3 file not found in resources");
        }
        Player player = new Player(fis);
        player.play();
    }

    void ShowFirstFrame() {
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame1.setMinimumSize(new Dimension(800, 600));
        frame1.setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        @SneakyThrows
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Play")) {
                Thread moanThread = new Thread(() -> {
                    try {
                        PlayMP3();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                moanThread.start();
                TimeUnit.SECONDS.sleep(2);
                frame1.dispose();

                Animations anim = new Animations(); // Assuming Animations is implemented elsewhere
            }
        }
    }

    // Custom panel for displaying the background image
    static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            // Load the image as a resource from the JAR
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
            if (backgroundImage == null) {
                throw new RuntimeException("Background image not found in resources");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
