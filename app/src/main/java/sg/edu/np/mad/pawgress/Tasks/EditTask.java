package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class EditTask extends AppCompatActivity {

    String Edit = "EditTask";
    int hr, min, sec;
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        Log.w(Edit, "In edit mode");
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        Task task = receivingEnd.getParcelableExtra("Task");

        // shows previous name and category so users can edit from that
        EditText etname = findViewById(R.id.editTextText);
        etname.setText(task.getTaskName());
        EditText etcat = findViewById(R.id.editTextText2);
        etcat.setText(task.getCategory());

        int id = task.getTaskID();
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
        int seconds = task.getTargetSec();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        hr = hours;
        min = minutes;
        sec = secs;

        Button confirmButton = findViewById(R.id.button12);
        Button cancelButton = findViewById(R.id.button13);

        NumberPicker ethr = (NumberPicker) findViewById(R.id.hourPicker3);
        ethr.setMaxValue(999);
        ethr.setMinValue(0);
        ethr.setValue(hours);
        ethr.setWrapSelectorWheel(true);

        NumberPicker etmin = (NumberPicker) findViewById(R.id.minPicker3);
        etmin.setMaxValue(999);
        etmin.setMinValue(0);
        etmin.setValue(minutes);
        etmin.setWrapSelectorWheel(true);

        NumberPicker etsec = (NumberPicker) findViewById(R.id.secPicker3);
        etsec.setMaxValue(999);
        etsec.setMinValue(0);
        etsec.setValue(secs);
        etsec.setWrapSelectorWheel(true);
        ethr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hr = i1;
            }
        });

        etmin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min = i1;
            }
        });

        etsec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                sec = i1;
            }
        });

        Task finalTask = task;
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Edit, "Editing Task");
                int totalSeconds = (hr * 3600) + (min * 60) + sec;
                // do not accept blank task name and category
                if (etname.length() > 0 &&  etname.getText().charAt(0) != ' '){
                    finalTask.setTaskName(etname.getText().toString());
                    finalTask.setStatus("In Progress");
                    finalTask.setCategory(etcat.getText().toString());
                    if (etcat.length()==0 || etcat.getText().charAt(0) == ' '){
                        finalTask.setCategory("Uncategorised");
                    }
                    finalTask.setTargetSec(totalSeconds);

                    myDBHandler.updateTask(finalTask, user.getUsername());
                    Intent editTask = new Intent(EditTask.this, MainMainMain.class);
                    editTask.putExtra("New Task List", user);
                    editTask.putExtra("User", user);
                    editTask.putExtra("tab", "tasks_tab");
                    startActivity(editTask);
                    finish();
                }
                else{
                    Toast.makeText(EditTask.this, "Invalid Title & Category", Toast.LENGTH_SHORT).show();
                }
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