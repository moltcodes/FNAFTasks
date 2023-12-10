import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

/*--------------------------------CHANGE FILES----------------------------------*/
public class WhackAFreddy extends JFrame {
    private static final int GRID_SIZE = 6;
    private static final int MOLE_COUNT = 10;
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private Sound sound = new Sound();
    private Timer timer;

    private Font pixelFont;
    private Random random;
    private int score = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WhackAFreddy());
    }

    public WhackAFreddy() {
        setTitle("Whack-a-Freddy");
        setSize(450, 460);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buttons = new JButton[GRID_SIZE][GRID_SIZE];
        scoreLabel = new JLabel("Score: 0");

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(Color.GRAY);
        scorePanel.add(scoreLabel);

        FlowLayout layout = (FlowLayout)scorePanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        random = new Random();
        add(scorePanel, BorderLayout.NORTH);
        initializeButtons();
        initializeTimer();
        setVisible(true);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 2, 0));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        playMusic(4);

        //------------> DO NOT CHANGE
        try {
            InputStream is = getClass().getResourceAsStream("Resources/Fonts/Minecraft.ttf");
            assert is != null;
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
        } catch (Exception e) {
            System.out.println("Font error!");
        }

        scoreLabel.setFont(pixelFont);
    }

    private void initializeButtons() {
        JPanel grid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        grid.setBackground(Color.GRAY);
        add(grid, BorderLayout.CENTER);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j] = new JButton();
                grid.add(buttons[i][j]);
                buttons[i][j].setBorder(new RoundedBorder(16)); //CAN BE ADJUSTED, FOR BORDER RADIUS
                buttons[i][j].setBackground(Color.GRAY);
                buttons[i][j].addActionListener(new MoleListener(i, j));
            }
        }
    }

    private void initializeTimer() {
        timer = new Timer(900, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideMoles();
                showRandomMole();
            }
        });
        timer.start();
    }

    private void hideMoles() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                buttons[i][j].setIcon(null);
                buttons[i][j].setBackground(Color.GRAY);
                buttons[i][j].setText("");
            }
        }
    }

    private void showRandomMole() {
        int row = random.nextInt(GRID_SIZE);
        int col = random.nextInt(GRID_SIZE);

        /*--------------------------------CHANGE FILES HERE----------------------------------*/
        try{
            Image img;
            if(row%2 == 0){
                img = ImageIO.read(Objects.requireNonNull(getClass().getResource("Resources/Pictures/freddy.png")));
                buttons[row][col].setText("1");
            }else{
                img = ImageIO.read(Objects.requireNonNull(getClass().getResource("Resources/Pictures/purpleMan.png")));
                buttons[row][col].setText("0");
            }
            Image newImg = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
            buttons[row][col].setIcon(new ImageIcon(newImg));
        }catch (ImagingOpException | IOException e){
            System.out.println("Freddy error!");
        }

        if(score==10){
            win();
        }

    }

    public void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(null, "You whacked Purple Man! Try again?");
        restartGame();
    }

    public void restartGame(){
        timer.start();
        hideMoles();
        initializeButtons();
        score = 0;
        scoreLabel.setText("Score: "+score);
    }

    public void win(){
            JOptionPane.showMessageDialog(null, "Congratulations, you win!");
            System.exit(0);
    }

    private class MoleListener implements ActionListener {
        private int row;
        private int col;

        public MoleListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttons[row][col].getText().equals("1")){
                playSE(5);
                ++score;
                scoreLabel.setText("Score: "+score);
            }else if(buttons[row][col].getText().equals("0")){
                gameOver();
            }else{
                playSE(6);
            }
        }
    }

    private static class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }
        public boolean isBorderOpaque() {
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
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
