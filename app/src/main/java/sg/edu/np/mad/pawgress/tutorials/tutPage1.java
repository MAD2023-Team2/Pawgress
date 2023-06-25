package sg.edu.np.mad.pawgress.tutorials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class tutPage1 extends AppCompatActivity {

    String tutorial = "Tutorial 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tut_page_1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Receive user data through intent
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");

        // Tutorial page 1
        TextView tut1_next = findViewById((R.id.tut1_next));

        // Right side of screen goes to next page
        tut1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutPage1.this, tutPage2.class);

                // Send user data
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}
