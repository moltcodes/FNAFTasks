import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/*--------------------------------CHANGE FILES----------------------------------*/
public class FoxyRuns extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 300;
    private static final int GROUND_HEIGHT = 40;
    private static final int PLAYER_SIZE = 50;
    private static final int OBSTACLE_SIZE = 30;
    private static final int MAX_JUMPS = 2;

    private Font pixelFont;
    private Timer timer;
    private int playerY, playerX;
    private int playerSpeed;
    private int jumpHeight;
    private boolean isJumping;
    private int obstacleX;
    private int jumpsRemaining;

    private Sound sound = new Sound();

    private List<Obstacle> obstacles;

    private int score;
    private JLabel scoreLabel;
    private ImageIcon playerIcon, floorIcon, backgroundIcon ;
    private List<ImageIcon> obstacleIcons;

    public FoxyRuns() {
        setTitle("Foxy Runs");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /*----------------------------------CHANGE images TO A MORE SUITABLE IMAGE------------------------------*/
        try{
            playerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/Pictures/Foxy.png")));
            floorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/Pictures/foxyRunsFloor.jpg")));
            obstacleIcons = new ArrayList<>();
            obstacleIcons.add(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/Pictures/purpleMan.png"))));
            backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Resources/Pictures/foxyRunsBG.png")));
            InputStream is = getClass().getResourceAsStream("Resources/Fonts/Minecraft.ttf");
            assert is != null;
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
        }catch (Exception e){
            System.out.println("Image error!");
        }

        playerX = 20;
        playerY = HEIGHT - GROUND_HEIGHT - PLAYER_SIZE;
        playerSpeed = 5;
        jumpHeight = 0;
        isJumping = false;
        jumpsRemaining = MAX_JUMPS;

        playMusic(3);
        obstacles = new ArrayList<>();
        spawnObstacle();

        score = 0;
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(pixelFont);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 2, 0));
        scoreLabel.setForeground(Color.WHITE);
        setLayout(new BorderLayout());
        add(scoreLabel, BorderLayout.NORTH);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(scoreLabel, BorderLayout.NORTH);
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        setContentPane(backgroundLabel);

        timer = new Timer(20, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setResizable(false);
    }

    public void spawnObstacle() {
        Random random = new Random();
        int obstacleY = HEIGHT - GROUND_HEIGHT - OBSTACLE_SIZE;

        int minGap;
        int maxGap;
        int maxDistance;

        if(playerSpeed>=10){
            minGap = 160;
            maxGap = 200;
            maxDistance = 200;
        }else{
            minGap = 160;
            maxGap = 300;
            maxDistance = 100;
        }

        int gap = random.nextInt(maxGap - minGap + 1) + minGap;
        gap = Math.min(gap, maxDistance);
        int rightmostX = 0;
        if (!obstacles.isEmpty()) {
            Obstacle rightmostObstacle = obstacles.get(obstacles.size() - 1);
            rightmostX = rightmostObstacle.getX() + rightmostObstacle.getSize();
        }

        // For minimum distance between obstacles
        obstacleX = Math.max(WIDTH, rightmostX + gap);
        obstacles.add(new Obstacle(obstacleX, obstacleY, OBSTACLE_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            update();
            repaint();
        }
    }

    public void update() {
        if (isJumping) {
            playerY -= jumpHeight;
            jumpHeight--;
            if (playerY >= HEIGHT - GROUND_HEIGHT - PLAYER_SIZE) {
                playerY = HEIGHT - GROUND_HEIGHT - PLAYER_SIZE;
                isJumping = false;
                jumpsRemaining = MAX_JUMPS;
            }
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.move();
            if (obstacle.intersects(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)) {
                gameOver();
                return;
            }
        }

        if (Math.random() < 0.01) {
            spawnObstacle();
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.getX() + obstacle.getSize() < 0) {
                score++;
                updateScoreLabel();
                obstacles.remove(i);
                i--;

            }
        }

        if(score==15){
            JOptionPane.showMessageDialog(FoxyRuns.this, "Congratulations, Foxy escaped!");
            System.exit(0);
        }
    }

    public void jump() {
        if(jumpsRemaining>0){
            isJumping = true;
            jumpHeight = 15;
            jumpsRemaining--;
        }
    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
        if(score%5 == 0 && score != 0){
            playerSpeed+=2;
        }
    }

    public void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(null, "Foxy was caught! Try again?");
        stopMusic();
        restartGame();
    }

    public void restartGame() {
        playerY = HEIGHT - GROUND_HEIGHT - PLAYER_SIZE;
        isJumping = false;
        obstacles.clear();
        jumpsRemaining = 2;
        score = 0;
        playerSpeed = 5;
        playMusic(3);
        timer.start();
        updateScoreLabel();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Draws the floor/ground
        Image floorImage = floorIcon.getImage();
        g.drawImage(floorImage, 0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT, this);

        //Draws the player
        Image playerImage = playerIcon.getImage();
        //Change width & height parameters to change size
        g.drawImage(playerImage, playerX, playerY, 50, 50, this);


        for (Obstacle obstacle : obstacles) {
            //Draws the obstacles
            Image obstacleImage = obstacleIcons.get(0).getImage();
            //Change width & height parameters to change size
            g.drawImage(obstacleImage, obstacle.getX(), obstacle.getY(), 50, 50, this);
        }

        Toolkit.getDefaultToolkit().sync();

    }
    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        // Draw the score label
        scoreLabel.paint(g);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FoxyRuns game = new FoxyRuns();
            game.setVisible(true);
        });
    }

    private class Obstacle {
        private int x;
        private int y;
        private int size;

        public Obstacle(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }

        public void move() {
            x -= playerSpeed;
        }

        public boolean intersects(int otherX, int otherY, int otherWidth, int otherHeight) {
            return x < otherX + otherWidth &&
                    x + size > otherX &&
                    y < otherY + otherHeight &&
                    y + size > otherY;
        }
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic(){
        sound.stop();
    }

    public void playSE(int i){
        sound.setFile(i);
        sound.play();
    }
}
