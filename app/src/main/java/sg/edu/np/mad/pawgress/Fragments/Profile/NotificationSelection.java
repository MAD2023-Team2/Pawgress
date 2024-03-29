package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import sg.edu.np.mad.pawgress.R;

public class NotificationSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_notification_selection);

        Switch notificationSwitch = findViewById(R.id.switch1);

        // Load the current preference from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean userWantsNotifications = sharedPreferences.getBoolean("notification_preference", true);

        // Set the switch state based on the preference
        notificationSwitch.setChecked(userWantsNotifications);

        // Save the preference when the switch state changes
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notification_preference", isChecked);
            editor.apply();

            // Set the result to indicate the user's preference has changed
            setResult(RESULT_OK);
            finish(); // Finish the activity and go back to MainMainMain activity
        });

        TextView backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}