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
import android.widget.EditText;
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
    private static Button startGame;
    private static ImageView card1;
    private static ImageView card2;
    private static ImageView topCard;
    private static ImageView deck;
    private static TextView notifs;
    private static TextView playerList;
    private static Context mContext;
    private boolean offline = false;
    private AlertDialog radioDialog;
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

        mContext = getApplicationContext();

        //to remove the action bar (title bar)
        getSupportActionBar().hide();
        constants = new Constants();

        //Initialize Resources
        initializeResources();

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
                } else if (!player.isInGame()) {
                    //Add player to the game
                    game.addPlayer(player);
                    FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
                }

                //In-game updates
                if (game.isHasStarted()) {

                    //Check if game is done
                    if (game.getPlayers().size() == 1) {
                        game.addBroadcast("Game over");
                    }

                    card1.setImageResource(constants.drawableHashMap.get(player.getCard1()));

                    playerList.setVisibility(View.INVISIBLE);
                    deck.setVisibility(View.VISIBLE);

                    //Display turn
                    game.addBroadcast("Player " + game.getTurn() + "'s turn");

                    //Display broadcast message
                    showNotification(game.getBroadcast());

                    //Show secret messages
                    if (player.getSecretMessage() != null) {
                        Toast.makeText(MainActivity.this, player.getSecretMessage(), Toast.LENGTH_LONG);
                    }

                    //Allow person with the correct turn to play
                    if (game.getTurn() == player.getPlayerId()) {
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
                game.setBroadcast("");
                game.setHasStarted(false);
                game.setPlayers(new ArrayList<Player>());
                game.setTopCard(null);
                game.setDeck(constants.deck);
                game.turn = 0;
                FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
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
        Log.d("yakkity", game.agetDeck().toString());
        game.asetTurn(player);
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    public static void showNotification(String s) {
        notifs.setText(s);
    }

    public static void showToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    public Integer showRadioDialog(CharSequence[] values){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select target player");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                MainActivity.guess = item;
                radioDialog.dismiss();
            }
        });
        radioDialog = builder.create();
        radioDialog.show();

        return MainActivity.guess;
    }

    public void showInputDialog(String title, String msg, String hint) {
        final EditText txtUrl = new EditText(this);

        txtUrl.setHint(hint);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setView(txtUrl)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        game.playCard(player, game.nextPlayer(player), player.getCard1(), 2, 1);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}
