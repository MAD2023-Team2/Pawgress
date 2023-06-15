package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import sg.edu.np.mad.pawgress.R;

public class TaskCompletion extends AppCompatActivity {
    private TextView seconds_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_completion);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent receivingEnd = getIntent();
        int seconds = receivingEnd.getIntExtra("seconds",0);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        seconds_complete = findViewById(R.id.seconds_complete);
        seconds_complete.setText(String.format(Locale.getDefault(),"Time Studied:\n%d Hours %02d Mins %02d Secs",hours, minutes, secs));
    }
}