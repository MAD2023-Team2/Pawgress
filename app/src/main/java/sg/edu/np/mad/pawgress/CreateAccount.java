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

import java.util.ArrayList;

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


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MY_USERNAME, etUsername.getText().toString());
                editor.putString(MY_PASSWORD, etPassword.getText().toString());
                editor.commit();
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                */

                UserData dbData = myDBHandler.findUser(etUsername.getText().toString());
                if (dbData == null){
                    ArrayList<Task> taskList = new ArrayList<Task>();
                    String dbUsername = etUsername.getText().toString();
                    String dbPassword = etPassword.getText().toString();
                    UserData dbUserData = new UserData(dbUsername,dbPassword);
                    myDBHandler.addUser(dbUserData);
                    SaveSharedPreference.setUserName(CreateAccount.this ,etUsername.getText().toString());
                    Intent intent = new Intent(CreateAccount.this, tutPage1.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreateAccount.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
}