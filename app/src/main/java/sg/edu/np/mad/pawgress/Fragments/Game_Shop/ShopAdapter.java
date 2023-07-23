package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskCardViewHolder;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {
    private List<Product> allProducts;
    public ShopAdapter(List<Product> allProducts){
        this.allProducts = allProducts;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Product shopItem1 = allProducts.get(position * 2); // Get first item
        Product shopItem2 = null;

        // Check if there is a second item to display
        if ((position * 2) + 1 < allProducts.size()) {
            shopItem2 = allProducts.get((position * 2) + 1);
        }

        holder.itemName.setText(shopItem1.getName());
        holder.itemCost.setText(shopItem1.getPriceString());
        holder.itemPic.setImageResource(R.drawable.grey_cat);

        // Check if the second item exists and set its data
        if (shopItem2 != null) {
            holder.itemName2.setText(shopItem2.getName());
            holder.itemCost2.setText(shopItem2.getPriceString());
            holder.itemPic2.setImageResource(R.drawable.grey_cat);
        } else {
            // If there is no second item, hide the second Relative Card
            holder.itemCard2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        // Calculate the total number of items to be displayed, considering pairs
        return (int) Math.ceil(allProducts.size() / 2.0);
    }
}
