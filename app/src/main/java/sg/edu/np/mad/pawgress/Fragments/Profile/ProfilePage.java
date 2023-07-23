package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class ProfilePage extends AppCompatActivity {

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
    }
    protected void onStart() {
        super.onStart();

        //Log Out button
        Button logoutButton = findViewById(R.id.logOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(ProfilePage.this); //clears shared preference so no auto login
                Intent intent = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        // Edit Profile Button
        Button editProfileButton = findViewById(R.id.editProfilePicture);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SaveSharedPreference.clearUserName(ProfilePage.this); //clears shared preference so no auto login
                Intent intent = new Intent(ProfilePage.this, editProfilePassword.class);
                startActivity(intent);
            }
        });

        // User Settings Button
        Button userSettingsButton = findViewById(R.id.userSettings);
        userSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log the click event
                Log.i("ProfilePage", "User Settings button clicked");

                //SaveSharedPreference.clearUserName(ProfilePage.this); //clears shared preference so no auto login
                Intent intent = new Intent(ProfilePage.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        // Analytics Button
        Button analyticsButton = findViewById(R.id.analytics);
        analyticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Analytics.class);
                startActivity(intent);
            }
        });

        // Update/Display User Profile Pic
        // receive intent/data from pfp selection?? 

        // Setting Username Text
        UserData dbData = myDBHandler.findUser(SaveSharedPreference.getUserName(ProfilePage.this));
        TextView username = findViewById(R.id.ProfileUsername);
        username.setText(dbData.getUsername());
    }

}