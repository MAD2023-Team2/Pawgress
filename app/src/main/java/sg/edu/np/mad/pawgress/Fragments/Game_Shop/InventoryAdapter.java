package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder>{
    private List<InventoryItem> inventoryItemList;
    MyDBHandler myDBHandler;
    UserData user;
    Context context;

    public InventoryAdapter(List<InventoryItem> inventoryItemList, UserData userData, MyDBHandler myDBHandler, Context context){
        this.inventoryItemList = inventoryItemList;
        this.user = userData;
        this.myDBHandler = myDBHandler;
        this.context = context;
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

        holder.itemName.setText(item1.getItemName());
        holder.itemQuantity.setText(item1.getQuantity() + "x");
        holder.itemPic.setImageResource(R.drawable.grey_cat);

        // Check if the second item exists and set its data
        if (item2 != null) {
            holder.itemName2.setText(item2.getItemName());
            holder.itemQuantity2.setText(item2.getQuantity() + "x");
            holder.itemPic2.setImageResource(R.drawable.grey_cat);

        } else {
            // If there is no second item, hide the second Relative Card
            holder.itemCard2.setVisibility(View.GONE);
        }

        holder.itemCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("InventoryAdapter", item1.getItemName() + " has been clicked");
            }
        });

        // Check if the second item exists and set its click listener
        if (item2 != null) {
            InventoryItem finalItem = item2;
            holder.itemCard2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("InventoryAdapter", finalItem.getItemName() + " has been clicked");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        // Calculate the total number of items to be displayed, considering pairs
        return (int) Math.ceil(inventoryItemList.size() / 2.0);
    }
}
