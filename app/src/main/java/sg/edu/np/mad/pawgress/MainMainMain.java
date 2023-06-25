package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import sg.edu.np.mad.pawgress.Fragments.Game.GameFragment;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.Fragments.Profile.ProfileFragment;
import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.Tasks.CreateTask;
import sg.edu.np.mad.pawgress.databinding.ActivityMainMainMainBinding;

public class MainMainMain extends AppCompatActivity {
    ActivityMainMainMainBinding binding;
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting content view
        setContentView(R.layout.activity_main_main_main);

        // Binding of navigation bar and fragments
        binding = ActivityMainMainMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Update the bottomNavigationView selection based of page of origin
        Intent receivingEnd = getIntent();
        String tab = receivingEnd.getExtras().getString("tab");
        Log.i(null, "------------------------------------" + tab);
        if (tab.equals("tasks_tab")){
            replaceFragment(new TasksFragment());

            // Sets navigation bar item to tasks
            binding.bottomNavigationView.setSelectedItemId(R.id.tasks_tab);

        } else if (tab.equals("profile_tab")) {
            replaceFragment(new ProfileFragment());

            // Sets navigation bar item to profile
            binding.bottomNavigationView.setSelectedItemId(R.id.profile_tab);

        } else{
            replaceFragment(new HomeFragment());
        }

        // Switching of navigation bar tabs
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.home_tab){
                //Replace fragment with HomeFragment
                replaceFragment(new HomeFragment());
            }
            if (itemID == R.id.game_tab){
                //Replace fragment with GameFragment
                replaceFragment(new GameFragment());
            }
            if (itemID == R.id.tasks_tab){
                //Replace fragment with TaskFragment
                replaceFragment(new TasksFragment());
            }
            if (itemID == R.id.profile_tab){
                //Replace fragment with ProfileFragment
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    // Replaces current fragment with new fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}