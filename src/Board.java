import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.image.BufferedImage;

public class Board extends JPanel implements KeyListener, MouseListener,MouseMotionListener {
    public static int STATE_GAME_PLAY =0;
    public static int STATE_GAME_PAUSE=1;
    public static int STATE_GAME_OVER=2;
    private boolean leftClick = false;
    private BufferedImage pause, refresh;
    private int score = 0;
    private int mouseX, mouseY;
    private boolean gamePaused = false;

    private boolean gameOver = false;

    private int state = STATE_GAME_PLAY;
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
    private Shape currentShape,nextShape;
    private Random random;
    private Rectangle stopBounds, refreshBounds;
    private Timer buttonLapse = new Timer(300, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });

    public Board(){
        pause = ImageLoader.loadImage("/pause.png");
        refresh = ImageLoader.loadImage("/refresh.png");
        stopBounds = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight() / 2);
        refreshBounds = new Rectangle(350, 500 - refresh.getHeight() - 20, refresh.getWidth(),
                refresh.getHeight() + refresh.getHeight() / 2);

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

        if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver) {
            buttonLapse.start();
            gamePaused = !gamePaused;
        }

        if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
            startGame();
        }

        if (gamePaused || gameOver) {
            return;
        }

        currentShape.update();

    }
    public void setNextShape() {
        int index = random.nextInt(shapes.length);
        int colorIndex = random.nextInt(colors.length);
        nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);
    }
    public void setCurrentShape(){

        currentShape = nextShape;
        setNextShape();

        for (int row = 0; row < currentShape.getCoords().length; row++) {
            for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
                if (currentShape.getCoords()[row][col] != 0) {
                    if (board[currentShape.getY() + row][currentShape.getX() + col] != null) {
                        gameOver = true;
                    }
                }
            }
        }


//        currentShape = shapes[random.nextInt(shapes.length)];
//           currentShape.reset();
//           checkOverGame();




    }
    private void checkOverGame(){
        int [][] coords = currentShape.getCoords();
        for(int row=0;row<coords.length;row++){
            for(int col=0;col<coords[0].length;col++){
                if(coords[row][col] != 0){
                    if(board[row+currentShape.getY()][col+currentShape.getX()]!=null){
                        state = STATE_GAME_OVER;
                    }
                }

            }

        }
    }
    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentShape.render(g);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {

                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }

            }
        }

        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCoords().length; row++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }

        if (stopBounds.contains(mouseX, mouseY)) {
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT), stopBounds.x + 3, stopBounds.y + 3, null);
        } else {
            g.drawImage(pause, stopBounds.x, stopBounds.y, null);
        }

        if (refreshBounds.contains(mouseX, mouseY)) {
            g.drawImage(refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), refreshBounds.x + 3, refreshBounds.y + 3, null);
        } else {
            g.drawImage(refresh, refreshBounds.x, refreshBounds.y, null);
        }

        if (gamePaused) {
            String gamePausedString = "GAME PAUSED";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 35, WindowGame.HEIGHT / 2);
        }
        if (gameOver) {
            String gameOverString = "GAME OVER";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gameOverString, 50, WindowGame.HEIGHT / 2);
        }


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

       if(state==STATE_GAME_OVER){
           g.setColor(Color.white);
           g.drawString("Game Over",50,200);
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
        //clean the board
        if(state== STATE_GAME_OVER){
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                for (int row = 0; row < board.length; row++) {
                    for (int col = 0; col < board[row].length; col++) {

                        board[row][col]=null;

                    }
                }
                setCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }
        //pause
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            if(state==STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            }else if(state == STATE_GAME_PAUSE){
                state= STATE_GAME_PLAY;
            }
        }

    }
    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()== KeyEvent.VK_DOWN){
            currentShape.speedDown();

        }

    }
    public void startGame() {
        stopGame();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        loop.start();

    }
    public void stopGame() {
        score = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = null;
            }
        }
        loop.stop();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
