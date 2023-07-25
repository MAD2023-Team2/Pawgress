package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import java.util.ArrayList;

public class InventoryItem {
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
}
