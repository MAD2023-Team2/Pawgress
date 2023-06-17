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
    MyDBHandler myDBHandler = new MyDBHandler(this, null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        int id = receivingEnd.getIntExtra("Task ID", 0);
        Task task = myDBHandler.findTask(id);
        ImageButton backButton = findViewById(R.id.backButton);
        Button gameButton = findViewById(R.id.to_Game);
        TextView taskName = findViewById(R.id.textView7);
        taskName.setText(task.getTaskName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(TaskView.this, TaskGame.class);
                activityName.putExtra("User", user);
                startActivity(activityName);
            }
        });
    }
}