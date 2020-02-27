package com.example.npuzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndActivity extends AppCompatActivity {

    Button playAgain;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        playAgain = (Button) findViewById(R.id.play_again);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent again = new Intent(EndActivity.this, InputActivity.class);
                startActivity(again);
            }
        });
    }
}
