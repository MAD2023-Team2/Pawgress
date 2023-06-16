package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    String title = "Main Activity";
    /*
        private String GLOBAL_PREF = "MyPrefs";
        private String MY_USERNAME = "MyUSerName";
        private String MY_PASSWORD = "MyPassword";
        SharedPreferences sharedPreferences;
    */
    @Override
    protected void onStart(){
        super.onStart();
        Log.v(title, "On Login Page");
        if(SaveSharedPreference.getUserName(LoginPage.this).length() == 0) //shared preferences for auto login, if shared preference has no data, brings to login page
        {
            setContentView(R.layout.login_page);
            Log.i(title, "Starting App Login Page");

            TextView newUser = findViewById(R.id.textView4);
            Button loginButton = findViewById(R.id.button);

            newUser.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(LoginPage.this, CreateAccount.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return false;
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText etUsername = findViewById(R.id.editTextText);
                    EditText etPassword = findViewById(R.id.editTextText2);
                    String username = etUsername.getText().toString();
                    UserData user = myDBHandler.findUser(username);
                    if (isValidCredentials(etUsername.getText().toString(), etPassword.getText().toString())){
                        SaveSharedPreference.setUserName(LoginPage.this ,etUsername.getText().toString());
                        // Intent intent = new Intent(LoginPage.this, HomePage.class);
                        Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else // if shared preference has data, skips log in
        {
            UserData user = myDBHandler.findUser(SaveSharedPreference.getUserName(LoginPage.this));
            Log.v(title, "TaskList" + user.getTaskList().size());
            //Intent intent = new Intent(LoginPage.this, HomePage.class);
            Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
            intent.putExtra("User", user);
            startActivity(intent);
        }
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

    private boolean isValidCredentials(String username, String password){

        /*
        sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
        String sharedUsername = sharedPreferences.getString(MY_USERNAME, "");
        String sharedPassword = sharedPreferences.getString(MY_PASSWORD, "");

        if (sharedUsername.equals(username) && sharedPassword.equals(password)){
            return true;
        }
        return false;

         */
        UserData dbData = myDBHandler.findUser(username);
        try {
            if(dbData.getUsername().equals(username) && dbData.getPassword().equals(password)){
                return true;
            }
            return false;
        }
        catch(NullPointerException e) {
            return false;
        }
    }
}