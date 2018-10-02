package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

//this is pretty much a gameview
public class SwimmingTurtleView extends SurfaceView implements Runnable{

    //background music
    //w&w caribbean rave
    //https://www.youtube.com/watch?v=t0thau7RIWA
    //dubstep but the carribean drum instead of the key board
    Thread gameThread = null;
    SurfaceHolder ourHolder;  // when we use paint and canvas in thread

    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    long fps;  //  game frame rate
    private long timeThisFrame;  // helps calculate the fps
    private long lastFrameChangeTime = 0;

    private int frameLengthInMilliseconds = 150;  // animation speed;

    Bitmap bitmap_turtle;
    private int turtleX = 100;
    private int turtleY;
    private int turtleSpeed;
    // I dont like these hard coded values;
    private int turtle_frameWidth = 350;
    private int turtle_frameHeight = 150;
    private int turtle_upFrameCoount = 4;
    private int turtle_idleFrameCount = 2;
    private int turtle_frameCount;
    private int turtle_currentFrame = 0;
    boolean isMoving = false;

    private int canvasWidth, canvasHeight;


    //THESE WILL BE BITMAPS LATER
    Bitmap bitmap_worm;
    private int wormX, wormY, wormSpeed = 16;
    private int worm_frameWidth = 150;
    private int worm_frameHeight = 50;
    private int worm_frameCount = 4;
    private int worm_currentFrame = 0;

    Bitmap bitmap_straw;
    private int strawX, strawY, strawSpeed = 20;
    private Paint strawPaint = new Paint();

    Bitmap bitmap_magicWorm;
    private int magicWormX, magicWormY, magicWormSpeed = 32;
    private Paint magicWormPaint = new Paint();

    //this is going to have a hook case
    //then falls to its y,
    //then zooms off
    Bitmap bitmap_flyingHook;
    private int flyingHookX, flyingHookY, flyingHookSpeed = 35;
    private Paint flyingHookPaint = new Paint();

    //background that moves
    Bitmap bitmap_background;
    private int backgroundX, backgroundY;

    private int score, lifeCounterOFTurtle;

    private boolean touch = false;

    private Paint scorePaint = new Paint();

    private Bitmap life[] = new Bitmap[2];

    //draw functions
    private Rect turtle_frameToDraw = frameToDraw(turtle_frameWidth, turtle_frameHeight);
    //im not sure if this needs to be here or just in the update function
    private RectF turtle_whereToDraw = whereToDraw(turtleX, turtleY, turtle_frameWidth, turtle_frameHeight);

    private Rect worm_frameToDraw = frameToDraw(worm_frameWidth, worm_frameHeight);
    private RectF worm_whereToDraw = whereToDraw(wormX, wormY, worm_frameWidth, worm_frameHeight);

    public SwimmingTurtleView(Context context){
        super(context);
        ourHolder = getHolder();
        paint = new Paint();

        bitmap_background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bitmap_turtle = BitmapFactory.decodeResource(this.getResources(), R.drawable.turtle_swim_350_235);
        bitmap_turtle = Bitmap.createScaledBitmap(
                bitmap_turtle,
                turtle_frameWidth * turtle_currentFrame,
                turtle_frameHeight,
                false
        );




        /*
        THIS IS WHERE I SKIPPED TO MY OVERIDES AND UPDATES;
         */

        //NonPlayerCharachters
        strawPaint.setColor(Color.RED);
        strawPaint.setAntiAlias(false);

        magicWormPaint.setColor(Color.GREEN);
        magicWormPaint.setAntiAlias(false);

        flyingHookPaint.setColor(Color.RED);
        flyingHookPaint.setAntiAlias(false);

        //DISPLAYS
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);
        turtleY = 500;

