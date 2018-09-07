package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private SwimmingTurtleView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        gameView = new SwimmingTurtleView(this);
        setContentView(gameView);
    }
}
