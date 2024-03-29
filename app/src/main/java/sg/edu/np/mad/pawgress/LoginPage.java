package sg.edu.np.mad.pawgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryItem;
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
//        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        // Admin account credentials verification
        /*
        validateCredentials("admin", "admin123", new CredentialsValidationListener() {
            @Override
            public void onCredentialsValidated(boolean isValid) {
                if (!isValid) {
                    Log.v(title,"Admin Account Not Found");
                    String dbUsername = "admin";
                    String dbPassword = "admin123";
                    ArrayList<Task> taskList = new ArrayList<Task>();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String newDayDate = formatter.format(new Date());
                    Task task = new Task(1, "name", "In Progress", "cat" ,0, 1, "1", newDayDate,null,null, 0, 1);
                    Task task2 = new Task(2, "name2", "In Progress", "cat" ,0, 1, "1", newDayDate,null,null, 0, 0);
                    taskList.add(task);
                    taskList.add(task2);
                    String accCreateDate = formatter.format(new Date());

                    ArrayList<FriendData> friendList = new ArrayList<>();
                    FriendData friendData = new FriendData("abc", "Friend");
                    friendList.add(friendData);

                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    FriendRequest friendRequest = new FriendRequest("admin", "Pending");
                    friendRequests.add(friendRequest);

                    UserData dbUserData = new UserData(1,dbUsername,dbPassword,taskList,accCreateDate,1,0,"No","dog",2354, friendList, friendRequests);
                    System.out.println(dbUsername + dbPassword + taskList+ accCreateDate+dbUserData.getStreak()+dbUserData.getCurrency()+dbUserData.getLoggedInTdy());
                    myDBHandler.addUser(dbUserData);

                    myRef.child("admin").setValue(dbUserData);
                    Log.v(title,"Admin Account Added");
                } else {
                    Log.v(title,"Admin account found");
                }
            }
        });
        */
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
                    String password = etPassword.getText().toString();
                    Log.i(title, "PASSWORD CHECK" + password);
                    if (etUsername.length() > 0 && etPassword.length() > 0) {

                        validateCredentials(username, password, new CredentialsValidationListener() {
                            @Override
                            public void onCredentialsValidated(boolean isValid) {
                                if (isValid) {
                                    Query query = myRef.orderByChild("username").equalTo(username);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    UserData user = snapshot.getValue(UserData.class);

                                                    // Setting Shared preferences
                                                    SaveSharedPreference.clearSeenFriendReq(LoginPage.this);
                                                    SaveSharedPreference.setUserName(LoginPage.this ,etUsername.getText().toString());
                                                    SaveSharedPreference.setProfilePic(LoginPage.this, Integer.parseInt(user.getProfilePicturePath()));

                                                    // Wiping sql database
                                                    myDBHandler.clearDatabase("ACCOUNTS");
                                                    myDBHandler.clearDatabase("TASKS");
                                                    myDBHandler.clearDatabase("FRIENDS");
                                                    myDBHandler.clearDatabase("FRIENDREQUEST");
                                                    myDBHandler.clearDatabase("INVENTORY");
                                                    Log.v(null, "ON LOGIN ---------------------" + SaveSharedPreference.getProfilePic(LoginPage.this) + user.getProfilePicturePath());
                                                    myDBHandler.addUser(user);

                                                    // Populate sql database with user data
                                                    for (Task task: user.getTaskList()){
                                                        myDBHandler.addTask(task, user);
                                                    }
                                                    for (FriendData friend: user.getFriendList()){
                                                        myDBHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                                                    }
                                                    for (FriendRequest friendRequest: user.getFriendReqList()){
                                                        myDBHandler.addFriendReq(friendRequest.getFriendReqName(), user, friendRequest.getReqStatus());
                                                    }
                                                    try{
                                                        for (InventoryItem inventoryItem : user.getInventoryList()) {
                                                            myDBHandler.addInventoryItem(inventoryItem, user);
                                                        }
                                                    }
                                                    catch (Exception e){
                                                        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
                                                        InventoryItem inventoryItem1 = new InventoryItem("Banana", 1, "Food");
                                                        inventoryItems.add(inventoryItem1);
                                                        user.setInventoryList(inventoryItems);
                                                        myDBHandler.addInventoryItem(inventoryItem1, user);
                                                    }

                                                    Log.v(null, "LOGIN PET DESIGN ----------------------"+ user.getPetDesign());

                                                    Log.v(null, "LOGIN PET DESIGN ----------------------"+ user.getPetDesign());
                                                    Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
                                                    intent.putExtra("User", user);
                                                    Log.i(title, "---------------------------------" + user.getFriendReqList().get(0).getFriendReqName());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    if (!isNetworkAvailable()) {
                                        Toast.makeText(LoginPage.this, "No internet access. Unable to login.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        }
//                        UserData user = myDBHandler.findUser(username);
//                        if (isValidCredentials(etUsername.getText().toString(), etPassword.getText().toString())){
//                            SaveSharedPreference.setUserName(LoginPage.this ,etUsername.getText().toString());
//                            Intent intent = new Intent(LoginPage.this, DailyLogIn.class);
//                            intent.putExtra("User", user);
//                            startActivity(intent);
//                            finish();
//                        }
                    else{
                        if (!isNetworkAvailable()) {
                            Toast.makeText(LoginPage.this, "No internet access. Unable to login.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginPage.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                        }
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
    boolean valid = false;

    // Interface for asynchronous behaviour of firebase data retrieval
    private interface CredentialsValidationListener {
        void onCredentialsValidated(boolean isValid);
    }

    // Verifies username and password
    private void validateCredentials(String username, String password, CredentialsValidationListener listener) {
        if (!isNetworkAvailable()) {
            Toast.makeText(LoginPage.this, "No internet access. Unable to login.", Toast.LENGTH_SHORT).show();
            listener.onCredentialsValidated(false);
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        Query query = myRef.orderByChild("username").equalTo(username);

        // Read from the firebase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isValid = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        String getPass = dataSnapshot.child(userId).child("password").getValue(String.class);

                        // Checking password
                        if (getPass.equals(password)) {
                            isValid = true;
                            break;
                        }
                    }
                }
                listener.onCredentialsValidated(isValid);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // User does not exist
                listener.onCredentialsValidated(false);
            }
        });
    }

    // Checking for network connection for access to firebase
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}


