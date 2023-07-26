package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskView extends AppCompatActivity {

    private Button gameButton;
    String View = "Task View";
    String dueDate, category;
    Task task;
    TextView spend, spendHr, spendMin, spendSec, dateText, timeText;
    EditText description;
    UserData user;
    int hr,min,sec;
    int taskPriority, finalTaskPriority;
    RelativeLayout bottom;
    BottomSheetBehavior behavior;
    SpinnerAdapter adapter, adapter1;
    MyDBHandler myDBHandler = new MyDBHandler(this, null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.task_view);

        Log.v(View, "In Task View");
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        task = receivingEnd.getParcelableExtra("Task");

        ImageButton editButton = findViewById(R.id.editButton);
        ImageButton deleteButton = findViewById(R.id.delete);
        ImageButton backButton = findViewById(R.id.close);
        refreshView(task.getTaskID());
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, h:mm");
                String newDayDate = formatter.format(new Date().getTime());
                task.setDateStart(newDayDate);
                Intent Timer = new Intent(TaskView.this, TaskGame.class);
                Bundle info = new Bundle();
                info.putParcelable("User", user);
                info.putParcelable("Task", task);
                Timer.putExtras(info);
                startActivity(Timer);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                BottomSheetDialog editTask = new BottomSheetDialog(TaskView.this);
                editTask.setContentView(R.layout.task_edit);
                editTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editTask.setCancelable(true);
                editTask.setDismissWithAnimation(true);
                ImageButton edit = editTask.findViewById(R.id.edit);
                EditText taskName = editTask.findViewById(R.id.taskName);
                TextView taskCat = editTask.findViewById(R.id.taskCategory);
                taskName.setText(task.getTaskName());
                TextView chooseDate = editTask.findViewById(R.id.dueDate);
                TextView date = editTask.findViewById(R.id.date);
                date.setText(task.getDueDate());
                dueDate = date.getText().toString();
                Spinner spinner = editTask.findViewById(R.id.priority);
                Spinner chooseCat = editTask.findViewById(R.id.chooseCat);


                int seconds = task.getTargetSec();
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                hr = hours;
                min = minutes;
                sec = secs;

                NumberPicker ethr = (NumberPicker) editTask.findViewById(R.id.hourPicker);
                ethr.setMaxValue(999);
                ethr.setMinValue(0);
                ethr.setValue(hours);
                ethr.setWrapSelectorWheel(true);

                NumberPicker etmin = (NumberPicker) editTask.findViewById(R.id.minPicker);
                etmin.setMaxValue(999);
                etmin.setMinValue(0);
                etmin.setValue(minutes);
                etmin.setWrapSelectorWheel(true);

                NumberPicker etsec = (NumberPicker) editTask.findViewById(R.id.secPicker);
                etsec.setMaxValue(999);
                etsec.setMinValue(0);
                etsec.setValue(seconds);
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
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TaskView.this, R.style.DatePicker, new DatePickerDialog.OnDateSetListener() {
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
                        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                        datePickerDialog.show();}
                });
                dueDate = null;
                ArrayList<String> priority = new ArrayList<>();
                priority.add("Normal");
                priority.add("Prioritised");
                adapter = new SpinnerAdapter(TaskView.this, priority);
                spinner.setAdapter(adapter);
                if (task.getPriority()==1){
                    spinner.setSelection(adapter.getPosition("Prioritised"));
                }
                else spinner.setSelection(adapter.getPosition("Normal"));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String priority = (String)parent.getItemAtPosition(position);
                        if (priority.equals("Prioritised")){
                            finalTaskPriority = 1;
                        }
                        else finalTaskPriority = 0;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ArrayList<String> cats = new ArrayList<>();
                cats.add("Others");
                cats.add("School");
                cats.add("Work");
                cats.add("Lifestyle");
                cats.add("TBC");//CHANGE
                adapter1 = new SpinnerAdapter(TaskView.this, cats);
                chooseCat.setSelection(adapter1.getPosition("Others"));
                chooseCat.setAdapter(adapter1);
                if (task.getCategory().equals("School")){
                    chooseCat.setSelection(adapter1.getPosition("School"));
                }
                else if (task.getCategory().equals("Work")){
                    chooseCat.setSelection(adapter1.getPosition("Work"));
                }
                else if (task.getCategory().equals("Lifestyle")){
                    chooseCat.setSelection(adapter1.getPosition("Lifestyle"));
                }
                else if (task.getCategory().equals("Chores")){
                    chooseCat.setSelection(adapter1.getPosition("Chores"));
                }
                else chooseCat.setSelection(adapter1.getPosition("Others"));;
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
                            category = "Chores";
                        }
                        else category = "Others";
                        Log.w(null, category);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        chooseCat.setSelection(adapter1.getPosition("Others"));
                        category = "Others";
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totalSeconds = (hr * 3600) + (min * 60) + sec;
                        // do not accept blank task title or category
                        if (taskName.length() > 0 && taskName.getText().charAt(0) != ' ') {
                            task.setTaskName(taskName.getText().toString());
                            task.setCategory(category);
                            task.setDueDate(dueDate);
                            task.setPriority(finalTaskPriority);
                            task.setTargetSec(totalSeconds);
                            myDBHandler.updateTask(task, user.getUsername());
                            editTask.dismiss();
                            refreshView(task.getTaskID());
                            Toast.makeText(TaskView.this, "Task edited", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(TaskView.this, "Invalid title", Toast.LENGTH_SHORT).show();
                        }
                    }});
                editTask.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskView.this);
                builder.setTitle("Deleting Task: " + task.getTaskName());
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        task.setStatus("Deleted");
                        myDBHandler.updateTask(task, user.getUsername());
                        dialog.dismiss();
                        Intent intent = new Intent(TaskView.this, MainMainMain.class);
                        intent.putExtra("User", user);
                        intent.putExtra("tab", "tasks_tab");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Log.v(null , " Description " + description.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = task.getTaskID();
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
        int seconds = task.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        spendHr = findViewById(R.id.spendHours);
        spendMin = findViewById(R.id.spendMins);
        spendSec = findViewById(R.id.spendSecs);
        spendHr.setText(String.format(Locale.getDefault(), "%02d h",hours));
        spendMin.setText(String.format(Locale.getDefault(), "%02d m",minutes));
        spendSec.setText(String.format(Locale.getDefault(), "%02d s",secs));
        if (task.getTimeSpent() > 0) { gameButton.setText("Resume Timer");}

    }

    public void refreshView(int id){
        // reflect task details and subsequent updates
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));

        // task details
        TextView taskName = findViewById(R.id.name); // name
        taskName.setText(task.getTaskName());
        ImageView background = findViewById(R.id.wallpaper); // background image
        spendHr = findViewById(R.id.spendHours);
        spendMin = findViewById(R.id.spendMins);
        spendSec = findViewById(R.id.spendSecs);
        // setting background depending on task category
        if (task.getCategory().equals("School")){
            background.setImageDrawable(getDrawable(R.drawable.study_background));
        }
        else if (task.getCategory().equals("Work")){
            background.setImageDrawable(getDrawable(R.drawable.work_background));
        }
        else if (task.getCategory().equals("Lifestyle")){
            background.setImageDrawable(getDrawable(R.drawable.lifestyle_background));
            background.setAlpha(0.3f);
        }
        else if (task.getCategory().equals("Chores")){
            background.setImageDrawable(getDrawable(R.drawable.chores_background));
            background.setAlpha(0.2f);
        }
        // getting time spent on this task
        int seconds = task.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        spendHr.setText(String.format(Locale.getDefault(), "%02d h",hours));
        spendMin.setText(String.format(Locale.getDefault(), "%02d m",minutes));
        spendSec.setText(String.format(Locale.getDefault(), "%02d s",secs));


        // start timer button
        gameButton = findViewById(R.id.to_Game);
        if (task.getTimeSpent() > 0) { gameButton.setText("Resume Timer");}

        //bottom sheet elements
        bottom = findViewById(R.id.bottom_sheet);
        behavior=BottomSheetBehavior.from(bottom);
        TextView taskCategory = bottom.findViewById(R.id.category);
        taskCategory.setText(task.getCategory());
        description = bottom.findViewById(R.id.descText); // description(notes) for the task
        description.setText(task.getDescription());
        dateText = bottom.findViewById(R.id.dateText); // due date
        timeText = bottom.findViewById(R.id.timeText); // target time
        // if no due date was set
        if (task.getDueDate() == null){
            dateText.setText("No due date");
        }
        else dateText.setText(task.getDueDate());
        // get target time set
        int tseconds = myDBHandler.getTaskTargetSec(task.getTaskID());
        int thours = tseconds / 3600;
        int tminutes = (tseconds % 3600) / 60;
        int tsecs = tseconds % 60;
        timeText.setText(String.format(Locale.getDefault(), "%02d h %02d m %02d s",thours,tminutes,tsecs));

        // back button
        ImageButton backButton = findViewById(R.id.close);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setDescription(description.getText().toString());
                myDBHandler.updateTask(task, user.getUsername());
                Intent intent = new Intent(TaskView.this, MainMainMain.class);
                intent.putExtra("User", user);
                intent.putExtra("tab", "tasks_tab");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                description.setMinLines(3);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}