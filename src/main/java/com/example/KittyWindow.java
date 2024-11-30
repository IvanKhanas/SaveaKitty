package com.example;

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;




public class KittyWindow {
    protected final JTextArea textArea;
    protected int clickCounter;
    public JPanel backgroundPanel;
    public JLabel bombLabel;
    public JFrame frame2;
    JLabel kittyLabel;
    JPanel bombandKittyPanel;
    Player player;

    public KittyWindow() throws IOException {
        Thread audioThread = new Thread(KittyWindow.this::playAlarm);
        audioThread.start();
        // Загрузка изображения фона
        BufferedImage backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("background.jpg")));

        // Создание панели с фоном
        backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Изменение размера фона в соответствии с размером панели
                BufferedImage resizedBackground = resize(backgroundImage, getWidth(), getHeight());
                g.drawImage(resizedBackground, 0, 0, null);
            }

            public static BufferedImage resize(BufferedImage img, int newW, int newH) {
                Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = dimg.createGraphics();
                // Для улучшения качества изображения
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                return dimg;
            }


        };
        backgroundPanel.setLayout(null);

         frame2 = new JFrame("Kitty Window");

        // Создание кнопки
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/heart.png")));
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JButton button = new JButton(scaledIcon);


        // Панель для кнопки, делаем ее прозрачной
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);
        button.setBounds(0,0,50,50);
        buttonPanel.add(button);
        buttonPanel.setBounds(1100, 300, 100,100);

        backgroundPanel.add(buttonPanel);

        // Загрузка изображения котенка
        ImageIcon kittyImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/kitten-Photoroom.png")));
         kittyLabel = new JLabel(kittyImage);
        kittyLabel.setBounds(200, 400, kittyImage.getIconWidth(), kittyImage.getIconHeight()); // Set position        // Панель для котенка, делаем ее прозрачной
         bombandKittyPanel = new JPanel();
        bombandKittyPanel.setLayout(null);

        bombandKittyPanel.setOpaque(false);



        // Настройка текстовой области, делаем панель прозрачной
        textArea = new JTextArea();
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.add(textArea);
        textPanel.setBounds(1080,400, 100, 100);
        backgroundPanel.add(textPanel);

        //Текст около кнопки
        JTextArea buttonText = new JTextArea();
        buttonText.setText("Click Here!");
        JPanel buttonTextPanel = new JPanel();
        buttonTextPanel.setOpaque(false);
        buttonTextPanel.add(buttonText);
        buttonTextPanel.setBounds(1080,350, 100, 100);
        backgroundPanel.add(buttonTextPanel);


        // Настройка бомбы
        BufferedImage bombImg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/photo_2024-11-15_18-17-25-Photoroom.png")));
        BufferedImage resizedBombImg = resize(bombImg, 200, 250);
        bombLabel = new JLabel(new ImageIcon(resizedBombImg));
        bombLabel.setBounds(kittyLabel.getX(),-150, 200,250);
        // Панель для бомбы, делаем ее прозрачной

        bombandKittyPanel.setOpaque(false);




        bombandKittyPanel.add(bombLabel);




        bombandKittyPanel.setBounds(500, 0, 600,1000 );
        bombandKittyPanel.add(kittyLabel);


        backgroundPanel.revalidate();


        backgroundPanel.add(bombandKittyPanel);




        // Добавление слушателя к кнопке
        button.setActionCommand("Click");
        button.addActionListener(new ButtonListener());




        Timer timer1 = new Timer();

        TimerTask moveBombTask = new TimerTask() {
            int y = -150;
            @lombok.SneakyThrows
            @Override
            public void run() {
                if (y < kittyLabel.getY() + 100) { // Adjust final position as needed
                    y += 2;
                    bombLabel.setBounds(kittyLabel.getX(), y, 200, 250);
                }else{bombLabel.setVisible(false); timer1.cancel();}
                textArea.setText(Integer.toString(clickCounter));
                if(bombLabel.getY()==kittyLabel.getY()+100){timer1.cancel();kittyLabel.setVisible(false);bombLabel.setVisible(false);player.close();
                    InputStream fis = getClass().getClassLoader().getResourceAsStream("nuclear-bomb-exploding-close-tomas-herudek-1-00-08.mp3");
                    assert fis != null;
                    player = new Player(fis);
                    player.play();}
                if(clickCounter==75){bombLabel.setVisible(false);player.close();timer1.cancel();}

            }
        };
        timer1.schedule(moveBombTask, 0, 50);

        frame2.add(backgroundPanel);
        frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.setMinimumSize(new Dimension(800, 600));
        frame2.setVisible(true);

    }
    void  playAlarm()  {
        try{
            InputStream fis = getClass().getClassLoader().getResourceAsStream("Voicy_tactical nuke incoming mw2 sound effect.mp3");
            assert fis != null;
            player = new Player(fis);
            player.play();




        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }

    }




// Метод для изменения размера изображения
public static BufferedImage resize(BufferedImage img, int newW, int newH) {
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    // Для улучшения качества изображения
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
}





    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Click")) {
                clickCounter++;
            }
        }
    }





}




