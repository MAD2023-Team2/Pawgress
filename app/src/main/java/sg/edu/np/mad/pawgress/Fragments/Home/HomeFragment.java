package sg.edu.np.mad.pawgress.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.Fragments.Profile.ProfileFragment;
import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskCardAdapter;
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
    private TextView emptyTaskText;
    private TextView emptySpaceTextView;
    private ArrayList<Task> taskList;
    String todaysDate;
    String checkDate;


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
        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        // Get user data from the intent
        Intent receivingEnd = getActivity().getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");

        TextView quoteTextView = view.findViewById(R.id.quoteTextView);
        TextView authorTextView = view.findViewById(R.id.authorTextView);
        Log.i(null, ""+ myDBHandler.getQuote(user) + myDBHandler.getAuthor(user));
        quoteTextView.setText(myDBHandler.getQuote(user));
        authorTextView.setText(myDBHandler.getAuthor(user));

        TextView taskLeft = view.findViewById(R.id.taskLeft);
        TextView productiveTime = view.findViewById(R.id.productiveToday);

        taskList=myDBHandler.findTaskList(user);

        int inProgress = 0;
        int totalTime = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterTime = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

        todaysDate = formatter.format(new Date());

        for (Task task:taskList){
            checkDate = task.getDateComplete();
            System.out.println(checkDate);
            if (task.getStatus().equals("In Progress")){
                inProgress++;
            }
            else if (checkDate != null){
                try{
                    Date completedDate;
                    if (checkDate.contains(",")){
                        completedDate = formatterTime.parse(checkDate);
                    }
                    else{
                        completedDate = formatterTime.parse(checkDate);
                    }

                    String completedDateString = formatter.format(completedDate);
                    if (completedDateString.equals(todaysDate)){
                        totalTime += task.getTimeSpent();
                    }
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }

        int hrs = totalTime / 3600;
        int mins = (totalTime % 3600) / 60;
        int secs = totalTime % 60;

        taskLeft.setText("Total task left In Progress: " + inProgress);
        productiveTime.setText("Productive Time: " + String.format("%d hrs %d mins %d secs",hrs,mins,secs));;


        /*
        // Set pet picture based on user's pet design
        ImageView pet_picture = view.findViewById(R.id.homeGame);
        if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user.getPetDesign() == R.drawable.orange_cat){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.corgi);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}
         */

        // ImageButton profilePhoto = view.findViewById((R.id.profile));
        TextView greeting = view.findViewById(R.id.greeting);
        greeting.setText("Hello " + user.getUsername()); // add username
        /*
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new instance of the profilefragment
                ProfileFragment profileFragment = new ProfileFragment();


                // Replace the current fragment with the TasksFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, profileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                // Update the bottomNavigationView selection
                if (getActivity() instanceof MainMainMain) {
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                    bottomNavigationView.setSelectedItemId(R.id.profile_tab);
                }

            }
        });

         */

        // Set up the task list RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.taskcardlist);
        emptyTaskText = view.findViewById(R.id.emptyTextView);
        emptySpaceTextView = view.findViewById(R.id.emptyspace_home);

        try {
            // Try retrieving the user from the intent with new task list
            Intent receivingEnd_2 = getActivity().getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("New Task List");
            TaskCardAdapter mAdapter = new TaskCardAdapter(user_2,myDBHandler, getActivity(),recyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter.emptyTasktext = emptyTaskText;
            mAdapter.updateEmptyView();
            recyclerView.setAdapter(mAdapter);

        } catch (RuntimeException e) {
            // if there is no new task list, get intent from user
            Intent receivingEnd_2 = getActivity().getIntent();
            UserData user_2 = receivingEnd_2.getParcelableExtra("User");
            TaskCardAdapter mAdapter = new TaskCardAdapter(user_2,myDBHandler, getActivity(),recyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            mAdapter.emptyTasktext = emptyTaskText;
            mAdapter.updateEmptyView();
            recyclerView.setAdapter(mAdapter);
        }

        emptyTaskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the TasksFragment
                TasksFragment tasksFragment = new TasksFragment();

                // Pass the user information and tab value to the fragment
                Bundle args = new Bundle();
                args.putParcelable("User", user);
                args.putString("tab", "tasks_tab");
                tasksFragment.setArguments(args);

                // Replace the current fragment with the TasksFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, tasksFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                // Update the bottomNavigationView selection
                if (getActivity() instanceof MainMainMain) {
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                    bottomNavigationView.setSelectedItemId(R.id.tasks_tab);
                }

            }
        });
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Adjust the height of the RecyclerView to fit within the border box.
                // Run this code after all the code get executed
                int maxHeight = emptySpaceTextView.getHeight() - 25;
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                layoutParams.height = Math.min(maxHeight, recyclerView.getHeight());
                recyclerView.setLayoutParams(layoutParams);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String newDayDate = formatter.format(new Date());
                String lastInDate = user.getLastLogInDate();

                if (!lastInDate.equals(newDayDate)){
                    Log.v("HomeFragment","not same day");
                    Intent intent = new Intent(getContext(), DailyLogIn.class);
                    intent.putExtra("User", user);
                    intent.putExtra("tab", "home_tab");
                    intent.putExtra("new_day",true);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

}