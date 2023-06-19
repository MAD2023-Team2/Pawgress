package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sg.edu.np.mad.pawgress.CreateAccount;
import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

public class editProfilePassword extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSave;
    private MyDBHandler dbHandler;
    UserData user;

    String oldusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_password);
        System.out.println("IN EDIT PROFILE");
        etUsername = findViewById(R.id.editTextText5);
        etPassword = findViewById(R.id.editTextText6);
        btnSave = findViewById(R.id.button7);

        dbHandler = new MyDBHandler(this, null, null, 1);


        UserData user = dbHandler.findUser(SaveSharedPreference.getUserName(editProfilePassword.this));

        etUsername.setText(user.getUsername());
        String oldName = etUsername.getText().toString();
        etPassword.setText(user.getPassword());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedUsername = etUsername.getText().toString();
                String updatedPassword = etPassword.getText().toString();
                if (etUsername.length() > 0 && etPassword.length() > 0) {
                    UserData dbData = dbHandler.findUser(etUsername.getText().toString());
                    if (dbData == null){
                        for (Task task: dbHandler.findTaskList(user)) {
                            dbHandler.updateTask(task, updatedUsername);
                        }
                        user.setUsername(updatedUsername);
                        user.setPassword(updatedPassword);
                        dbHandler.updateUser(updatedUsername, updatedPassword, oldName);
                        Toast.makeText(editProfilePassword.this, "User information updated", Toast.LENGTH_SHORT).show();

                        SaveSharedPreference.setUserName(editProfilePassword.this, updatedUsername);
                        finish();
                    }
                    else{
                        Toast.makeText(editProfilePassword.this, "Username Already Exist!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(editProfilePassword.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
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
}