package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SwimmingTurtleView extends View {

    //background music
    //w&w caribbean rave
    //https://www.youtube.com/watch?v=t0thau7RIWA
    //dubstep but the carribean drum instead of the key board
    private RelativeLayout mRelativeLayout;
    private ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    //private Bitmap turtle[] = new Bitmap[2];
    private ImageView ivTurtle;
    private int turtleX = 100;
    private int turtleY;
    private int turtleSpeed;

    private int canvasWidth, canvasHeight;


    //THESE WILL BE BITMAPS LATER
    private ImageView ivWorm;
    private int wormX, wormY, wormSpeed = 16;

    private ImageView ivStraw;
    private int strawX, strawY, strawSpeed = 20;
    private Paint strawPaint = new Paint();

    private ImageView ivMagicStraw;
    private int magicWormX, magicWormY, magicWormSpeed = 32;
    private Paint magicWormPaint = new Paint();

    //this is going to have a hook case
    //then falls to its y,
    //then zooms off
    private ImageView ivFlyingHook;
    private int flyingHookX, flyingHookY, flyingHookSpeed = 35;
    private Paint flyingHookPaint = new Paint();

    //background that moves
    private ImageView ivBackground;
    private int backgroundX, backgroundY;

    private int score, lifeCounterOFTurtle;

    private boolean touch = false;

    private Bitmap backgroundImage;

    private Paint scorePaint = new Paint();

    private Bitmap life[] = new Bitmap[2];

    public SwimmingTurtleView(Context context){
        super(context);
        mRelativeLayout = findViewById(R.id.relativeLayout);

        //TURTLE
        ivTurtle = new ImageView(context);

       // turtle[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
       // turtle[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        int minTurtleY = ivTurtle.getHeight();
        int maxTurtleY = canvasHeight - (ivTurtle.getHeight() );
        turtleY = turtleY + turtleSpeed;

        //Turtle Goes Off Screen
        if(turtleY < minTurtleY){
            turtleY = minTurtleY;
        }
        if(turtleY > maxTurtleY){
            turtleY = maxTurtleY;
        }
        turtleSpeed = turtleSpeed + 2;

        canvas.drawBitmap(backgroundImage,0,0,null);
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
            ivTurtle.setImageResource(R.drawable.turtle_swim_up);
            //canvas.drawBitmap(turtle[1], turtleX, turtleY, null);
            touch = false;
        }
        else
        {
            ivTurtle.setImageResource(R.drawable.turtle_swim_idle);
            //canvas.drawBitmap(turtle[0], turtleX, turtleY, null);
        }


        //worm logic
        if(collisionChecker(wormX, wormY)){
            score += 10;
            wormX -= 300;
        }
        wormX = wormX - wormSpeed;
        if(wormX < 0) {
            wormX = canvasWidth + 21;
            wormY = (int) Math.floor(Math.random() * (maxTurtleY - minTurtleY) + minTurtleY);
        }
        //canvas.drawCircle(wormX, wormY, 25, wormPaint);
        //canvas.draw;
        ivWorm.setImageResource(R.drawable.worm_swim);
        lp.layoutAnimationParameters()
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

    public boolean collisionChecker(int x, int y){
        if(turtleX < x && x < (turtleX + turtle[0].getWidth()) &&
                turtleY < y && y < (turtleY + turtle[0].getHeight())){
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
