//package com.example.junaidpatel.loveletter;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
///**
// * Layout for guard class
// * This is necessary as after you choose your target player you must then guess a card which they may or may not possess
// * A new intent is the cleanest way we found of accomplishing this task
// * Created by padcf, paulvincentphillips & bradyc12 on 16/12/2016.
// */
//
//public class GuardLayout extends Activity {
//
//    Bundle guardChoice = new Bundle();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.guard);
//
//        final Button priestChoice = (Button) findViewById(R.id.priestChoice);
//        final Button baronChoice = (Button) findViewById(R.id.baronChoice);
//        final Button handmaidChoice = (Button) findViewById(R.id.handmaidChoice);
//        final Button princeChoice = (Button) findViewById(R.id.princeChoice);
//        final Button kingChoice = (Button) findViewById(R.id.kingChoice);
//        final Button countessChoice = (Button) findViewById(R.id.countessChoice);
//        final Button princessChoice = (Button) findViewById(R.id.princessChoice);
//
//        priestChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 2);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," Two", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        baronChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 3);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," Three", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        handmaidChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 4);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," handmaid", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        princeChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 5);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," handmaid", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        kingChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 6);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," king", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        countessChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 7);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," countess", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        princessChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                guardChoice.putInt("One choice", 8);
//                finish();       //destroys this activity and returns to main
//                Toast.makeText(getApplicationContext()," princess", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
