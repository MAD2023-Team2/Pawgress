package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private UserData user;
    private MyDBHandler myDBHandler;
    private static final String CAPY = "CAPY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_user_settings);
        myDBHandler = new MyDBHandler(this, null, null, 1);
        Log.i("UserSettingsPage", "User Settings page opened");
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        // Set click listeners for the feature buttons
        Button themeButton = findViewById(R.id.findTheme);
        Button profilePictureButton = findViewById(R.id.editProfilePicture);
        Button notificationsButton = findViewById(R.id.Notifs);
        Button appModesButton = findViewById(R.id.appMode);
        TextView backButton = findViewById(R.id.backButton);
        Button capyModeButton = findViewById(R.id.capyMode);

        themeButton.setOnClickListener(this);
        profilePictureButton.setOnClickListener(this);
        notificationsButton.setOnClickListener(this);
        appModesButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        capyModeButton.setOnClickListener(this);
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
        if (view.getId() == R.id.capyMode) {
            showCapyModeConfirmationDialog();
        }
    }

    private void showCapyModeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Capymode Confirmation");
        builder.setMessage("Do you want to change your pet design to a capybara? (Your pet will be a capybara forever)");
        if (user.getPetDesign() == R.drawable.capybara){
            builder.setMessage("Capymode is ON. Your pet is already a capybara.");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        else{
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Set the pet design to capybara
                    user.setPetDesign(R.drawable.capybara);
                    myDBHandler.savePetDesign(user.getUsername(), CAPY, R.drawable.capybara);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.show();
    }
}