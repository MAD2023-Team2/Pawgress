package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
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
        EditText etcat = findViewById(R.id.editTextText2);

        Button confirmButton = findViewById(R.id.button);
        Button cancelButton = findViewById(R.id.button2);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Edit, "Editing Task");
                task.setTaskName(etname.getText().toString());
                task.setStatus("In Progress");
                task.setCategory(etcat.getText().toString());
                myDBHandler.updateTask(task);
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