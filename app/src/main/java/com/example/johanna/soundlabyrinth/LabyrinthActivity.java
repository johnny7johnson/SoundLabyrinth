package com.example.johanna.soundlabyrinth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.johanna.soundlabyrinth.audio.output.AudioOutput;
import com.example.johanna.soundlabyrinth.db.DataBaseHelper;
import com.example.johanna.soundlabyrinth.game.Direction;
import com.example.johanna.soundlabyrinth.game.Game;
import com.example.johanna.soundlabyrinth.game.Mode;
import com.example.johanna.soundlabyrinth.game.PuzzlePiece;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LabyrinthActivity extends AppCompatActivity {

    /**********************************************************************************************/
    /****** Auto generated *************************************************************************/
    /**********************************************************************************************/

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    /**********************************************************************************************/
    /**** END Auto generated *************************************************************************/
    /**********************************************************************************************/
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ MINE ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    private PuzzlePiece puzzlePiece;
    private Game game;
    private Mode gamemode;
    private ImageButton buttonUp;
    private ImageButton buttonRight;
    private ImageButton buttonDown;
    private ImageButton buttonLeft;
    private ImageView hintArrow;
    private Button buttonHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gamemode = (Mode) this.getIntent().getExtras().get("mode");
        setContentView(R.layout.activity_labyrinth);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        hintArrow = findViewById(R.id.img_arrow_hint);
        hintArrow.setVisibility(View.INVISIBLE);
        buttonHint = findViewById(R.id.dummy_button);
        buttonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hintArrow.getVisibility() == View.VISIBLE) {
                    buttonHint.setText("Show Hint");
                    hintArrow.setVisibility(View.INVISIBLE);
                } else if (hintArrow.getVisibility() == View.INVISIBLE) {
                    buttonHint.setText("Hide Hint");
                    hintArrow.setVisibility(View.VISIBLE);

                }
            }
        });
        buttonUp = findViewById(R.id.button_up);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goUp();
            }
        });
        buttonRight = findViewById(R.id.button_right);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRight();
            }
        });
        buttonDown = findViewById(R.id.button_down);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goDown();
            }
        });
        buttonLeft = findViewById(R.id.button_left);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLeft();
            }
        });

        game = new Game();

        puzzlePiece = new PuzzlePiece().buildRandom();
        puzzlePiece = game.getCurrentPuzzlePiece(gamemode, Direction.LEFT);
        updatePuzzlePieceView();

        tryOpenDB();
        game.start(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void updatePuzzlePieceView() {
        ImageView img = findViewById(R.id.img_upper_left);
        img.setImageResource(puzzlePiece.getUpperLeftRes());
        img = findViewById(R.id.img_upper_right);
        img.setImageResource(puzzlePiece.getUpperRightRes());
        img = findViewById(R.id.img_down_right);
        img.setImageResource(puzzlePiece.getDownRightRes());
        img = findViewById(R.id.img_down_left);
        img.setImageResource(puzzlePiece.getDownLeftRes());
    }


    public void goToLeft() {
       movePlayer(Direction.LEFT);
    }

    public void goUp() {
     movePlayer(Direction.UP);
    }

    public void goToRight() {
        movePlayer(Direction.RIGHT);
    }

    public void goDown() {
       movePlayer(Direction.DOWN);
    }

    public void movePlayer(Direction dir) {

        game.movePlayer(dir);
        if (game.checkWin()) {
            Intent toWin = new Intent(LabyrinthActivity.this, WinActivity.class);
            startActivity(toWin);
        }

        puzzlePiece = game.getCurrentPuzzlePiece(gamemode, dir);
        updatePuzzlePieceView();

        setVisibilityOfArrows();
        updateHintArrowDirection();
    }


    private void setVisibilityOfArrows() {
        if (puzzlePiece.hasWayUp())
            buttonUp.setVisibility(View.VISIBLE);
        else
            buttonUp.setVisibility(View.INVISIBLE);

        if (puzzlePiece.hasWayToRight())
            buttonRight.setVisibility(View.VISIBLE);
        else
            buttonRight.setVisibility(View.INVISIBLE);

        if (puzzlePiece.hasWayDown())
            buttonDown.setVisibility(View.VISIBLE);
        else
            buttonDown.setVisibility(View.INVISIBLE);

        if (puzzlePiece.hasWayToLeft())
            buttonLeft.setVisibility(View.VISIBLE);
        else
            buttonLeft.setVisibility(View.INVISIBLE);
    }


    private void updateHintArrowDirection() {
        float angle = game.getCurrentSoundDirection();
        hintArrow.setRotation(angle - 90);
    }

    private void tryOpenDB() {
        DataBaseHelper myDbHelper;
        myDbHelper = new DataBaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        myDbHelper.close();
    }




    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ MINE ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    /**********************************************************************************************/
    /****** Auto generated *************************************************************************/
    /**********************************************************************************************/

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
