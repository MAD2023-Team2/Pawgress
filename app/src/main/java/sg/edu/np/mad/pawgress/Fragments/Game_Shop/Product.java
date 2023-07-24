package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

public class Product {
    private String name;
    private int price;
    private String category;

    public Product() {
    }

    public Product(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPriceString() {
        return String.valueOf(price);
    }
    public int getPrice() {
        return price;
    }
}
