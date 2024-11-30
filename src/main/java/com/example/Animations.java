package com.example;

import javazoom.jl.player.Player;
import lombok.SneakyThrows;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

class Animations extends KittyWindow {

    Java2DFrameConverter converter = new Java2DFrameConverter();
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(getClass().getClassLoader().getResourceAsStream("NuclearBOOM.mp4"));
    Canvas canvas = new Canvas();


    BufferedImage loseTextlabel = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("gameOver-Photoroom.png")));
    BufferedImage resizeLoseIcon = resize(loseTextlabel, 400, 400);
    JLabel loseTextLabel = new JLabel(new ImageIcon(resizeLoseIcon));
    BufferedImage winLabel = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(("venok-Photoroom.png"))));
    BufferedImage resizeWinIcon = resize(winLabel, 400, 400);
    JLabel WinLabel = new JLabel(new ImageIcon(resizeWinIcon));

    BufferedImage HelloKitty = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("Remove-bg.ai_1732888615606.png")));
    BufferedImage resizeHelloKitty = resize(HelloKitty, 200, 200);
    JLabel HelloKittyLabel = new JLabel(new ImageIcon(resizeHelloKitty));
    public Animations() throws IOException {
        super();


        bombandKittyPanel.add(canvas);
        bombandKittyPanel.add(loseTextLabel);
        loseTextLabel.setBounds(100,50, 400, 400);
        canvas.setVisible(false);
        loseTextLabel.setVisible(false);

        bombandKittyPanel.add(WinLabel);
        WinLabel.setBounds(100,50, 400, 400);
        WinLabel.setVisible(false);


        bombandKittyPanel.add(HelloKittyLabel);
        HelloKittyLabel.setBounds(200,130, 200, 200);
        HelloKittyLabel.setVisible(false);








        canvas.setBounds(150, 400, kittyLabel.getWidth()+50, kittyLabel.getHeight()-50);
        canvas.setBackground(Color.white);
        makeAbOOM();
        Timer timer = new Timer();
        TimerTask checkMoves = new TimerTask() {
            @SneakyThrows
            public void run() {
                if(bombLabel.getY()==kittyLabel.getY()+100){


                    canvas.setVisible(true);
                    TimeUnit.SECONDS.sleep(3);
                    loseTextLabel.setVisible(true);


                    makeAbOOM();

                    timer.cancel();
                }
                if(clickCounter==40){
                    InputStream fis =  getClass().getClassLoader().getResourceAsStream("krasava.mp3");
                    assert fis != null;
                    Player PLAYER = new Player(fis);
                    PLAYER.play();
                }
                if(clickCounter==75){
                    WinLabel.setVisible(true);

                    HelloKittyLabel.setVisible(true);

                    InputStream fis1 =getClass().getClassLoader().getResourceAsStream("children-yay-sfx.mp3");
                    assert fis1 != null;
                    Player player1 =new Player(fis1);
                    player1.play();



                }
            }
        };
        timer.schedule(checkMoves, 0, 50);



    }
    void makeAbOOM(){

        try {
            grabber.start();
            new Thread(this::run).start();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }}


    public void run() {
        while (true) {
            try {
                Frame frame = grabber.grabImage();

                if (frame == null) {
                    break;
                }

                // Конвертируем Frame в BufferedImage
                BufferedImage image = converter.getBufferedImage(frame);

                // Применяем функцию для удаления зеленого фона
                BufferedImage transparentImage = removeGreenBackground(image);

                // Рисуем изображение на Canvas
                Graphics g = canvas.getGraphics();
                g.drawImage(transparentImage, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
                g.dispose();

                Thread.sleep(33);
            } catch (RuntimeException | InterruptedException | FFmpegFrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            grabber.stop();
            canvas.setVisible(false);
        } catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage removeGreenBackground(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Создаем новый BufferedImage с поддержкой альфа-канала
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = image.getRGB(x, y);
                int r = (rgba >> 16) & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = rgba & 0xFF;

                // Условие для определения зеленого цвета
                if (r < 100 && g > 150 && b < 100) { // Параметры могут быть отрегулированы
                    outputImage.setRGB(x, y, 0x00000000); // Прозрачный пиксель
                } else {
                    outputImage.setRGB(x, y, rgba); // Сохранить оригинальный цвет
                }
            }
        }


        return outputImage;
    }
}
