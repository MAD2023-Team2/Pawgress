package sg.edu.np.mad.pawgress.Fragments.Tasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.FilterAdapter;
import sg.edu.np.mad.pawgress.Tasks.ParentTaskAdapter;
import sg.edu.np.mad.pawgress.Tasks.SpinnerAdapter;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView emptyTaskText, emptyCatText, filter, clear;
    private String dueDate;
    int hr,min,sec;
    int taskPriority = 0;
    UserData user;
    RecyclerView recyclerView, filterCard;
    MyDBHandler myDBHandler;
    SpinnerAdapter adapter, adapter1;
    String category;
    public ArrayList<String> categories = new ArrayList<>();
    ArrayList<Integer> positions = new ArrayList<>();

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

        String TAG = "Task List";
        myDBHandler = new MyDBHandler(getActivity(),null,null,1);
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Calling layout elements
        recyclerView = view.findViewById(R.id.list);
        emptyTaskText = view.findViewById(R.id.emptyTextView);
        filter = view.findViewById(R.id.filter);
        filterCard = view.findViewById(R.id.filterCard);
        emptyCatText = view.findViewById(R.id.emptyCatText);
        clear = view.findViewById(R.id.clear);
        FloatingActionButton add = view.findViewById(R.id.addTask); // button to create task

        // Getting user data
        Intent receivingEnd = getActivity().getIntent();
        user = receivingEnd.getParcelableExtra("User");

        // method to start and display recycler view
        refreshTaskRecyclerView();

        // filter at the top right corner of the screen
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set adapter and layout manager for category tags to filter the main recyclerview of tasks
                FilterAdapter fAdapter = new FilterAdapter(user,myDBHandler, getActivity(), TasksFragment.this, positions);
                GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 5);
                filterCard.setLayoutManager(fLayoutManager);
                fAdapter.positions = positions;
                fAdapter.clear = clear;
                fAdapter.emptyCatText=emptyCatText;
                Log.w("this", "Positions: "+ positions);
                // if filter button pressed when filter is already visible, filter view will collapse
                if (filterCard.getVisibility() == View.VISIBLE || (filterCard.getVisibility()==View.GONE && fAdapter.emptyCatText.getVisibility()==View.VISIBLE)){
                    filterCard.setVisibility(View.GONE);
                    clear.setVisibility(View.GONE);
                    fAdapter.emptyCatText.setVisibility(View.GONE);
                }
                // if filter button is pressed when filter is collapsed, filter view will expanded and displayed
                else{
                    filterCard.setAdapter(fAdapter);
                    fAdapter.updateEmptyView();
                    // if there are no categories to filter (no tasks created), textView will be shown to indicate
                    if (fAdapter.emptyCatText.getVisibility()==View.VISIBLE){
                        filterCard.setVisibility(View.GONE);
                    }
                    else filterCard.setVisibility(View.VISIBLE);
                }
                // clears the filtering
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categories.clear();
                        positions.clear();
                        categories.clear();
                        FilterAdapter fAdapter = new FilterAdapter(user,myDBHandler, getActivity(), TasksFragment.this, positions);
                        GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 5);
                        filterCard.setLayoutManager(fLayoutManager);
                        fAdapter.positions = positions;
                        fAdapter.clear = clear;
                        fAdapter.emptyCatText=emptyCatText;
                        fAdapter.updateEmptyView();
                        filterCard.setAdapter(fAdapter);
                        filterCard.setVisibility(View.VISIBLE);
                        refreshTaskRecyclerView();
                    }
                });
            }
        });
        // creating task
        BottomSheetDialog createTask = new BottomSheetDialog(getActivity());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                // counting created tasks that are active (In Progress), excluding daily challenge
                for (Task task : myDBHandler.findTaskList(user)) {
                    if (task.getStatus().equals("In Progress") && task.getDailyChallenge() != 1) {
                        count += 1;
                    }
                }
                // if there are 20 active tasks that have been created by user, restrict users from creating more
                if (count == 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Maximum tasks reached");
                    builder.setMessage("You cannot create another task. Please complete one task excluding daily challenge");
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                    createTask.dismiss();
                }
                // if there are lesser than 20 active tasks, users can proceed to enter details to create task
                else {
                    createTask.setContentView(R.layout.task_create);
                    createTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    createTask.setCancelable(true);
                    createTask.setDismissWithAnimation(true);

                    // Calling bottom sheet dialog elements
                    ImageButton create = createTask.findViewById(R.id.create); // button to proceed with creating task
                    EditText taskName = createTask.findViewById(R.id.taskName); // task name
                    TextView chooseDate = createTask.findViewById(R.id.dueDate); // for users to select due date using DateDialogPicker(below)
                    TextView date = createTask.findViewById(R.id.date); // text to set chosen due date
                    Spinner spinner = createTask.findViewById(R.id.priority); // spinner to select priority type of the task (Normal/Prioritised)
                    Spinner chooseCat = createTask.findViewById(R.id.chooseCat); // spinner to select category of task (Others by default)

                    // Number Picker for target hour (scroll/input)
                    NumberPicker ethr = (NumberPicker) createTask.findViewById(R.id.hourPicker);
                    ethr.setMaxValue(999);
                    ethr.setMinValue(0);
                    ethr.setValue(0);
                    ethr.setWrapSelectorWheel(true);

                    // Number Picker for target minutes (scroll/input)
                    NumberPicker etmin = (NumberPicker) createTask.findViewById(R.id.minPicker);
                    etmin.setMaxValue(999);
                    etmin.setMinValue(0);
                    etmin.setValue(0);
                    etmin.setWrapSelectorWheel(true);

                    // Number Picker for target seconds (scroll/input)
                    NumberPicker etsec = (NumberPicker) createTask.findViewById(R.id.secPicker);
                    etsec.setMaxValue(999);
                    etsec.setMinValue(0);
                    etsec.setValue(0);
                    etsec.setWrapSelectorWheel(true);

                    // Sets target time variable with hr, min and sec
                    ethr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            hr = i1;
                        }
                    });

                    etmin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            min = i1;
                        }
                    });

                    etsec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            sec = i1;
                        }
                    });

                    // Date Dialog Picker for selecting due date
                    chooseDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.w(null, "ONCLICK DUE DATE");
                            final Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH);
                            int day = c.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePicker, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    String day1 = String.valueOf(dayOfMonth);
                                    String month1 = String.valueOf(monthOfYear + 1);
                                    if (day1.length() != 2) {
                                        day1 = "0" + day1;
                                    }
                                    if (month1.length() != 2) {
                                        month1 = "0" + month1;
                                    }
                                    date.setText(day1 + "/" + month1 + "/" + year); // sets the text to the chosen date
                                    dueDate = date.getText().toString(); // setting the dueDate variable for the task object
                                }
                            },
                                    year, month, day);
                            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()); // minimum date will be day of creation, selection of days before will be restricted
                            datePickerDialog.show();
                        }
                    });
                    dueDate = null; // sets dueDate null by default

                    // Selecting task priority
                    ArrayList<String> priority = new ArrayList<>();
                    priority.add("Normal");
                    priority.add("Prioritised");
                    adapter = new SpinnerAdapter(getActivity(), priority);
                    spinner.setSelection(adapter.getPosition("Normal"));
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String priority = (String) parent.getItemAtPosition(position);
                            if (priority.equals("Prioritised")) {
                                taskPriority = 1;
                            } else taskPriority = 0;
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            taskPriority = 0;
                        }
                    });

                    // Selecting task category
                    ArrayList<String> cats = new ArrayList<>();
                    cats.add("Others");
                    cats.add("School");
                    cats.add("Work");
                    cats.add("Lifestyle");
                    cats.add("Chores");
                    adapter1 = new SpinnerAdapter(getActivity(), cats);
                    chooseCat.setSelection(adapter1.getPosition("Others"));
                    chooseCat.setAdapter(adapter1);
                    chooseCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String cat = (String) parent.getItemAtPosition(position);
                            if (cat.equals("School")) {
                                category = "School";
                            } else if (cat.equals("Work")) {
                                category = "Work";
                            } else if (cat.equals("Lifestyle")) {
                                category = "Lifestyle";
                            } else if (cat.equals("Chores")) {
                                category = "Chores";
                            } else category = "Others";
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            chooseCat.setSelection(adapter1.getPosition("Others"));
                            category = "Others";
                        }
                    });

                    // creates the task on click (basically a confirm button)
                    create.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int totalSeconds = (hr * 3600) + (min * 60) + sec; // target time for task in seconds

                            // does not allow for task name to be null
                            String name = taskName.getText().toString().trim(); // removes any space in front of input text
                            if (name.length() > 0) {
                                // getting date of current day
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String newDayDate = formatter.format(new Date());
                                // creating task object
                                Task task = new Task(1, name, "In Progress", category, 0, totalSeconds, dueDate, newDayDate, null, null, 0, taskPriority, null);
                                // adds the task object into the local database
                                myDBHandler.addTask(task, user);
                                createTask.dismiss();
                                refreshTaskRecyclerView();
                                Toast.makeText(getActivity(), "Task created", Toast.LENGTH_SHORT).show();
                                // if filter is currently expanded, adds new category if there are
                                if (filterCard.getVisibility() == View.VISIBLE) {
                                    FilterAdapter fAdapter = new FilterAdapter(user, myDBHandler, getActivity(), TasksFragment.this, positions);
                                    GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 5);
                                    filterCard.setLayoutManager(fLayoutManager);
                                    fAdapter.positions = positions;
                                    fAdapter.clear = clear;
                                    fAdapter.emptyCatText = emptyCatText;
                                    fAdapter.updateEmptyView();
                                    filterCard.setAdapter(fAdapter);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Invalid title", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    createTask.show();
                }
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        String lastInDate = user.getLastLogInDate();

        if (!lastInDate.equals(newDayDate)){
            Log.v("TaskFragment","not same day");
            Intent intent = new Intent(getContext(), DailyLogIn.class);
            intent.putExtra("User", user);
            intent.putExtra("tab", "tasks_tab");
            intent.putExtra("new_day",true);
            startActivity(intent);
        }
        return view;
    }

    public void refreshTaskRecyclerView(){
        ParentTaskAdapter mAdapter = new ParentTaskAdapter(user,myDBHandler, getActivity(), TasksFragment.this, categories);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.emptyTasktext = emptyTaskText;
        if (categories == null || categories.size()==0){
            mAdapter.updateEmptyView();
        }
        else {
            mAdapter.updateFilteredView();
        }
        recyclerView.setAdapter(mAdapter);
    }
}