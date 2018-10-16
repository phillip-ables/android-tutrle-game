package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.app.Activity;
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
import android.util.DisplayMetrics;
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
    private long lastFrameChangeTime;
    private int frameLengthInMilliseconds = 150;  // animation speed;

    boolean isMoving = false;

    private int canvasWidth, canvasHeight, lastCanvasHeight;
    private int score, lifeCounterOfTurtle;
    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2];

    private Bitmap bitmap_background;
    private int background_x;
    private int background_speed = 7;

    private Bitmap bitmap_turtle;
    private int turtle_x = 5;
    private int turtle_y;
    private int turtle_speed = 4;
    private int turtle_gravity = 2;
    private int turtle_jumpSpeed = 40;
    private int turtle_sink = 35;
    private int turtle_fly = -35;
    private int minTurtleY;
    private int maxTurtleY;

    private int turtle_scaleFactor = 5;
    private int turtle_frameWidth = 300;
    private int turtle_frameHeight = 125;
    private int turtle_upFrameCount = 4;
    private int turtle_idleFrameCount = 2;
    private int turtle_frameCount;
    private int turtle_currentFrame = 0;
    //float turtle_speedPerSecond = 250;

    private Rect turtle_frameToDraw = new Rect(
            0,
            0,
            turtle_frameWidth,
            turtle_frameHeight
    );

    RectF turtle_whereToDraw = new RectF(
            turtle_x,
            turtle_y,
            //turtleXPosition + turtle_frameWidth,
            turtle_x + turtle_frameWidth,
            turtle_y + turtle_frameHeight
    );

    Bitmap bitmap_worm;
    private int worm_x = 200;
    private int worm_y = 300;
    private int worm_speed = 16;

    private int worm_scaleWidth = 8;
    private int worm_scaleHeight = 3 * worm_scaleWidth;
    private int worm_frameWidth = 200;
    private int worm_frameHeight = 93;
    private int worm_frameCount = 4;
    private int worm_currentFrame = 0;

    private Rect worm_frameToDraw = new Rect(
            0,
            0,
            worm_frameWidth,
            worm_frameHeight
    );

    RectF worm_whereToDraw = new RectF(
            worm_x,
            worm_y,
            worm_x + worm_frameWidth,
            worm_y + worm_frameHeight
    );

    Bitmap bitmap_splashEffect;
    private int splash_x;
    private boolean isSplash = false;
    private int splash_velocity = 1;
    private int splash_frameWidth = 160;
    private int splash_frameHeight = 90;
    private int splash_scale = 13;
    private int splash_frameCount = 9;
    private int splash_currentFrame = 0;
    private Rect splash_frameToDraw = new Rect(
            0,
            0,
            splash_frameWidth,
            splash_frameHeight
    );

    RectF splash_whereToDraw = new RectF(
            turtle_x,
            100,
            turtle_x + splash_frameWidth,
            100 + splash_frameHeight
    );

    Bitmap bitmap_sparkEffect;
    private int spark_x;
    private boolean isSpark = false;
    private int spark_velocity = 1;
    private int spark_frameWidth = 65;
    private int spark_frameHeight = 120;
    private int spark_scale = 13;
    private int spark_frameCount = 9;
    private int spark_currentFrame;
    private Rect spark_frameToDraw = new Rect(
            0,
            0,
            spark_frameWidth,
            spark_frameHeight
    );

    RectF spark_whereToDraw = new RectF(
            turtle_x,
            10,
            turtle_x + spark_frameWidth,
            10 + spark_frameHeight
    );

    Bitmap bitmap_dropEffect;
    private int drop_x;
    private int drop_velocity;
    private int drop_frameWidth;
    private int drop_frameHeight;
    private int drop_scaleWidth;
    private int drop_scaleHeight;
    private int drop_frameCount;
    private int drop_currentFrame;
    private Rect drop_frameToDraw = new Rect(
            0,
            0,
            drop_frameWidth,
            drop_frameHeight
    );

    RectF drop_whereToDraw = new RectF(
            drop_x,
            turtle_y,
            drop_x + drop_frameWidth,
            worm_y + drop_frameHeight
    );

    //Bitmaps for hire

    //Bitmap bitmapWorm;
    //Bitmap bitmapTurtle;
    Bitmap bitmapBackground;
    Bitmap bitmapstraw;
    float backgroundXPos = 0;




    //THESE WILL BE BITMAPS LATER
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


    public SwimmingTurtleView(Context context){
        super(context);  // ask surface view to set up our object
        ourHolder = getHolder();
        paint = new Paint();

        bitmap_turtle = BitmapFactory.decodeResource(this.getResources(), R.drawable.turtle_swim_350_235);
        bitmap_turtle = Bitmap.createScaledBitmap(
                bitmap_turtle,
                turtle_frameWidth,
                turtle_frameHeight,
                false
        );
        bitmap_splashEffect = BitmapFactory.decodeResource(this.getResources(), R.drawable.splash_160_90);
        bitmap_splashEffect = Bitmap.createScaledBitmap(
                bitmap_splashEffect,
                splash_frameWidth,
                splash_frameHeight,
                false
        );
        bitmap_sparkEffect = BitmapFactory.decodeResource(this.getResources(), R.drawable.sparks_65_120);
        bitmap_sparkEffect = Bitmap.createScaledBitmap(
                bitmap_sparkEffect,
                spark_frameWidth,
                spark_frameHeight,
                false
        );

        //worm
        bitmap_worm = BitmapFactory.decodeResource(this.getResources(), R.drawable.worm_200_93);
        bitmap_worm = Bitmap.createScaledBitmap(
                bitmap_worm,
                worm_frameWidth,
                worm_frameHeight,
                false
        );


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lastCanvasHeight = displayMetrics.heightPixels;
        canvasWidth = displayMetrics.widthPixels;

        turtle_frameWidth = canvasWidth / turtle_scaleFactor;
        worm_frameWidth = canvasWidth / worm_scaleWidth;
        worm_frameHeight = lastCanvasHeight/ worm_scaleHeight;

        //DISPLAYS
        bitmap_background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background_x = 0;

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        score = 0;
        lifeCounterOfTurtle = 3;

        //NonPlayerCharachters
        strawPaint.setColor(Color.RED);
        strawPaint.setAntiAlias(false);

        magicWormPaint.setColor(Color.GREEN);
        magicWormPaint.setAntiAlias(false);

        flyingHookPaint.setColor(Color.RED);
        flyingHookPaint.setAntiAlias(false);
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        canvasHeight = displayMetrics.heightPixels;

        if(lastCanvasHeight != canvasHeight){
            Log.e("CHANGE", "reset frameWidth");
            canvasWidth = displayMetrics.widthPixels;
            canvasHeight = displayMetrics.heightPixels;

            turtle_frameWidth = canvasWidth / turtle_scaleFactor;
            turtle_frameHeight = canvasHeight / turtle_scaleFactor;
            spark_frameHeight = spark_frameWidth = canvasHeight / spark_scale;
            splash_frameHeight = splash_frameWidth = canvasHeight / splash_scale;

            worm_frameWidth = canvasWidth / worm_scaleWidth;
            worm_frameHeight = canvasHeight / worm_scaleHeight;

            lastCanvasHeight = canvasHeight;
        }

        minTurtleY = bitmap_turtle.getHeight(); // i think i want this to be half of what it is
        maxTurtleY = canvasHeight - (2 * bitmap_turtle.getHeight());
        turtle_y += turtle_speed;
        // i think below is a better substitute for this
        //turtle_y += (turtle_speedPerSecond / fps);
        turtle_speed += turtle_gravity;


        if (turtle_y < minTurtleY && turtle_speed < 0) {
            turtle_y = 0;
            turtle_speed = turtle_sink;
            isSplash = true;
        }
        if (turtle_y > maxTurtleY) {
            turtle_y = maxTurtleY;
            turtle_speed = turtle_fly;
            isSpark = true;
        }

        //junk code for which resource to use
        if(isMoving) {
            bitmap_turtle = BitmapFactory.decodeResource(this.getResources(), R.drawable.turtle_up_350_235);
            turtle_frameCount = turtle_upFrameCount;
        }else{
            bitmap_turtle = BitmapFactory.decodeResource(this.getResources(), R.drawable.turtle_swim_350_235);
            turtle_frameCount = turtle_idleFrameCount;
        }

        //BACKGROUND
        background_x -= background_speed;
        if(Math.abs(background_x) >= canvasWidth )
            background_x = 0;

        //worm logic
        if(collisionChecker(worm_x, worm_y)){
            score += 10;
            worm_x -= 300;
        }
        worm_x -= worm_speed;
        if(worm_x < 0){
            worm_x = canvasWidth + worm_frameWidth;
            worm_y = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }

        //THIS IS THE CODE YOU ARE LOOKING FOR
        /*
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
        */

    }

    public void draw(){
        //valid or we crash
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(45);

            canvas.drawBitmap(bitmap_background, background_x, 0, null);

            canvas.drawText("Score : "+ score, 20, 60, scorePaint);

            //HEARTS
            for(int i = 0; i < 3; i++){
                int x = (canvasWidth - 100 - (100 * i));
                int y = 10;

                if( i < lifeCounterOfTurtle)
                    canvas.drawBitmap(life[0], x, y, null);
                else
                    canvas.drawBitmap(life[1], x, y, null);
            }

            //this will be placed in a final draw function
            bitmap_turtle = Bitmap.createScaledBitmap(
                    bitmap_turtle,
                    turtle_frameWidth * turtle_frameCount,
                    turtle_frameHeight,
                    false
            );
            turtle_whereToDraw.set(
                    turtle_x,
                    turtle_y,
                    turtle_x + turtle_frameWidth,
                    turtle_y + turtle_frameHeight
            );
            if(isSpark){
                bitmap_sparkEffect = Bitmap.createScaledBitmap(
                        bitmap_sparkEffect,
                        spark_frameWidth * spark_frameCount,
                        spark_frameHeight,
                        false
                );
                spark_whereToDraw.set(
                        turtle_x,
                        maxTurtleY,
                        turtle_x + spark_frameWidth,
                        maxTurtleY + spark_frameHeight
                );
            }
            if(isSplash){

                //this is where the error is
                //its something to do with the width






                bitmap_splashEffect = Bitmap.createScaledBitmap(
                        bitmap_splashEffect,
                        splash_frameWidth * splash_frameCount,
                        splash_frameHeight,
                        false
                );
                splash_whereToDraw.set(
                        turtle_x,
                        minTurtleY,
                        turtle_x + splash_frameWidth,
                        minTurtleY + splash_frameHeight
                );
            }

            //WORM
            bitmap_worm = Bitmap.createScaledBitmap(
                    bitmap_worm,
                    worm_frameWidth * worm_frameCount,
                    worm_frameHeight,
                    false
            );
            worm_whereToDraw.set(
                    worm_x,
                    worm_y,
                    worm_x + worm_frameWidth,
                    worm_y + worm_frameHeight
            );


            //not sure why this is here
            getCurrentFrame();

            canvas.drawBitmap(
                    bitmap_turtle,
                    turtle_frameToDraw,
                    turtle_whereToDraw,
                    paint
            );

            canvas.drawBitmap(
                    bitmap_worm,
                    worm_frameToDraw,
                    worm_whereToDraw,
                    paint
            );

            if(isSpark){
                canvas.drawBitmap(
                        bitmap_sparkEffect,
                        spark_frameToDraw,
                        spark_whereToDraw,
                        paint
                );
            }

            if(isSplash){
                canvas.drawBitmap(
                        bitmap_splashEffect,
                        splash_frameToDraw,
                        splash_whereToDraw,
                        paint);
                Log.e("Draw()","isSplash "+splash_currentFrame);
            }



            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void getCurrentFrame() {
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {  // this logic orig came after is moving logic
            turtle_currentFrame++;
            worm_currentFrame++;
            if(isMoving) {  // animate for if is moving
                lastFrameChangeTime = time;  // this maybe should be in the main function and not ever inner peice
                if (turtle_currentFrame >= turtle_upFrameCount){
                    turtle_currentFrame = 1;
                    isMoving = false;
                }
            }
            else{
                lastFrameChangeTime = time;
                if(turtle_currentFrame >= turtle_idleFrameCount)
                    turtle_currentFrame = 0;
            }
            if(isSpark) {
                spark_currentFrame++;
                if(spark_currentFrame >= spark_frameCount){
                    isSpark = false;
                    spark_currentFrame = 0;
                }
            }
            if(isSplash) {
                splash_currentFrame++;
                if(splash_currentFrame >= splash_frameCount){
                    isSplash = false;
                    splash_frameCount = 0;
                }
            }

            if(worm_currentFrame >= worm_frameCount)
                worm_currentFrame = 0;
        }
        turtle_frameToDraw.left = turtle_currentFrame * turtle_frameWidth;
        turtle_frameToDraw.right = turtle_frameToDraw.left + turtle_frameWidth;

        if(isSpark){
            spark_frameToDraw.left = spark_currentFrame * spark_frameWidth;
            spark_frameToDraw.right = spark_frameToDraw.left + spark_frameWidth;
        }
        if(isSplash){
            splash_frameToDraw.left = splash_currentFrame * splash_frameWidth;
            splash_frameToDraw.right = splash_frameToDraw.left + splash_frameWidth;
        }

        worm_frameToDraw.left = worm_currentFrame * worm_frameWidth;
        worm_frameToDraw.right = worm_frameToDraw.left + worm_frameWidth;
    }

    public boolean collisionChecker(int x, int y){
        if(turtle_x < x && x < (turtle_x + bitmap_turtle.getWidth()) &&
                turtle_y < y && y < (turtle_y + turtle_frameWidth))
            return true;
        return false;
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
