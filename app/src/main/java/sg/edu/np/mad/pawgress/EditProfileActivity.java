package sg.edu.np.mad.pawgress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.Tasks.TaskCompletion;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSave;

    // Retrieve existing user information
    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_password);

        etUsername = findViewById(R.id.editTextText5);
        etPassword = findViewById(R.id.editTextText6);
        btnSave = findViewById(R.id.button7);

        user = getIntent().getParcelableExtra("User");

        if (user != null) {
            etUsername.setText(user.getUsername());
            etPassword.setText(user.getPassword());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated username and password
                String updatedUsername = etUsername.getText().toString();
                String updatedPassword = etPassword.getText().toString();

                if (user != null) {
                    user.updateUsername(updatedUsername);
                    user.updatePassword(updatedPassword);
                }

                Intent intent = new Intent(EditProfileActivity.this,MainMainMain.class);
                intent.putExtra("User", user);
                intent.putExtra("tab", "profile_tab");
                startActivity(intent);
                finish();
            }
        });
    }
}
