package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class SwimmingTurtleView extends View {

    private Bitmap turtle;

    public SwimmingTurtleView(Context context){
        super(context);

        turtle = new BitmapFactory().decodeResource(getResources(), R.drawable.fish1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(turtle, 0,0,null);
    }
}
