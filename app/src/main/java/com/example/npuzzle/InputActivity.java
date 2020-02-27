package com.example.npuzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;

public class InputActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected int size_i = 3;
    protected int size_j = 3;
    Spinner input_number_i;
    Spinner input_number_j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button start_b = (Button) findViewById(R.id.choose_b);
        start_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_game(v);
            }
        });


        input_number_i = (Spinner) findViewById(R.id.input_i);
        input_number_j = (Spinner) findViewById(R.id.input_j);

        ArrayAdapter<CharSequence> adapter_i = ArrayAdapter.createFromResource(this, R.array.input_numbers, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_j = ArrayAdapter.createFromResource(this, R.array.input_numbers, android.R.layout.simple_spinner_item);

        adapter_i.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter_j.setDropDownViewResource(android.R.layout.simple_spinner_item);

        input_number_i.setAdapter(adapter_i);
        input_number_i.setOnItemSelectedListener(this);
        input_number_j.setAdapter(adapter_j);
        input_number_j.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String size = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), size, Toast.LENGTH_SHORT);

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.input_i)
            size_i = Integer.parseInt(spinner.getSelectedItem().toString());
        else if(spinner.getId() == R.id.input_j)
            size_j = Integer.parseInt(spinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

   public void start_game(View v){

        Intent screen3 = new Intent(InputActivity.this, GameActivity.class);
        screen3.putExtra("size_i", size_i);
        screen3.putExtra("size_j", size_j);

        startActivity(screen3);
    }
}
