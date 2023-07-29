package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sg.edu.np.mad.pawgress.R;

public class ThemeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selection);

        // Find the TextView by its ID (formattedTextView)
        TextView formattedTextView = findViewById(R.id.formattedTextView);

        // Set the formatted text using HTML formatting
        String formattedText = "<b>Dark Mode</b><br/>"
                + "- Device screen emits lesser blue light that can interrupt sleep<br/>"
                + "- Goes easier on the eyes, offering relief<br/>"
                + "- Extends device battery life";
        formattedTextView.setText(Html.fromHtml(formattedText));

        // Find the second TextView by its ID (secondFormattedTextView)
        TextView secondFormattedTextView = findViewById(R.id.secondFormattedTextView);

        // Set the formatted text for the second TextView
        String secondFormattedText = "<b>Light Mode</b><br/>"
                + "- Provides better readability in bright environments<br/>"
                + "- Reduces eye strain in well-lit surroundings<br/>"
                + "- Suitable for daytime use";
        secondFormattedTextView.setText(Html.fromHtml(secondFormattedText));

        TextView backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}