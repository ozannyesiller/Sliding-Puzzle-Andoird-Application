package com.example.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatDrawableManager;

import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public class NPuzzle{

	public class Cell extends Button
    {
    	private int cell_i;
    	private int cell_j;
    	private int value;

    	public Cell(Context context, int i, int j, int value)
    	{
    	    super(context);
    	    setButton_i(i);
    	    setButton_j(j);

			Drawable d =  AppCompatDrawableManager.get().getDrawable(context, R.drawable.back);
			this.setBackground(d);

			if((getSize_j() == 9 || getSize_j() == 8) || (getSize_i() == 9 || getSize_i() == 8))
				this.setLayoutParams(new LinearLayout.LayoutParams(77, 80));
			else if((getSize_j() == 7 || getSize_j() == 6) || (getSize_i() == 7 || getSize_i() == 6))
				this.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
			else if((getSize_j() == 5 ||getSize_j() == 4) || (getSize_i() == 5 || getSize_i() == 4))
				this.setLayoutParams(new LinearLayout.LayoutParams(140, 140));
			else if(getSize_j() == 3 || getSize_i() == 3)
				this.setLayoutParams(new LinearLayout.LayoutParams(160, 160));

			this.setValue(value);
        }

        public void setButton_i(int x){ this.cell_i = x; }
        public void setButton_j(int x){ this.cell_j = x; }
        public void setValue(int x){
    		this.value = x;

    		if(x != 0) {
				this.setText(String.valueOf(x));
				this.setVisibility(View.VISIBLE);
			} else {
				this.setText("");
				this.setVisibility(View.INVISIBLE);
			}
		}

        public int getButton_i(){ return this.cell_i; }
        public int getButton_j(){ return this.cell_j; }
    }

    private int size_i;
    private int size_j;
    private int empty_cell_i;
    private int empty_cell_j;
    private int totalMoves = 0;
    private char previouse_move = 'X';
    private int[][] gameBoard;
    private Cell[][] gameButton;
    private final Context context;

    public NPuzzle(Context context)
    {
    	this.context = (Context) context;

    	setSize_i(3);
    	setSize_j(3);

		this.gameBoard = new int[getSize_i()][getSize_j()];

    	do{
    		generateMap();
		}while (!isSolvable());

    	generateButton(context);
    }

    public NPuzzle(Context context, int val_i, int val_j)
    {
    	this.context = (Context) context;

    	if(val_i > 3)
    		setSize_i(val_i);
    	else
    		setSize_i(3);

    	if(val_j > 3)
    		setSize_j(val_j);
    	else
    		setSize_j(3);

		this.gameBoard = new int[getSize_i()][getSize_j()];

		if(this.getSize_i() != this.getSize_j()) {
			generateMap();
			shuffle(val_i * val_j);
		}else {
			do {
				generateMap();
			}while(!isSolvable());
		}

    	generateButton(context);
    }

    public NPuzzle(Context context, int val) {
		this.context = (Context) context;

		if (val > 3)
			setSize_i(val);
		else
			setSize_i(3);

		setSize_j(3);

		this.gameBoard = new int[getSize_i()][getSize_j()];

		if (getSize_i() == 3) {
			do {
				generateMap();
			}while (!isSolvable());
		} else{
			generateMap();
			shuffle(val * 3);
		}

    	generateButton(context);
    }

	private boolean isSolvable()
	{
		int[] game_temp = new int[getSize_i() * getSize_j()];
		int inversion = 0;

		for(int i = 0 ; i < getSize_i() ; ++i)
			for(int j = 0 ; j < getSize_j() ; ++j)
				game_temp[(i*getSize_j()) + j] = gameBoard[i][j];


		for(int i = 0 ; i < (getSize_i()*getSize_j()) - 1 ; ++i) {
			for (int j = i + 1; j < (getSize_i() * getSize_j()); ++j) {
				if (game_temp[i] != -1 && game_temp[j] != -1 && game_temp[i] != 0 && game_temp[j] != 0 && game_temp[i] > game_temp[j])
					++inversion;
			}
		}

		if(getSize_i() % 2 == 1 && getSize_j() % 2 == 1) {
			if (inversion % 2 == 0)
				return true;
		}

		else if(getSize_i() % 2 == 0 && getSize_j() % 2 == 0){
			if((getSize_i() - get_emptyCell_i()) % 2 == 0 && inversion % 2 == 1)
				return true;
			else if((getSize_i() - get_emptyCell_i()) % 2 == 0 && inversion % 2 == 0)
				return true;
		}

		return false;
	}

    public void generateButton(Context context)
    {
    	this.gameButton = new Cell[this.getSize_i()][this.getSize_j()];

    	for(int i = 0 ; i < getSize_i() ; ++i)
    		for(int j = 0 ; j < getSize_j() ; ++j)
    			gameButton[i][j] = new Cell(context, i, j, cell(i, j));
    }

    public void fill_Button()
	{
		for(int i = 0 ; i < getSize_i() ; ++i)
			for(int j = 0 ; j < getSize_j() ; ++j) {
				if (gameBoard[i][j] != 0) {
					gameButton[i][j].setText(String.valueOf(gameBoard[i][j]));
					gameButton[i][j].setVisibility(View.VISIBLE);
				}
				else {
					gameButton[i][j].setText("");
					gameButton[i][j].setVisibility(View.INVISIBLE);
				}
			}
	}

	public char whichMove_button(int cell_i, int cell_j)
	{
		if( cell_j < (this.getSize_j() - 1) )
			if(cell(cell_i, cell_j+1) == 0)
				return 'L';

		if(cell_j != 0)
			if(cell(cell_i, cell_j-1) == 0)
				return 'R';

		if( cell_i < (this.getSize_i() - 1) )
			if(cell(cell_i+1, cell_j) == 0)
				return 'U';

		if(cell_i != 0)
			if(cell(cell_i-1, cell_j) == 0)
				return 'D';

		return 'X';
	}


    public Cell getCell_button(int i, int j) { return this.gameButton[i][j]; }

    public void setSize_i(int val)
    {
    	if(val > 0)
    		this.size_i = val;
    	else
    		this.size_i = 0;
    }

    public void setSize_j(int val){
    	if(val > 0)
    		this.size_j = val;
    	else
    		this.size_j = 0;
    }

    public void set_emptyCell(int val_i, int val_j){
    	this.empty_cell_i = val_i;
    	this.empty_cell_j = val_j;
    }

    private void set_previous_move(char x){ this.previouse_move = x; }
    private void set_totalMoves(int x){ this.totalMoves = x; }
    
    public int get_emptyCell_i(){ return this.empty_cell_i; }
    public int get_emptyCell_j(){ return this.empty_cell_j; }
    public int getSize_i(){ return this.size_i; }
    public int getSize_j(){ return this.size_j; }
    public int getTotalMoves(){ return this.totalMoves; }
    public char get_previous_move(){ return this.previouse_move; }
    public int cell(int index_i, int index_j){ return this.gameBoard[index_i][index_j]; }

    private void generateMap()
    {
    	int val_i = this.getSize_i();
    	int val_j = this.getSize_j();
    	Random rand = new Random();

    	int number = rand.nextInt(val_i * val_j);

    	fill_and_init_gamemap();

    	for(int i = 0 ; i < val_i ; ++i)
    	{
    		for(int j = 0 ; j < val_j ; ++j)
    		{
    			while(testSame(number))
    				number = rand.nextInt(val_i * val_j);

    			gameBoard[i][j] = number;
    		}
    	}

    	set_emptyCellPosition();
    }

    private void fill_and_init_gamemap()
    {
		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
				this.gameBoard[i][j] = -1;
	}

	private boolean testSame(int finder)
	{
		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
				if(finder == this.gameBoard[i][j])
					return true;

		return false;
	}

	private void set_emptyCellPosition()
	{
		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
				if(this.gameBoard[i][j] == 0)
				{
					this.set_emptyCell(i, j);
					return;
				}
	}

	private int findMax()
	{
		int max = 0;

		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
				if(max < this.gameBoard[i][j])
					max = this.gameBoard[i][j];

		return max;
	}

	private void shuffle(int n)
	{
		this.reset();

		int prev_cell_i = this.get_emptyCell_i();
		int prev_cell_j = this.get_emptyCell_j();

		for(int i = 0 ; i < n ; ++i)
		{
			while(prev_cell_i == this.get_emptyCell_i() && prev_cell_j == this.get_emptyCell_j())
				this.moveRandom();

			prev_cell_i = this.get_emptyCell_i();
			prev_cell_j = this.get_emptyCell_j();
		}

		this.set_previous_move('S');
		this.set_totalMoves(this.getTotalMoves() - n);
	}

	public char moveRandom()
	{
		int random_move;
		Random rand = new Random();

		int val_i = this.getSize_i();
		int val_j = this.getSize_j();

		while(true)
		{
			random_move = rand.nextInt(4);

			if(random_move == 0 && this.get_emptyCell_j() != 0) // condition left
			{
				this.move('L');
				return 'L';
			}

			else if(random_move == 1 && this.get_emptyCell_j() < val_j-1) // condition right
			{
				this.move('R');
				return 'R';
			}
		
			else if(random_move == 2 && this.get_emptyCell_i() != 0) // condition up
			{
				this.move('U');
				return 'U';
			}

			else if(random_move == 3 && this.get_emptyCell_i() < (val_i-1)) // condition down
			{
				this.move('D');
				return 'D';
			}
		}
	}

	public char move(char choice)
	{
		if(choice == 'L' || choice == 'l')
		{
			int temp, i, j;
			int cell__i = this.get_emptyCell_i();
			int cell__j = this.get_emptyCell_j();

			if(cell__j != 0) // condition left
			{
				i = cell__i;
				j = cell__j;
				temp = this.gameBoard[i][j-1];
				this.gameBoard[i][j-1] = 0;
				this.gameBoard[i][j] = temp;

				this.set_emptyCellPosition();
				this.set_previous_move('L');
				this.set_totalMoves(this.getTotalMoves() + 1);
				return 'L';
			}
		}

		else if(choice == 'R' || choice == 'r')
		{
			int temp, i, j;
			int cell__i = this.get_emptyCell_i();
			int cell__j = this.get_emptyCell_j();

			if(cell__j < (this.getSize_j() - 1)) // condition right
			{
				i = cell__i;
				j = cell__j;
				temp = this.gameBoard[i][j+1];
				this.gameBoard[i][j+1] = 0;
				this.gameBoard[i][j] = temp;

				this.set_emptyCellPosition();
				this.set_previous_move('R');
				this.set_totalMoves(this.getTotalMoves() + 1);
				return 'R';
			}
		}

		else if(choice == 'U' || choice == 'u')
		{
			int temp, i, j;
			int cell__i = this.get_emptyCell_i();
			int cell__j = this.get_emptyCell_j();

			if(cell__i != 0) // condition up
			{
				i = cell__i;
				j = cell__j;
				temp = this.gameBoard[i-1][j];
				this.gameBoard[i-1][j] = 0;
				 this.gameBoard[i][j] = temp;

				this.set_emptyCellPosition();
				this.set_previous_move('U');
				this.set_totalMoves(this.getTotalMoves() + 1);
				return 'U';
			}
		}

		else if(choice == 'D' || choice == 'd')
		{
			int temp, i, j;
			int cell__i = this.get_emptyCell_i();
			int cell__j = this.get_emptyCell_j();

			if(cell__i < (this.getSize_i() - 1)) // condition down
			{
				i = cell__i;
				j = cell__j;
				temp = this.gameBoard[i+1][j];
				this.gameBoard[i+1][j] = 0;
				this.gameBoard[i][j] = temp;

				this.set_emptyCellPosition();
				this.set_previous_move('D');
				this.set_totalMoves(this.getTotalMoves() + 1);
				return 'D';
			}
		}

		return 'X';
	}

	public void reset()
	{
		int count = 1;

		int maxNumber = this.findMax();

		for(int i = 0 ; i < this.getSize_i() ; ++i)
		{
			for(int j = 0 ; j < this.getSize_j() ; ++j)
			{
				if(count <= maxNumber)
				{
					this.gameBoard[i][j] = count;
					++count;
				}

				else if(count == maxNumber + 1)
				{
					this.gameBoard[i][j] = 0;
					this.set_emptyCellPosition();
					this.set_totalMoves(0);
					this.set_previous_move('S');
					return;
				}
			}
		}
	}

	public boolean isSolved()
	{
		int size = this.getSize_i() * this.getSize_j();
		int[] finalBoard = new int[size];
		int index = 0;
		int count = 1;

		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
			{
				finalBoard[index] = count;
				++count;
				++index;
			}

		finalBoard[size-1] = 0;
		index = 0;

		for(int i = 0 ; i < this.getSize_i() ; ++i)
			for(int j = 0 ; j < this.getSize_j() ; ++j)
			{
				if(this.cell(i, j) != finalBoard[index])
					return false;

				++index;
			}

		return true;
	}
}