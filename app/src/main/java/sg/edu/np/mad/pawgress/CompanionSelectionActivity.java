package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class CompanionSelectionActivity extends AppCompatActivity {
    private static final String CAT = "cat";
    private static final String DOG = "dog";
    Button catButton;
    Button dogButton;
    // The catDesigns and dogDesigns arrays store the resource IDs of cat and dog designs respectively
    int[] catDesigns = {R.drawable.grey_cat, R.drawable.orange_cat};
    int[] dogDesigns = {R.drawable.corgi, R.drawable.golden_retriever};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_companion_selection);

        //  Intent is received from the previous activity, and the user data is extracted using the key "User".
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");


        catButton = findViewById(R.id.catButton);
        dogButton = findViewById(R.id.dogButton);

        // Click listeners are set for the cat and dog buttons
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign(CAT, user);
            }
        });

        dogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignCompanionDesign(DOG, user);
            }
        });
    }

    // The assignCompanionDesign method is called with the corresponding selection
    private void assignCompanionDesign(String selection, UserData user){
        // An instance of the MyDBHandler is created to interact with the database
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        int selectedDesign;
        if (selection.equals(CAT)) {
            // A random index is generated to select a design from the appropriate array
            int randomIndex = new Random().nextInt(catDesigns.length);
            selectedDesign = catDesigns[randomIndex];

            // selected design is saved in the database and the user's pet information is updated
            dbHandler.savePetDesign(user.getUsername(), CAT, selectedDesign);
            user.setPetDesign(selectedDesign);
            user.setPetType(CAT);


        } else {
            // same logic here but for dogs
            int randomIndex = new Random().nextInt(dogDesigns.length);
            selectedDesign = dogDesigns[randomIndex];
            dbHandler.savePetDesign(user.getUsername(), DOG, selectedDesign);
            user.setPetDesign(selectedDesign);
            user.setPetType(DOG);
        }

        // Create an Intent to launch the "MainMainMain" activity
        Intent intent = new Intent(CompanionSelectionActivity.this, DailyLogIn.class);
        // User data is passed as an extra
        intent.putExtra("User",user);
        // The activity is started, and the current activity is finished
        startActivity(intent);

        finish();
    }

}