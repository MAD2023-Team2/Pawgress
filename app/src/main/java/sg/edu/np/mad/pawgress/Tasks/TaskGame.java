package sg.edu.np.mad.pawgress.Tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;
import java.util.Random;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class TaskGame extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    private ImageView buttonStart;
    private ImageView buttonReset;
    private Button buttonFinish;
    public Task task;
    public UserData user;
    private TextView timeView;
    private Handler handler;
    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
    private ToggleButton powerSavingToggle;
    private float originalBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.task_game);

        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        UserData user1 = myDBHandler.findUser(SaveSharedPreference.getUserName(this));
        ImageView pet_picture = findViewById(R.id.corgi_1);
        if (user1.getPetDesign() == 1){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user1.getPetDesign() == 2){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user1.getPetDesign() == 3){pet_picture.setImageResource(R.drawable.corgi);}
        else if (user1.getPetDesign() == 4){pet_picture.setImageResource(R.drawable.capybara);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}

        // Retrieving references to the ImageViews from the XML file
        ImageView topLeftPic = findViewById(R.id.replaceImage_topLeft);
        ImageView topRightPic = findViewById(R.id.replaceImage_topRight);
        ImageView topMiddlePic = findViewById(R.id.replaceImage_topMiddle);

        // Getting image paths from the database for the user
        String topLeft = myDBHandler.getTopLeft(user1.getUsername());
        String topRight = myDBHandler.getTopRight(user1.getUsername());
        String topMiddle = myDBHandler.getTopMiddle(user1.getUsername());

        // Displaying the top-left image if it exists
        if (!topLeft.equals("")) {
            topLeftPic.setVisibility(View.VISIBLE); // Make the ImageView visible
            String pathName = myDBHandler.getImageURL(topLeft); // Get the file path of the image
            Bitmap bitmap = BitmapFactory.decodeFile(pathName); // Decode the file path into a Bitmap
            topLeftPic.setImageBitmap(bitmap); // Set the Bitmap as the source for the ImageView
        }

        // Displaying the top-right image if it exists
        if (!topRight.equals("")) {
            topRightPic.setVisibility(View.VISIBLE); // Make the ImageView visible
            String pathName = myDBHandler.getImageURL(topRight); // Get the file path of the image
            Bitmap bitmap = BitmapFactory.decodeFile(pathName); // Decode the file path into a Bitmap
            topRightPic.setImageBitmap(bitmap); // Set the Bitmap as the source for the ImageView
        }

        // Displaying the top-middle image if it exists
        if (!topMiddle.equals("")) {
            topMiddlePic.setVisibility(View.VISIBLE); // Make the ImageView visible
            String pathName = myDBHandler.getImageURL(topMiddle); // Get the file path of the image
            Bitmap bitmap = BitmapFactory.decodeFile(pathName); // Decode the file path into a Bitmap
            topMiddlePic.setImageBitmap(bitmap); // Set the Bitmap as the source for the ImageView
        }

        pet_picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                YoYo.with(Techniques.Bounce)
                        .duration(1000)
                        .repeat(0)
                        .playOn(pet_picture);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        int random = new Random().nextInt(3);
                        MediaPlayer mediaPlayer;

                        if (user1.getPetDesign() == 1){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat1_1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat1_2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat1_3);
                            }
                            mediaPlayer.start();
                        }
                        else if (user1.getPetDesign() == 2){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat2_1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat2_2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.cat2_3);
                            }
                            mediaPlayer.start();
                        }

                        else if (user1.getPetDesign() == 3){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi3);
                            }
                            mediaPlayer.start();
                        }
                        else if (user.getPetDesign() == 4){
                            mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.capybara);
                            mediaPlayer.start();
                        }
                        else{
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.gr1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.gr2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.gr3);
                            }
                            mediaPlayer.start();
                        }


                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            };
                        });
                        Animation anim = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                1f, 0.85f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim.setFillAfter(true); // Needed to keep the result of the animation
                        anim.setDuration(150);
                        v.startAnimation(anim);

                        break;

                    case MotionEvent.ACTION_UP:
                        Animation anim2 = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                0.85f, 1f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim2.setFillAfter(true); // Needed to keep the result of the animation
                        anim2.setDuration(150);
                        v.startAnimation(anim2);
                        break;
                }

                return true;
            }
        });


        powerSavingToggle = findViewById(R.id.power_saving_toggle);

        powerSavingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Enable power saving mode (dim the screen)
                    originalBrightness = getSystemBrightness();
                    dimScreen();
                } else {
                    // Disable power saving mode (restore screen brightness)
                    restoreBrightness();
                }
            }
        });

        // Getting data from the intent (previous activity)
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        task = receivingEnd.getParcelableExtra("Task");

        // Set up the "Back" button to save the current timer state and update the task
        TextView backButton = findViewById(R.id.close);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasRunning = running;
                pauseTimer();
                // update task current time spent, as well as task database
                task.setTimeSpent(seconds);
                myDBHandler.updateTask(task, user.getUsername());
                finish(); // Close the current activity and return to the previous one
            }
        });

        // Initializing UI elements and restoring the timer state if there is a saved instance
        buttonStart = findViewById(R.id.start_timer_imagebutton);
        buttonReset = findViewById(R.id.reset_timer_imagebutton);
        buttonFinish = findViewById(R.id.finish_timer);
        timeView = findViewById(R.id.text_view_Countdown);
        seconds = task.getTimeSpent(); // Get the previously saved time for the task
        updateTimerText(); // Update and display the time spent

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        handler = new Handler();
        runTimer(); // Start or resume the timer based on the saved state

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "Start/Stop button has been pressed!");
                if (running) {
                    pauseTimer();
                } else {
                    startTimer();
                }
                updateButtonUI(); // Update UI elements based on the timer state
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    showResetConfirmationDialog(); // Display a confirmation dialog before resetting the timer
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    pauseTimer();
                }
                showFinishConfirmationDialog(user, task); // Display a confirmation dialog before finishing the task
            }
        });

        if (!running) {
            startTimer();
            updateButtonUI();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        pauseTimer();
        task.setTimeSpent(seconds);
        myDBHandler.updateTask(task,user.getUsername());
    }

    @Override
    protected void onResume() {
        super.onResume();
        seconds = task.getTimeSpent();
        updateTimerText();

        if (wasRunning) {
            startTimer();
        }
        int id = task.getTaskID();
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
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
        task.setTimeSpent(0);
        myDBHandler.updateTask(task, user.getUsername());
        updateTimerText();
    }
    private void updateTimerText(){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
        timeView.setText(time);
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

    private void showFinishConfirmationDialog(UserData user, Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish Task?");
        builder.setMessage("Has the task been completed?");

        // Set the positive button and its click listener
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTimeSpent(seconds);
                Intent activityName = new Intent(TaskGame.this, TaskCompletion.class);
                activityName.putExtra("User", user);
                activityName.putExtra("seconds", seconds);
                activityName.putExtra("Task", task);
                startActivity(activityName);
            }
        });

        // Set the negative button and its click listener
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!running) {
                    startTimer();
                    buttonStart.setImageResource(R.mipmap.timer_play_square_round);
                    buttonReset.setAlpha(0.5F);
                }
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

                if (running) {
                    seconds++;
                    updateTimerText();
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void updateButtonUI() {
        if (running) {
            buttonStart.setImageResource(R.mipmap.timer_play_square_round);
            buttonReset.setAlpha(0.5F);
        }
        else {
            buttonStart.setImageResource(R.mipmap.timer_play_round);
            buttonReset.setAlpha(1.0F);
        }
    }
    private void dimScreen() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.1f; // Set brightness level (0.0f to 1.0f)
        getWindow().setAttributes(layoutParams);
    }

    // Restore screen brightness to the default value
    private void restoreBrightness() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = originalBrightness;
        getWindow().setAttributes(layoutParams);
    }

    private float getSystemBrightness(){
        try {
            return Settings.System.getFloat(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 0.5f; // Default to 50% brightness if the system setting is not found
        }
    }

}