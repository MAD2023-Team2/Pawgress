package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.CreateAccount;
import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryItem;
import sg.edu.np.mad.pawgress.FriendData;
import sg.edu.np.mad.pawgress.FriendRequest;
import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

public class editProfilePassword extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSave;
    private TextView back;
    private MyDBHandler dbHandler;
    UserData user, orginalUser;

    private TextView noPass, noName, sameAccount;

    private CardView frameOne, frameTwo, frameThree;
    private boolean passChar8 = false, passUpper = false, passNum = false, isRegistrationClickable = false;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_edit_profile_password);
        System.out.println("IN EDIT PROFILE");
        etUsername = findViewById(R.id.editTextText5);
        etPassword = findViewById(R.id.editTextText6);
        btnSave = findViewById(R.id.button7);
        back = findViewById(R.id.backButton);
        dbHandler = new MyDBHandler(this, null, null, 1);
        database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        myRef = database.getReference("Users");

        frameOne = findViewById(R.id.frameOne);
        frameTwo = findViewById(R.id.frameTwo);
        frameThree = findViewById(R.id.frameThree);
        noName = findViewById(R.id.noName);
        noPass = findViewById(R.id.noPass);
        sameAccount = findViewById(R.id.sameAccount);

        // Getting user data
        UserData user = dbHandler.findUser(SaveSharedPreference.getUserName(editProfilePassword.this));
        orginalUser = dbHandler.findUser(SaveSharedPreference.getUserName(editProfilePassword.this));

        // Setting texts for current username and password
        etUsername.setText(user.getUsername());
        String oldName = etUsername.getText().toString();
        etPassword.setText(user.getPassword());

        // Save Button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUsername.getText().toString(), password = etPassword.getText().toString();

                if (name.length() > 0 && password.length() > 0) {
                    if (isRegistrationClickable) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(editProfilePassword.this, "No internet access. Unable to edit account.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String updatedUsername = etUsername.getText().toString();
                            String updatedPassword = etPassword.getText().toString();
                            // Getting user data
                            UserData dbData = dbHandler.findUser(etUsername.getText().toString());
                            String userId = String.valueOf(user.getUserId());
                            // Checks that user does not already exists
                            if (dbData == null || oldName.equals(updatedUsername)){

                                user.setTaskList(dbHandler.findTaskList(user));
                                user.setFriendList(dbHandler.findFriendList(user));
                                user.setFriendReqList(dbHandler.findFriendReqList(user));
                                user.setInventoryList(dbHandler.findInventoryList(user));

                                // Set friends and friend request list based on Firebase, not SQLite
                                Query query = myRef.orderByChild("username").equalTo(user.getUsername());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                UserData tempUser = snapshot.getValue(UserData.class);

                                                // Store old username
                                                String oldUsername = user.getUsername();

                                                // Remove user in firebase
                                                myRef.child(user.getUsername()).removeValue();

                                                // Get most recent friend and inventory items, request list from firebase
                                                user.setFriendList(tempUser.getFriendList());
                                                user.setFriendReqList(tempUser.getFriendReqList());
                                                for (FriendData friend: user.getFriendList()){
                                                    Log.i(null, "Clear and Update---------------------------------" + friend.getFriendName());
                                                }

                                                // Update username and password
                                                user.setUsername(updatedUsername);
                                                user.setPassword(updatedPassword);

                                                // Add user back with updated name in firebase
                                                myRef.child(updatedUsername).setValue(user);

                                                dbHandler.clearDatabase("ACCOUNTS");
                                                dbHandler.clearDatabase("TASKS");
                                                dbHandler.clearDatabase("FRIENDS");
                                                dbHandler.clearDatabase("FRIENDREQUEST");
                                                dbHandler.clearDatabase("INVENTORY");
                                                dbHandler.addUser(user);
                                                for (Task task: user.getTaskList()){
                                                    dbHandler.addTask(task, user);
                                                }
                                                for (InventoryItem item: user.getInventoryList()){
                                                    dbHandler.addInventoryItem(item, user);
                                                }
                                                for (FriendData friend: user.getFriendList()){
                                                    dbHandler.addFriend(friend.getFriendName(), user, friend.getStatus());
                                                    Query query = myRef.orderByChild("username").equalTo(friend.getFriendName());
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()){
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                    UserData friend = snapshot.getValue(UserData.class);
                                                                    ArrayList<FriendData> friendFriendList = friend.getFriendList();
                                                                    for (FriendData friendData: friendFriendList){
                                                                        if (friendData.getFriendName().equals(oldUsername)){
                                                                            friendData.setFriendName(updatedUsername);
                                                                        }
                                                                    }
                                                                    myRef.child(friend.getUsername()).child("friendList").setValue(friendFriendList);
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });
                                                }
                                                for (FriendRequest friendRequest: user.getFriendReqList()){
                                                    dbHandler.addFriendReq(friendRequest.getFriendReqName(), user, friendRequest.getReqStatus());
                                                    Query query = myRef.orderByChild("username").equalTo(friendRequest.getFriendReqName());
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()){
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                    UserData friend = snapshot.getValue(UserData.class);
                                                                    ArrayList<FriendRequest> friendFriendReqList = friend.getFriendReqList();
                                                                    for (FriendRequest friendRequest: friendFriendReqList){
                                                                        if (friendRequest.getFriendReqName().equals(oldUsername)){
                                                                            friendRequest.setFriendReqName(updatedUsername);
                                                                        }
                                                                    }
                                                                    myRef.child(friend.getUsername()).child("friendReqList").setValue(friendFriendReqList);
                                                                }

                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });
                                                }

                                                Toast.makeText(editProfilePassword.this, "User information updated", Toast.LENGTH_SHORT).show();

                                                // Updates shared preference for auto login
                                                SaveSharedPreference.setUserName(editProfilePassword.this, updatedUsername);

                                                // Goes back to Profile page
                                                Intent newTask = new Intent(editProfilePassword.this, MainMainMain.class);
                                                newTask.putExtra("New Task List", user);
                                                newTask.putExtra("User", user);
                                                newTask.putExtra("tab", "profile_tab");
                                                startActivity(newTask);
                                                finish();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error
                                    }
                                });
                            }
                            else{
                                Toast.makeText(editProfilePassword.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (name.length() == 0) {
                        noName.setVisibility(View.VISIBLE);
                    }
                    if (password.length() == 0) {
                        noPass.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // Add a TextWatcher to the username field
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { } // do nth
            @Override
            public void afterTextChanged(Editable editable) { } // do nth
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // When name input changes, validate registration data
                registrationDataCheck();
            }
        });

        // Add a TextWatcher to the password field
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { } // do nth
            @Override
            public void afterTextChanged(Editable s) { } // do nth
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When password input changes, validate registration data
                registrationDataCheck();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the latest user information from the database
        if (user != null) {
            String existingUsername = dbHandler.findUsername(user.getUsername());
            String existingPassword = dbHandler.findPassword(user.getUsername());

            if (existingUsername != null && existingPassword != null) {
                etUsername.setText(existingUsername);
                etPassword.setText(existingPassword);
            }
        }
    }

    // Method to validate registration data and update UI
    @SuppressLint("ResourceType")
    private void registrationDataCheck() {
        String password = etPassword.getText().toString();
        String name = etUsername.getText().toString();

        // Check for empty name and password fields and display error messages if empty
        if (!name.isEmpty() && noName.getVisibility() == View.VISIBLE) {
            noName.setVisibility(View.GONE);
        }
        if (!password.isEmpty() && noPass.getVisibility() == View.VISIBLE) {
            noPass.setVisibility(View.GONE);
        }

        // Check if password contains at least 8 characters and less than 20
        if (password.length() >= 8 && password.length() <= 20) {
            passChar8 = true;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent))); // Set frameOne background to accent color
        } else {
            passChar8 = false;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault))); // Set frameOne background to default color
        }

        // Check if password contains at least one uppercase letter and one lowercase letter
        if (password.matches("(?=.*[A-Z])(?=.*[a-z]).+")) {
            passUpper = true;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent))); // Set frameTwo background to accent color
        } else {
            passUpper = false;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault))); // Set frameTwo background to default color
        }

        // Check if password contains at least one numeric digit
        if (password.matches("(.*[0-9].*)")) {
            passNum = true;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent))); // Set frameThree background to accent color
        } else {
            passNum = false;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault))); // Set frameThree background to default color
        }



        // Check all password requirements and update UI
        if (passChar8 && passUpper && passNum) {
            // Validate that user has change at least the password or username
            if(password.equals(orginalUser.getPassword()) && name.equals(orginalUser.getUsername())){
                isRegistrationClickable = false;
                btnSave.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault))); // Set default background color
                sameAccount.setVisibility(View.VISIBLE);
            }
            else{
                // If all password requirements are met, enable save button and change background color
                isRegistrationClickable = true;
                btnSave.setBackgroundColor(Color.parseColor("#88E473")); // Set background color to light green
                sameAccount.setVisibility(View.GONE);
            }

        } else {

            // If any password requirement is not met, disable registration button and change background color
            isRegistrationClickable = false;
            btnSave.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault))); // Set default background color
            sameAccount.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}