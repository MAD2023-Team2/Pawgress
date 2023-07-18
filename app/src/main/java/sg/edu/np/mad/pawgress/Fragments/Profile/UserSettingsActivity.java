package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        Log.i("UserSettingsPage", "User Settings page opened");

        // Set click listeners for the feature buttons
        Button themeButton = findViewById(R.id.findTheme);
        Button fontButton = findViewById(R.id.editFonts);
        Button profilePictureButton = findViewById(R.id.editProfilePicture);
        Button notificationsButton = findViewById(R.id.Notifs);
        Button appModesButton = findViewById(R.id.appMode);

        themeButton.setOnClickListener(this);
        fontButton.setOnClickListener(this);
        profilePictureButton.setOnClickListener(this);
        notificationsButton.setOnClickListener(this);
        appModesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Handle button clicks
        if (view.getId() == R.id.editProfilePicture) {
            // Open profile picture settings
            Intent intent = new Intent(UserSettingsActivity.this, ProfilePictureSelection.class);
            startActivity(intent);
        }
    }
}