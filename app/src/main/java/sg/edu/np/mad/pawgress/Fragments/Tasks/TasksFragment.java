package sg.edu.np.mad.pawgress.Fragments.Tasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

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
    public ArrayList<String> categories;

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

        recyclerView = view.findViewById(R.id.list);
        emptyTaskText = view.findViewById(R.id.emptyTextView);
        filter = view.findViewById(R.id.filter);
        filterCard = view.findViewById(R.id.filterCard);
        emptyCatText = view.findViewById(R.id.emptyCatText);
        clear = view.findViewById(R.id.clear);


        Intent receivingEnd = getActivity().getIntent();
        user = receivingEnd.getParcelableExtra("User");

        refreshTaskRecyclerView();
        FloatingActionButton button = view.findViewById(R.id.addTask);
        BottomSheetDialog createTask = new BottomSheetDialog(getActivity());

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterAdapter fAdapter = new FilterAdapter(user,myDBHandler, getActivity(), TasksFragment.this);
                GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 3);
                filterCard.setLayoutManager(fLayoutManager);
                fAdapter.clear = clear;
                fAdapter.emptyCatText=emptyCatText;
                // if filter button pressed when filter is visible, filter view will be removed
                if (filterCard.getVisibility() == View.VISIBLE || (filterCard.getVisibility()==View.GONE && fAdapter.emptyCatText.getVisibility()==View.VISIBLE)){
                    filterCard.setVisibility(View.GONE);
                    clear.setVisibility(View.GONE);
                    fAdapter.emptyCatText.setVisibility(View.GONE);
                }
                // if filter button is pressed when filter is invisible, filter view will be shown
                else{
                    fAdapter.updateEmptyView();
                    filterCard.setAdapter(fAdapter);
                    if (fAdapter.emptyCatText.getVisibility()==View.VISIBLE){
                        filterCard.setVisibility(View.GONE);
                    }
                    else filterCard.setVisibility(View.VISIBLE);
                }
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categories.clear();
                        FilterAdapter fAdapter = new FilterAdapter(user,myDBHandler, getActivity(), TasksFragment.this);
                        GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 5);
                        filterCard.setLayoutManager(fLayoutManager);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask.setContentView(R.layout.task_create);
                createTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                createTask.setCancelable(true);
                createTask.setDismissWithAnimation(true);

                ImageButton create = createTask.findViewById(R.id.create);
                EditText taskName = createTask.findViewById(R.id.taskName);
                TextView taskCat = createTask.findViewById(R.id.taskCategory);
                TextView chooseDate = createTask.findViewById(R.id.dueDate);
                TextView date = createTask.findViewById(R.id.date);
                Spinner spinner = createTask.findViewById(R.id.priority);
                Spinner chooseCat = createTask.findViewById(R.id.chooseCat);

                NumberPicker ethr = (NumberPicker) createTask.findViewById(R.id.hourPicker);
                ethr.setMaxValue(999);
                ethr.setMinValue(0);
                ethr.setValue(0);
                ethr.setWrapSelectorWheel(true);

                NumberPicker etmin = (NumberPicker) createTask.findViewById(R.id.minPicker);
                etmin.setMaxValue(999);
                etmin.setMinValue(0);
                etmin.setValue(0);
                etmin.setWrapSelectorWheel(true);

                NumberPicker etsec = (NumberPicker) createTask.findViewById(R.id.secPicker);
                etsec.setMaxValue(999);
                etsec.setMinValue(0);
                etsec.setValue(0);
                etsec.setWrapSelectorWheel(true);

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
                chooseDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(null, "ONCLICK DUE DATE");
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                                date.setText(day1 + "/" + month1 + "/" + year);
                                dueDate = date.getText().toString();
                            }
                            },
                                year, month, day);
                        datePickerDialog.show();}
                });
                dueDate = null;
                ArrayList<String> priority = new ArrayList<>();
                priority.add("Normal");
                priority.add("Prioritised");
                adapter = new SpinnerAdapter(getActivity(), priority);
                spinner.setSelection(adapter.getPosition("Normal"));
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String priority = (String)parent.getItemAtPosition(position);
                        if (priority.equals("Prioritised")){
                            taskPriority = 1;
                        }
                        else taskPriority=0;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        taskPriority=0;
                    }
                });
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
                        String cat = (String)parent.getItemAtPosition(position);
                        if (cat.equals("School")){
                             category = "School";
                        }
                        else if (cat.equals("Work")){
                            category = "Work";
                        }
                        else if (cat.equals("Lifestyle")){
                            category = "Lifestyle";
                        }
                        else if (cat.equals("Chores")){
                            category = "Chores";//CHANGE
                        }
                        else category = "Others";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        chooseCat.setSelection(adapter1.getPosition("Others"));
                        category = "Others";
                    }
                });

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = taskName.getText().toString();
                        // do not accept blank task title or category
                        int totalSeconds = (hr * 3600) + (min * 60) + sec;
                        if (taskName.length() > 0 && taskName.getText().charAt(0) != ' ') {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String newDayDate = formatter.format(new Date());
                            Log.w(null, "Task Category: " + category);
                            Task task = new Task(1, name, "In Progress", category ,0, totalSeconds, dueDate, newDayDate, null, null, 0, taskPriority);
                            myDBHandler.addTask(task, user);
                            createTask.dismiss();
                            refreshTaskRecyclerView();
                            Toast.makeText(getActivity(), "Task created", Toast.LENGTH_SHORT).show();
                            if (filterCard.getVisibility()==View.VISIBLE){
                                FilterAdapter fAdapter = new FilterAdapter(user,myDBHandler, getActivity(), TasksFragment.this);
                                GridLayoutManager fLayoutManager = new GridLayoutManager(getActivity(), 5);
                                filterCard.setLayoutManager(fLayoutManager);
                                fAdapter.clear = clear;
                                fAdapter.emptyCatText=emptyCatText;
                                fAdapter.updateEmptyView();
                                filterCard.setAdapter(fAdapter);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "Invalid title", Toast.LENGTH_SHORT).show();
                        }
                    }});
                createTask.show();

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