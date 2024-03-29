package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class RemoveRoomItem extends AppCompatActivity {
    MyDBHandler myDBHandler;
    ImageView replaceImage_topLeft;
    ImageView replaceImage_topRight;
    ImageView replaceImage_topMiddle;
    TextView backButton;
    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_remove_room_item);

        // Initialize the database handler and UI elements
        myDBHandler = new MyDBHandler(this, null, null, 1);
        replaceImage_topLeft = findViewById(R.id.replaceImage_topLeft);
        replaceImage_topRight = findViewById(R.id.replaceImage_topRight);
        replaceImage_topMiddle = findViewById(R.id.replaceImage_topMiddle);
        backButton = findViewById(R.id.backButton);

        // Retrieve the user object from the previous activity
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("user");

        // Update the images displayed in the room
        updateImage();
    }

    private void updateImage() {
        // setting top left item image in the room
        String topLeft = user.getTopLeft();
        if (!topLeft.equals("")){
            String pathName = myDBHandler.getImageURL(topLeft);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            replaceImage_topLeft.setImageBitmap(bitmap);
        }
        else{
            // If the respective items are not available, set the corresponding ImageView to be invisible (GONE)
            replaceImage_topLeft.setVisibility(View.GONE);
        }

        // setting top right item image in the room
        String topRight = user.getTopRight();
        if (!topRight.equals("")){
            String pathName2 = myDBHandler.getImageURL(topRight);
            Bitmap bitmap2 = BitmapFactory.decodeFile(pathName2);
            replaceImage_topRight.setImageBitmap(bitmap2);
        }
        else{
            // If the respective items are not available, set the corresponding ImageView to be invisible (GONE)
            replaceImage_topRight.setVisibility(View.GONE);
        }

        // setting top middle item image in the room
        String topMiddle = user.getTopMiddle();
        if (!topMiddle.equals("")){
            String pathName3 = myDBHandler.getImageURL(topMiddle);
            Bitmap bitmap3 = BitmapFactory.decodeFile(pathName3);
            replaceImage_topMiddle.setImageBitmap(bitmap3);
        }
        else{
            // If the respective items are not available, set the corresponding ImageView to be invisible (GONE)
            replaceImage_topMiddle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Context context = this;

        // Set click listeners for the image views of the top-right items
        replaceImage_topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display an alert dialog to confirm removing the top-right item from the room
                // If confirmed, update the database and user object, and then update the images displayed

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove item from room: " );
                builder.setMessage(user.getTopRight());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Toast.makeText(context, "Removed " + user.getTopRight() + " from the room", Toast.LENGTH_SHORT).show();
                        myDBHandler.setTopRight(user.getUsername(),"");
                        user.setTopRight("");
                        updateImage();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Set click listeners for the image views of the top-left items
        replaceImage_topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display an alert dialog to confirm removing the top-left item from the room
                // If confirmed, update the database and user object, and then update the images displayed

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove item from room: " );
                builder.setMessage(user.getTopLeft());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Toast.makeText(context, "Removed " + user.getTopLeft() + " from the room", Toast.LENGTH_SHORT).show();
                        myDBHandler.setTopLeft(user.getUsername(),"");
                        user.setTopLeft("");
                        updateImage();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Set click listeners for the image views of the top-middle items
        replaceImage_topMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display an alert dialog to confirm removing the top-middle item from the room
                // If confirmed, update the database and user object, and then update the images displayed

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove item from room: " );
                builder.setMessage(user.getTopMiddle());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Toast.makeText(context, "Removed " + user.getTopMiddle() + " from the room", Toast.LENGTH_SHORT).show();
                        myDBHandler.setTopMiddle(user.getUsername(),"");
                        user.setTopMiddle("");
                        updateImage();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Set click listener for the back button to finish the activity and return to the previous one
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}