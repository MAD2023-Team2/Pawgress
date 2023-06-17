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
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");

        // Retrieve the username from the shared preference
        // SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // String username = sharedPreferences.getString(MY_USERNAME, etUsername.getText().toString());

        catButton = findViewById(R.id.catButton);
        dogButton = findViewById(R.id.dogButton);

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign("cat", user);
            }
        });

        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign("dog", user);
            }
        });
    }
    int[] catDesigns = {R.drawable.grey_cat, R.drawable.orange_cat};
    int[] dogDesigns = {R.drawable.corgi, R.drawable.golden_retriever};
    private void assignCompanionDesign(String selection, UserData user){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //for (int n : catDesigns){System.out.println(n);}
        //for (int n : dogDesigns){System.out.println(n);}

        int selectedDesign;
        if (selection.equals("cat")) {
            int randomIndex = new Random().nextInt(catDesigns.length);
            selectedDesign = catDesigns[randomIndex];
            dbHandler.savePetDesign(user.getUsername(), "cat", selectedDesign);


        } else {
            int randomIndex = new Random().nextInt(dogDesigns.length);
            selectedDesign = dogDesigns[randomIndex];
            dbHandler.savePetDesign(user.getUsername(), "dog", selectedDesign);
        }

        // MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //dbHandler.savePetDesign(user.getUsername(), "cat", selectedDesign);

        // Create an Intent to launch the "MainMainMain" activity
        Intent intent = new Intent(CompanionSelectionActivity.this, DailyLogIn.class);
        intent.putExtra("User",user);
        startActivity(intent);

        // Finish the current activity if needed
        finish();
    }

}