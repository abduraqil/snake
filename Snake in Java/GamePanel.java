import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
    private static final long serialVersionID = 1L;

    static final int width = 500;
    static final int height = 500;
    static final int unitSize = 20;
    static final int numberOfUnits = (width * height) / (unitSize * unitSize);

    // hold x and y cords for body part of the snake
    final int x[] = new int[numberOfUnits];
    final int y[] = new int[numberOfUnits];

    // initial length of the snake
    int length = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addFood();
        running = true;

        timer = new Timer (80, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() { // shift the snake one unit to desired destination for one move
        for (int i = length; i > 0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if (direction == 'L') {
            x[0] = x[0] - unitSize;
        } else if (direction == 'R') {
            x[0] = x[0] + unitSize;
        } else if (direction == 'U') {
            y[0] = y[0] - unitSize;
        } else if (direction == 'D') {
            y[0] = y[0] + unitSize;
        }
    }

    public void checkFood() {
        if (x[0] == foodX & y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }

    public void draw(Graphics graphics) {

        if (running) {
            graphics.setColor(new Color(210, 115, 90));
            graphics.fillOval(foodX, foodY, unitSize, unitSize);

            graphics.setColor(new Color(40, 200, 150));
            graphics.fillRect(x[0], y[0], unitSize, unitSize);

            for (int i = 1; i < length; i++) {
                graphics.setColor(new Color(40, 200, 150));
                graphics.fillRect(x[i], y[i], unitSize, unitSize);
            }
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (width - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int)(width/unitSize)) * unitSize;
        foodY = random.nextInt((int)(width/unitSize)) * unitSize;
    }

    public void checkHit() {
        // check if head has run into the body
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // check if head runs into wall
        if (x[0] < 0 || x[0] > width || y[0] < 0 || y[0] > height) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }
    
    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (width - metrics.stringWidth("Game OVer")) / 2, height / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (width - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}