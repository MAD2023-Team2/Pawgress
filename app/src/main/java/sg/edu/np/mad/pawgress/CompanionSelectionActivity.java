package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class CompanionSelectionActivity extends AppCompatActivity {
    private String GLOBAL_PREF = "MyPrefs";
    private String MY_USERNAME = "MyUserName";
    EditText etUsername = findViewById(R.id.editTextText3);

    Button catButton;
    Button dogButton;
    //UserData User;
    //public void setUser(UserData User){
    // this.User = User;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion_selection);

        // Retrieve the username from the shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(MY_USERNAME, etUsername.getText().toString());

        catButton = findViewById(R.id.catButton);
        dogButton = findViewById(R.id.dogButton);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign("cat");
            }
        });

        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign("dog");
            }
        });
    }

    private void assignCompanionDesign(String selection){
        int[] catDesigns = {R.drawable.grey_cat, R.drawable.orange_cat};
        int[] dogDesigns = {R.drawable.corgi, R.drawable.golden_retriever};

        int selectedDesign;
        if (selection.equals("cat")) {
            int randomIndex = new Random().nextInt(catDesigns.length);
            selectedDesign = catDesigns[randomIndex];

        } else {
            int randomIndex = new Random().nextInt(dogDesigns.length);
            selectedDesign = dogDesigns[randomIndex];
        }

        //MyDBHandler dbHandler = new MyDBHandler(username, );
        //dbHandler.savePetDesign();

        // Create an Intent to launch the "MainMainMain" activity
        Intent intent = new Intent(CompanionSelectionActivity.this, DailyLogIn.class);
        startActivity(intent);

        // Finish the current activity if needed
        finish();
    }
}