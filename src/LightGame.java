import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

/*--------------------------------NOTHING TO CHANGE, TO RUN, CHANGE THE ABSOLUTE PATH OF dot1.txt AT LINE 369----------------------------------*/
class Dimension {
    int x,y;
}
public class LightGame extends JFrame implements MouseMotionListener,MouseListener,ActionListener {
    boolean flag = true;
    boolean win;
    int noOfColors,y=0,cellPos;
    static int levelNo;
    int cellSize=4;
    JFrame frame;
    boolean[] winCond=new boolean[cellSize*cellSize];
    boolean[] cellVisible = new boolean[cellSize*cellSize];
    boolean[] emptyCell = new boolean[noOfColors];

    ArrayList<Integer> []color_select;
    int color[][]=new int[cellSize][cellSize];
    int pos_color[];
    Dimension q = new Dimension();
    int activeCell=1;

    Sound sound = new Sound();
    Dimension operate(int activeCell){
        if(activeCell%cellSize==0){
            q.x=100*cellSize;
            q.y=100+100*((activeCell/cellSize) -1);
        }else{
            q.x=100+100*((activeCell)%cellSize-1);
            q.y=100+100*(activeCell /cellSize);
        }
        return q;
    }
    public LightGame(String levelLoc,int levelNo){
        setTitle("Fix the Lights");
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.decode("#0D0D0D"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        playMusic(7);

        levelNo = this.levelNo;
        frame=new JFrame();
        BufferedReader br = null;

        BufferedReader br2 = null;
        try{
            br = new BufferedReader(new FileReader(levelLoc));
            String contentLine = br.readLine();
            cellSize=Integer.parseInt(contentLine);
            contentLine=br.readLine();
            noOfColors=Integer.parseInt(contentLine);
            pos_color=new int[noOfColors*2];
            winCond=new boolean[cellSize*cellSize];
            cellVisible = new boolean[cellSize*cellSize];
            emptyCell = new boolean[noOfColors];
            color=new int[cellSize][cellSize];
            int i=0;
            while (contentLine != null && i<noOfColors*2) {
                contentLine = br.readLine();
                pos_color[i]=Integer.parseInt(contentLine);
                i++;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally{
            try{
                if (br != null)
                    br.close();
                if (br2 != null)
                    br2.close();
            }catch (IOException ioe){
                System.out.println("Error in closing the BufferedReader");
            }
        }

        setSize( 200+100*cellSize, 200+100*cellSize );
        setVisible(true);
        addMouseMotionListener(this);
        addMouseListener(this);
        color_select =new ArrayList[noOfColors];
        for (int i = 0; i < noOfColors; i++) {
            color_select[i] = new ArrayList<Integer>();
        }
    }
    @Override
    public void paint( Graphics g ){
        g.setColor(Color.decode("#0D0D0D"));
        g.fillRect(0,0,200+100*cellSize,200+100*cellSize);
        g.setColor(Color.WHITE);


        for ( int x = 100; x < 100+100*cellSize; x += 100 ){
            for ( int y = 100; y <100+100*cellSize; y += 100 ){
                g.drawRect( x, y, 100, 100 );
            }
        }

        for(int m=0;m<noOfColors*2;m++){
            if(m<2){
                activeCell=pos_color[m];
                g.setColor(Color.CYAN);
                q=operate(activeCell);
                cellVisible[activeCell-1]=true;
                g.fillOval(q.x+20, q.y+20, 60, 60);
                color[(int)(q.y/100)-1][(int)(q.x/100)-1]=5;
            }else if(m<4){
                activeCell=pos_color[m];
                g.setColor(Color.YELLOW);
                q=operate(activeCell);
                cellVisible[activeCell-1]=true;
                g.fillOval(q.x+20, q.y+20, 60, 60);
                color[(int)(q.y/100)-1][(int)(q.x/100)-1]=6;
            }else if(m<6){
                activeCell=pos_color[m];
                g.setColor(Color.RED);
                q=operate(activeCell);
                cellVisible[activeCell-1]=true;
                g.fillOval(q.x+20, q.y+20, 60, 60);
                color[(int)(q.y/100)-1][(int)(q.x/100)-1]=7;
            }else if(m<8){
                activeCell=pos_color[m];
                g.setColor(Color.MAGENTA);
                q=operate(activeCell);
                cellVisible[activeCell-1]=true;
                g.fillOval(q.x+20, q.y+20, 60, 60);
                color[(int)(q.y/100)-1][(int)(q.x/100)-1]=8;
            }else if(m<16){
                activeCell=pos_color[m];
                g.setColor(Color.PINK);
                q=operate(activeCell);
                cellVisible[activeCell-1]=true;
                g.fillOval(q.x+20, q.y+20, 60, 60);
                color[(int)(q.y/100)-1][(int)(q.x/100)-1]=12;
            }
        }

        Dimension prevXY=new Dimension();
        for(int i=0;i<noOfColors;i++){
            if(i==0)g.setColor(Color.CYAN);
            else if(i==1)g.setColor(Color.YELLOW);
            else if(i==2)g.setColor(Color.RED);
            else if(i==3)g.setColor(Color.MAGENTA);
            else if(i==7)g.setColor(Color.PINK);

            for(int j=1;j<color_select[i].size();j++){
                int dx,dy;
                int ox,oy,px,py;

                Dimension drawe=new Dimension();
                drawe=operate(color_select[i].get(j));

                drawe.x+=50;drawe.y+=50;
                dx=drawe.x;dy=drawe.y;
                prevXY=operate(color_select[i].get(j-1));

                prevXY.x+=50;prevXY.y+=50;
                px=prevXY.x;py=prevXY.y;

                int height,width;
                if(dx==px ){
                    if(py>dy){
                        ox=dx;oy=dy;
                    }else{
                        ox=px;oy=py;
                    }
                    width=10;
                    height=100;
                }else{
                    if(px>dx){
                        ox=dx;
                        oy=dy;
                    }else{
                        ox=px;
                        oy=py;
                    }
                    height=10;
                    width=100;
                }
                g.fillRect(ox, oy, width, height);
            }
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {

        int a,b,x=0;
        a= (e.getX())/100;
        b= e.getY()/100;
        x++;
        if(e.getX()<=100||e.getX()>=100+100*cellSize||e.getY()<=100||e.getY()>=100+100*cellSize){
            return;
        }else if(a>100||a<100+100*cellSize||b>100||b<100+100*cellSize){
            System.out.println(x);
            if(flag){
                if(cellVisible[(b-1)*cellSize+a-1]){
                    y=color[b-1][a-1];
                    flag=false;
                    //System.out.println(x);
                }
            }
            else{
                cellPos=(b-1)*cellSize+a-1;
                if(cellVisible[cellPos]){
                    if(color_select[y-5].size()==0 && y==color[b-1][a-1]){
                        color_select[y-5].add(cellPos+1);
                        winCond[cellPos]=true;
                    }if(color_select[y-5].get(color_select[y-5].size()-1)!=(b-1)*cellSize+a && y==color[b-1][a-1]){
                        if(color_select[y-5].get(0)==cellPos+1){
                            for(int i=0;i<noOfColors;i++){
                                for(int j=0;j<color_select[i].size();j++) {
                                    if(color_select[i].get(j)==cellPos+1) {
                                        for(int k=0;k<color_select[i].size();k++){
                                            int p=color_select[i].get(k);
                                            winCond[p-1]=false;
                                            if(p%cellSize==0){
                                                color[p/cellSize-1][3]=0;
                                            }else{
                                                color[p/cellSize][p%cellSize-1]=0;
                                            }
                                        }
                                        color_select[i].clear();
                                    }
                                }
                            }
                        }
                        else{
                            color_select[y-5].add(cellPos+1);
                            winCond[cellPos]=true;
                        }
                    }
                }
                else if(color[b-1][a-1]==y && color_select[y-5].size()>2) {
                    for(int i=0;i<noOfColors;i++){
                        for(int j=0;j<color_select[i].size()-1;j++) {
                            if(color_select[i].get(j)==cellPos+1) {
                                for(int k=0;k<color_select[i].size();k++) {
                                    int p=color_select[i].get(k);
                                    winCond[p-1]=false;
                                    if(p%cellSize==0){
                                        color[p/cellSize-1][3]=0;
                                    }else{
                                        color[p/cellSize][p%cellSize-1]=0;
                                    }
                                }
                                color_select[i].clear();
                                emptyCell[i]=true;
                            }
                        }
                    }
                }
                else if(color[b-1][a-1]!=y && !cellVisible[cellPos]){
                    for(int i=0;i<noOfColors;i++){
                        for(int j=0;j<color_select[i].size();j++){
                            if(color_select[i].get(j)==cellPos+1){
                                for(int k=0;k<color_select[i].size();k++){
                                    int p=color_select[i].get(k);
                                    winCond[p-1]=false;
                                    if(p%cellSize==0){
                                        color[p/cellSize-1][3]=0;
                                    }else{
                                        color[p/cellSize][p%cellSize-1]=0;
                                    }
                                }
                                color_select[i].clear();
                            }
                        }
                    }
                    color_select[y-5].add(cellPos+1);
                    winCond[cellPos]=true;
                    color[b-1][a-1]=y;
                }

                else{
                    color[b-1][a-1]=y;
                    if(color_select[y-5].size()==0) {
                        color_select[y-5].add(cellPos+1);
                        winCond[cellPos]=true;
                    }else{
                        if(color_select[y-5].get(color_select[y-5].size()-1)!=cellPos+1){
                            color_select[y-5].add(cellPos+1);
                            winCond[cellPos]=true;
                        }
                    }
                }
            }
            repaint();
        }

        for (int i = 0; i < noOfColors; i++) {
            win=true;
            for(int k=0;k<cellSize*cellSize;k++) {
                if(winCond[k]==false)
                    win=false;
            }if(win==true){
                JOptionPane.showMessageDialog(null,"Successfully fixed lights!");
                System.exit(0);
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        playSE(8);
        for(int i=0;i<noOfColors;i++){
            if(emptyCell[i]){
                for(int k=0;k<color_select[i].size();k++){
                    int p=color_select[i].get(k);
                    winCond[p-1]=false;
                    if(p%cellSize==0){
                        color[p/cellSize-1][3]=0;
                    }else{
                        color[p/cellSize][p%cellSize-1]=0;
                    }
                }
                color_select[i].clear();
                emptyCell[i]=false;
            }
        }
        flag=true;
        y=0;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e){

    }
    @Override
    public void mouseExited(MouseEvent e){

    }
    @Override
    public void mousePressed(MouseEvent e){

    }
    public void actionPerformed(ActionEvent e){
        String str=e.getActionCommand();
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
    public static void main( String args[] ){
        String levelLoc="D:\\School\\CIT\\Second Year\\CS227\\Codes\\FNAFTasks\\src\\Resources\\Pictures\\dot1.txt"; //COPY & PASTE ABSOLUTE PATH HERE
        System.out.println(levelLoc);
        LightGame  application = new LightGame (levelLoc,1);
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }


}