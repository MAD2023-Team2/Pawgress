package sg.edu.np.mad.pawgress.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.Fragments.Profile.ProfilePage;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.TaskList;
import sg.edu.np.mad.pawgress.UserData;

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
        TextView homeTask = findViewById(R.id.homeTask);
        homeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add in code to transfer user task list data to task list
                Intent intent = new Intent(HomePage.this, TaskList.class);
                intent.putExtra("TaskList", user.getTaskList());
                startActivity(intent);
            }
        });
    }
}
