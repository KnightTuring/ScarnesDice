package scarnesdice.appliedcs.com.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textViewComputerScore,textViewUserScore,textViewPlayerTurn,textViewGameStatus;
    ImageView imageViewDiceFace;
    Button buttonRoll,buttonHold,buttonReset;
    int MAX_WINNER_SCORE=100;


    int userTotalScore,userTurnScore,computerTotalScore,computerTurnScore;
    boolean isUserTurn=true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();

        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.println(Log.INFO,"Message","In Roll()");
                roll();
            }
        });

        buttonHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hold();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

    }

    public void initialise()
    {

        textViewComputerScore=(TextView) findViewById(R.id.computerScore);
        textViewComputerScore.setText("");


        textViewUserScore=(TextView)findViewById(R.id.userScore);
        textViewComputerScore.setText("");

        textViewPlayerTurn=(TextView)findViewById(R.id.textViewPlayer);
        textViewPlayerTurn.setText("");

        textViewGameStatus=(TextView)findViewById(R.id.textViewStatus);
        textViewGameStatus.setText("");
        textViewGameStatus.setMovementMethod(new ScrollingMovementMethod());

        imageViewDiceFace=(ImageView)findViewById(R.id.diceView);

        buttonRoll=(Button)findViewById(R.id.buttonRoll);
        buttonHold=(Button)findViewById(R.id.buttonHold);
        buttonReset=(Button)findViewById(R.id.buttonReset);
    }

    public void roll()
    {
        Random randomObject=new Random();
        int randomDiceNumber=randomObject.nextInt(6)+1;
        updateDiceImage(randomDiceNumber);
        setGameStatusMessage(randomDiceNumber);
        if(randomDiceNumber!=1) {
            if (isUserTurn) {
                userTurnScore = userTurnScore + randomDiceNumber;
            } else {
                Log.println(Log.INFO,"Message","Computer rolling in roll()");
                computerTurnScore = computerTurnScore + randomDiceNumber;
            }
        }
        else //if rolled dice value is 1
        {
            if (isUserTurn) {
                //user will be forced to hold with turn score 0
                userTurnScore = 0;
                hold();
            } else {
                //computer is forced to hold with turn score 0
                Log.println(Log.INFO,"Message","Computer rolled a 1");
                computerTurnScore = 0;
                hold();
            }
        }


    }

    public void hold()
    {
        if(isUserTurn)
        {

            setScoreViews();
            userTurnScore=0;
            checkScoreProgress();

        }
        else {
            Log.println(Log.INFO,"Message","Computer will now hold");
            setScoreViews();
            computerTurnScore = 0;
            checkScoreProgress();
        }
    }

    public void reset()
    {
        buttonRoll.setEnabled(true);
        buttonReset.setEnabled(true);
        buttonHold.setEnabled(true);
        computerTotalScore=0;
        computerTurnScore=0;
        userTotalScore=0;
        userTurnScore=0;
        textViewGameStatus.setText("");
        setScoreViews();

    }

    public void setGameStatusMessage(int diceValue)
    {
        if(isUserTurn)
        {
            textViewGameStatus.setText("\nUser rolled a "+diceValue+"\n"+textViewGameStatus.getText());
        }
        else
        {
            textViewGameStatus.setText("\nComputer rolled a "+diceValue+"\n"+textViewGameStatus.getText());
        }
    }


    public void computerPlays()
    {
        Log.println(Log.INFO,"Message","In computerPlays()");
            /*
            BELOW CODE RUNS WITHOUT DELAY
            while (computerTurnScore < 20 && !isUserTurn) {
                roll();
                //setScoreViews();
            }
            setScoreViews();
            if(computerTurnScore>=20)
            {
                hold();
            }*/
        if(!isUserTurn) {
            if(computerTurnScore<20) {
                roll();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerPlays();
                    }
                }, 1000);
            }
            else
            {
                hold();
            }
        }


    }



    public void checkScoreProgress()
    {
        if(isUserTurn)
        {
            if(userTotalScore>=MAX_WINNER_SCORE)
            {
                textViewGameStatus.setText("User Wins");
                reset();
            }
            else
            {
                isUserTurn=false;
                System.out.println("\nTransferring control to computerPlays()\n");
                buttonReset.setEnabled(false);
                buttonRoll.setEnabled(false);
                buttonHold.setEnabled(false);
                computerPlays();
            }


        }
        else {
            if (computerTotalScore >= MAX_WINNER_SCORE) {
                textViewGameStatus.setText("Computer Wins");
                reset();

            } else {
                isUserTurn = true;
                buttonReset.setEnabled(true);
                buttonRoll.setEnabled(true);
                buttonHold.setEnabled(true);

            }
        }

    }

    public void setScoreViews()
    {
        userTotalScore+=userTurnScore;
        computerTotalScore+=computerTurnScore;
        textViewComputerScore.setText("Computer Score is "+computerTotalScore);
        textViewUserScore.setText("User score is "+userTotalScore);
    }

    public void updateDiceImage(int diceNumber)
    {
        switch (diceNumber)
        {
            case 1:imageViewDiceFace.setImageResource(R.drawable.dice1);
                break;
            case 2:imageViewDiceFace.setImageResource(R.drawable.dice2);
                break;
            case 3: imageViewDiceFace.setImageResource(R.drawable.dice3);
                break;
            case 4:imageViewDiceFace.setImageResource(R.drawable.dice4);
                break;
            case 5:imageViewDiceFace.setImageResource(R.drawable.dice5);
                break;
            case 6:imageViewDiceFace.setImageResource(R.drawable.dice6);
                break;

            default:break; //do something
        }

    }


}