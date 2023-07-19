package sg.edu.np.mad.pawgress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import sg.edu.np.mad.pawgress.tutorials.Tutorial_Page;

public class CreateAccount extends AppCompatActivity {
    String title = "Main Activity 2";

    private String GLOBAL_PREF = "MyPrefs";
    private String MY_USERNAME = "MyUSerName";
    private String MY_PASSWORD = "MyPassword";
    SharedPreferences sharedPreferences;

    private Button createButton, cancelButton;
    private EditText etUsername;
    private EditText etPassword;
    private TextView noPass, noName;
    private CardView frameOne, frameTwo, frameThree;
    private boolean passChar8 = false, passUpper = false, passNum = false, isRegistrationClickable = false;

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

        etUsername = findViewById(R.id.editTextText3);
        etPassword = findViewById(R.id.editTextText4);
        createButton = findViewById(R.id.button2);
        cancelButton = findViewById(R.id.button3);

        frameOne = findViewById(R.id.frameOne);
        frameTwo = findViewById(R.id.frameTwo);
        frameThree = findViewById(R.id.frameThree);
        noName = findViewById(R.id.noName);
        noPass = findViewById(R.id.noPass);

        createButton.setOnClickListener(new View.OnClickListener() { //create button in create account page
            @Override
            public void onClick(View v) {
                String dbUsername = etUsername.getText().toString();
                String dbPassword = etPassword.getText().toString();

                if (dbUsername.length() > 0 && dbPassword.length() > 0) {
                    if (isRegistrationClickable) {
                        UserData dbData = myDBHandler.findUser(etUsername.getText().toString());

                        // Checking that account does not already exists
                        if (dbData == null){
                            ArrayList<Task> taskList = new ArrayList<Task>();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String accCreateDate = formatter.format(new Date());

                            ArrayList<FriendData> friendList = new ArrayList<>();
                            FriendData friendData = new FriendData("abc", "Unfriended");
                            friendList.add(friendData);

                            ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                            FriendRequest friendReq = new FriendRequest("admin", "Rejected");
                            friendRequests.add(friendReq);

                            final int[] newID = new int[1];
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
                            DatabaseReference myRef = database.getReference("Users");

                            // Query the users collection and sort by the unique ID field in descending order
                            Query query = myRef.orderByChild("userID");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        int highestUniqueId = -1;
                                        // Iterate over the result and retrieve the highest unique ID
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            UserData user = snapshot.getValue(UserData.class);
                                            int currentUniqueId = user.getUserId() + 1;
                                            if (currentUniqueId > highestUniqueId) {
                                                highestUniqueId = currentUniqueId;
                                                newID[0] = highestUniqueId;
                                            }
                                        }
                                    UserData dbUserData = new UserData(newID[0],dbUsername,dbPassword,taskList,accCreateDate,1,0,"No","dog",2354, friendList, friendRequests);
                                    System.out.println(dbUsername + dbPassword + taskList+ accCreateDate+dbUserData.getStreak()+dbUserData.getCurrency()+dbUserData.getLoggedInTdy());
                                    myDBHandler.clearDatabase("ACCOUNTS");
                                    myDBHandler.clearDatabase("TASKS");
                                    myDBHandler.clearDatabase("FRIENDS");
                                    myDBHandler.clearDatabase("FRIENDREQUEST");
                                    for (Task task: dbUserData.getTaskList()){
                                        myDBHandler.addTask(task, dbUserData);
                                    }
                                    for (FriendData friend: dbUserData.getFriendList()){
                                        myDBHandler.addFriend(friend.getFriendName(), dbUserData, friend.getStatus());
                                    }
                                    for (FriendRequest friendRequest: dbUserData.getFriendReqList()){
                                        myDBHandler.addFriendReq(friendRequest.getFriendReqName(), dbUserData, friendRequest.getReqStatus());
                                    }
                                    // Adding user to database
                                    myDBHandler.addUser(dbUserData);
                                    // Setting shared preference for auto login
                                    SaveSharedPreference.setUserName(CreateAccount.this ,etUsername.getText().toString());
                                    Intent intent = new Intent(CreateAccount.this, Tutorial_Page.class);
                                    intent.putExtra("User", dbUserData);

                                    // for CreateAccount to CompanionSelectionActivity
                                    // pass username info over
                                    // sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
                                    // SharedPreferences.Editor editor = sharedPreferences.edit();
                                    // editor.putString(MY_USERNAME, etUsername.getText().toString());
                                    // editor.commit();

                                    startActivity(intent);
                                    finish();
                                    } else {
                                        // Handle the case when there are no users in the database
                                        UserData dbUserData = new UserData(1,dbUsername,dbPassword,taskList,accCreateDate,1,0,"No","dog",2354, friendList, friendRequests);
                                        System.out.println(dbUsername + dbPassword + taskList+ accCreateDate+dbUserData.getStreak()+dbUserData.getCurrency()+dbUserData.getLoggedInTdy());
                                        myDBHandler.clearDatabase("ACCOUNTS");
                                        myDBHandler.clearDatabase("TASKS");
                                        myDBHandler.clearDatabase("FRIENDS");
                                        myDBHandler.clearDatabase("FRIENDREQUEST");
                                        for (Task task: dbUserData.getTaskList()){
                                            myDBHandler.addTask(task, dbUserData);
                                        }
                                        for (FriendData friend: dbUserData.getFriendList()){
                                            myDBHandler.addFriend(friend.getFriendName(), dbUserData, friend.getStatus());
                                        }
                                        for (FriendRequest friendRequest: dbUserData.getFriendReqList()){
                                            myDBHandler.addFriendReq(friendRequest.getFriendReqName(), dbUserData, friendRequest.getReqStatus());
                                        }
                                        // Adding user to database
                                        myDBHandler.addUser(dbUserData);
                                        // Setting shared preference for auto login
                                        SaveSharedPreference.setUserName(CreateAccount.this ,etUsername.getText().toString());
                                        Intent intent = new Intent(CreateAccount.this, Tutorial_Page.class);
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
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle the error if the database operation was canceled
                                    Log.e(title, "Database error: " + databaseError.getMessage());
                                }
                            });
                        }
                        else{
                            Toast.makeText(CreateAccount.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (dbUsername.length() == 0) {
                        noName.setVisibility(View.VISIBLE);
                    }
                    if (dbPassword.length() == 0) {
                        noPass.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        inputChange();

        cancelButton.setOnClickListener(new View.OnClickListener() { // Cancel button for create account page, goes back to login page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }
    private void checkEmpty(String name, String password) {
        if (name.length() > 0 && noName.getVisibility() == View.VISIBLE) {
            noName.setVisibility(View.GONE);
        }
        if (password.length() > 0 && noPass.getVisibility() == View.VISIBLE) {
            noPass.setVisibility(View.GONE);
        }
    }
    @SuppressLint("ResourceType")
    private void checkAllData() {
        if (passChar8 && passUpper && passNum) {
            isRegistrationClickable = true;
            createButton.setBackgroundColor(Color.parseColor("#88E473"));
        }
        else {
            isRegistrationClickable = false;
            createButton.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }
    // Using Regular Expression for Password Validation
    // Conditions : Password Length 8-20 characters, must have lower and upper case & digits
    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = etPassword.getText().toString(), name = etUsername.getText().toString();

        checkEmpty(name, password);

        if (password.length() >= 8 && password.length() <= 20) {
            passChar8 = true;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            passChar8 = false;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        if (password.matches("(?=.*[A-Z])(?=.*[a-z]).+")) {
            passUpper = true;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            passUpper = false;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        if (password.matches("(.*[0-9].*)")) {
            passNum = true;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            passNum = false;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        checkAllData();
    }
    private void inputChange() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registrationDataCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}