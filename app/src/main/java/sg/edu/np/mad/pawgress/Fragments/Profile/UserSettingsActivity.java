package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private UserData user;
    private MyDBHandler myDBHandler;
    private ToggleButton capyMode;
    int initialPetDesign;
    String initialPetType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_user_settings);
        myDBHandler = new MyDBHandler(this, null, null, 1);
        Log.i("UserSettingsPage", "User Settings page opened");
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        initialPetDesign = user.getPetDesignInitial();
        initialPetType = user.getPetType();




        // Set click listeners for the feature buttons
        Button themeButton = findViewById(R.id.findTheme);
        Button profilePictureButton = findViewById(R.id.editProfilePicture);
        Button notificationsButton = findViewById(R.id.Notifs);
        TextView backButton = findViewById(R.id.backButton);
        capyMode = findViewById(R.id.capyMode);

        themeButton.setOnClickListener(this);
        profilePictureButton.setOnClickListener(this);
        notificationsButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        capyMode.setOnClickListener(this);
        System.out.println(user.getCapyMode());

        if (user.getCapyMode().equals("OFF")){
            capyMode.setBackgroundResource(R.color.toggleRed);
            capyMode.setChecked(false);
        }
        else{
            capyMode.setBackgroundResource(R.color.toggleGreen);
            capyMode.setChecked(true);
        }

        capyMode.setChecked(isCapyModeON());

        capyMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    capyMode.setBackgroundResource(R.color.toggleRed);
                    user.setPetDesign(initialPetDesign);
                    myDBHandler.savePetDesign(user.getUsername(), initialPetType, initialPetDesign);
                    myDBHandler.updateCapyMode(user.getUsername(), "OFF");
                }
                else{
                    capyMode.setBackgroundResource(R.color.toggleGreen);
                    user.setPetDesign(R.drawable.capybara);
                    myDBHandler.savePetDesign(user.getUsername(), initialPetType, 4);
                    myDBHandler.updateCapyMode(user.getUsername(), "ON");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        // Handle button clicks
        if (view.getId() == R.id.editProfilePicture) {
            // Open profile picture settings
            Intent intent = new Intent(UserSettingsActivity.this, ProfilePictureSelection.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.Notifs) {
            // Open profile picture settings
            Intent intent = new Intent(UserSettingsActivity.this, NotificationSelection.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.findTheme) {
            // Open profile picture settings
            Intent intent = new Intent(UserSettingsActivity.this, ThemeSelectionActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.backButton) {
            finish();
        }
    }

    private boolean isCapyModeON(){
        if (user.getCapyMode().equals("OFF")){
            return false;
        }
        else{
            return true;
        }
    }
}