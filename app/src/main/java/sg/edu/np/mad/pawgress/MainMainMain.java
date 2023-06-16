package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import sg.edu.np.mad.pawgress.Fragments.Game.GameFragment;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.Fragments.Profile.ProfileFragment;
import sg.edu.np.mad.pawgress.databinding.ActivityMainMainMainBinding;

public class MainMainMain extends AppCompatActivity {
    ActivityMainMainMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main_main);
        binding = ActivityMainMainMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.home_tab){
                replaceFragment(new HomeFragment());
            }
            if (itemID == R.id.game_tab){
                replaceFragment(new GameFragment());
            }
            if (itemID == R.id.tasks_tab){
                replaceFragment(new GameFragment());
            }
            if (itemID == R.id.profile_tab){
                replaceFragment(new ProfileFragment());
            }
            /*
            switch (item.getItemId()){

                case R.id.home_tab:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.game_tab:
                    replaceFragment(new GameFragment());
                    break;
                case R.id.tasks_tab:
                    replaceFragment((new TasksFragment()));
                    break;
                case R.id.profile_tab:
                    replaceFragment(new ProfileFragment());
                    break;
            }*/
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}