package sg.edu.np.mad.pawgress.tutorials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.pawgress.R;

public class tutPage2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tut_page_2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView tut2_next = findViewById((R.id.tut2_next));
        tut2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutPage2.this, tutPage3.class);
                startActivity(intent);
            }
        });
        TextView tut2_back = findViewById((R.id.tut2_back));
        tut2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutPage2.this, tutPage1.class);
                startActivity(intent);
            }
        });
    }
}
