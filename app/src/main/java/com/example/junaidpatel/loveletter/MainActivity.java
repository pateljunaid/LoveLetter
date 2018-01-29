package com.example.junaidpatel.loveletter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private Player player;
    public FirebaseDatabase fbd;
    Button endGame;
    Button startGame;
    TextView playerList;
    TextView notifs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate the game and player
        player = new Player();
        game = new Game();

        fbd.getInstance().getReference("gameData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                game = dataSnapshot.getValue(Game.class);

                //TODO: move to oncreate main activity
                if (!game.getPlayers().contains(player.getPlayerName())) {
                    joinGame();
                }

                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        endGame = (Button)findViewById(R.id.endGame);
        startGame = (Button)findViewById(R.id.startGame);
        playerList = (TextView)findViewById(R.id.playerList);
        notifs = (TextView)findViewById(R.id.notifs);


        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGame();
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Table.class);
                intent.putExtra("game", (Parcelable) game);
                intent.putExtra("player", (Parcelable) player);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    private void updatePlayerList() {

    }

    private void endGame() {
        game.endGame();
        pushData();
        updateUI();
    }

    private void pushData(){
        game.setLastTurn(player.getPlayerId());
        FirebaseDatabase.getInstance().getReference("gameData").setValue(game);
    }

    private void joinGame(){
        player = game.addPlayer(player);
        pushData();
    }

    private void showNotification(String s) {
        notifs.setText(s);
    }

    public void updateUI() {
        if (player.isHost()) {
            startGame.setVisibility(View.VISIBLE);
        }

        String builder = "";
        for (String player: game.getPlayers()) {
            builder += (game.getPlayers().indexOf(player) + 1) + ": " + player + "\n";
        }
        playerList.setText("Players List\n\n" + builder);
    }
}
