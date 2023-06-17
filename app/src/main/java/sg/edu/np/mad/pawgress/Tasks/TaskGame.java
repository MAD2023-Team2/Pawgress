package sg.edu.np.mad.pawgress.Tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskGame extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    private Button buttonStart;
    private Button buttonReset;
    private Button buttonFinish;

    private TextView timeView;
    private Handler handler;
    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_game);

        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        ArrayList<Task> taskList = myDBHandler.findTaskList(user);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonStart = findViewById(R.id.start_timer_button);
        buttonReset = findViewById(R.id.reset_timer_button);
        buttonFinish = findViewById(R.id.finish_timer);
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
                if (!running){
                    showResetConfirmationDialog();
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFinishConfirmationDialog();
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
        running = false;
        seconds = 0;
    }
    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Timer");
        builder.setMessage("Are you sure you want to reset the timer?");

        // Set the positive button and its click listener
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetTimer();
            }
        });

        // Set the negative button and its click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, return back
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFinishConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish Task?");
        builder.setMessage("Has the task been completed?");

        // Set the positive button and its click listener
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent activityName = new Intent(TaskGame.this, TaskCompletion.class);
                activityName.putExtra("seconds", seconds);
                startActivity(activityName);
            }
        });

        // Set the negative button and its click listener
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, return back
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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