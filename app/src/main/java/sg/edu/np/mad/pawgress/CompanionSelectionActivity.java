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
    //private String GLOBAL_PREF = "MyPrefs";
    //private String MY_USERNAME = "MyUserName";
    private static final String CAT = "cat";
    private static final String DOG = "dog";
    Button catButton;
    Button dogButton;
    int[] catDesigns = {R.drawable.grey_cat, R.drawable.orange_cat};
    int[] dogDesigns = {R.drawable.corgi, R.drawable.golden_retriever};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion_selection);

        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");


        catButton = findViewById(R.id.catButton);
        dogButton = findViewById(R.id.dogButton);

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
    private void assignCompanionDesign(String selection, UserData user){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //for (int n : catDesigns){System.out.println(n);}
        //for (int n : dogDesigns){System.out.println(n);}

        int selectedDesign;
        if (selection.equals(CAT)) {
            int randomIndex = new Random().nextInt(catDesigns.length);
            selectedDesign = catDesigns[randomIndex];
            dbHandler.savePetDesign(user.getUsername(), CAT, selectedDesign);


        } else {
            int randomIndex = new Random().nextInt(dogDesigns.length);
            selectedDesign = dogDesigns[randomIndex];
            dbHandler.savePetDesign(user.getUsername(), DOG, selectedDesign);
        }

        // Create an Intent to launch the "MainMainMain" activity
        Intent intent = new Intent(CompanionSelectionActivity.this, DailyLogIn.class);
        intent.putExtra("User",user);
        startActivity(intent);

        finish();
    }

}