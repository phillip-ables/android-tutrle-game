package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class SwimmingTurtleView extends View {

    private Bitmap turtle[] = new Bitmap[2];
    private int turtleX = 10;
    private int turtleY;
    private int turtleSpeed;

    private int canvasWidth, canvasHeight;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);
        canvas.drawText("Score : ", 20,60,scorePaint);

        canvas.drawBitmap(life[0], canvasWidth-300,10,null);
        canvas.drawBitmap(life[0], canvasWidth-200,10,null);
        canvas.drawBitmap(life[0], canvasWidth-100,10,null);
    }
}
