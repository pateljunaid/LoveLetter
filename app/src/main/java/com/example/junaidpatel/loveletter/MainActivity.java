package com.example.junaidpatel.loveletter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Game game;
    public static Player player;
    public static Constants constants;
    public FirebaseDatabase fbd;
    public static ImageView card2;
    public static ImageView card1;
    public static ImageView topCard;
    public static Button startGame;
    private static ImageView deck;
    private static TextView notifs;
    private static TextView playerList;
    public static Context mMainActivity;
    private boolean offline = false;
    private boolean single = false;
    private Button resetDB;
    private static Integer guess;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //to remove "information bar" above the action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMainActivity = this;

        //to remove the action bar (title bar)
        getSupportActionBar().hide();
        constants = new Constants();

        //Initialize Resources
        initializeResources();

        if (single) {
            resetGame();
        }

        //Initialize a game and player
        player = new Player();
        game = new Game();

        if (offline) {
            game.addPlayer(player);
            pushData();
        }

        fbd.getInstance().getReference("gameData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: Separate what executes once, vs on every onDataChange

                //Update Game and Player
                game = dataSnapshot.getValue(Game.class);
                if (player.isInGame()) {
                    player = game.agetPlayerById(player.getPlayerId());
                } else if (!player.isInGame() && !player.isEliminated()) {
                    //Add new player to the game
                    game.addPlayer(player);
                    FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
                } else {
                    showToast("You are eliminated!");
                    Log.d("yakkity", "You are eliminated!");
                    fbd.getInstance().getReference("gameData").removeEventListener(this);
                    return;
                }

                //In-game updates
                if (game.isHasStarted()) {
                    Log.d("yakkity", "You are eliminated!");

                    //Check if game is done
                    if (game.getPlayers().size() == 1) {
                        endGame();
                    }
                    else {
                        card1.setImageResource(constants.drawableHashMap.get(player.getCard1()));

                        playerList.setVisibility(View.INVISIBLE);
                        deck.setVisibility(View.VISIBLE);

                        //Display turn
                        game.addBroadcast(constants.playerNames.get(game.getTurn() - 1) + "'s turn");

                        //Display broadcast message
                        showNotification(game.getBroadcast());

                        //Show secret messages
                        if (player.getSecretMessage() != null) {
                            showToast(player.getSecretMessage());
                        }

                        //Allow person with the correct turn to play
                        if (game.getTurn() == player.getPlayerId()) {
                            showToast("Your turn");
                            deck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    player.setCard2(game.drawCard());
                                    card2.setImageResource(constants.drawableHashMap.get(player.getCard2()));
                                    card2.setVisibility(View.VISIBLE);
                                    setListeners();
                                    deck.setOnClickListener(null);
                                }
                            });
                        }
                    }
                }

                //Game hasn't started
                else {
                    //Update player list
                    String builder = "";
                    for (Player x: game.getPlayers()) {
                        builder += x.getPlayerId() + ": " + x.getPlayerName() + "\n";
                    }
                    MainActivity.playerList.setText("Players List\n\n" + builder);
                }

                //Update ui on every turn based on returned objects
                //TODO: opt
                if (game.getTopCard() != null) {
                    topCard.setImageResource(constants.drawableHashMap.get(game.getTopCard()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        resetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            resetGame();
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame.setVisibility(View.INVISIBLE);
                game.startGame();
                pushData();
            }
        });
    }

    private void endGame() {
        showToast("Game over!");
        resetGame();
    }

    private void resetGame() {
        Game game = new Game();
        game.setBroadcast("");
        game.setHasStarted(false);
        game.setPlayers(new ArrayList<Player>());
        game.playerNames = new ArrayList<String>();
        game.setTopCard(null);
        game.setDeck(new ArrayList<Integer>());
        game.turn = 0;
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    private void setListeners() {
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.playCard(player, player.getCard1(),1);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.playCard(player, player.getCard2(),2);
            }
        });
    }

    public static void removeListeners() {
        card1.setOnClickListener(null);
        card2.setOnClickListener(null);
    }

    private void initializeResources() {
        resetDB = (Button)findViewById(R.id.resetDB);
        startGame = (Button)findViewById(R.id.startGame);
        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        topCard = (ImageView)findViewById(R.id.topCard);
        deck = (ImageView)findViewById(R.id.deck);
        playerList = (TextView)findViewById(R.id.playerList);
        notifs = (TextView)findViewById(R.id.notifs);
    }

    public static void pushData(){
        game.asetTurn(player);
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    public static void showNotification(String s) {
        notifs.setText(s);
    }

    public static void showToast(String s) {
        Toast.makeText(mMainActivity, s, Toast.LENGTH_LONG).show();
    }


}