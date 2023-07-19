package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ImageButton back;
    private MyDBHandler dbHandler;
    UserData user;

    private TextView noPass, noName;

    private CardView frameOne, frameTwo, frameThree;
    private boolean passChar8 = false, passUpper = false, passNum = false, isRegistrationClickable = false;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_password);
        System.out.println("IN EDIT PROFILE");
        etUsername = findViewById(R.id.editTextText5);
        etPassword = findViewById(R.id.editTextText6);
        btnSave = findViewById(R.id.button7);
        back = findViewById(R.id.imageButton);
        dbHandler = new MyDBHandler(this, null, null, 1);
        database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        myRef = database.getReference("Users");

        frameOne = findViewById(R.id.frameOne);
        frameTwo = findViewById(R.id.frameTwo);
        frameThree = findViewById(R.id.frameThree);
        noName = findViewById(R.id.noName);
        noPass = findViewById(R.id.noPass);

        // Getting user data
        UserData user = dbHandler.findUser(SaveSharedPreference.getUserName(editProfilePassword.this));

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
                        String updatedUsername = etUsername.getText().toString();
                        String updatedPassword = etPassword.getText().toString();
                        // Getting user data
                        UserData dbData = dbHandler.findUser(etUsername.getText().toString());
                        String userId = String.valueOf(user.getUserId());
                        // Checks that user does not already exists
                        if (dbData == null || oldName.equals(updatedUsername)){

                            // Updating user data in database and firebase
                            DatabaseReference userRef = myRef.child(userId);
                            userRef.child("username").setValue(updatedUsername);

                            dbHandler.updatePassword(oldName,updatedPassword);
                            dbHandler.updateUsername(userId,updatedUsername);

                            // Updates username associated with tasks
                            for (Task task: dbHandler.findTaskList(user)) {
                                dbHandler.updateTask(task, updatedUsername);
                            }

                            // Update username and password
                            user.setUsername(updatedUsername);
                            user.setPassword(updatedPassword);

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
                        else{
                            Toast.makeText(editProfilePassword.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();
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
        inputChange();
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
            btnSave.setBackgroundColor(Color.parseColor("#88E473"));
        }
        else {
            isRegistrationClickable = false;
            btnSave.setBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }
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