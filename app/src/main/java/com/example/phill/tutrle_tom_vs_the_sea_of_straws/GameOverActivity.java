package com.example.phill.tutrle_tom_vs_the_sea_of_straws;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameOverActivity extends AppCompatActivity {

    private Button playAgainButton;
    private TextView scoreText;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        score = getIntent().getExtras().get("score").toString();

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        scoreText = (TextView) findViewById(R.id.scoreText);

        playAgainButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        scoreText.setText("SCore = " + score);
    }
}
