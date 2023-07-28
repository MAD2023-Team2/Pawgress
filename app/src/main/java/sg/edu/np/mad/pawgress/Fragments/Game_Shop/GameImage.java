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
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class GameImage extends AppCompatActivity {
    MyDBHandler myDBHandler;
    ImageView replaceImage_topLeft;
    ImageView replaceImage_topRight;
    ImageView replaceImage_topMiddle;
    TextView backButton;
    Bitmap bitmap;
    InventoryItem inventoryItem;
    UserData user;
    boolean topOnly;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.game_image);

        myDBHandler = new MyDBHandler(this,null,null,1);
        replaceImage_topLeft = findViewById(R.id.replaceImage_topLeft);
        replaceImage_topRight = findViewById(R.id.replaceImage_topRight);
        backButton = findViewById(R.id.backButton);

        Intent receivingEnd = getIntent();
        String pathName = receivingEnd.getStringExtra("pathName");
        topOnly = receivingEnd.getBooleanExtra("topOnly",false);
        bitmap = BitmapFactory.decodeFile(pathName);
        inventoryItem = receivingEnd.getParcelableExtra("item");
        user = receivingEnd.getParcelableExtra("user");

        // set image to image clicked previously and make it transparent
        if (topOnly){ // check if it can only be placed on ceiling
            replaceImage_topMiddle = findViewById(R.id.replaceImage_topMiddle);
            ImageView replaceImage_topMiddle_add = findViewById(R.id.replaceImage_topMiddle_add);
            replaceImage_topMiddle_add.setVisibility(View.VISIBLE);
            replaceImage_topMiddle.setVisibility(View.VISIBLE);
            replaceImage_topMiddle.setImageBitmap(bitmap);

            ImageView replaceImage_topRight_add = findViewById(R.id.replaceImage_topRight_add);
            ImageView replaceImage_topLeft_add = findViewById(R.id.replaceImage_topLeft_add);
            replaceImage_topLeft.setVisibility(View.GONE);
            replaceImage_topRight.setVisibility(View.GONE);
            replaceImage_topRight_add.setVisibility(View.GONE);
            replaceImage_topLeft_add.setVisibility(View.GONE);
        }
        else{
            replaceImage_topLeft.setImageBitmap(bitmap);
            replaceImage_topRight.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        replaceImage_topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHandler.setTopRight(user.getUsername(),inventoryItem.getItemName());

                if( user.getTopRight().equals(inventoryItem.getItemName()) ){
                    Toast.makeText(view.getContext(), "The same item is placed there", Toast.LENGTH_SHORT).show();
                }
                else{
                    user.setTopRight(inventoryItem.getItemName());
                    int inventoryQuantity = inventoryItem.getQuantity();
                    if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                        myDBHandler.removeInventoryItem(inventoryItem,user);
                    }
                    else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                        myDBHandler.updateInventoryQuantity(inventoryItem, user, inventoryQuantity-1);
                    }
                }
                finish();
            }
        });
        replaceImage_topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHandler.setTopLeft(user.getUsername(),inventoryItem.getItemName());

                if( user.getTopLeft().equals(inventoryItem.getItemName()) ){
                    Toast.makeText(view.getContext(), "The same item is placed there", Toast.LENGTH_SHORT).show();
                }
                else{
                    user.setTopLeft(inventoryItem.getItemName());
                    int inventoryQuantity = inventoryItem.getQuantity();
                    if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                        myDBHandler.removeInventoryItem(inventoryItem,user);
                    }
                    else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                        myDBHandler.updateInventoryQuantity(inventoryItem, user, inventoryQuantity-1);
                    }
                }
                finish();
            }
        });

        if (topOnly){
            replaceImage_topMiddle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDBHandler.setTopMiddle(user.getUsername(),inventoryItem.getItemName());

                    if( user.getTopMiddle().equals(inventoryItem.getItemName()) ){
                        Toast.makeText(view.getContext(), "The same item is placed there", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        user.setTopMiddle(inventoryItem.getItemName());
                        int inventoryQuantity = inventoryItem.getQuantity();
                        if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                            myDBHandler.removeInventoryItem(inventoryItem,user);
                        }
                        else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                            myDBHandler.updateInventoryQuantity(inventoryItem, user, inventoryQuantity-1);
                        }
                    }
                    finish();
                }
            });
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Item has been returned to Inventory", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}