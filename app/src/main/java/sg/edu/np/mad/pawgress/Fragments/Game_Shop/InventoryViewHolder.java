package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class InventoryViewHolder extends RecyclerView.ViewHolder{
    ImageView itemPic;
    TextView itemName;
    TextView itemQuantity;
    TextView itemQuantity2;
    RelativeLayout itemCard2;
    RelativeLayout itemCard1;
    ImageView itemPic2;
    TextView itemName2;
    public InventoryViewHolder(View itemView){
        super(itemView);
        // First card items
        itemCard1 = itemView.findViewById(R.id.itemCard1);
        itemPic = itemView.findViewById(R.id.itemPic);
        itemQuantity = itemView.findViewById(R.id.itemQuantity);
        itemName = itemView.findViewById(R.id.itemName);

        // Second card items
        itemCard2 = itemView.findViewById(R.id.itemCard2);
        itemPic2 = itemView.findViewById(R.id.itemPic2);
        itemQuantity2 = itemView.findViewById(R.id.itemQuantity2);
        itemName2 = itemView.findViewById(R.id.itemName2);
    }
}
