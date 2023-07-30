package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryAdapter;
import sg.edu.np.mad.pawgress.Fragments.Game_Shop.InventoryItem;
import sg.edu.np.mad.pawgress.Fragments.Game_Shop.Product;
import sg.edu.np.mad.pawgress.Fragments.Game_Shop.ShopAdapter;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class ViewFriendGame extends AppCompatActivity {

    UserData user;
    MyDBHandler myDBHandler;
    public ImageView topLeftPic, topRightPic, topMiddlePic;
    TextView viewFriendName;
    ImageView viewFriendReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_game);
        myDBHandler = new MyDBHandler(ViewFriendGame.this,null,null,1);
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");

        // Setting friend name text
        viewFriendName = findViewById(R.id.viewFriendName);
        viewFriendName.setText(user.getUsername() + "'s room");

        // Return button
        viewFriendReturn = findViewById(R.id.viewFriendReturn);
        viewFriendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Setting pet design of friend
        ImageView pet_picture = findViewById(R.id.corgi_1_friend);
        if (user.getPetDesign() == 1){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user.getPetDesign() == 2){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user.getPetDesign() == 3){pet_picture.setImageResource(R.drawable.corgi);}
        else if (user.getPetDesign() == 4){pet_picture.setImageResource(R.drawable.capybara);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}

        // Setting accessories that friend has in game room
        topLeftPic = findViewById(R.id.replaceImage_topLeft_Friend);
        topRightPic = findViewById(R.id.replaceImage_topRight_Friend);
        topMiddlePic = findViewById(R.id.replaceImage_topMiddle_Friend);
        updateImage();
    }

    // Update friend's game room
    private void updateImage(){
        Log.v("GameFragment","Updating the game view");

        // Get the paths of the top-left, top-right, and top-middle images from user info
        String topLeft = user.getTopLeft();
        String topRight = user.getTopRight();
        String topMiddle = user.getTopMiddle();

        // Update the ImageViews for the top-left, top-right, and top-middle images if they exist
        if (!topLeft.equals(" ")){
            topLeftPic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topLeft);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topLeftPic.setImageBitmap(bitmap);
        }

        if (!topRight.equals(" ")){
            topRightPic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topRight);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topRightPic.setImageBitmap(bitmap);
        }

        if (!topMiddle.equals(" ")){
            topMiddlePic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topMiddle);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topMiddlePic.setImageBitmap(bitmap);
        }
    }
}