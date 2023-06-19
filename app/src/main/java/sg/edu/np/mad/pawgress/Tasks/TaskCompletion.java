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
        myDBHandler.updateTask(finalTask, user);

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
        seconds_complete.setText(String.format(Locale.getDefault(),"Time Studied:\n%d Hours %02d Mins %02d Secs",hours, minutes, secs));
    }
}