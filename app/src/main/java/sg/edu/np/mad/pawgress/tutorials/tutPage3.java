package sg.edu.np.mad.pawgress.tutorials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.Fragments.Home.HomePage;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class tutPage3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tut_page_3);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView tut3_next = findViewById((R.id.tut3_next));
        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        tut3_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutPage3.this, MainMainMain.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        TextView tut3_back = findViewById((R.id.tut3_back));
        tut3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}