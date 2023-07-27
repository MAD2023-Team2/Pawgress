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

        viewFriendName = findViewById(R.id.viewFriendName);
        viewFriendName.setText(user.getUsername() + "'s room");

        viewFriendReturn = findViewById(R.id.viewFriendReturn);
        viewFriendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView pet_picture = findViewById(R.id.corgi_1_friend);
        if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user.getPetDesign() == R.drawable.orange_cat){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.corgi);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}

        topLeftPic = findViewById(R.id.replaceImage_topLeft_Friend);
        topRightPic = findViewById(R.id.replaceImage_topRight_Friend);
        topMiddlePic = findViewById(R.id.replaceImage_topMiddle_Friend);
        updateImage();
    }
    private void updateImage(){
        Log.v("GameFragment","Updating the game view");
        String topLeft = user.getTopLeft();
        String topRight = user.getTopRight();
        String topMiddle = user.getTopMiddle();
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