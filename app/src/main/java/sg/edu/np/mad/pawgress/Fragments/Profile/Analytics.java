package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

public class Analytics extends AppCompatActivity {

    private MyDBHandler dbHandler;
    UserData user;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        dbHandler = new MyDBHandler(this, null, null, 1);
        // Getting userData
        UserData user = dbHandler.findUser(SaveSharedPreference.getUserName(Analytics.this));
        taskList = dbHandler.findTaskList(user);
        System.out.println(user.getUsername());
        System.out.println(taskList.size());
    }


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(taskList.size());
        int count = 0;
        int totalTime = 0;

        for (Task task : taskList){
            if (task.getStatus().equals("Completed")){
                count++;
                totalTime += task.getTimeSpent();
            }
        }

        int mins = totalTime/60;
        int secs = totalTime - (mins*60);
        int hrs = mins / 60;
        mins = mins - (hrs*60);


        TextView totalCompletedText = findViewById(R.id.totalComplete);
        TextView totalTimeSpent = findViewById(R.id.totalTime);

        totalCompletedText.setText("Total number of completed task:" + count);
        totalTimeSpent.setText(String.format("%d hrs %d mins %d secs",hrs,mins,secs));
    }
}