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

import sg.edu.np.mad.pawgress.R;

public class TaskView extends AppCompatActivity {

    private Button gameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Intent receivingEnd = getIntent();
        Task task = receivingEnd.getParcelableExtra("Task");

        ImageButton backButton = findViewById(R.id.backButton);
        Button gameButton = findViewById(R.id.to_Game);

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
                startActivity(activityName);
            }
        });
    }
}