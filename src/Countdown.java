import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Countdown extends JFrame {
    private int[] secondHolder = new int[1];
    protected int[] minuteHolder = new int[1];
    protected int minuteFlag;

    protected JLabel counterLabel;
    protected Timer timer;

    public Countdown(int second, int minute) {
        this.secondHolder[0] = second;
        this.minuteHolder[0] = minute;
    }

    public void puzzleTimer(int second, int minute, Font pixelFont, JPanel puzzlePanel) {
        add(counterLabel, BorderLayout.NORTH);
        counterLabel.setFont(pixelFont);
        counterLabel.setBackground(Color.BLACK);
        counterLabel.setOpaque(true);
        counterLabel.setForeground(Color.WHITE);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        //Uses Factory Pattern?? JAJAJA
        counterLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        counterLabel.setBounds(300, 320, 200, 200);
        add(puzzlePanel, BorderLayout.CENTER);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondHolder[0]--;
                if(secondHolder[0] < 0 && minuteFlag == 0){
                    minuteFlag = 1;
                    secondHolder[0] = 59;
                    minuteHolder[0] = 0;
                }
                if(minuteFlag == 0){
                    if(secondHolder[0]<10){
                        counterLabel.setText(minuteHolder[0]+":0"+secondHolder[0]);
                    }else{
                        counterLabel.setText(minuteHolder[0]+":"+secondHolder[0]);
                    }
                }else{
                    if(secondHolder[0]<10){
                        counterLabel.setText("0"+secondHolder[0]);
                    }else{
                        counterLabel.setText(""+secondHolder[0]);
                    }
                }

                if(secondHolder[0] < 0 && minuteHolder[0] == 0){
                    counterLabel.setText("You ran out of time!");
                    JOptionPane.showMessageDialog(null, "You failed. Try again?");
                    secondHolder[0] = 15;
                    minuteHolder[0] = 1;
                    minuteFlag = 0;
                    //shufflePuzzle();
                }
            }
        });

        // ... (rest of your code)
    }
}

