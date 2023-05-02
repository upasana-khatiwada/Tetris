import java.awt.*;

public class Shape {
    //x for column and y for row so start from 4th column 0 row
    private int x=4,y=0;

    // Set the normal and fast movement speeds, and initialize the current speed to normal
    private int normal =600;
    private int fast = 50;
    private int delayTimeForMovement =normal;
    private long beginTime;

    public static final int BOARD_WIDTH =10;
    public static final int BOARD_HEIGHT =20;
    public static final int BLOCK_SIZE =30;

    //This variable is used to store the amount of horizontal movement
    // that should be applied to the game object in each update cycle.
    private  int deltaX =0;
    private boolean collision =false;
    private int[][] coords ;
    public Shape(int[][] coords,Board board, Color color)
    {
        this.coords=coords;
    }
    public void update(){
        if(collision){
            for(int row= 0; row< coords.length;row++){
                for(int col=0;col<coords[0].length;col++){
                    if( )
                }
            }
            return;
        }
        //check moving horizontal for collision
        //right and left collision
        if(!(x+deltaX+coords[0].length>10)&& !(x+deltaX<0)){

            x+= deltaX;

        }
        //if the user presses the left or right arrow key to move the game object horizontally,
        // the code can update the "deltaX" value accordingly. If "deltaX" were not used,
        // the game object might move too fast or too slow in response to user input,
        // or might be limited to a fixed movement speed.
        deltaX = 0;
        //The "System.currentTimeMillis()" method returns the current system time in milliseconds,
        // and "beginTime" is a variable that holds the time of the last movement update.
        // By subtracting "beginTime" from the current time,
        // the code can calculate the amount of time that has elapsed since the last movement.
        //If the elapsed time is greater than the current "delayTimeForMovement" value,
        // which determines the speed of the game object's movement,
        //the code updates the "y" variable and sets "beginTime" to the current system time,
        // marking the start of a new movement cycle.
        if(System.currentTimeMillis()-beginTime>delayTimeForMovement){
            if(!(y+1+ coords.length>BOARD_HEIGHT)){
                y++;
            }else{
                collision =true;
            }

            beginTime = System.currentTimeMillis();
        }
    }
    public void render (Graphics g){
        //concept of 2d array for 2line shape

        for(int i=0;i< coords.length;i++){
            for(int j=0; j< coords[0].length;j++){
                if(coords[i][j]!=0){
                    g.setColor(Color.RED);
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
