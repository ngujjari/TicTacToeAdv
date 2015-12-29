package com.mobileapps.ngujjari.ticatacapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "TicTacSettingsActivity";
    public static final String PREFS_NAME = "NareshTicTacPrefsFile";

    private CheckBox singleplaycheckBox;
    private CheckBox twoplaycheckBox;

    private EditText editText_player1;
    private EditText editText_player2;

    private Button lowbutton;
    private Button midbutton;
    private Button highbutton;

    private Button startgamebtn;
    private Button clearscoresbtn;
    private String complexity;


   // @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SharedPreferences savedData = getSharedPreferences(PREFS_NAME, 0);
        setToolBar();


        singleplaycheckBox = (CheckBox) findViewById(R.id.singleplaycheckBox);
        singleplaycheckBox.setOnClickListener(new PlayerCheckboxClickListener());
        twoplaycheckBox = (CheckBox) findViewById(R.id.twoplaycheckBox);
        twoplaycheckBox.setOnClickListener(new PlayerCheckboxClickListener());

        editText_player1 = (EditText) findViewById(R.id.editText_player1);
        editText_player2 = (EditText) findViewById(R.id.editText_player2);

        lowbutton = (Button) findViewById(R.id.lowbutton) ;
        lowbutton.setTransformationMethod(null);
        lowbutton.setOnClickListener(new ComplexityBtnClickListener());

        midbutton = (Button) findViewById(R.id.midbutton) ;
        midbutton.setTransformationMethod(null);
        midbutton.setOnClickListener(new ComplexityBtnClickListener());

        highbutton = (Button) findViewById(R.id.highbutton) ;
        highbutton.setTransformationMethod(null);
        highbutton.setOnClickListener(new ComplexityBtnClickListener());

        lowbutton.setBackground(null);
        midbutton.setBackground(null);
        highbutton.setBackground(null);

        String savedplayer1Name = savedData.getString("player1Name", "Player1");
        editText_player1.setText(savedplayer1Name);
        String savedplayer2Name = savedData.getString("player2Name", "Player2");
        editText_player2.setText(savedplayer2Name);
        if(savedData.getBoolean("isTwoPlayerEnabled", false)) {
            Log.v(TAG, " twoplaycheckBox: true");
            //twoplaycheckBox.setChecked(true);
            twoplaycheckBox.performClick();
            singleplaycheckBox.setChecked(false);
        }
        else{
            Log.v(TAG, " singleplaycheckBox: true");
            //twoplaycheckBox.setChecked(false);
            //singleplaycheckBox.setChecked(true);
            singleplaycheckBox.performClick();
        }
        String saved_complexity = savedData.getString("complexity", "LOW");
        if(saved_complexity.equals("LOW")) {
            lowbutton.performClick();
        }
        else if(saved_complexity.equals("MID")) {
            midbutton.performClick();
        }
        else {
            highbutton.performClick();
        }

       /* Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null && !intent.getExtras().isEmpty()) { // Intent called from Main Activity
            boolean singlePlayerChecked = intent.getBooleanExtra("singlePlayerChecked", false); //if it's a string you stored.
            boolean twoPlayerChecked = intent.getBooleanExtra("twoPlayerChecked", false);
            singleplaycheckBox.setChecked(singlePlayerChecked);
            if(singlePlayerChecked) singleplaycheckBox.performClick();
            twoplaycheckBox.setChecked(twoPlayerChecked);
            if(twoPlayerChecked) twoplaycheckBox.performClick();

            String player1Name = intent.getStringExtra("player1Name");
            player1Name = (player1Name == null || player1Name.trim().equals("")) ? "Player1" : player1Name;
            editText_player1.setText(player1Name);
            String player2Name = intent.getStringExtra("player2Name");
            player2Name = (player2Name == null || player2Name.trim().equals("")) ? "Player2" : player2Name;
            editText_player2.setText(player2Name);
            complexity = intent.getStringExtra("complexity");
            complexity = (complexity == null || complexity.equals("")) ? "LOW" : complexity;
            if(complexity.equals("LOW")) {
                lowbutton.performClick();
            }
            else if(complexity.equals("MID")) {
                midbutton.performClick();
            }
            else {
                highbutton.performClick();
            }


        }*/
        startgamebtn = (Button) findViewById(R.id.startgamebtn) ;
        startgamebtn.setOnClickListener(new StartGameBtnClickListener());

        clearscoresbtn = (Button) findViewById(R.id.clearscoresbtn);
        clearscoresbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences savedData = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = savedData.edit();
                editor.putInt("player1score", 0);
                editor.putInt("player2score", 0);

                // Commit the edits!
                editor.commit();
            }
        });
    }

    public void setToolBar()
    {
          android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_actionbar_settingspage);
          myToolbar.setTitle("Tic Tac Toe - Advanced");
          setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final class StartGameBtnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            Log.v(TAG, " button id : " + view.getId());
            boolean singlePlayerChecked =  singleplaycheckBox.isChecked();
            boolean twoPlayerChecked =  twoplaycheckBox.isChecked();

            Log.v(TAG, "singlePlayerChecked: " + singlePlayerChecked + ", twoPlayerChecked = " + twoPlayerChecked);

            String player1Name = editText_player1.getText().toString();
            String player2Name = editText_player2.getText().toString();

            Log.v(TAG, "player1Name: " + player1Name + ", player2Name = " + player2Name + " , complexity = " + complexity);


            Intent settingsIntent = new Intent(getApplicationContext(), MainActivity.class);
            settingsIntent.putExtra("singlePlayerChecked", singlePlayerChecked); //Optional parameters
            settingsIntent.putExtra("twoPlayerChecked", twoPlayerChecked);
            settingsIntent.putExtra("player1Name", player1Name);
            settingsIntent.putExtra("player2Name", player2Name);
            settingsIntent.putExtra("complexity", complexity);

            startActivity(settingsIntent);
            SettingsActivity.this.finish();
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Animate slide right to left
            overridePendingTransition(R.anim.slide_in_right1 , R.anim.slide_out_left1);
            //overridePendingTransition(R.anim.slide_out_left , R.anim.slide_in_right);

            return ;
        }
    }



    private final class ComplexityBtnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            Log.v(TAG, " button id : " + view.getId());
            complexity = null;
            Resources res = getResources();
            Drawable shape = res.getDrawable(R.drawable.buttoncomplexity);
            //shape.applyTheme();
            if(view.getId() == R.id.lowbutton){
                complexity = "LOW";

                lowbutton.setBackground(shape);
                midbutton.setBackground(null);
                highbutton.setBackground(null);
            }
            else if (view.getId() == R.id.midbutton){
                complexity = "MID";
                lowbutton.setBackground(null);
                midbutton.setBackground(shape);
                highbutton.setBackground(null);
            }
            else if(view.getId() == R.id.highbutton){
                complexity = "HIGH";
                lowbutton.setBackground(null);
                midbutton.setBackground(null);
                highbutton.setBackground(shape);
            }

            return ;
        }
    }

    private final class PlayerCheckboxClickListener implements View.OnClickListener {

        public void onClick(View view) {
           // Log.v(TAG, " PlayerCheckboxClickListener : "+view.getId());
            if(view.getId() == R.id.singleplaycheckBox){
                Log.v(TAG, " PlayerCheckboxClickListener : singleplaycheckBox "+view.getId());
                singleplaycheckBox.setChecked(true);
                twoplaycheckBox.setChecked(false);
                editText_player2.setText("Player2");
                editText_player2.setFocusable(false);
            }
            else if(view.getId() == R.id.twoplaycheckBox){
                Log.v(TAG, " PlayerCheckboxClickListener : twoplaycheckBox "+view.getId());
                singleplaycheckBox.setChecked(false);
                twoplaycheckBox.setChecked(true);
                editText_player2.setFocusable(true);
                editText_player2.setFocusableInTouchMode(true);
            }
        }
    }
}
