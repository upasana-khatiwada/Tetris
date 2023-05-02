import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
    //represents the number of frames per second that will be displayed on the screen
    private static  int FPS = 60;
    //represents the delay time between each frame in milliseconds.
    private static int delay = 1000/FPS;
    public static final int BOARD_WIDTH =10;
    public static final int BOARD_HEIGHT =20;
    public static final int BLOCK_SIZE =30;

    private Timer loop;

    //to create grid
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    private Shape[] shapes = new Shape[7];
    private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
            Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    private Shape currentShape;
    private Random random;

    public Board(){
        random = new Random();


        // create shapes
        shapes[0] = new Shape(new int[][]{
                {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        shapes[1] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new Shape(new int[][]{
                {1, 1, 1},
                {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new Shape(new int[][]{
                {1, 1, 1},
                {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new Shape(new int[][]{
                {0, 1, 1},
                {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new Shape(new int[][]{
                {1, 1, 0},
                {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new Shape(new int[][]{
                {1, 1},
                {1, 1}, // O shape;
        }, this, colors[6]);

       currentShape = shapes[0];


        // Create a Timer object to update the game at regular intervals
        loop = new Timer(delay, new ActionListener() {

            // Initialize a counter for the ActionListener
            int n =0;
            //This block of code checks whether enough time has elapsed since the last time the game object was updated,
            // and if so, updates the object's position by incrementing the "y" variable by 1.
            @Override
            public void actionPerformed(ActionEvent e) {
                update();

            // Repaint the game object to reflect the updated state
                repaint();
            }
        });
        // Start the Timer object to begin updating the game
        loop.start();
    }
    private void update(){
        currentShape.update();

    }
    public void setCurrentShape(){
        currentShape.reset();


    }
    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {

                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }

            }
        }
        currentShape.render(g);


        //if we draw a shape later than grid it override means we cannot see the grid line
        g.setColor(Color.white);

        //loop to create grid in board
        //row
        for(int i =0;i < BOARD_HEIGHT ;i ++){
            //for second line suppose i=2 i<20;i++
            //then it draw line at coordinate (0,30*2,30*10,30*2) ie (0,60,300,60)
            g.drawLine(0,BLOCK_SIZE*i,BLOCK_SIZE*BOARD_WIDTH,BLOCK_SIZE*i);
        }
        //column
        for(int j=0;j<BOARD_WIDTH+1;j++){
            g.drawLine(BLOCK_SIZE*j,0,BLOCK_SIZE*j,BLOCK_SIZE*BOARD_HEIGHT);
        }




    }
    public Color[][] getBoard(){
        return board;
    }

    @Override
   public void keyTyped(KeyEvent e){

    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()== KeyEvent.VK_DOWN){
            currentShape.speedUp();

        }else if(e.getKeyCode()== KeyEvent.VK_RIGHT){
            currentShape.moveRight();
        }
        else if(e.getKeyCode()== KeyEvent.VK_LEFT){
            currentShape.moveLeft();
        }else if(e.getKeyCode()== KeyEvent.VK_UP){
            currentShape.rotateShape();
        }

    }
    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()== KeyEvent.VK_DOWN){
            currentShape.speedDown();

        }

    }
}
