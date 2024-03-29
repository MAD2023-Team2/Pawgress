package sg.edu.np.mad.pawgress.Fragments.Game_Shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewInventory;
    private ShopAdapter shopAdapter;
    private InventoryAdapter inventoryAdapter;
    private TextView currentCurrencyText;
    UserData user;
    private Button cat1;
    private Button cat2;
    private Button cat3;
    private Button cat4;
    private Button cat5;
    private ImageButton filterButton;
    DatabaseReference database;
    List<Product> allProducts, foodProducts, furnitureProducts, plantsProducts, toysProducts;
    List<InventoryItem> inventoryItemList, foodItems, furnitureItems, plantsItems, toysItems;
    MyDBHandler myDBHandler;
    BottomSheetDialog shop;
    public ImageView topLeftPic, topRightPic, topMiddlePic;
    private int queryMode; // 0 is unfiltered, 1 is descending, 2 is ascending
    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        queryMode = 1;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.v("GameFragment","OnResume");
        updateImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_game, container, false);

        myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        // Getting pet picture for user based on selection
        user = myDBHandler.findUser(SaveSharedPreference.getUserName(getActivity()));
        ImageView pet_picture = view.findViewById(R.id.corgi_1);
        if (user.getPetDesign() == 1){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user.getPetDesign() == 2){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user.getPetDesign() == 3){pet_picture.setImageResource(R.drawable.corgi);}
        else if (user.getPetDesign() == 4){pet_picture.setImageResource(R.drawable.capybara);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}

        FloatingActionButton goShop = view.findViewById(R.id.goShop);
        FloatingActionButton goInventory = view.findViewById(R.id.goInventory);
        FloatingActionButton openMenu = view.findViewById(R.id.openMenu);
        FloatingActionButton closeMenu = view.findViewById(R.id.close_menu);
        FloatingActionButton editRoom = view.findViewById(R.id.editRoom);
        RelativeLayout menu = view.findViewById(R.id.menu);
        RelativeLayout secondMenu = view.findViewById(R.id.secondMenu);

        topLeftPic = view.findViewById(R.id.replaceImage_topLeft);
        topRightPic = view.findViewById(R.id.replaceImage_topRight);
        topMiddlePic = view.findViewById(R.id.replaceImage_topMiddle);
        updateImage();
        shop = new BottomSheetDialog(getActivity());

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu.setVisibility(View.GONE);
                menu.setVisibility(View.VISIBLE);
                secondMenu.setVisibility(View.VISIBLE);
            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.GONE);
                secondMenu.setVisibility(View.GONE);
                openMenu.setVisibility(View.VISIBLE);
            }
        });

        editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Goes to adding of item in room
                Intent intent = new Intent(getActivity(), RemoveRoomItem.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        goInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

                shop.setContentView(R.layout.inventory);
                shop.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                shop.setCancelable(true);
                shop.setDismissWithAnimation(true);

                cat1 = shop.findViewById(R.id.cat1);
                cat2 = shop.findViewById(R.id.cat2);
                cat3 = shop.findViewById(R.id.cat3);
                cat4 = shop.findViewById(R.id.cat4);
                cat5 = shop.findViewById(R.id.cat5);

                recyclerViewInventory = shop.findViewById(R.id.inventoryRecyclerView);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerViewInventory.setLayoutManager(mLayoutManager);

                // when first open inventory recycler view, show all inventory from all category
                generateAllCat();

                cat1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // make all category button green and the rest as default button color
                        cat1.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        // make recycler view show all category
                        generateAllCat();
                    }
                });
                cat2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat2.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        foodItems = new ArrayList<>();
                        // Filter the data to get Food items
                        for (InventoryItem item : inventoryItemList) {
                            if (item.getItemCategory().equals("Food")) {
                                foodItems.add(item);
                            }
                        }
                        inventoryAdapter = new InventoryAdapter(foodItems, user, myDBHandler, getContext(), view, shop, getActivity());
                        recyclerViewInventory.setAdapter(inventoryAdapter);
                    }
                });

                cat3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat3.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        furnitureItems = new ArrayList<>();
                        // Filter the data to get Furniture items
                        for (InventoryItem item : inventoryItemList) {
                            if (item.getItemCategory().equals("Furniture")) {
                                furnitureItems.add(item);
                            }
                        }
                        inventoryAdapter = new InventoryAdapter(furnitureItems, user, myDBHandler, getContext(), view, shop, getActivity());
                        recyclerViewInventory.setAdapter(inventoryAdapter);
                    }
                });

                cat4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat4.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        plantsItems = new ArrayList<>();
                        // Filter the data to get Plants items
                        for (InventoryItem item : inventoryItemList) {
                            if (item.getItemCategory().equals("Plants")) {
                                plantsItems.add(item);
                            }
                        }
                        inventoryAdapter = new InventoryAdapter(plantsItems, user, myDBHandler, getContext(), view, shop, getActivity());
                        recyclerViewInventory.setAdapter(inventoryAdapter);
                    }
                });

                cat5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat5.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        toysItems = new ArrayList<>();
                        // Filter the data to get Toys items
                        for (InventoryItem item : inventoryItemList) {
                            if (item.getItemCategory().equals("Toys")) {
                                toysItems.add(item);
                            }
                        }
                        inventoryAdapter = new InventoryAdapter(toysItems, user, myDBHandler, getContext(), view, shop, getActivity());
                        recyclerViewInventory.setAdapter(inventoryAdapter);
                    }
                });

                shop.show();
            }
        });
        goShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the reference to the "ShopItems" node in the Firebase database
                database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ShopItems");

                // Inflate the shop layout and set its size and attributes
                shop.setContentView(R.layout.shop);
                shop.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                shop.setCancelable(true);
                shop.setDismissWithAnimation(true);

                // Initialize UI elements for the shop view
                currentCurrencyText = shop.findViewById(R.id.currentCurrency);
                currentCurrencyText.setText(user.getCurrency() +" Paws");
                filterButton = shop.findViewById(R.id.filterButton);

                // when first open shop recycler view, show all shop items in all categories, unsorted
                // Display all shop items in all categories, unsorted
                generateUnfiltered();

                // Set click listener for the filter button to toggle between different filter modes
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toggle the query mode
                        if (queryMode == 3){
                            queryMode = 0;
                        }

                        // set All Categories button to green
                        cat1.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        if (queryMode == 2) {
                            // Use query to generate the list
                            // Query the users collection and sort by the price in descending order
                            Log.v("GameFragment","Sorted by descending");
                            Query query = database.orderByChild("price");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    allProducts = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                        Product product = dataSnapshot.getValue(Product.class);
                                        allProducts.add(product);
                                    }
                                    Collections.reverse(allProducts);
                                    shopAdapter = new ShopAdapter(allProducts,user,myDBHandler,getContext());
                                    shopAdapter.currentCurrencyText = currentCurrencyText;
                                    recyclerView.setAdapter(shopAdapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // do nth
                                }
                            });

                            filterButton.setImageResource(R.drawable.filter);
                            filterButton.setRotation(0);

                        }
                        else if (queryMode == 1){
                            // Use query to generate the list
                            // Query the users collection and sort by the price in ascending order
                            Log.v("GameFragment","Sorted by ascending");
                            Query query = database.orderByChild("price");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    allProducts = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                        Product product = dataSnapshot.getValue(Product.class);
                                        allProducts.add(product);
                                    }
                                    shopAdapter = new ShopAdapter(allProducts,user,myDBHandler,getContext());
                                    shopAdapter.currentCurrencyText = currentCurrencyText;
                                    recyclerView.setAdapter(shopAdapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // do nth
                                }
                            });

                            filterButton.setImageResource(R.drawable.filter);
                            filterButton.setRotation(180);

                        }
                        else if (queryMode == 0){
                            generateUnfiltered();
                        }
                        queryMode = queryMode + 1;
                    }
                });

                // Set click listeners for category buttons to filter products by category
                cat1 = shop.findViewById(R.id.cat1);
                cat2 = shop.findViewById(R.id.cat2);
                cat3 = shop.findViewById(R.id.cat3);
                cat4 = shop.findViewById(R.id.cat4);
                cat5 = shop.findViewById(R.id.cat5);
                recyclerView = shop.findViewById(R.id.shopRecyclerView);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);

                cat1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat1.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        shopAdapter = new ShopAdapter(allProducts,user,myDBHandler,getContext());
                        shopAdapter.currentCurrencyText = currentCurrencyText;
                        recyclerView.setAdapter(shopAdapter);
                    }
                });
                cat2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat2.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        foodProducts = new ArrayList<>();
                        // Filter the data to get banana items
                        for (Product item : allProducts) {
                            if (item.getCategory().equals("Food")) {
                                foodProducts.add(item);
                            }
                        }
                        shopAdapter = new ShopAdapter(foodProducts,user,myDBHandler,getContext());
                        shopAdapter.currentCurrencyText = currentCurrencyText;
                        recyclerView.setAdapter(shopAdapter);
                    }
                });

                cat3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat3.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        furnitureProducts = new ArrayList<>();
                        // Filter the data to get Furniture items
                        for (Product item : allProducts) {
                            if (item.getCategory().equals("Furniture")) {
                                furnitureProducts.add(item);
                            }
                        }
                        shopAdapter = new ShopAdapter(furnitureProducts,user,myDBHandler,getContext());
                        shopAdapter.currentCurrencyText = currentCurrencyText;
                        recyclerView.setAdapter(shopAdapter);
                    }
                });

                cat4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat4.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat5.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        plantsProducts = new ArrayList<>();
                        // Filter the data to get Plants items
                        for (Product item : allProducts) {
                            if (item.getCategory().equals("Plants")) {
                                plantsProducts.add(item);
                            }
                        }
                        shopAdapter = new ShopAdapter(plantsProducts,user,myDBHandler,getContext());
                        shopAdapter.currentCurrencyText = currentCurrencyText;
                        recyclerView.setAdapter(shopAdapter);
                    }
                });

                cat5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cat5.setBackgroundColor(Color.parseColor("#B9C498"));
                        cat2.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat3.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat4.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        cat1.setBackgroundColor(Color.parseColor("#dcdcdc"));

                        toysProducts = new ArrayList<>();
                        // Filter the data to get Toys items
                        for (Product item : allProducts) {
                            if (item.getCategory().equals("Toys")) {
                                toysProducts.add(item);
                            }
                        }
                        shopAdapter = new ShopAdapter(toysProducts,user,myDBHandler,getContext());
                        shopAdapter.currentCurrencyText = currentCurrencyText;
                        recyclerView.setAdapter(shopAdapter);
                    }
                });

                shop.show();
            }
        });


        // THIS PART LETS THE PET GO SQUEAK SQUEAK
        // Listener for when pet picture is pressed, plays an animation and audio
        pet_picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                YoYo.with(Techniques.Bounce)
                        .duration(1000)
                        .repeat(0)
                        .playOn(pet_picture);
                switch (event.getAction()) {

                    // When pressing down on pet
                    case MotionEvent.ACTION_DOWN:

                        int random = new Random().nextInt(3);
                        MediaPlayer mediaPlayer;

                        // Randomly plays 1 out of 3 sounds for each petType
                        if (user.getPetDesign() == 1){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat1_1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat1_2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat1_3);
                            }
                            mediaPlayer.start();
                        }
                        else if (user.getPetDesign() == 2){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat2_1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat2_2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat2_3);
                            }
                            mediaPlayer.start();
                        }

                        else if (user.getPetDesign() == 3){
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.corgi3);
                            }
                            mediaPlayer.start();
                        }
                        else if (user.getPetDesign() == 4){
                            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.capybara);
                            mediaPlayer.start();
                        }
                        else{
                            if (random == 0){
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.gr1);
                            } else if (random == 1) {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.gr2);
                            }
                            else {
                                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.gr3);
                            }
                            mediaPlayer.start();
                        }

                        // Listener for when audio finishing playing, releases and resets media player to prevent overuse of resources
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            };
                        });

                        // Animated the pet so it squishes when pressed
                        Animation anim = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                1f, 0.85f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim.setFillAfter(true); // Needed to keep the result of the animation
                        anim.setDuration(150);
                        v.startAnimation(anim);

                        break;

                    // When pet is no longer being pressed
                    case MotionEvent.ACTION_UP:

                        // Animated the pet to un-squish
                        Animation anim2 = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                0.85f, 1f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim2.setFillAfter(true); // Needed to keep the result of the animation
                        anim2.setDuration(150);
                        v.startAnimation(anim2);
                        break;
                }

                return true;
            }
        });
        return view;
    }

    private void generateAllCat() {
        // Retrieve the inventory items for the user from the database
        inventoryItemList = myDBHandler.findInventoryList(user);

        // Create an adapter to display the inventory items in the RecyclerView
        inventoryAdapter = new InventoryAdapter(inventoryItemList, user, myDBHandler, getContext(), getView(), shop, getActivity());

        // Set the adapter for the RecyclerView that displays the inventory
        recyclerViewInventory.setAdapter(inventoryAdapter);
    }


    private void generateUnfiltered() {
        Log.v("GameFragment", "not sorted");

        // Add a ValueEventListener to the Firebase database to retrieve all products
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProducts = new ArrayList<>();
                // Loop through the database snapshot to retrieve Product objects and add them to the allProducts list
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    allProducts.add(product);
                }

                // Create a ShopAdapter with allProducts list and set it as the adapter for the RecyclerView
                shopAdapter = new ShopAdapter(allProducts, user, myDBHandler, getContext());
                shopAdapter.currentCurrencyText = currentCurrencyText;
                recyclerView.setAdapter(shopAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nth
            }
        });

        // Set the image resource of the filter button to off
        filterButton.setImageResource(R.drawable.filter_off);
    }


    private void updateImage() {
        Log.v("GameFragment", "Updating the game view");

        // Retrieve the user's data from the database
        user = myDBHandler.findUser(SaveSharedPreference.getUserName(getActivity()));

        // Get the paths of the top-left, top-right, and top-middle images for the user from the database
        String topLeft = myDBHandler.getTopLeft(user.getUsername());
        String topRight = myDBHandler.getTopRight(user.getUsername());
        String topMiddle = myDBHandler.getTopMiddle(user.getUsername());

        // Update the ImageViews for the top-left, top-right, and top-middle images if they exist
        if (!topLeft.equals("")) {
            topLeftPic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topLeft);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topLeftPic.setImageBitmap(bitmap);
        } else {
            topLeftPic.setImageResource(0);
        }

        if (!topRight.equals("")) {
            topRightPic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topRight);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topRightPic.setImageBitmap(bitmap);
        } else {
            topRightPic.setImageResource(0);
        }

        if (!topMiddle.equals("")) {
            topMiddlePic.setVisibility(View.VISIBLE);
            String pathName = myDBHandler.getImageURL(topMiddle);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            topMiddlePic.setImageBitmap(bitmap);
        } else {
            topMiddlePic.setImageResource(0);
        }
    }
}