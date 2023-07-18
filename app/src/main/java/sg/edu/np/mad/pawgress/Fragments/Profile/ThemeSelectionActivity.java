package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sg.edu.np.mad.pawgress.R;

//public class ThemeSelectionActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_theme_selection);
//
//        Button defaultButton = findViewById(R.id.defaultButton);
//        Button darkButton = findViewById(R.id.darkButton);
//        Button lightButton = findViewById(R.id.lightButton);
//        Button patternButton = findViewById(R.id.patternButton);
//
//        defaultButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                applyTheme("default");
//            }
//        });
//
//        nightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                applyTheme("night");
//            }
//        });
//
//        blueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                applyTheme("blue");
//            }
//        });
//
//        redButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                applyTheme("red");
//            }
//        });
//
//        private void applyTheme(String theme) {
//            switch (theme) {
//                case "dark":
//                    setTheme(R.color.colours_darkmode);
//                    break;
//                case "light":
//                    setTheme(R.color.colour_lightbeigemode);
//                    break;
//                case "pattern":
//                    setTheme(R.color.colours_beige_paw);
//                    break;
//                default:
//                    setTheme(R.color.colour_default);
//                    break;
//            }
//
//            recreate(); // Recreate the activity to apply the new theme
//        }
//
//    }
//}