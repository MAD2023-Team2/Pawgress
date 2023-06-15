package sg.edu.np.mad.pawgress.Profile;

import android.content.Intent;
import android.os.Bundle;
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
        Button logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(ProfilePage.this); //clears shared preference so no auto login
                Intent intent = new Intent(ProfilePage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        // Setting Username Text
        UserData dbData = myDBHandler.findUser(SaveSharedPreference.getUserName(ProfilePage.this));
        TextView username = findViewById(R.id.ProfileUsername);
        username.setText(dbData.getUsername());
    }
}