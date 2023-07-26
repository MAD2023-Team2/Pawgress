package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class GameImage extends AppCompatActivity {
    MyDBHandler myDBHandler;
    ImageView replaceImage_topLeft;
    ImageView replaceImage_topRight;
    ImageButton backButton;
    Bitmap bitmap;
    InventoryItem inventoryItem;
    UserData user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.game_image);

        myDBHandler = new MyDBHandler(this,null,null,1);
        replaceImage_topLeft = findViewById(R.id.replaceImage_topLeft);
        replaceImage_topRight = findViewById(R.id.replaceImage_topRight);
        backButton = findViewById(R.id.backButton);

        Intent receivingEnd = getIntent();
        bitmap = receivingEnd.getParcelableExtra("bitmap");
        inventoryItem = receivingEnd.getParcelableExtra("item");
        user = receivingEnd.getParcelableExtra("user");

        // set image to image clicked previously and make it transparent
        replaceImage_topLeft.setImageBitmap(bitmap);
        replaceImage_topRight.setImageBitmap(bitmap);
        replaceImage_topLeft.setAlpha(0.5F);
        replaceImage_topRight.setAlpha(0.5F);
    }

    @Override
    protected void onStart(){
        super.onStart();

        replaceImage_topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHandler.setTopRight(user.getUsername(),inventoryItem.getItemName());

                int inventoryQuantity = inventoryItem.getQuantity();
                if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                    myDBHandler.removeInventoryItem(inventoryItem,user);
                }
                else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                    myDBHandler.updateInventoryQuantity(inventoryItem, user, inventoryQuantity-1);
                }
                finish();
            }
        });
        replaceImage_topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHandler.setTopLeft(user.getUsername(),inventoryItem.getItemName());

                int inventoryQuantity = inventoryItem.getQuantity();
                if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                    myDBHandler.removeInventoryItem(inventoryItem,user);
                }
                else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                    myDBHandler.updateInventoryQuantity(inventoryItem, user, inventoryQuantity-1);
                }
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item has been returned to Inventory", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}