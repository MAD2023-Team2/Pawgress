package sg.edu.np.mad.pawgress;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.tutorials.tutPage1;

public class CreateAccount extends AppCompatActivity {
    String title = "Main Activity 2";

    private String GLOBAL_PREF = "MyPrefs";
    private String MY_USERNAME = "MyUSerName";
    private String MY_PASSWORD = "MyPassword";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(title, "Starting Acct Creation");

        EditText etUsername = findViewById(R.id.editTextText3);
        EditText etPassword = findViewById(R.id.editTextText4);
        Button createButton = findViewById(R.id.button2);
        Button cancelButton = findViewById(R.id.button3);


        createButton.setOnClickListener(new View.OnClickListener() { //create button in create account page
            @Override
            public void onClick(View v) {
                String dbUsername = etUsername.getText().toString();
                String dbPassword = etPassword.getText().toString();
                //input validation, no empty inputs
                if (etUsername.length() > 0 && validatePassword(dbPassword)) {
                    UserData dbData = myDBHandler.findUser(etUsername.getText().toString());

                    // Checking that account does not already exists
                    if (dbData == null){
                        ArrayList<Task> taskList = new ArrayList<Task>();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String accCreateDate = formatter.format(new Date());
                        UserData dbUserData = new UserData(1,dbUsername,dbPassword,taskList,accCreateDate,1,0,"No","dog",2354);
                        System.out.println(dbUsername + dbPassword + taskList+ accCreateDate+dbUserData.getStreak()+dbUserData.getCurrency()+dbUserData.getLoggedInTdy());

                        // Adding user to database
                        myDBHandler.addUser(dbUserData);

                        // Setting shared preference for auto login
                        SaveSharedPreference.setUserName(CreateAccount.this ,etUsername.getText().toString());
                        Intent intent = new Intent(CreateAccount.this, tutPage1.class);
                        intent.putExtra("User", dbUserData);

                        // for CreateAccount to CompanionSelectionActivity
                        // pass username info over
                        // sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
                        // SharedPreferences.Editor editor = sharedPreferences.edit();
                        // editor.putString(MY_USERNAME, etUsername.getText().toString());
                        // editor.commit();

                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(CreateAccount.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(CreateAccount.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() { // Cancel button for create account page, goes back to login page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    // Using Regular Expression for Password Validation
    // Conditions : Password Length 8-20 characters, must have lower and upper case & digits
    public static boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$";
        return Pattern.matches(passwordRegex, password);
    }
}