package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskView extends AppCompatActivity {

    private Button gameButton;
    String View = "Task View";
    String dueDate;
    Task task;
    TextView time;
    TextView targettime;
    UserData user;
    int hr,min,sec;
    int taskPriority, finalTaskPriority;
    SpinnerAdapter adapter;
    MyDBHandler myDBHandler = new MyDBHandler(this, null,null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);

        Log.v(View, "In Task View");
        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        task = receivingEnd.getParcelableExtra("Task");

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton editButton = findViewById(R.id.imageButton3);
        ImageButton deleteButton = findViewById(R.id.imageButton4);
        refreshView(task.getTaskID());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                editTask.setContentView(R.layout.edit_task);
                editTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editTask.setCancelable(true);
                editTask.setDismissWithAnimation(true);
                ImageButton edit = editTask.findViewById(R.id.edit);
                EditText taskName = editTask.findViewById(R.id.taskName);
                EditText taskCat = editTask.findViewById(R.id.taskCategory);
                taskName.setText(task.getTaskName());
                taskCat.setText(task.getCategory());
                TextView chooseDate = editTask.findViewById(R.id.dueDate);
                TextView date = editTask.findViewById(R.id.date);
                date.setText(task.getDueDate());
                dueDate = date.getText().toString();
                Spinner spinner = editTask.findViewById(R.id.priority);


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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TaskView.this, new DatePickerDialog.OnDateSetListener() {
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

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totalSeconds = (hr * 3600) + (min * 60) + sec;
                        // do not accept blank task title or category
                        if (taskName.length() > 0 && taskName.getText().charAt(0) != ' ') {
                            task.setTaskName(taskName.getText().toString());
                            task.setCategory(taskCat.getText().toString());
                            task.setDueDate(dueDate);
                            task.setPriority(finalTaskPriority);
                            task.setTargetSec(totalSeconds);
                            if (taskCat.length()==0 || taskCat.getText().charAt(0) == ' '){
                                task.setCategory("Uncategorised");
                            }
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
                        refreshView(task.getTaskID());
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
                        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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
        time.setText("Current Time Spent: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",hours, minutes, secs));
        if (task.getTimeSpent() > 0) { gameButton.setText("Resume Timer");}

    }

    public void refreshView(int id){
        task = myDBHandler.findTask(id, myDBHandler.findTaskList(user));
        gameButton = findViewById(R.id.to_Game);
        TextView taskName = findViewById(R.id.textView11);
        taskName.setText(task.getTaskName());
        TextView taskCategory = findViewById(R.id.textView10);
        taskCategory.setText(task.getCategory());
        targettime = findViewById(R.id.targettime);
        ImageButton backButton = findViewById(R.id.backButton);

        int tseconds = myDBHandler.getTaskTargetSec(task.getTaskID());
        int thours = tseconds / 3600;
        int tminutes = (tseconds % 3600) / 60;
        int tsecs = tseconds % 60;

        targettime.setText("Target Time: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",thours, tminutes, tsecs));
        time = findViewById(R.id.textView15);
        int seconds = task.getTimeSpent();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        time.setText("Current Time Spent: " + String.format(Locale.getDefault(), "%d Hours %02d Mins %02d Secs",hours, minutes, secs));
        if (task.getTimeSpent() > 0) { gameButton.setText("Resume Timer");}

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskView.this, MainMainMain.class);
                intent.putExtra("User", user);
                intent.putExtra("tab", "tasks_tab");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }
}