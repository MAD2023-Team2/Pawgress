package sg.edu.np.mad.pawgress.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Fragments.Profile.ProfilePage;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskCardAdapter;
import sg.edu.np.mad.pawgress.Tasks.TaskList;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;

        view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.i(null, "Starting App Home Page");
        Intent receivingEnd = getActivity().getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        ImageButton profilePhoto = view.findViewById((R.id.profile));
        TextView greeting = view.findViewById(R.id.greeting);
        ImageView pet_picture = view.findViewById(R.id.homeGame);
        greeting.setText("Hello " + user.getUsername()); // add username
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilePage.class);
                startActivity(intent);
            }
        });

        // add change pet picture code after implementing pet object

        // WALTER - add recycler view code (for now this goes to taskList page)
        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);
        RecyclerView recyclerView = view.findViewById(R.id.taskcardlist);

        try { // after creating new task
            Intent receivingEnd_2 = getActivity().getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("New Task List");
            ArrayList<Task> taskList = user_2.getTaskList();
            TaskCardAdapter mAdapter = new TaskCardAdapter(user_2,myDBHandler, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        } catch (RuntimeException e) {
            // from homepage or tab button
            //ArrayList<Task> taskList = new ArrayList<Task>();
            Intent receivingEnd_2 = getActivity().getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("User");
            ArrayList<Task> taskList = myDBHandler.findTaskList(user_2);
            //  testing
            //taskList.add(new Task(1, "Week 6 Practical", "In Progress", "MAD"));
            TaskCardAdapter mAdapter = new TaskCardAdapter(user_2,myDBHandler, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }

        return view;
    }
}