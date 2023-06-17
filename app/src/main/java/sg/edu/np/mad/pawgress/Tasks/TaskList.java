package sg.edu.np.mad.pawgress.Tasks;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskList extends AppCompatActivity {

    // make it more compact
    private TextView emptyTaskText;
    String TAG = "Task List";
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "In Task List");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        RecyclerView recyclerView = findViewById(R.id.list);
        emptyTaskText = findViewById(R.id.emptyTextView);
        try { // after creating new task
            Log.v(TAG, "starting try");
            Intent receivingEnd = getIntent();
            UserData user = receivingEnd.getParcelableExtra("New Task List");
            ArrayList<Task> taskList = myDBHandler.findTaskList(user);
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(user,myDBHandler, this );
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.emptyTasktext = emptyTaskText;
            mAdapter.updateEmptyView();
            FloatingActionButton button = findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(TaskList.this, CreateTask.class);
                    createTask.putExtra("User", user);
                    startActivity(createTask);
                    finish();
                }
            });
        } catch (RuntimeException e) {
            // from homepage or tab button
            Log.v(TAG, "starting exception");
            //ArrayList<Task> taskList = new ArrayList<Task>();
            Intent receivingEnd = getIntent();
            UserData user = receivingEnd.getParcelableExtra("User");
            ArrayList<Task> taskList = myDBHandler.findTaskList(user);
            //  testing
            //taskList.add(new Task(1, "Week 6 Practical", "In Progress", "MAD"));
            Log.v(TAG, "List size = " + taskList.size());
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(user,myDBHandler, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            mAdapter.emptyTasktext = emptyTaskText;
            mAdapter.updateEmptyView();
            FloatingActionButton button = findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(TaskList.this, CreateTask.class);
                    createTask.putExtra("User", user);
                    startActivity(createTask);
                    finish();
                }
            });
        }
    }
}
