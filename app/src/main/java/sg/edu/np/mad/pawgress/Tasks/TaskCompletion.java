package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskCompletion extends AppCompatActivity {
    private TextView seconds_complete;

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_completion);

        Intent receivingEnd = getIntent();
        Task task = receivingEnd.getParcelableExtra("Task");
        UserData user = receivingEnd.getParcelableExtra("User");
        Task finalTask = myDBHandler.findTask(task.getTaskID(), myDBHandler.findTaskList(user));
        finalTask.setStatus("Completed");
        myDBHandler.updateTask(finalTask, user.getUsername());

        // after completing any task, gain 5 currency
        // for every 30 minutes spent on the task, an additional 1 currency is added
        // if timespent > target time, an additional 1 currency is added
        int additonal_currency;
        int task_seconds = finalTask.getTimeSpent();
        int task_minutes = task_seconds / 60;
        int task_minutes_30 = task_minutes / 30;
        int current_currency = user.getCurrency();
        int target_sec = finalTask.getTargetSec();
        if (task_seconds >= target_sec){
            additonal_currency = 1;
        }
        else{
            additonal_currency = 0;
        }
        int new_currency = current_currency + 5 + task_minutes_30 + additonal_currency;
        user.setCurrency(new_currency);
        myDBHandler.updateCurrency(user.getUsername(), new_currency);

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskCompletion.this,MainMainMain.class);
                intent.putExtra("User", user);
                intent.putExtra("tab", "tasks_tab");
                startActivity(intent);
                finish();
            }
        });

        int seconds = finalTask.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        seconds_complete = findViewById(R.id.seconds_complete);
        seconds_complete.setText(String.format(Locale.getDefault(),"Time Spent on %s :\n%d Hours %02d Mins %02d Secs",task.getTaskName(),hours, minutes, secs));
    }
}