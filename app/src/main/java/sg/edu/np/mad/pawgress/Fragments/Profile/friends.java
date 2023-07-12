package sg.edu.np.mad.pawgress.Fragments.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class friends extends AppCompatActivity {

    UserData user;
    FloatingActionButton addFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        setContentView(R.layout.activity_friends);

        RecyclerView recyclerView = findViewById(R.id.friendsRecycler);
        FriendsAdapter mAdapter =
                new FriendsAdapter(this, user, myDBHandler);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        addFriend = findViewById(R.id.addFriend);
        Dialog searchFriend = new Dialog(this);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}