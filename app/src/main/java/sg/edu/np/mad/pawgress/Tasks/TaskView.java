package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.pawgress.R;

public class TaskView extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    private Button buttonStart;
    private Button buttonReset;
    private TextView timeView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Intent receivingEnd = getIntent();
        Task task = receivingEnd.getParcelableExtra("Task");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonStart = findViewById(R.id.start_timer_button);
        buttonReset = findViewById(R.id.reset_timer_button);
        timeView = findViewById(R.id.text_view_Countdown);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        handler = new Handler();
        runTimer();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "Start/Stop button has been pressed!");
                if (running) {
                    pauseTimer();
                } else {
                    startTimer();
                }
                updateButtonUI();
            }
        });


        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "Reset button has been pressed!");
                resetTimer();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            startTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    private void startTimer() {
        running = true;
        wasRunning = true;
    }

    private void pauseTimer() {
        running = false;
    }

    private void resetTimer() {
        if (!running){
            running = false;
            seconds = 0;
        }
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void updateButtonUI() {
        if (running) {
            buttonStart.setText("PAUSE");
            buttonReset.setAlpha(0.5F);
        }
        else {
            buttonStart.setText("START");
            buttonReset.setAlpha(1);
        }
    }
}