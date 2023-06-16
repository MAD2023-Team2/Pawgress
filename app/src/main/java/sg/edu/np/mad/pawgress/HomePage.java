package sg.edu.np.mad.pawgress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Profile.ProfilePage;
import sg.edu.np.mad.pawgress.Tasks.CreateTask;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskAdapter;
import sg.edu.np.mad.pawgress.Tasks.TaskCardAdapter;
import sg.edu.np.mad.pawgress.Tasks.TaskCardViewHolder;
import sg.edu.np.mad.pawgress.Tasks.TaskList;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(null, "Starting App Home Page");
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        ImageButton profilePhoto = findViewById((R.id.profile));
        TextView greeting = findViewById(R.id.greeting);
        ImageView pet_picture = findViewById(R.id.homeGame);
        greeting.setText("Hello " + user.getUsername()); // add username
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        // add change pet picture code after implementing pet object

        // WALTER - add recycler view code (for now this goes to taskList page)
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        RecyclerView recyclerView = findViewById(R.id.taskcardlist);

        try { // after creating new task
            Intent receivingEnd_2 = getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("New Task List");
            ArrayList<Task> taskList = user_2.getTaskList();
            TaskCardAdapter mAdapter = new TaskCardAdapter(taskList, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        } catch (RuntimeException e) {
            // from homepage or tab button
            //ArrayList<Task> taskList = new ArrayList<Task>();
            Intent receivingEnd_2 = getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("User");
            ArrayList<Task> taskList = myDBHandler.findTaskList(user_2);
            //  testing
            //taskList.add(new Task(1, "Week 6 Practical", "In Progress", "MAD"));
            TaskCardAdapter mAdapter = new TaskCardAdapter(taskList, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
