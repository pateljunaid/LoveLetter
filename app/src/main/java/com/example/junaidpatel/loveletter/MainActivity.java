package com.example.junaidpatel.loveletter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static Game game;
    public static Player player;
    public static Constants constants;
    public FirebaseDatabase fbd;
    private ViewGroup mainLayout;

    public static ImageView card2;
    public static ImageView card1;
    public static ImageView topCard;
    public static Button startGame;
    public static Button resetDB;
    public static Button ok;
    private static ImageView deck;
    private static TextView notifs;
    private static TextView playerList;
    private static EditText inputName;
    public static Context mMainActivity;
    private boolean offline = false;
    private static int xDelta;
    static GestureDetector gestureDetector;
    private static int yDelta;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivity = this;

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
            nextTurn();
        }

//        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
//        deck.setOnTouchListener(onTouchListener());

//        final int windowwidth;
//        final int windowheight;
//        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
//        windowheight = getWindowManager().getDefaultDisplay().getHeight();
//        final ImageView deck = (ImageView) findViewById(R.id.deck);

        resetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setHasStarted(true);
                startGame.setVisibility(View.INVISIBLE);
                game.deal();
                nextTurn();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayerName(inputName.getText().toString());
                pushData();
                ok.setVisibility(View.INVISIBLE);
                inputName.setVisibility(View.INVISIBLE);
            }
        });

        fbd.getInstance().getReference("gameData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Update game
                game = dataSnapshot.getValue(Game.class);

                //Update player
                if (player != null) {
                    if (player.isInGame()) {
                        player = game.agetPlayerById(player.getPlayerId());
                    } else if (!player.isInGame() && !player.isEliminated()) {
                        //Add new player to the game
                        game.addPlayer(player);
                        pushData();
                    }
                }

                //Further in-game updates
                if (game.getHasStarted()) {
                    //Update cards on every turn
                    if (game.getTopCard() != null) {
                        topCard.setImageResource(constants.drawableHashMap.get(game.getTopCard()));
                    }

                    if (player != null) {
                        card1.setImageResource(constants.drawableHashMap.get(player.getCard1()));
                    }

                    //Display turn
                    game.addBroadcast(game.agetCurrentTurnPlayer().getPlayerName() + "'s turn");

                    //Display broadcast message
                    showNotification(game.getBroadcast());

                    if (player == null ||  game.isOver()) {
//                        fbd.getInstance().getReference("gameData").removeEventListener(this);
                        return;
                    }

                    //update UI for all devices
                    playerList.setVisibility(View.INVISIBLE);
                    inputName.setVisibility(View.INVISIBLE);
                    ok.setVisibility(View.INVISIBLE);
                    deck.setVisibility(View.VISIBLE);

                    //Show secret messages
                    if (player.getSecretMessage() != null) {
                        showToast(player.getSecretMessage());
                    }

                    //Allow person with the correct turn to play
                    if (game.agetCurrentTurnPlayer() == player) {
                        showToast("Your Turn");
                        deck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                player.setCard2(game.drawCard());
                                card2.setImageResource(constants.drawableHashMap.get(player.getCard2()));
                                card2.setVisibility(View.VISIBLE);
                                setClickListeners();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void resetGame() {
        Game newGame = new Game();
        FirebaseDatabase.getInstance().getReference("gameData").setValue(newGame);
    }

    private void setClickListeners() {
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

        if (player.hasCard(7)) {
            if (player.hasCard(5) || player.hasCard(6)) {
                if (player.getCard1() == 7) {
                    card2.setOnClickListener(null);
                }
                else {
                    card1.setOnClickListener(null);
                }
            }
        }
    }

    public static void setTouchListeners() {
        deck.setOnTouchListener(onTouchListener());
    }

    public static void removeListeners() {
        card1.setOnClickListener(null);
        card2.setOnClickListener(null);
    }

    private void initializeResources() {
        resetDB = (Button)findViewById(R.id.resetDB);
        ok = (Button)findViewById(R.id.ok);
        startGame = (Button)findViewById(R.id.startGame);
        card1 = (ImageView) findViewById(R.id.card1);
        card2 = (ImageView) findViewById(R.id.card2);
        topCard = (ImageView)findViewById(R.id.topCard);
        deck = (ImageView)findViewById(R.id.deck);
        playerList = (TextView)findViewById(R.id.playerList);
        inputName = (EditText) findViewById(R.id.inputName);
        notifs = (TextView)findViewById(R.id.notifs);
    }

    public static void nextTurn(){
        //If only one player remains, broadcast game over
        if (game.getPlayers().size() == 1 && game.getHasStarted()) {
            game.addBroadcast("GAME OVER! " + game.getPlayers().get(0).getPlayerName() + " WINS!");
            game.setOver(true);
        }
        else {
            game.asetTurn(player);
        }
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    public static void pushData(){
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    public static void showNotification(String s) {
        notifs.setText(s);
    }

    public static void showToast(String s) {
        Toast.makeText(mMainActivity, s, Toast.LENGTH_LONG).show();
    }

    private static View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                else {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                    view.getLayoutParams();
                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                            break;
                    }
                    view.invalidate();
                    return false;
                }
            }
        };
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}