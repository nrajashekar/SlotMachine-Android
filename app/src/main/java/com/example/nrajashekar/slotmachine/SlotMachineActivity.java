package com.example.nrajashekar.slotmachine;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.nrajashekar.fragments.PrizeDialogFragment;

import java.util.Random;


public class SlotMachineActivity extends FragmentActivity {

    private static final int MAX_SPEED = 30;
    private static final int MIN_SPEED = 20;
    private static final int MAX_ROLLS = 20;
    private static final int MIN_ROLLS = 10;
    private static final int MAX_ROLL_DELAY_FACTOR = 30;
    private static final int MIN_ROLL_DELAY_FACTOR = 20;

    private ViewFlipper reel1;
    private ViewFlipper reel2;
    private ViewFlipper reel3;
    private int mSpeed1;
    private int mSpeed2;
    private int mSpeed3;
    private int mCount1;
    private int mCount2;
    private int mCount3;
    private int mFactor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_slot_machine);
        reel1 = (ViewFlipper) findViewById(R.id.reel1);
        reel2 = (ViewFlipper) findViewById(R.id.reel2);
        reel3 = (ViewFlipper) findViewById(R.id.reel3);
        Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpeed1 = generateRandomNumber(MAX_SPEED, MIN_SPEED);
                mSpeed2 = generateRandomNumber(MAX_SPEED, MIN_SPEED);
                mSpeed3 = generateRandomNumber(MAX_SPEED, MIN_SPEED);

                mCount1 = generateRandomNumber(MAX_ROLLS, MIN_ROLLS);
                mCount2 = generateRandomNumber(MAX_ROLLS, MIN_ROLLS);
                mCount3 = generateRandomNumber(MAX_ROLLS, MIN_ROLLS);

                mFactor = generateRandomNumber(MAX_ROLL_DELAY_FACTOR, MIN_ROLL_DELAY_FACTOR);
                Handler h = new Handler();
                h.postDelayed(r1, mSpeed1);
                h.postDelayed(r2, mSpeed2);
                h.postDelayed(r3, mSpeed3);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slot_machine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
        private Runnable r1 = new Runnable() {
        @Override
        public void run() {
            if( mCount1 > 0 ) {
                mCount1--;
                mSpeed1+= mFactor;
                roll(reel1, mSpeed1);
                Handler h = new Handler();
                h.postDelayed(r1, mSpeed1);
                return;
            }
            CheckIfWon();
        }
    };
    private Runnable r2 = new Runnable() {
        @Override
        public void run() {
            if( mCount2 > 0 ) {
                mCount2--;
                mSpeed2+= mFactor;
                roll(reel2, mSpeed2);
                Handler h = new Handler();
                h.postDelayed(r2, mSpeed2);
                return;
            }
            CheckIfWon();
        }
    };
    private Runnable r3 = new Runnable() {
        @Override
        public void run() {
            if( mCount3 > 0 ) {
                mCount3--;
                mSpeed3+= mFactor;
                roll(reel3, mSpeed3);
                Handler h = new Handler();
                h.postDelayed(r3, mSpeed3);
                return;
            }
            CheckIfWon();
        }
    };

    private void CheckIfWon() {
        if(mCount3 == 0 && mCount1 == 0 && mCount2 == 0) {
            if((reel1.getCurrentView().getId() == R.id.coffee_maker) && (reel2.getCurrentView().getId() == R.id.coffee_filter) && (reel3.getCurrentView().getId() == R.id.coffee_grounds))  {
                showPrizeDialog(R.string.prize_coffee);
            } else if((reel1.getCurrentView().getId() == R.id.tea_pot) && (reel2.getCurrentView().getId() == R.id.tea_strainer) && (reel3.getCurrentView().getId() == R.id.loose_tea))  {
                showPrizeDialog(R.string.prize_tea);
            } else if((reel1.getCurrentView().getId() == R.id.espresso_machine) && (reel2.getCurrentView().getId() == R.id.espresso_tamper) && (reel3.getCurrentView().getId() == R.id.ground_espresso_beans)) {
                showPrizeDialog(R.string.prize_espresso);
            }
        }
    }

    private void showPrizeDialog(int textId) {
        // remove any old dialogs
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(PrizeDialogFragment.DIALOG_ID);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        PrizeDialogFragment prizeDialogFragment = PrizeDialogFragment.newInstance(textId);
        prizeDialogFragment.show(fm, PrizeDialogFragment.DIALOG_ID);

    }

    private void roll(ViewFlipper viewFlipper, int speed) {
        Animation outToBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f);
        outToBottom.setInterpolator(new AccelerateInterpolator());
        outToBottom.setDuration(speed);
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromTop.setInterpolator(new AccelerateInterpolator());
        inFromTop.setDuration(speed);
        viewFlipper.clearAnimation();
        viewFlipper.setInAnimation(inFromTop);
        viewFlipper.setOutAnimation(outToBottom);
        if (viewFlipper.getDisplayedChild() == 0) {
            viewFlipper.setDisplayedChild(2);
        } else {
            viewFlipper.showPrevious();
        }
    }

    private int generateRandomNumber(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}