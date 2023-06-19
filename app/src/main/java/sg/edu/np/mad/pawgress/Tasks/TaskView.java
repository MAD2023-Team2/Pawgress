package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskView extends AppCompatActivity {

    private Button gameButton;
    String View = "Task View";
    Task task;
    TextView time;
    TextView targettime;
    UserData user;
    MyDBHandler myDBHandler = new MyDBHandler(this, null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Log.v(View, "In Task View");
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        task = receivingEnd.getParcelableExtra("Task");
        ImageButton backButton = findViewById(R.id.backButton);
        gameButton = findViewById(R.id.to_Game);
        TextView taskName = findViewById(R.id.textView11);
        taskName.setText(task.getTaskName());
        TextView taskCategory = findViewById(R.id.textView10);
        taskCategory.setText(task.getCategory());
        targettime = findViewById(R.id.targettime);
        int tseconds = task.getTargetSec();
        int thours = tseconds / 3600;
        int tminutes = (tseconds % 3600) / 60;
        int tsecs = tseconds % 60;
        targettime.setText("Targeted Time Spent: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",thours, tminutes, tsecs));


        time = findViewById(R.id.textView15);
        int seconds = task.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        time.setText("Current Time Spent: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",hours, minutes, secs));
        if (task.getTimeSpent() > 0) { gameButton.setText("RESUME TIMER");}


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Timer = new Intent(TaskView.this, TaskGame.class);
                Bundle info = new Bundle();
                info.putParcelable("User", user);
                info.putParcelable("Task", task);
                Timer.putExtras(info);
                startActivity(Timer);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = task.getTaskID();
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
        int seconds = task.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        time.setText("Time spent so far: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",hours, minutes, secs));
        if (task.getTimeSpent() > 0) { gameButton.setText("RESUME TIMER");}

    }
}