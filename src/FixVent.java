import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

/*-------------------SAME MECHANICS AS SLIDING PUZZLE BUT WITH LESS TIME, CHANGE PICTURES TO FORM A VENT----------------------------------*/
public class FixVent extends JFrame {

    private JButton[][] puzzleButtons;
    private JLabel counterLabel1;
    private int emptyRow, emptyCol;
    private Timer timer;
    int second, minute, minuteFlag;
    public Font pixelFont;
    private Sound sound =  new Sound();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FixVent());
    }
    public FixVent() {
        setResizable(false);
        setTitle("Fix Bonnie");
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 750);
        setLocationRelativeTo(null);

        //DO NOT CHANGE
        try{
            InputStream is = getClass().getResourceAsStream("Resources/Fonts/Minecraft.ttf");
            assert is != null;
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f);
            System.out.println("Made it!");

        }catch (Exception e) {
            System.out.println("Font error!");
        }

        puzzleButtons = new JButton[3][3];
        counterLabel1 = new JLabel();
        second = 60;
        //minute = 0;
        minuteFlag = 0;
        puzzleTimer();
        timer.start();

        JPanel puzzlePanel = new JPanel(new GridLayout(3, 3));

        initializePuzzle();
        shufflePuzzle();

        for (int i = 0; i < puzzleButtons.length; i++) {
            for (int j = 0; j < puzzleButtons[i].length; j++) {
                puzzlePanel.add(puzzleButtons[i][j]);
                puzzleButtons[i][j].addActionListener(new PuzzleButtonListener());
                puzzleButtons[i][j].setBorder(new MatteBorder(0,0,0,0,Color.BLACK));
                puzzleButtons[i][j].setBackground(Color.WHITE);

            }
        }

        add(counterLabel1, BorderLayout.NORTH);
        counterLabel1.setFont(pixelFont);
        counterLabel1.setBackground(Color.decode("#0D0D0D"));
        counterLabel1.setOpaque(true);
        counterLabel1.setForeground(Color.decode("#5F5085"));
        counterLabel1.setHorizontalAlignment(JLabel.CENTER);
        //Uses Factory Pattern?? JAJAJA
        counterLabel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        counterLabel1.setBounds(300, 320, 200, 200);
        add(puzzlePanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void initializePuzzle() {
        playMusic(2);
        int count = 1;
        String imgName;
        for (int i = 0; i < puzzleButtons.length; i++) {
            for (int j = 0; j < puzzleButtons[i].length; j++) {
                int number = i * puzzleButtons.length + j + 1;
                puzzleButtons[i][j] = new JButton(String.valueOf(number));
                /*----------------CHANGE imgName TO A MORE SUITABLE IMAGE------------------------------*/
                try{
                    //IMAGE GET IMAGE
                    imgName = "Resources/Pictures/bonnieCut"+count+".jpg";
                    Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgName)));
                    Image newImg = img.getScaledInstance(260, 260, java.awt.Image.SCALE_SMOOTH);
                    puzzleButtons[i][j].setIcon(new ImageIcon(newImg));
                    puzzleButtons[i][j].setBorderPainted(true);
                    if(count != 8){
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Picture error!");
                }
            }
        }
        emptyRow = puzzleButtons.length - 1;
        emptyCol = puzzleButtons[0].length - 1;
        puzzleButtons[emptyRow][emptyCol].setText("");
    }

    private void shufflePuzzle() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            int direction = rand.nextInt(4);
            moveTile(direction);
        }
    }

    private void moveTile(int direction) {
        int newRow = emptyRow;
        int newCol = emptyCol;

        switch (direction) {
            case 0: //up
                newRow = Math.min(emptyRow + 1, puzzleButtons.length - 1);
                break;
            case 1: //down
                newRow = Math.max(emptyRow - 1, 0);
                break;
            case 2: //left
                newCol = Math.min(emptyCol + 1, puzzleButtons[0].length - 1);
                break;
            case 3: //right
                newCol = Math.max(emptyCol - 1, 0);
                break;
        }

        puzzleButtons[emptyRow][emptyCol].setIcon(puzzleButtons[newRow][newCol].getIcon());
        puzzleButtons[newRow][newCol].setIcon(null);
        puzzleButtons[emptyRow][emptyCol].setText(puzzleButtons[newRow][newCol].getText());
        puzzleButtons[newRow][newCol].setText("");
        emptyRow = newRow;
        emptyCol = newCol;
    }

    private class PuzzleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            int row = -1, col = -1;

            //PANGITAA position
            for (int i = 0; i < puzzleButtons.length; i++) {
                for (int j = 0; j < puzzleButtons[i].length; j++) {
                    if (puzzleButtons[i][j] == button) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            // checks if empty button is beside empty space
            if ((Math.abs(row - emptyRow) == 1 && col == emptyCol) || (row == emptyRow && Math.abs(col - emptyCol) == 1)) {
                puzzleButtons[emptyRow][emptyCol].setIcon(button.getIcon());
                puzzleButtons[row][col].setIcon(null);
                puzzleButtons[emptyRow][emptyCol].setText(button.getText());
                puzzleButtons[row][col].setText("");
                emptyRow = row;
                emptyCol = col;

                playSE(1);

                if (isPuzzleSolved()) {
                    JOptionPane.showMessageDialog(FixVent.this, "Congratulations! Puzzle solved!");
                    stopMusic();
                    dispose();
                }
            }
        }

        private boolean isPuzzleSolved() {
            int expectedValue = 1;

            for (int i = 0; i < puzzleButtons.length; i++) {
                for (int j = 0; j < puzzleButtons[i].length; j++) {
                    String buttonText = puzzleButtons[i][j].getText();
                    System.out.println(buttonText);

                    if (buttonText.isEmpty()) {
                        if (!(i == puzzleButtons.length - 1 && j == puzzleButtons[i].length - 1)) {
                            return false;
                        }
                    } else {
                        int value = Integer.parseInt(buttonText);

                        if (value != expectedValue) {
                            return false;
                        }
                        expectedValue = (expectedValue % (puzzleButtons.length * puzzleButtons[0].length)) + 1;
                    }
                }
            }

            return true;
        }
    }

    public void puzzleTimer(){
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second--;
                System.out.println("HAA");
                counterLabel1.setText(""+second);
                if(second == 0){
                    counterLabel1.setText("You ran out of time!");
                    JOptionPane.showMessageDialog(null, "You failed. Try again?");
                    second = 59;
                    shufflePuzzle();
                }
            }
        });
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
