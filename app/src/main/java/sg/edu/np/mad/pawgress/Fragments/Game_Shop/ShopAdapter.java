package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
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

        // handle first item, setting image and name etc.
        holder.itemName.setText(shopItem1.getName());
        holder.itemCost.setText(shopItem1.getPriceString() + " Paws");
        String pathName = myDBHandler.getImageURL(shopItem1.getName());
        if (pathName.equals("")){
            holder.itemPic.setImageResource(R.drawable.grey_cat);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            holder.itemPic.setImageBitmap(bitmap);
        }

        // Check if the second item exists and set its data
        if (shopItem2 != null) {
            // handle second item, setting image and name etc.
            holder.itemName2.setText(shopItem2.getName());
            holder.itemCost2.setText(shopItem2.getPriceString() + " Paws");
            String pathName2 = myDBHandler.getImageURL(shopItem2.getName());
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
                Log.v("ShopAdapter", shopItem1.getName() + " has been clicked");

                int currentCurrency = user.getCurrency();
                int itemPrice = shopItem1.getPrice();
                if (currentCurrency < itemPrice){ // user has insufficient funds to purchase item
                    Toast.makeText(v.getContext(), "Unable to buy due to insufficient funds", Toast.LENGTH_SHORT).show();
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
                            currentCurrencyText.setText(finalCurrency +" Paws");

                            // add bought item into user inventory
                            ArrayList<InventoryItem> inventoryItems = myDBHandler.findInventoryList(user);
                            boolean hasItem = false;
                            for (InventoryItem item: inventoryItems){
                                // if item exist in the current inventory, update quantity of item to +1
                                if(item.getItemName().equals(shopItem1.getName())){
                                    hasItem = true;
                                    myDBHandler.updateInventoryQuantity(item, user, item.getQuantity()+1);
                                }
                            }
                            // if item dosent exist in the current inventory, add item to inventory
                            if (!hasItem){
                                InventoryItem inventoryItem1 = new InventoryItem(shopItem1.getName(), 1, shopItem1.getCategory());
                                myDBHandler.addInventoryItem(inventoryItem1, user);
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.dismiss(); // do nth
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
                        Toast.makeText(v.getContext(), "Unable to buy due to insufficient funds", Toast.LENGTH_SHORT).show();
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
                                currentCurrencyText.setText(finalCurrency +" Paws");

                                // add bought item into user inventory
                                ArrayList<InventoryItem> inventoryItems = myDBHandler.findInventoryList(user);
                                boolean hasItem = false;
                                for (InventoryItem item: inventoryItems){
                                    // if item exist in the current inventory, update quantity of item to +1
                                    if(item.getItemName().equals(finalShopItem.getName())){
                                        hasItem = true;
                                        myDBHandler.updateInventoryQuantity(item, user, item.getQuantity()+1);
                                    }
                                }
                                // if item dosent exist in the current inventory, add item to inventory
                                if (!hasItem){
                                    InventoryItem inventoryItem1 = new InventoryItem(finalShopItem.getName(), 1, finalShopItem.getCategory());
                                    myDBHandler.addInventoryItem(inventoryItem1, user);
                                }
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.dismiss(); // do nth
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
