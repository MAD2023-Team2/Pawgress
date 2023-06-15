package sg.edu.np.mad.pawgress.Tasks;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import sg.edu.np.mad.pawgress.R;

public class TaskList extends AppCompatActivity {

    // make it more compact
    String TAG = "Task List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        RecyclerView recyclerView = findViewById(R.id.list);
        try { // after creating new task
            Log.v(TAG, "starting try");
            Intent receivingEnd = getIntent();
            ArrayList<Task> taskList = receivingEnd.getParcelableArrayListExtra("New Task List");
            Log.v(TAG, "List size = " + taskList.size());
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(taskList, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            FloatingActionButton button = findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(TaskList.this, CreateTask.class);
                    createTask.putParcelableArrayListExtra("Task List", taskList);
                    startActivity(createTask);
                    finish();
                }
            });
        } catch (RuntimeException e) {
            Log.v(TAG, "starting exception");
            //ArrayList<Task> taskList = new ArrayList<Task>();
            Intent receivingEnd = getIntent();
            ArrayList<Task> taskList = receivingEnd.getParcelableArrayListExtra("TaskList");
            //  testing
            //taskList.add(new Task(1, "Week 6 Practical", "In Progress", "MAD"));
            Log.v(TAG, "List size = " + taskList.size());
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(taskList, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            FloatingActionButton button = findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(TaskList.this, CreateTask.class);
                    createTask.putParcelableArrayListExtra("Task List", taskList);
                    startActivity(createTask);
                    finish();
                }
            });
        }
    }
}
