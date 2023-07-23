package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sg.edu.np.mad.pawgress.Fragments.Profile.editProfilePassword;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskCardViewHolder;
import sg.edu.np.mad.pawgress.UserData;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {
    private List<Product> allProducts;
    MyDBHandler myDBHandler;
    UserData user;
    Context context;
    public TextView currentCurrencyText;
    public ShopAdapter(List<Product> allProducts, UserData userData, MyDBHandler myDBHandler, Context context){
        this.allProducts = allProducts;
        this.user = userData;
        this.myDBHandler = myDBHandler;
        this.context = context;
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

        holder.itemCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ShopAdapter", shopItem1.getName() + " has been clicked");

                int currentCurrency = user.getCurrency();
                int itemPrice = shopItem1.getPrice();
                if (currentCurrency < itemPrice){ // user has insufficient funds to purchase item
                    Log.v("ShopAdapter", "Unable to buy due to insufficient funds");
                    // do smth
                }
                else{ // user has sufficient funds to purchase item
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Comfirm Purchase of" );
                    builder.setMessage(shopItem1.getName());
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            int finalCurrency = currentCurrency - itemPrice;

                            // update database of final currency left
                            user.setCurrency(finalCurrency);
                            myDBHandler.updateCurrency(user.getUsername(), finalCurrency);

                            // update shop of current final currency left
                            currentCurrencyText.setText(finalCurrency +" Currency");
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
            }
        });

        // Check if the second item exists and set its click listener
        if (shopItem2 != null) {
            Product finalShopItem = shopItem2;
            holder.itemCard2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ShopAdapter", finalShopItem.getName() + " has been clicked");

                    int currentCurrency = user.getCurrency();
                    int itemPrice = finalShopItem.getPrice();
                    if (currentCurrency < itemPrice){ // user has insufficient funds to purchase item
                        Log.v("ShopAdapter", "Unable to buy due to insufficient funds");
                        // do smth
                    }
                    else{ // user has sufficient funds to purchase item
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Comfirm Purchase of" );
                        builder.setMessage(finalShopItem.getName());
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                int finalCurrency = currentCurrency - itemPrice;

                                // update database of final currency left
                                user.setCurrency(finalCurrency);
                                myDBHandler.updateCurrency(user.getUsername(), finalCurrency);

                                // update shop of current final currency left
                                currentCurrencyText.setText(finalCurrency +" Currency");
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
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        // Calculate the total number of items to be displayed, considering pairs
        return (int) Math.ceil(allProducts.size() / 2.0);
    }
}
