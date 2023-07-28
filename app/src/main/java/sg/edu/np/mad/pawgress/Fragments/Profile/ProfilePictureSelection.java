package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class ProfilePictureSelection extends AppCompatActivity implements ProfilePictureAdapter.ProfilePictureSelectionListener {

    private RecyclerView recyclerView;
    private ProfilePictureAdapter profilePictureAdapter;
    private SharedPreferences sharedPreferences;
    private UserData userData;
    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_profile_picture_selection);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Get the UserData from the intent
        UserData userData = dbHandler.findUser(SaveSharedPreference.getUserName(ProfilePictureSelection.this));

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProfilePictures);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Create an instance of ProfilePictureAdapter
        profilePictureAdapter = new ProfilePictureAdapter(getProfilePictureIcons(), this, userData, this);

        // Set the adapter on RecyclerView
        recyclerView.setAdapter(profilePictureAdapter);

        // Set the Return Button
        TextView returnButton = findViewById(R.id.backButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // Method to retrieve the profile picture icons
    private ArrayList<Integer> getProfilePictureIcons() {
        ArrayList<Integer> profilePictureIcons = new ArrayList<>();
        profilePictureIcons.add(R.drawable.corgi);
        profilePictureIcons.add(R.drawable.corgi_sunglasses);
        // Add more profile picture icons as needed
        return profilePictureIcons;
    }

    @Override
    public void onProfilePictureSelected(int profilePictureResId) {
        // Update the profile picture path in the SharedPreferences
        sharedPreferences.edit().putString("profilePicturePath", String.valueOf(profilePictureResId)).apply();
    }
}







