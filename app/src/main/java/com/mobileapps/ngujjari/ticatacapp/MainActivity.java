package com.mobileapps.ngujjari.ticatacapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener{
    private static final String TAG = "TicTacMainActivity";
    final Context context = this;
    private Button btnClick;
    // private ImageButton btnClickPly1;
    //  private ImageButton btnClickPly2;
    private Button btnClickPlayAgain;
    private Button btnClickOptions;


    private ImageButton optionsBtnTop;
    private ImageButton playAgainBtnTop;

    private ImageButton btnClick1;
    private ImageButton btnClick2;
    private ImageButton btnClick3;
    private ImageButton btnClick4;
    private ImageButton btnClick5;
    private ImageButton btnClick6;
    private ImageButton btnClick7;
    private ImageButton btnClick8;
    private ImageButton btnClick9;

    private boolean isTwoPlayerEnabled = false;
    private String complexity = "";

    private Integer player1WinsCnt = 0;
    private Integer player2WinsCnt = 0;

    private TextView player1LblName;
    private TextView player2LblName;

    private TextView player1Score;
    private TextView player2Score;

    private Animation animation;
    // Animation
    Animation animBlink;
    private TextView toastText;
    private Toast toast;
    private View layout;
    private boolean highLevel = true;
    private ActionBar topToolBar;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences savedData = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
       // setToolBar();

        TicTacToeApp application = (TicTacToeApp) getApplication();
        mTracker = application.getDefaultTracker();

        Intent intent = getIntent();
        boolean singlePlayerChecked = intent.getBooleanExtra("singlePlayerChecked", false); //if it's a string you stored.
        boolean twoPlayerChecked = intent.getBooleanExtra("twoPlayerChecked", false);
        isTwoPlayerEnabled = (twoPlayerChecked == true) ? true : false;


        String player1Name = intent.getStringExtra("player1Name");
        player1Name =(player1Name == null || player1Name.trim().equals("")) ? "Player1" : player1Name;
        String player2Name = intent.getStringExtra("player2Name");
        player2Name =(player2Name == null || player2Name.trim().equals("")) ? "Player2" : player2Name;
        complexity = intent.getStringExtra("complexity");
        complexity = (complexity == null || complexity.equals("")) ? "LOW" : complexity;
        player1LblName = (TextView)findViewById(R.id.player1Lbl) ;
        player2LblName = (TextView)findViewById(R.id.player2Lbl) ;
        player1LblName.setText(player1Name);

        if(isTwoPlayerEnabled) player2LblName.setText(player2Name);
        Log.v(TAG, "player1Name: " + player1Name + ", player2Name = " + player2Name + " , complexity = " + complexity +" singlePlayerChecked= "+singlePlayerChecked);

        btnClick1 = (ImageButton) findViewById(R.id.button1) ;
        //btnClick1.setOnClickListener(this);
        btnClick1.setOnTouchListener(new MyTouchListener());
        btnClick1.setOnDragListener(new MyDragListener());

        btnClick2 = (ImageButton) findViewById(R.id.button2) ;
        // btnClick2.setOnClickListener(this);
        btnClick2.setOnTouchListener(new MyTouchListener());
        btnClick2.setOnDragListener(new MyDragListener());

        btnClick3 = (ImageButton) findViewById(R.id.button3) ;
        //// btnClick3.setOnClickListener(this);
        btnClick3.setOnTouchListener(new MyTouchListener());
        btnClick3.setOnDragListener(new MyDragListener());

        btnClick4 = (ImageButton) findViewById(R.id.button4) ;
        // btnClick4.setOnClickListener(this);
        btnClick4.setOnTouchListener(new MyTouchListener());
        btnClick4.setOnDragListener(new MyDragListener());

        btnClick5 = (ImageButton) findViewById(R.id.button5) ;
        // btnClick5.setOnClickListener(this);
        btnClick5.setOnTouchListener(new MyTouchListener());
        btnClick5.setOnDragListener(new MyDragListener());

        btnClick6 = (ImageButton) findViewById(R.id.button6) ;
        // btnClick6.setOnClickListener(this);
        btnClick6.setOnTouchListener(new MyTouchListener());
        btnClick6.setOnDragListener(new MyDragListener());

        btnClick7 = (ImageButton) findViewById(R.id.button7) ;
        //btnClick7.setOnClickListener(this);
        btnClick7.setOnTouchListener(new MyTouchListener());
        btnClick7.setOnDragListener(new MyDragListener());

        btnClick8 = (ImageButton) findViewById(R.id.button8) ;
        // btnClick8.setOnClickListener(this);
        btnClick8.setOnTouchListener(new MyTouchListener());
        btnClick8.setOnDragListener(new MyDragListener());

        btnClick9 = (ImageButton) findViewById(R.id.button9) ;
        //btnClick9.setOnClickListener(this);
        btnClick9.setOnTouchListener(new MyTouchListener());
        btnClick9.setOnDragListener(new MyDragListener());

        //ImageView xview = (ImageView) findViewById(R.id.player1Image) ;
        //xview.setColorFilter(Color.argb(255, 100, 20, 100)); // White Tint/


        player1Score = (TextView)findViewById(R.id.player1score) ;
        player1WinsCnt = savedData.getInt("player1score", 0);
        player1Score.setText(player1WinsCnt + "");
        player2WinsCnt = savedData.getInt("player2score", 0);
        player2Score = (TextView)findViewById(R.id.player2score) ;
        player2Score.setText( player2WinsCnt+ "");

        // Messages
       /* LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.activity_main,
                (ViewGroup) findViewById(R.id.myrelativelayout));

        toastText = (TextView) layout.findViewById(R.id.toasttext);


        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);*/

        toastText = (TextView) findViewById(R.id.toasttext) ;
        toastText.setVisibility(View.INVISIBLE);
        // load the animation
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        // set animation listener
        animBlink.setAnimationListener(this);


        btnClickPlayAgain = (Button) findViewById(R.id.playagain) ;
        btnClickOptions = (Button) findViewById(R.id.optionsbtn) ;

        btnClickPlayAgain.setVisibility(View.INVISIBLE);
        btnClickOptions.setVisibility(View.INVISIBLE);

        btnClickPlayAgain.setOnClickListener(new PlayAgainOnclickListener());
        /*btnClickPlayAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


               // slideToLeft(rightlayout);
                Log.v(TAG, "PlayAgainClickListener onclick Begin : " + v.getId() + ", complexity= " + complexity);
                toastText.setVisibility(View.INVISIBLE);
                toastText.clearAnimation();

                TicTacToeApp app = (TicTacToeApp) getApplication();
                app.setMs(null);
                TicTacAbstractController ms = null;

                if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
                else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
                else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();

                app.setMs(ms);

                setMessages(ms, v);
     *//*           TranslateAnimation moveLefttoRight = new TranslateAnimation(0, v.getWidth(), 0, 0);
                moveLefttoRight.setDuration(500);
                moveLefttoRight.setFillAfter(true);
                btnClickPlayAgain.startAnimation(moveLefttoRight);*//*
                btnClickPlayAgain.setVisibility(View.INVISIBLE);
                btnClickOptions.setVisibility(View.INVISIBLE);
                stopSuccessAnimation();

            }
        });*/

        btnClickOptions.setOnClickListener(new OptionsClickListener());
        optionsBtnTop = (ImageButton) findViewById(R.id.optionsbtn2) ;
        optionsBtnTop.setOnClickListener(new OptionsClickListener());

        playAgainBtnTop  = (ImageButton) findViewById(R.id.playagain2) ;
        playAgainBtnTop.setOnClickListener(new PlayAgainOnclickListener());
        /*btnClickOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // slideToLeft(rightlayout);
                Log.v(TAG, "OptionsClickListener onclick Begin : " + v.getId() + ", complexity= " + complexity);
                toastText.clearAnimation();
                TicTacToeApp app = (TicTacToeApp) getApplication();
                app.setMs(null);
                TicTacAbstractController ms = null;

                if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
                else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
                else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();

                app.setMs(ms);

                final Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                MainActivity.this.startActivity(intent);
                //MainActivity.this.
                //MainActivity.this.finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);


               // Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
               *//* Intent mainIntent = new Intent(MainActivity.this, SettingsActivity.class);


                MainActivity.this.startActivity(mainIntent);

                // Animate slide right to left
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
*//*
                //setMessages(ms, v);
*//*

                TranslateAnimation moveLefttoRight = new TranslateAnimation(0, v.getWidth(), 0, 0);
                moveLefttoRight.setDuration(500);
                moveLefttoRight.setFillAfter(true);
                btnClickPlayAgain.startAnimation(moveLefttoRight);
                btnClickPlayAgain.setVisibility(View.INVISIBLE);
                btnClickOptions.setVisibility(View.INVISIBLE);
                stopSuccessAnimation();
*//*

            }
        });*/


        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in


        SharedPreferences.Editor editor = savedData.edit();
        editor.putString("player1Name", player1Name);
        editor.putString("player2Name", player2Name);
        editor.putBoolean("isTwoPlayerEnabled", isTwoPlayerEnabled);
        editor.putString("complexity", complexity);

        // Commit the edits!
        editor.commit();

        Log.v(TAG, "MainActivity: onCreate End ");

    }


    //@TargetApi(21)
    public void setToolBar()
    {
          Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
          myToolbar.setTitle("Tic Tac Toe - Advanced"); // -- Ultimate or advanced
          setSupportActionBar(myToolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private final class OptionsClickListener implements View.OnClickListener {

        public void onClick(View v) {
            v.playSoundEffect(SoundEffectConstants.CLICK);
            Log.v(TAG, "OptionsClickListener onclick Begin : " + v.getId() + ", complexity= " + complexity);
            toastText.clearAnimation();
            TicTacToeApp app = (TicTacToeApp) getApplication();
            app.setMs(null);
            TicTacAbstractController ms = null;

            if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
            else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
            else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();

            app.setMs(ms);

            final Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            boolean singlePlayerChecked = (isTwoPlayerEnabled) ? false: true;
            boolean twoPlayerChecked = (isTwoPlayerEnabled) ? true: false;
            intent.putExtra("singlePlayerChecked", singlePlayerChecked); //Optional parameters
            intent.putExtra("twoPlayerChecked", twoPlayerChecked);
            intent.putExtra("player1Name", player1LblName.getText());
            intent.putExtra("player2Name", player2LblName.getText());
            intent.putExtra("complexity", complexity);

            MainActivity.this.startActivity(intent);
            //MainActivity.this.
            //MainActivity.this.finish();
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

        }
    }

    private final class PlayAgainOnclickListener implements View.OnClickListener {

        public void onClick(View v) {

            Log.v(TAG, "PlayAgainClickListener onclick Begin : " + v.getId() + ", complexity= " + complexity);
            v.playSoundEffect(SoundEffectConstants.CLICK);
            toastText.setVisibility(View.INVISIBLE);
            toastText.clearAnimation();

            TicTacToeApp app = (TicTacToeApp) getApplication();
            app.setMs(null);
            TicTacAbstractController ms = null;

            if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
            else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
            else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();

            app.setMs(ms);

            setMessages(ms, v);
     /*           TranslateAnimation moveLefttoRight = new TranslateAnimation(0, v.getWidth(), 0, 0);
                moveLefttoRight.setDuration(500);
                moveLefttoRight.setFillAfter(true);
                btnClickPlayAgain.startAnimation(moveLefttoRight);*/
            btnClickPlayAgain.setVisibility(View.INVISIBLE);
            btnClickOptions.setVisibility(View.INVISIBLE);
            stopSuccessAnimation();

        }
    }

    private final class PlayAgainTouchListener implements View.OnTouchListener {

        public boolean onTouch(View v, MotionEvent motionEvent) {

            Log.v(TAG, "PlayAgainClickListener onclick Begin : " + v.getId() +", complexity= "+complexity);

            //mTracker.setScreenName("MainActivityPlayAgain");
            //mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            TicTacToeApp app = (TicTacToeApp) getApplication();
            app.setMs(null);
            TicTacAbstractController ms = null;

            if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
            else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
            else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();

            app.setMs(ms);
            stopSuccessAnimation();
            setMessages(ms, v);
            TranslateAnimation moveLefttoRight = new TranslateAnimation(0, v.getWidth(), 0, 0);
            moveLefttoRight.setDuration(500);
            moveLefttoRight.setFillAfter(true);
            btnClickPlayAgain.startAnimation(moveLefttoRight);

            btnClickPlayAgain.setVisibility(View.INVISIBLE);
            btnClickOptions.setVisibility(View.INVISIBLE);
            return true;

        }
    }
    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {

            //initToast();

            Log.v(TAG, "MyTouchListener onTouch Begin : " + view.getId());
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                Log.v(TAG, "MyTouchListener onTouch end  true : " + view.getId());
                return true;
            } else if (isTwoPlayerEnabled) {
                Log.v(TAG, "MyTouchListener onTouch isTwoPlayerEnabled true button id : " + view.getId());
                String buttonVlu = getButtonId(view.getId());
                Log.v(TAG, " button id : " + view.getId() + " buttonVlu : " + buttonVlu);

                TicTacToeApp app = (TicTacToeApp) getApplication();
                TicTacAbstractController ms = null;
                ms =  (app.getMs() == null) ? new HighComplexity() : app.getMs();
                app.setMs(ms);
                if(ms.isWon) return false;
                Log.v(TAG, ms.flipPlayer + " onTouch method Begin   Button ID : " + view.getId());
                ms.player =  (ms.player == null || ms.player.equals("")) ? "Player1" : ms.player;

                Log.v(TAG, " onTouch Begin  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  buttonVlu = " + buttonVlu);
                if (!ms.player.equals("") && ms.tList.size() < 6) {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                    boolean executeStep = ms.execute(Integer.parseInt(buttonVlu));
                    Log.v(TAG, " onTouch executeStep  : " + executeStep);
                    if (executeStep) {
                        boolean isWonGame = ms.isWon;
                        if (isWonGame == false) {
                            ms.player = (ms.player.equals("Player1") ? "Player2" : "Player1"); // Next step player
                            ms.flipPlayer = true;
                        }

                    } else {
                        ms.flipPlayer = false;
                    }

                }

                setMessages(ms, view);


            } else {
                Log.v(TAG, "MyTouchListener onTouch end False : " + view.getId());

                String buttonVlu = getButtonId(view.getId());
                Log.v(TAG, " button id : " + view.getId() + " buttonVlu : " + buttonVlu);

                // Messages
                // setMessage(toast, layout, toastText, "");

                TicTacToeApp app = (TicTacToeApp) getApplication();
                TicTacAbstractController ms = null;
                if (app.getMs() == null) {
                    if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
                    else if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
                    else if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();
                } else {
                    ms = app.getMs();
                }
                app.setMs(ms);
                if(ms.isWon) return false;
                Log.v(TAG, ms.flipPlayer + " onTouch method Begin   Button ID : " + view.getId());
                ms.player = "Player1";

                Log.v(TAG, " onTouch Begin  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  buttonVlu = " + buttonVlu);
                if (!buttonVlu.startsWith("Player") && !ms.player.equals("")) {
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                    boolean executeStep = ms.execute(Integer.parseInt(buttonVlu));
                    Log.v(TAG, " onTouch executeStep  : " + executeStep);
                    if (executeStep) {
                    } else {
                        ms.flipPlayer = false;
                    }
                    ms.previousPlayer = ms.player;
                    ms.flipPlayer = true;

                    if (!(ms.msgList != null && ms.msgList.size() > 0)) {
                        boolean isWonGame = ms.isWon;
                        if (isWonGame == false) {
                            ms.player = "Player2";

                            executeStep = true;
                            while (executeStep) {

                                if (ms.tList.size() < 6) {

                                    int toNd = ms.predictUserinput("singleNd", ms.player);
                                    if (toNd == -1) {
                                        toNd = ms.getRandomNum();
                                    }

                                    Log.v(TAG, "runAlg toNd before execute == " + toNd + " " + " from palyer = " + ms.player);
                                    executeStep = ms.execute(toNd);
                                } else {
                                    Log.v(TAG, "MyTouchListener: runAlg Lets play the game !!! !!!!  player = " + ms.player);
                                    ActionTakenBean userInput = ms.predictUserinput(ms.player);
                                    int fromNd = (userInput.getFromNd() != null) ? userInput.getFromNd().intValue() : -1;
                                    int toNd = (userInput.getToNd() != null) ? userInput.getToNd().intValue() : -1;
                                    if (fromNd > 0 && toNd > 0) {
                                        executeStep = ms.execute(fromNd, toNd);
                                        //break;
                                    }
                                }

                                executeStep = (executeStep == true) ? false : true;
                            }

                        }
                    }

                    setMessages(ms, view);
                }
                Log.v(TAG, " END  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  buttonVlu = " + buttonVlu);

                return false;
            }
            return true;
        }
    }

    private class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            // Log.v(TAG, "MyDragListener onTouch begin : "+v.getId() +"  action : "+action);
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    //mTracker.setScreenName("MainActivityDropEvent");
                   // mTracker.send(new HitBuilders.ScreenViewBuilder().build());

                    //initToast();
                    View view = (View) event.getLocalState();


                    Log.v(TAG, "MyDragListener onDrag after ACTION_DROP : source : "+view.getId() +"  target : "+v.getId());

                    String fromButton = getButtonId(view.getId());
                    String toButton = getButtonId(v.getId());
                    Log.v(TAG, "MyDragListener onDrag end False : " + view.getId() + " fromButton : " + fromButton + ",   toButton = " + toButton);
                    setMessage(toast, layout, toastText, "");
                    if (isTwoPlayerEnabled) {
                    Log.v(TAG, "MyDragListener onDrag isTwoPlayerEnabled true button id : " + view.getId());

                    TicTacToeApp app = (TicTacToeApp) getApplication();
                    TicTacAbstractController ms = null;
                    ms =  (app.getMs() == null) ? new HighComplexity() : app.getMs();
                    app.setMs(ms);

                    if(ms.isWon) return false;

                    ms.player =  (ms.player == null || ms.player.equals("")) ? "Player1" : ms.player;

                        Log.v(TAG, " two player onDrag  Begin  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  fromButton = " + Integer.parseInt(fromButton) + " toBtn = " + Integer.parseInt(toButton) + "  ,ms.dragStatus = " + ms.dragStatus);
                        if (!ms.player.equals("") && ms.tList.size() == 6 && !fromButton.equals(toButton)) {

                            boolean executeStep = ms.execute(Integer.parseInt(fromButton), Integer.parseInt(toButton));
                            if (executeStep) {
                                v.playSoundEffect(SoundEffectConstants.CLICK); // Play Sound
                                boolean isWonGame = ms.isWon;
                                if (isWonGame == false) {
                                    ms.player = (ms.player.equals("Player1") ? "Player2" : "Player1"); // Next step player
                                    ms.flipPlayer = true;
                                }

                            } else {
                                ms.flipPlayer = false;
                            }


                            setMessages(ms, view);
                        }

                }
                    else {
                        // Init Toast
                       // setMessage(toast, layout, toastText, "");

                        TicTacToeApp app = (TicTacToeApp) getApplication();
                        TicTacAbstractController ms = null;
                        if (app.getMs() == null) {
                            if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
                            if (complexity.equalsIgnoreCase("MID")) ms = new MediumComplexity();
                            if (complexity.equalsIgnoreCase("HIGH")) ms = new HighComplexity();
                        } else {
                            ms = app.getMs();
                        }
                        app.setMs(ms);
                        if(ms.isWon) return false;

                        ms.player = "Player1";
                        Log.v(TAG, "onDrag  Begin  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  fromButton = " + Integer.parseInt(fromButton) + " toBtn = " + Integer.parseInt(toButton) + "  ,ms.dragStatus = " + ms.dragStatus);
                        if (ms.player.equals("Player1") && !fromButton.equals(toButton) && ms.tList.size() == 6) {

                            boolean executeStep = ms.execute(Integer.parseInt(fromButton), Integer.parseInt(toButton));
                            Log.v(TAG, "onDrag  executeStep  : " + executeStep);
                            if (executeStep) {
                                v.playSoundEffect(SoundEffectConstants.CLICK); // Play Sound
                               // ms.previousPlayer = ms.player;
                                // ms.player = buttonVlu;
                                ms.flipPlayer = true;

                                if (!(ms.msgList != null && ms.msgList.size() > 0)) {
                                    boolean isWonGame = ms.isWon;
                                    if (isWonGame == false) {
                                        ms.player = "Player2";

                                        executeStep = true;
                                        while (executeStep) {

                                            if (ms.tList.size() < 6) {

                                                int toNd = ms.predictUserinput("singleNd", ms.player);
                                                if (toNd == -1) {
                                                    toNd = ms.getRandomNum();
                                                }

                                                Log.v(TAG, "runAlg toNd before execute == " + toNd + " " + " from palyer = " + ms.player);
                                                executeStep = ms.execute(toNd);
                                            } else {
                                                ActionTakenBean userInput = ms.predictUserinput(ms.player);
                                                Log.v(TAG, "MyDragListener :runAlg Lets play the game !!! !!!!  player = " + ms.player + " from = " + userInput.getFromNd() + " toNd = " + userInput.getToNd());

                                                int fromNd = (userInput.getFromNd() != null) ? userInput.getFromNd().intValue() : -1;
                                                int toNd = (userInput.getToNd() != null) ? userInput.getToNd().intValue() : -1;
                                                if (fromNd > 0 && toNd > 0) {
                                                    executeStep = ms.execute(fromNd, toNd);
                                                    //break;
                                                } else {
                                                    Log.v(TAG, "ELSE MyDragListener :runAlg Lets play the game !!! !!!!  player = " + ms.player + " from = " + fromNd + " toNd = " + toNd);

                                                }

                                            }

                                            executeStep = (executeStep == true) ? false : true;
                                        }


                                    }
                                }


                            } else {
                                ms.flipPlayer = false;

                            }

                            setMessages(ms, v);
                        }

                        Log.v(TAG, " END  : " + ms.flipPlayer + "=  curr: " + ms.player + "  prev  = " + ms.previousPlayer + "  buttonVlu = " + Integer.parseInt(toButton));

                    } // End single player
                            view.setVisibility(View.VISIBLE);
                    // view.dispatchTouchEvent(event);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            Log.v(TAG, "MyDragListener onDrag end : " + v.getId() + "  action : " + action);
            return true;
        }
    }


    public void onGameCompleteEvent(TicTacAbstractController ms, View v)
    {

        mTracker.setScreenName("MainActivityPlayerWin");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

       /* mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("MainActivity")
                .setAction("GameComplete")
                .setLabel(ms.player)
                .setValue(1)
                .build());*/

        List<Animator> animations = new ArrayList<>();
        String playerLbl = "";
        if (ms.player!= null && ms.player.equals("Player1") )
        {
            playerLbl = player1LblName.getText().toString();
        }
        else if (ms.player!= null && ms.player.equals("Player2") )
        {
            playerLbl = player2LblName.getText().toString();
        }
        else{
            playerLbl = "Player1";
        }

        toastText.setText(playerLbl + " won the game !!!!");
        toastText.setVisibility(View.VISIBLE);
        // start the animation
        toastText.startAnimation(animBlink);
        btnClickPlayAgain.setVisibility(View.VISIBLE);
        btnClickOptions.setVisibility(View.VISIBLE);
        /*TranslateAnimation moveLefttoRight = new TranslateAnimation(0, v.getWidth(), 0, 0);
        moveLefttoRight.setDuration(500);
        moveLefttoRight.setFillAfter(true);
        btnClickOptions.startAnimation(moveLefttoRight);
        btnClickOptions.setClickable(true);*/

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator xTrasnlationObjectAnimator = ObjectAnimator.ofFloat(btnClickOptions, View.TRANSLATION_X, 0, v.getWidth());
        xTrasnlationObjectAnimator.setRepeatCount(0);
        ObjectAnimator yTrasnlationObjectAnimator = ObjectAnimator.ofFloat(btnClickOptions, View.TRANSLATION_Y, 0, 0);
        yTrasnlationObjectAnimator.setRepeatCount(0);

        ObjectAnimator xpTrasnlationObjectAnimator = ObjectAnimator.ofFloat(btnClickPlayAgain, View.TRANSLATION_X,0, -v.getWidth());
        xpTrasnlationObjectAnimator.setRepeatCount(0);
        ObjectAnimator ypTrasnlationObjectAnimator = ObjectAnimator.ofFloat(btnClickPlayAgain, View.TRANSLATION_Y, 0, 0);
        ypTrasnlationObjectAnimator.setRepeatCount(0);

        animations.add(xTrasnlationObjectAnimator);
        animations.add(yTrasnlationObjectAnimator);
        animations.add(xpTrasnlationObjectAnimator);
        animations.add(ypTrasnlationObjectAnimator);

        animatorSet.playSequentially(animations);
        animatorSet.start();


        /*TranslateAnimation moveRighttoLeft = new TranslateAnimation(0, -v.getWidth(), 0, 0);
        moveRighttoLeft.setDuration(500);
        moveRighttoLeft.setFillAfter(true);
        btnClickPlayAgain.startAnimation(moveRighttoLeft);
        btnClickPlayAgain.setClickable(true);*/

    }

 /*   private void initToast()
    {
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.activity_main_toast,
                (ViewGroup) findViewById(R.id.activity_main_toast));

        toastText = (TextView) layout.findViewById(R.id.toasttext);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);

    }*/
   private void setMessage(Toast toast, View layout, TextView text, String msg)
    {
        Log.v(TAG, "toast setMessage begin msg = "+msg);


        try {
            /*LayoutInflater inflater = getLayoutInflater();
            layout = inflater.inflate(R.layout.activity_main,
                    (ViewGroup) findViewById(R.id.myrelativelayout));

            toastText = (TextView) layout.findViewById(R.id.toasttext);
            text.setText(msg);


            toast.setView(layout);
            toast.show();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMessages( TicTacAbstractController ms, final View v)
    {
        List<String> msgList = ms.msgList;
        Set<Integer> aList = ms.aList;
        Set<Integer> bList = ms.bList;
        Log.v(TAG, "setMessages begin msgList = "+msgList);
        if(!(msgList != null && msgList.size() > 0)) {
            boolean isWonGame = ms.isWon;

            setColor(aList, bList);
            if (isWonGame) {
               // setMessage(toast, layout, toastText, "Congratulations !! "+ms.player + " won the game.");
                //startSuccessAnimation(ms.player, aList, bList);
                onGameCompleteEvent(ms, v);
                Log.v(TAG, ms.player + " WON THE GAME !!!!!!! before player1WinsCnt = " + player1WinsCnt + "  ,  player2WinsCnt = " + player2WinsCnt);
                if (ms.player!= null && ms.player.equals("Player1") )  player1WinsCnt++ ;
                if (ms.player!= null && ms.player.equals("Player2") )  player2WinsCnt++ ;

                SharedPreferences savedData = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = savedData.edit();
                editor.putInt("player1score", player1WinsCnt);
                editor.putInt("player2score", player2WinsCnt);

                // Commit the edits!
                editor.commit();
                player1Score.setText(player1WinsCnt + "");
                player2Score.setText(player2WinsCnt + "");

                // custom dialog
               /* final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.customdialoge);


                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(ms.player + " won the game !!!!");
                *//*ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_person_black_24dp);
*//*
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogOptions);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.CENTER | Gravity.BOTTOM;

                dialog.show();*/

              /* AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.SuccessAlertDialogStyle);
                String pName = ms.player;
                pName = (pName != null) ? pName : pName;
                //alert.setTitle("Success");
                alert.setMessage(ms.player + " won the game !!!!");*/
                /*
                //alert.setIcon(R.drawable.ic_clear_white_48dp);
                alert.setNegativeButton("Options",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();

                                TicTacToeApp app = (TicTacToeApp) getApplication();
                                app.setMs(null);
                                TicTacAbstractController ms = null;

                                if (complexity.equalsIgnoreCase("LOW")) ms = new LowComplexity();
                                else if (complexity.equalsIgnoreCase("MID"))
                                    ms = new MediumComplexity();
                                else if (complexity.equalsIgnoreCase("HIGH"))
                                    ms = new HighComplexity();

                                app.setMs(ms);
                                stopSuccessAnimation();
                                setMessages(ms, v);

                            }
                        });

                AlertDialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                */
                /*WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

                View btn6Ref = (View) findViewById(R.id.myrelativelayout);


                wmlp.x = Math.round( btn6Ref.getX());   //x position
                wmlp.y = Math.round( btn6Ref.getY());  // 100*/

                //dialog.show();




                Log.v(TAG, ms.player + " WON THE GAME !!!!!!! after player1WinsCnt = " + player1WinsCnt + "  ,  player2WinsCnt = " + player2WinsCnt);
            }

        }
        else
        {
            for(String msg : msgList) {
                Log.v(TAG, ms.player + " Message : " + msg);
                if(msg.equals(TicTacAbstractController.MSG_1002)){
                    setColor(aList, bList); // reset colors
                    //setMessage(toast, layout, toastText, "Invalid move !!!!");
                }
            }
        }

    }


    private void startSuccessAnimation(String player, Set<Integer> aList,  Set<Integer> bList)
    {

        Set<Integer> abList = (player.equals("Player1")) ? aList : bList;
        for (Integer node : abList)
        {
            Log.v(TAG, " Success Nodes  " + node +" =  "+buttonMapRev.get(node+""));
            ImageButton btnClick = (ImageButton) findViewById(buttonMapRev.get(node+""));
            //Resources res = getResources();
            //Drawable shape = res. getDrawable(R.drawable.buttonplayer1);
            // btnClick.setBackground();;
            btnClick.startAnimation(animation);
            // btnClick.setBackground(shape);
        }
    }

    private void stopSuccessAnimation()
    {

        Set<Integer> abList = new HashSet(Arrays.asList(TicTacAbstractController.nodes));

        for (Integer node : abList)
        {
            Log.v(TAG, " Success Nodes  " + node +" =  "+buttonMapRev.get(node+""));
            ImageButton btnClick = (ImageButton) findViewById(buttonMapRev.get(node+""));
            //Resources res = getResources();
            //Drawable shape = res. getDrawable(R.drawable.buttonplayer1);
            // btnClick.setBackground();;
            btnClick.clearAnimation();
            // btnClick.setBackground(shape);
        }
    }


    private void setColor( Set<Integer> aList,  Set<Integer> bList)
    {

        // Flowers, X's and Os ,
        Set<Integer> setA = new HashSet(Arrays.asList(TicTacAbstractController.nodes));
        for (Integer node : aList)
        {
            //Log.v(TAG, " A SetColor Node  " + node +" =  "+buttonMapRev.get(node+""));
            ImageButton btnClick = (ImageButton) findViewById(buttonMapRev.get(node+""));
            Resources res = getResources();
            Matrix m = btnClick.getImageMatrix();
            RectF drawableRect = new RectF(0, 0, 2, 2);
            RectF viewRect = new RectF(0, 0, btnClick.getWidth(), btnClick.getHeight());
            m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
            btnClick.setImageMatrix(m);
            // btnClick.setImageMatrix();

            btnClick.setImageResource(R.drawable.ic_clear_white_48dp);
            btnClick.setScaleType(ImageView.ScaleType.FIT_XY);
            // b1.setText(adapt_objmenu.city_name_array[i]);
            //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //  lp.addRule(RelativeLayout.RIGHT_OF, btnClick.getId() - 1);

            //btnClick.setLayoutParams(lp);
            Drawable shape = res. getDrawable(R.drawable.buttonplayer1);
            // btnClick.setBackground();;
            btnClick.setBackground(shape);
        }
        for (Integer node : bList)
        {
            //Log.v(TAG, " B SetColor Node  " + node +" = "+buttonMapRev.get(node+""));
            /*Button btnClick = (Button) findViewById(buttonMapRev.get(node+""));
            btnClick.setBackgroundColor(Color.CYAN);*/
            ImageButton btnClick = (ImageButton) findViewById(buttonMapRev.get(node+""));
            Resources res = getResources();

            btnClick.setImageResource(R.drawable.ic_fiber_manual_record_white_48dp);
            btnClick.setScaleType(ImageView.ScaleType.FIT_XY);
            // b1.setText(adapt_objmenu.city_name_array[i]);
            // RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //lp.addRule(RelativeLayout.RIGHT_OF, btnClick.getId() - 1);

            //btnClick.setLayoutParams(lp);
            Drawable shape = res. getDrawable(R.drawable.buttonplayer2);
            // btnClick.setBackground();;

            btnClick.setBackground(shape);

        }


        setA.removeAll(aList);
        setA.removeAll(bList);
        for (Integer node : setA)
        {
            Log.v(TAG, " Default SetColor Node  " + node +" = "+buttonMapRev.get(node+""));
            /*Button btnClick = (Button) findViewById(buttonMapRev.get(node+""));
            btnClick.setBackgroundColor(Color.LTGRAY);*/
            ImageButton btnClick = (ImageButton) findViewById(buttonMapRev.get(node+""));
            Resources res = getResources();
            Drawable shape = res. getDrawable(R.drawable.grey);
            btnClick.setBackground(shape);
            btnClick.setImageResource(R.drawable.grey);
        }


    }

    private String getButtonId(int buttonId)
    {

        return buttonMap.get(buttonId);
    }



    static Map<Integer, String> buttonMap = new HashMap<>();
    static Map<String, Integer> buttonMapRev = new HashMap<String, Integer>();

    static {

        buttonMap.put(R.id.button1, "1");
        buttonMap.put(R.id.button2, "2");
        buttonMap.put(R.id.button3, "3");
        buttonMap.put(R.id.button4, "4");
        buttonMap.put(R.id.button5, "5");
        buttonMap.put(R.id.button6, "6");
        buttonMap.put(R.id.button7, "7");
        buttonMap.put(R.id.button8, "8");
        buttonMap.put(R.id.button9, "9");


        buttonMapRev.put("1", R.id.button1);
        buttonMapRev.put("2", R.id.button2);
        buttonMapRev.put("3", R.id.button3);
        buttonMapRev.put("4", R.id.button4);
        buttonMapRev.put("5", R.id.button5);
        buttonMapRev.put("6", R.id.button6);
        buttonMapRev.put("7", R.id.button7);
        buttonMapRev.put("8", R.id.button8);
        buttonMapRev.put("9", R.id.button9);


    }

    @Override
    public void onAnimationStart(Animation animation) {

        // check for blink animation
        if (animation == animBlink) {
            Log.v(TAG, "onAnimationStart  ...........................");
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for blink animation
        if (animation == animBlink) {
            Log.v(TAG, "onAnimationEnd  ...........................");
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

        // check for blink animation
        if (animation == animBlink) {
        }
    }

}
