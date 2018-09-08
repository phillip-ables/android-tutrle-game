package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwimmingTurtleView extends View {

    private Bitmap turtle[] = new Bitmap[2];
    private float turtleX = 100;
    private float turtleY;
    private int turtleSpeed;

    private int canvasWidth, canvasHeight;

    private boolean touch = false;

    private Bitmap backgroundImage;

    private Paint scorePaint = new Paint();

    private Bitmap life[] = new Bitmap[2];

    public SwimmingTurtleView(Context context){
        super(context);

        turtle[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        turtle[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);
        turtleY = 90;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        int minTurtleY = turtle[0].getHeight();
        int maxTurtleY = canvasHeight - turtle[0].getHeight() * 3;
        turtleY = turtleY + turtleSpeed;

        //Turtle Goes Off Screen
        if(turtleY < minTurtleY){
            turtleY = minTurtleY;
        }
        if(turtleY > maxTurtleY){
            turtleY = maxTurtleY;
        }
        turtleSpeed = turtleSpeed + 2;

        if(touch)
        {
            canvas.drawBitmap(turtle[1], turtleX, turtleY, null);
            touch = false;
        }
        else
        {
            canvas.drawBitmap(turtle[0], turtleX, turtleY, null);
        }

        canvas.drawBitmap(backgroundImage,0,0,null);
        canvas.drawText("Score : ", 20,60,scorePaint);

        canvas.drawBitmap(life[0], canvasWidth-300,10,null);
        canvas.drawBitmap(life[0], canvasWidth-200,10,null);
        canvas.drawBitmap(life[0], canvasWidth-100,10,null);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touch = true;

            turtleSpeed = -22;
            Log.d("posX", turtleX+"");
            Log.d("speed", turtleSpeed+"");
        }
        return true;
    }
}
