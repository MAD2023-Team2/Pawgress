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

import java.util.Locale;
import java.util.Map;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskView extends AppCompatActivity {

    private Button gameButton;
    String View = "Task View";
    Task task;
    TextView time;
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
        Button gameButton = findViewById(R.id.to_Game);
        TextView taskName = findViewById(R.id.textView11);
        taskName.setText(task.getTaskName());
        TextView taskCategory = findViewById(R.id.textView10);
        taskCategory.setText(task.getCategory());
        time = findViewById(R.id.textView15);
        time.setText("Time spent so far: " + task.getTimeSpent() + " seconds");

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
        time.setText("Time spent so far: " + task.getTimeSpent() + " seconds");
    }
}