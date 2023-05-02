import java.awt.*;
import javax.swing.*;


public class Shape {
    //x for column and y for row so start from 4th column 0 row
    private int x=4,y=0;

    // Set the normal and fast movement speeds, and initialize the current speed to normal
    private int normal =600;
    private int fast = 50;
    private int delayTimeForMovement =normal;
    private long beginTime;

//    public static final int BOARD_WIDTH =10;
//    public static final int BOARD_HEIGHT =20;
    public static final int BLOCK_SIZE =30;

    //This variable is used to store the amount of horizontal movement
    // that should be applied to the game object in each update cycle.
    private  int deltaX =0;
    private boolean collision =false,moveX=false;
    private int[][] coords ;
    private Board board ;
    private Color color;
    private int delay;
    private long time, lastTime;
    private int timePassedFromCollision = -1;
    long deltaTime;
    public Shape(int[][] coords,Board board, Color color)
    {
        this.coords=coords;
        this.board = board;
        this.color = color;
    }
    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void reset() {
        this.x =4;
        this.y=0;
        collision = false;
    }

    public void update(){
        moveX = true;
        deltaTime = System.currentTimeMillis() - lastTime;
        time += deltaTime;
        lastTime = System.currentTimeMillis();

        if (collision && timePassedFromCollision > 500) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) {
                    if (coords[row][col] != 0) {
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
           // board.addScore();
            board.setCurrentShape();
            timePassedFromCollision = -1;
            return;
        }

        //check moving horizontal for collision
        //right and left collision

        if (!(x + deltaX + coords[0].length > 10) && !(x + deltaX < 0)) {

            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if (coords[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }

                    }
                }
            }

            if (moveX) {
                x += deltaX;
            }

        }
        //if the user presses the left or right arrow key to move the game object horizontally,
        // the code can update the "deltaX" value accordingly. If "deltaX" were not used,
        // the game object might move too fast or too slow in response to user input,
        // or might be limited to a fixed movement speed.

        //The "System.currentTimeMillis()" method returns the current system time in milliseconds,
        // and "beginTime" is a variable that holds the time of the last movement update.
        // By subtracting "beginTime" from the current time,
        // the code can calculate the amount of time that has elapsed since the last movement.
        //If the elapsed time is greater than the current "delayTimeForMovement" value,
        // which determines the speed of the game object's movement,
        //the code updates the "y" variable and sets "beginTime" to the current system time,
        // marking the start of a new movement cycle.
//        if (timePassedFromCollision == -1) {
//            if (!(y + 1 + coords.length > 20)) {
//
//                for (int row = 0; row < coords.length; row++) {
//                    for (int col = 0; col < coords[row].length; col++) {
//                        if (coords[row][col] != 0) {
//
//                            if (board.getBoard()[y + 1 + row][x + col] != null) {
//                                collision();
//                            }
//                        }
//                    }
//                }
//                if (time > delay) {
//                    y++;
//                    time = 0;
//                }
//            } else {
//                collision();
//            }
//        } else {
//            timePassedFromCollision += deltaTime;
//        }
//
//        deltaX = 0;
        if(System.currentTimeMillis()-beginTime >delayTimeForMovement){
          if (!(y + 1 + coords.length > 20)) {
              for (int row = 0; row < coords.length; row++) {
                   for (int col = 0; col < coords[row].length; col++) {
                        if (coords[row][col] != 0) {

                            if (board.getBoard()[y + 1 + row][x + col] != null) {
                                collision=true;
                            }
                        }
                    }
                }
              if(!collision){
                  y++;
              }

          }
          else{
              collision=true;
          }
            beginTime = System.currentTimeMillis();
        }

    }
    private int[][] reverseRows(int[][] matrix) {

        int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];

            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }

        return matrix;

    }

    private void collision() {
        collision = true;
        timePassedFromCollision = 0;
    }
    public void rotateShape() {

        int[][] rotatedShape     = transposeMatrix(coords);

        rotatedShape = reverseRows(rotatedShape);
///         check for right side and bottom
        if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getBoard()[y + row][x + col] != null) {
                        return;
                    }
                }
            }
        }
        coords = rotatedShape;
    }

    private int[][] transposeMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }
        private void checkLine() {
        int bottomline = board.getBoard().length-1;
        for (int topLine = board.getBoard().length-1;topLine>0;topLine--){
            int count = 0;
            for(int col=0;col<board.getBoard()[0].length;col++){
                if(board.getBoard()[topLine][col]!=null){
                    count++;
                }
                board.getBoard()[bottomline][col]=board.getBoard()[topLine][col];

            }
            if (count < board.getBoard()[0].length) {
               bottomline--;
            }
        }
    }

    public void render (Graphics g){
        //concept of 2d array for 2line shape

        for(int i=0;i< coords.length;i++){
            for(int j=0; j< coords[0].length;j++){
                if(coords[i][j]!=0){
                    g.setColor(color);
                    g.fillRect(j*BLOCK_SIZE+x*BLOCK_SIZE,i *BLOCK_SIZE+y*BLOCK_SIZE ,BLOCK_SIZE,BLOCK_SIZE);

                }
            }
        }
    }
    public void speedUp(){
        delayTimeForMovement = fast;
    }
    public void speedDown(){
        delayTimeForMovement = normal;

    }
    public void moveRight(){
        deltaX=1;
    }
    public void moveLeft(){
        deltaX=-1;
    }
}
