package sg.edu.np.mad.pawgress.Fragments.Tasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.Tasks.TaskAdapter;
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
    private TextView emptyTaskText;
    private String dueDate;
    int hr,min,sec;
    UserData user;
    RecyclerView recyclerView;
    MyDBHandler myDBHandler;

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
        Intent receivingEnd = getActivity().getIntent();
        user = receivingEnd.getParcelableExtra("User");
        refreshRecyclerView();
        FloatingActionButton button = view.findViewById(R.id.addTask);
        Dialog createTask = new Dialog(getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask.setContentView(R.layout.create_task);
                createTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                createTask.setCancelable(false);
                createTask.getWindow().getAttributes().windowAnimations = R.style.animation;
                Button create_task = createTask.findViewById(R.id.friendsButton);
                Button discard = createTask.findViewById(R.id.button5);
                TextView date = createTask.findViewById(R.id.btnPickDate);

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
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day1 = String.valueOf(dayOfMonth);
                                String month1 =String.valueOf(monthOfYear + 1);
                                if (day1.length() != 2){
                                    day1 = "0" +day1;
                                }
                                if (month1.length() != 2){
                                    month1 = "0" + month1;
                                }
                                date.setText(day1 + "/" + month1 + "/" + year);
                                dueDate = date.getText().toString();
                            }
                        },
                                year, month, day);
                        datePickerDialog.show();
                    }
                });

                create_task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etname = createTask.findViewById(R.id.editTitle);
                        String name = etname.getText().toString();
                        EditText etcat = createTask.findViewById(R.id.editCat);
                        // do not accept blank task title or category
                        int totalSeconds = (hr * 3600) + (min * 60) + sec;
                        if (etname.length() > 0 && etname.getText().charAt(0) != ' ') {
                            String cat = etcat.getText().toString();
                            if (cat.length()==0 || etcat.getText().charAt(0) == ' '){
                                cat = "Uncategorised";
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String newDayDate = formatter.format(new Date());

                            Task task = new Task(1, name, "In Progress", cat ,0, totalSeconds, dueDate, newDayDate, null, 0);
                            myDBHandler.addTask(task, user);
                            createTask.dismiss();
                            refreshRecyclerView();
                            Toast.makeText(getActivity(), "Task Created", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Invalid Title", Toast.LENGTH_SHORT).show();
                        }
                    }});

                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createTask.dismiss();
                        Toast.makeText(getActivity(), "Task Discarded", Toast.LENGTH_SHORT).show();
                    }
                });

                createTask.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                createTask.show();
                //Intent createTask = new Intent(getActivity(), CreateTask.class);
                //createTask.putExtra("User", user);
                //startActivity(createTask);
                //getActivity().finish();
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

    public void refreshRecyclerView(){
        TaskAdapter mAdapter = new TaskAdapter(user,myDBHandler, getActivity() );
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.emptyTasktext = emptyTaskText;
        mAdapter.updateEmptyView();
        recyclerView.setAdapter(mAdapter);
    }
}