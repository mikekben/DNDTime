package si.neb.dndtime;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;

import static si.neb.dndtime.R.id.fullscreen_content;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TimeScreen extends AppCompatActivity {
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

    public date currDate = new date(5628,10,25);


    TextView clockText;
    FrameLayout frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_screen);

        frame = (FrameLayout)findViewById(R.id.frame);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        clockText = (TextView)findViewById(R.id.fullscreen_content);



        Button button_1min = (Button)findViewById(R.id.button);
        Button button_5min = (Button)findViewById(R.id.button2);
        Button button_30min = (Button)findViewById(R.id.button3);

        button_1min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(1);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });
        button_5min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(5);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });
        button_30min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(30);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });

        Button button_1hour = (Button)findViewById(R.id.button4);
        Button button_4hour = (Button)findViewById(R.id.button5);
        Button button_8hour = (Button)findViewById(R.id.button6);

        button_1hour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(60);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });
        button_4hour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(240);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });
        button_8hour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addMinutes(480);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });

        Button button_1day = (Button)findViewById(R.id.button7);

        button_1day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currDate.addDays(1);
                clockText.setText(currDate.toString());
                frame.setBackgroundColor(currDate.getColor());
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        clockText.setText(currDate.toString());
        frame.setBackgroundColor(currDate.getColor());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

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
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
