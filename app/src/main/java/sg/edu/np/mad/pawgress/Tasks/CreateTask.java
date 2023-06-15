package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

import sg.edu.np.mad.pawgress.R;

public class CreateTask extends AppCompatActivity {


    String title = "Create Task";
    // is there a limit to how many tasks users can have at one time?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        Log.i(title, "Creating task");

        Intent receivingEnd = getIntent();
        ArrayList<Task> taskList = receivingEnd.getParcelableArrayListExtra("Task List");
        Button createButton = findViewById(R.id.button6);
        Button cancelButton = findViewById(R.id.editProfilePassword);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = taskList.size() + 1;
                EditText etname = findViewById(R.id.editTitle);
                String name = etname.getText().toString();
                EditText etcat = findViewById(R.id.editCat);
                String cat = etcat.getText().toString();
                Task task = new Task(id, name, "In Progress", cat);
                taskList.add(task);
                Intent newTask = new Intent(CreateTask.this, TaskList.class);
                newTask.putParcelableArrayListExtra("New Task List", taskList);
                startActivity(newTask);
                Log.i(title, "task added");
                finish();
                Log.i(title, "back to list");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(title, "discarding");
                Intent back = new Intent(CreateTask.this, TaskList.class);
                back.putParcelableArrayListExtra("New Task List", taskList);
                startActivity(back);
                finish();
            }
        });
    }
}