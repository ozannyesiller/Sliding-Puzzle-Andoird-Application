package com.example.npuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private NPuzzle game_obj;
    private int size_i;
    private int size_j;
    private TextView totalMoves_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        size_i = this.getIntent().getIntExtra("size_i", 3);
        size_j = this.getIntent().getIntExtra("size_j", 3);
        totalMoves_text = (TextView) findViewById(R.id.totalMoves);

        generateGame(size_i,size_j);
    }

    public void generateGame(int size_i, int size_j)
    {
        this.game_obj = new NPuzzle(this, size_i, size_j);
        generateButton();

        GridLayout gl = (GridLayout) findViewById(R.id.cell_grid);
        gl.setColumnCount(game_obj.getSize_j());
        gl.setRowCount(game_obj.getSize_i());

        for(int i = 0 ; i < game_obj.getSize_i() ; ++i)
            for(int j = 0 ; j < game_obj.getSize_j() ; ++j)
                gl.addView(game_obj.getCell_button(i, j));
    }

    public void generateButton()
    {
        game_obj.generateButton(this);

         for(int i = 0 ; i < game_obj.getSize_i() ; ++i) {
             for (int j = 0; j < game_obj.getSize_j(); ++j) {

                 final Integer i_ = new Integer(i);
                 final Integer j_ = new Integer(j);

                 game_obj.getCell_button(i_, j_).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         game_obj.move(game_obj.whichMove_button(i_, j_));
                         game_obj.fill_Button();

                         Integer temp = new Integer(game_obj.getTotalMoves());
                         totalMoves_text.setText(temp.toString());

                         if (game_obj.isSolved()) {
                            toEnd();
                         }
                     }
                 });
             }
         }
    }

    public void toEnd(){
        Intent end_intent = new Intent(GameActivity.this, EndActivity.class);
        startActivity(end_intent);
    }
}
