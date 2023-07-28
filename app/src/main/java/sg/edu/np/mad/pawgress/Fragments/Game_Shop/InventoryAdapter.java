package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sg.edu.np.mad.pawgress.LoginPage;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder>{
    private List<InventoryItem> inventoryItemList;
    MyDBHandler myDBHandler;
    UserData user;
    Context context;
    View gameView;
    BottomSheetDialog shop;
    Activity activity;

    public InventoryAdapter(List<InventoryItem> inventoryItemList, UserData userData,
                            MyDBHandler myDBHandler, Context context, View gameView, BottomSheetDialog shop,
                            Activity activity){
        this.inventoryItemList = inventoryItemList;
        this.user = userData;
        this.myDBHandler = myDBHandler;
        this.context = context;
        this.gameView = gameView;
        this.shop = shop;
        this.activity = activity;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem item1 = inventoryItemList.get(position * 2); // Get first item
        InventoryItem item2 = null;

        // Check if there is a second item to display
        if ((position * 2) + 1 < inventoryItemList.size()) {
            item2 = inventoryItemList.get((position * 2) + 1);
        }

        // handle first item, setting image and name etc.
        holder.itemName.setText(item1.getItemName());
        holder.itemQuantity.setText(item1.getQuantity() + "x");
        String pathName = myDBHandler.getImageURL(item1.getItemName());
        if (pathName.equals("")){
            holder.itemPic.setImageResource(R.drawable.grey_cat);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            holder.itemPic.setImageBitmap(bitmap);
        }

        // Check if the second item exists and set its data
        if (item2 != null) {
            // handle second item, setting image and name etc.
            holder.itemName2.setText(item2.getItemName());
            holder.itemQuantity2.setText(item2.getQuantity() + "x");
            String pathName2 = myDBHandler.getImageURL(item2.getItemName());
            if (pathName2.equals("")){
                holder.itemPic2.setImageResource(R.drawable.grey_cat);
            }
            else{
                Bitmap bitmap = BitmapFactory.decodeFile(pathName2);
                holder.itemPic2.setImageBitmap(bitmap);
            }

        } else {
            // If there is no second item, hide the second Relative Card
            holder.itemCard2.setVisibility(View.GONE);
        }

        holder.itemCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("InventoryAdapter", item1.getItemName() + " has been clicked");
                handleOnClick(item1);
            }
        });

        // Check if the second item exists and set its click listener
        if (item2 != null) {
            InventoryItem finalItem = item2;
            holder.itemCard2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("InventoryAdapter", finalItem.getItemName() + " has been clicked");
                    handleOnClick(finalItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // Calculate the total number of items to be displayed, considering pairs
        return (int) Math.ceil(inventoryItemList.size() / 2.0);
    }

    public void handleOnClick(InventoryItem finalItem){
        if (!finalItem.getItemCategory().equals("Food")){ // as long as it is not a food, it can be added to the room
            // Goes to adding of item in room
            Intent intent = new Intent(activity, GameImage.class);
            String pathName = myDBHandler.getImageURL(finalItem.getItemName());
            intent.putExtra("pathName",pathName);
            intent.putExtra("item", finalItem);
            intent.putExtra("user",user);
            intent.putExtra("topOnly", (finalItem.getItemName().equals("Chandelier")||finalItem.getItemName().equals("Disco Ball")));
            activity.startActivity(intent);
            shop.cancel();
        }
        else{ // do some cool trick

            Toast.makeText(context, finalItem.getItemName() + " has been consumed.", Toast.LENGTH_SHORT).show();
            int inventoryQuantity = finalItem.getQuantity();
            if (inventoryQuantity == 1){ // if used all of the item, remove from the inventory
                myDBHandler.removeInventoryItem(finalItem,user);
            }
            else{ // there is still at least 1 item of the same item in the inventory, update the new quantity of the item
                myDBHandler.updateInventoryQuantity(finalItem, user, inventoryQuantity-1);
            }

            // make pet do cool back to back dancing animation trick
            shop.cancel();
            ImageView pet_picture = gameView.findViewById(R.id.corgi_1);
            YoYo.with(Techniques.Swing)
                    .duration(1000)
                    .repeat(2)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            // The first animation (RotateIn) has ended, start the second animation (Tada)
                            YoYo.with(Techniques.Tada)
                                    .duration(1000)
                                    .repeat(1)
                                    .onEnd(new YoYo.AnimatorCallback() {
                                        @Override
                                        public void call(Animator animator) {
                                            // The second animation (Tada) has ended, start the third animation (ZoomIn)
                                            YoYo.with(Techniques.Wave)
                                                    .duration(1000)
                                                    .repeat(1)
                                                    .onEnd(new YoYo.AnimatorCallback() {
                                                        @Override
                                                        public void call(Animator animator) {
                                                            // The third animation (ZoomIn) has ended, start the fourth animation (Shake)
                                                            YoYo.with(Techniques.Shake)
                                                                    .duration(1000)
                                                                    .repeat(1)
                                                                    .onEnd(new YoYo.AnimatorCallback() {
                                                                        @Override
                                                                        public void call(Animator animator) {
                                                                            // The fourth animation (Shake) has ended, start the fifth animation (FadeIn)
                                                                            YoYo.with(Techniques.Bounce)
                                                                                    .duration(1000)
                                                                                    .repeat(1)
                                                                                    .playOn(pet_picture);
                                                                        }
                                                                    })
                                                                    .playOn(pet_picture);
                                                        }
                                                    })
                                                    .playOn(pet_picture);
                                        }
                                    })
                                    .playOn(pet_picture);
                        }
                    })
                    .playOn(pet_picture);
        }
    }
}
