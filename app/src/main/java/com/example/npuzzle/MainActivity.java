package com.example.npuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start_input_screen = (Button) findViewById(R.id.start_b);
    }

    public void start_input_screen(View v){
        Intent screen2 = new Intent(MainActivity.this, InputActivity.class);

        startActivity(screen2);
    }
}
