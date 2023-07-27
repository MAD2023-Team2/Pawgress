package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class InventoryItem implements Parcelable{
    private String itemName;
    private String itemCategory;
    private int quantity;
    public InventoryItem(){

    }
    public InventoryItem(String itemName, int quantity, String itemCategory){
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }
    public String getItemCategory() {
        return itemCategory;
    }
    public int getQuantity() { return quantity; }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemCategory);
        dest.writeInt(quantity);
    }

    protected InventoryItem(Parcel in) {
        itemName = in.readString();
        itemCategory = in.readString();
        quantity = in.readInt();
    }

    public static final Parcelable.Creator<InventoryItem> CREATOR = new Parcelable.Creator<InventoryItem>() {
        @Override
        public InventoryItem createFromParcel(Parcel in) {
            return new InventoryItem(in);
        }

        @Override
        public InventoryItem[] newArray(int size) {
            return new InventoryItem[size];
        }
    };
}
