package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class EditTask extends AppCompatActivity {

    String Edit = "EditTask";
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Log.w(Edit, "In edit mode");
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        Task task = receivingEnd.getParcelableExtra("Task");
        Log.v(Edit, "Task Id = " + task.getTaskID());
        EditText etname = findViewById(R.id.editTextText);
        etname.setText(task.getTaskName());
        EditText etcat = findViewById(R.id.editTextText2);
        etcat.setText(task.getCategory());
        Button confirmButton = findViewById(R.id.button);
        Button cancelButton = findViewById(R.id.button2);
        EditText ethr = findViewById(R.id.editHrs2);
        EditText etmin = findViewById(R.id.editMins2);
        EditText etsec = findViewById(R.id.editSec2);
        int id = task.getTaskID();
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
        int seconds = task.getTargetSec();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        ethr.setText(String.format(Locale.getDefault(), "%02d",hours));
        etmin.setText(String.format(Locale.getDefault(), "%02d",minutes));
        etsec.setText(String.format(Locale.getDefault(), "%02d",secs));
        Log.v("12345", String.valueOf(task.getTargetSec()));
        Task finalTask = task;
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Edit, "Editing Task");
                finalTask.setTaskName(etname.getText().toString());
                finalTask.setStatus("In Progress");
                finalTask.setCategory(etcat.getText().toString());

                int hr = 0;

                try {
                    hr = Integer.parseInt(ethr.getText().toString());
                } catch (NumberFormatException e) {
                    // Parsing failed, set hr to 0 or handle the error
                }

                int min = 0;

                try {
                    min = Integer.parseInt(etmin.getText().toString());
                } catch (NumberFormatException e) {
                    // Parsing failed, set min to 0 or handle the error
                }

                int sec = 0;

                try {
                    sec = Integer.parseInt(etsec.getText().toString());
                } catch (NumberFormatException e) {
                    // Parsing failed, set sec to 0 or handle the error
                }

                int totalSeconds = (hr * 3600) + (min * 60) + sec;
                finalTask.setTargetSec(totalSeconds);

                myDBHandler.updateTask(finalTask, user.getUsername());
                Intent editTask = new Intent(EditTask.this, MainMainMain.class);
                editTask.putExtra("New Task List", user);
                editTask.putExtra("User", user);
                editTask.putExtra("tab", "tasks_tab");
                startActivity(editTask);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Edit, "discarding");
                Intent back = new Intent(EditTask.this, MainMainMain.class);
                back.putExtra("New Task List", user);
                back.putExtra("User", user);
                back.putExtra("tab", "tasks_tab");
                startActivity(back);
                finish();
            }
        });

    }
}