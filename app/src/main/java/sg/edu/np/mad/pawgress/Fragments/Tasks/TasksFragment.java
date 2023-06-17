package sg.edu.np.mad.pawgress.Fragments.Tasks;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.CreateTask;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskAdapter;
import sg.edu.np.mad.pawgress.Tasks.TaskList;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
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
        String TAG = "Task List";
        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        View view;
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        try { // after creating new task
            Log.v(TAG, "starting try");
            Intent receivingEnd = getActivity().getIntent();
            UserData user = receivingEnd.getParcelableExtra("New Task List");
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(user,myDBHandler, getActivity() );
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            FloatingActionButton button = view.findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(getActivity(), CreateTask.class);
                    createTask.putExtra("User", user);
                    startActivity(createTask);
                    getActivity().finish();
                }
            });
        } catch (RuntimeException e) {
            Log.v(TAG, "starting exception");
            Intent receivingEnd = getActivity().getIntent();
            UserData user = receivingEnd.getParcelableExtra("User");
            ArrayList<Task> taskList = myDBHandler.findTaskList(user);
            Log.v(TAG, "List size = " + taskList.size());
            Log.v(TAG, "Starting recyclerview");
            TaskAdapter mAdapter = new TaskAdapter(user,myDBHandler, getActivity() );
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            FloatingActionButton button = view.findViewById(R.id.addTask);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createTask = new Intent(getActivity(), CreateTask.class);
                    createTask.putExtra("User", user);
                    startActivity(createTask);
                    getActivity().finish();
                }
            });
        }

        return view;
    }
}