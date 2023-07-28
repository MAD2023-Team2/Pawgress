package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Analytics.BarFragment;
import sg.edu.np.mad.pawgress.Analytics.MainStats;
import sg.edu.np.mad.pawgress.Analytics.PieFragment;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

public class Analytics extends AppCompatActivity {

    private MyDBHandler dbHandler;
    UserData user;
    private ArrayList<Task> taskList;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_analytics);
        dbHandler = new MyDBHandler(this, null, null, 1);
        // Getting userData
        UserData user = dbHandler.findUser(SaveSharedPreference.getUserName(Analytics.this));
        taskList = dbHandler.findTaskList(user);
        System.out.println(user.getUsername());
        System.out.println(taskList.size());
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Main Stat Fragment
        Button mainBtn = findViewById(R.id.mainBut);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new MainStats());
            }
        });

        // Bar Fragment
        Button barBtn = findViewById(R.id.barBut);
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new BarFragment());
            }
        });

        // Pie Chart Fragment
        Button pieBtn = findViewById(R.id.pieBut);
        pieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PieFragment());
            }
        });
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

}