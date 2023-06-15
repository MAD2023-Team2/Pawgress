package sg.edu.np.mad.pawgress.tutorials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.ProfilePage;
import sg.edu.np.mad.pawgress.R;

public class tutPage1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tut_page_1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Tutorial page 1
        TextView tut1_next = findViewById((R.id.tut1_next));
        tut1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutPage1.this, tutPage2.class);
                startActivity(intent);
            }
        });
    }
}
