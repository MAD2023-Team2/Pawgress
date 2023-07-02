package sg.edu.np.mad.pawgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.pawgress.Tasks.Task;

public class LoginPage extends AppCompatActivity {
    @Override
    public void onBackPressed(){ //exit app when pressing back button
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Admin account credentials verification
        if (!isValidCredentials("admin", "admin123")) {
            Log.v(title,"Admin Account Not Found");
            String dbUsername = "admin";
            String dbPassword = "admin123";
            ArrayList<Task> taskList = new ArrayList<Task>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String accCreateDate = formatter.format(new Date());
            UserData dbUserData = new UserData(dbUsername,dbPassword,taskList,accCreateDate,1,0,"No","dog",2354);
            System.out.println(dbUsername + dbPassword + taskList+ accCreateDate+dbUserData.getStreak()+dbUserData.getCurrency()+dbUserData.getLoggedInTdy());
            myDBHandler.addUser(dbUserData);
        }
        else {
            Log.v(title,"Admin account found");
        }

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
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        // Checking shared preferences for auto login, if shared preference has no data, brings to login page
        if(SaveSharedPreference.getUserName(LoginPage.this).length() == 0)
        {
            setContentView(R.layout.login_page);
            Log.i(title, "Starting App Login Page");

            TextView newUser = findViewById(R.id.textView4);
            Button loginButton = findViewById(R.id.button);

            newUser.setOnTouchListener(new View.OnTouchListener() { //create account button
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(LoginPage.this, CreateAccount.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    return false;
                }
            });

            // Login button
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText etUsername = findViewById(R.id.editTextText);
                    EditText etPassword = findViewById(R.id.editTextText2);
                    String username = etUsername.getText().toString();
                    if (etUsername.length() > 0 && etPassword.length() > 0) {
                        // Read from the database
                        Query query = myRef.orderByChild("username").equalTo(username);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String userId = snapshot.getKey();
                                        final String getPass = dataSnapshot.child(userId).child("password").getValue(String.class);
                                        String password = etPassword.getText().toString().trim();

                                        if (getPass.equals(password)){
                                            Intent intent = new Intent(LoginPage.this,DailyLogIn.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // do nth
                            }
                        });
//                        UserData user = myDBHandler.findUser(username);
//                        if (isValidCredentials(etUsername.getText().toString(), etPassword.getText().toString())){
//                            SaveSharedPreference.setUserName(LoginPage.this ,etUsername.getText().toString());
//                            Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
//                            intent.putExtra("User", user);
//                            startActivity(intent);
//                            finish();
//                        }
                        Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        // If shared preference has data, skips log in
        else
        {
            UserData user = myDBHandler.findUser(SaveSharedPreference.getUserName(LoginPage.this));
            Log.v(title, "TaskList" + user.getTaskList().size());
            Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
            intent.putExtra("User", user);
            startActivity(intent);
        }
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

    // Verifies username and password
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