        score = 0;
        lifeCounterOFTurtle = 3;
    }
    @Override
    public void run() {
        while(playing){
            long startFrameTime = System.currentTimeMillis();

            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1)
                fps = 1000/ timeThisFrame;
        }
    }

    //decided to change this all to the update function, good luck, fingers crossed
    /*
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    */
    public void update(){
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        int minTurtleY = bitmap_turtle.getHeight();
        int maxTurtleY = canvasHeight - (bitmap_turtle.getHeight() );
        turtleY = turtleY + turtleSpeed;

        //Turtle Goes Off Screen
        if(turtleY < minTurtleY){
            turtleY = minTurtleY;
        }
        if(turtleY > maxTurtleY){
            turtleY = maxTurtleY;
        }
        turtleSpeed = turtleSpeed + 2;

        canvas.drawBitmap(bitmap_background,0,0,null);
        canvas.drawText("Score : " + score, 20,60,scorePaint);

        //HEARTS
        for (int i = 0; i < 3; i++){
            int x = (int) (canvasWidth - 100 - (100 * i));
            int y = 10;

            if(i < lifeCounterOFTurtle){
                canvas.drawBitmap(life[0], x, y, null);
            }
            else{
                canvas.drawBitmap(life[1], x, y, null);
            }
        }

        if(touch)
        {
            //need to do a draw swim so it runs the full animation
            bitmap_turtle = BitmapFactory.decodeResource(this.getResources(),R.drawable.turtle_up_350_235);
            turtle_frameCount = turtle_upFrameCoount;
            bitmap_turtle = Bitmap.createScaledBitmap(
                    bitmap_turtle,
                    turtle_frameWidth * turtle_frameCount,
                    turtle_frameHeight,
                    false
            );
            //canvas.drawBitmap(turtle[1], turtleX, turtleY, null);
            touch = false;
            isMoving = true;
        }
        else
        {
            bitmap_turtle = BitmapFactory.decodeResource(this.getResources(), R.drawable.turtle_swim_350_235);
            turtle_frameCount = turtle_idleFrameCount;
            bitmap_turtle = Bitmap.createScaledBitmap(
                    bitmap_turtle,
                    turtle_frameWidth * turtle_frameCount,
                    turtle_frameHeight,
                    false
            );
            //canvas.drawBitmap(turtle[0], turtleX, turtleY, null);
        }


        //worm logic
        if(collisionChecker(wormX, wormY)){
            score += 10;
            wormX -= 300;
        }
        wormX = wormX - wormSpeed;
        if(wormX < 0) {
            wormX = canvasWidth + worm_frameWidth;
            wormY = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }

        //THIS IS WHERE THE WORM WILL GO
        //as soon as i test the game


        //this shall appear as a bitmap
        //this is frame animation and will need a frame count so i shall return to this at a later date



        //leftover code.
        //canvas.drawCircle(wormX, wormY, 25, wormPaint);
        //canvas.draw;
        //ivWorm.setImageResource(R.drawable.worm_swim);
        //lp.layoutAnimationParameters()
        //AnimationDrawable swimming_worm = (AnimationDrawable)ivWorm.getDrawable();

        //straw logic
        if(collisionChecker(strawX,strawY)){
            strawX -= 300;
            lifeCounterOFTurtle--;
            if(lifeCounterOFTurtle == 0){
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);
            }
        }
        strawX -= strawSpeed;
        if(strawX < 0){
            strawX = canvasWidth + 21;
            strawY = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }
        canvas.drawCircle(strawX, strawY, 50, strawPaint);

        //magicWorm logic
        if(collisionChecker(magicWormX,magicWormY)){
            score += 25;
            magicWormX -= 300;
        }
        magicWormX -= magicWormSpeed;
        if(magicWormX < 0){
            magicWormX = canvasWidth + 21;
            magicWormY = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }
        canvas.drawCircle(magicWormX, magicWormY, 10, magicWormPaint);

        //flying hook logic
        if(collisionChecker(flyingHookX, flyingHookY)){
            flyingHookX -= 300;
            lifeCounterOFTurtle -= 2;
            if(lifeCounterOFTurtle <= 0){
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);            }
        }
        flyingHookX -= flyingHookSpeed;
        if(flyingHookX < 0){
            flyingHookX = canvasWidth + 21;
            flyingHookY = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }
        canvas.drawCircle(flyingHookX, flyingHookY, 10, flyingHookPaint);

    }

    public void draw(){
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            turtle_whereToDraw.set(
                    (int)turtleX,
                    0,
                    (int)turtleX + turtle_frameWidth,
                    turtle_frameHeight
            );

            getCurrentFrame();


        }
    }

    public void getCurrentFrame() {
        long time = System.currentTimeMillis();
        if(time > lastFrameChangeTime + frameLengthInMilliseconds){
            turtle_currentFrame++;
            //first try for a one shot animation, wish me luck
            if(isMoving){
                lastFrameChangeTime = time;
                if(turtle_currentFrame >= turtle_upFrameCoount)
                    isMoving = false;
            }else{
                lastFrameChangeTime = time;
                if(turtle_currentFrame >= turtle_idleFrameCount)
                    turtle_currentFrame = 0;
            }
        }

        turtle_frameToDraw.left = turtle_currentFrame * turtle_frameWidth;
        turtle_frameToDraw.right = turtle_frameToDraw.left + turtle_frameWidth;
    }

    public Rect frameToDraw(int frameWidth, int frameHeight){
        return new Rect (
                0,
                0,
                frameWidth,
                frameHeight
        );

    };
    public RectF whereToDraw(int x, int y, int frameWidth, int frameHeight){
        return new RectF(
                x,
                y,
                x + frameWidth,
                frameHeight
        );
    }

    public boolean collisionChecker(int x, int y){
        if(turtleX < x && x < (turtleX + bitmap_turtle.getWidth()) &&
                turtleY < y && y < (turtleY + bitmap_turtle.getHeight())){
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touch = true;

            turtleSpeed = -22;
        }
        return true;
    }
}
