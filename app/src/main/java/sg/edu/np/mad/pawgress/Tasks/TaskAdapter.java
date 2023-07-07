package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> taskList;
    ArrayList<Task> recyclerTaskList;
    Context context;
    String THIS = "Adapter";
    UserData user;
    MyDBHandler mDataBase;
    int hr, min, sec;
    String dueDate;
    public TaskAdapter(UserData userData, MyDBHandler mDatabase, Context context){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
    }
    @NonNull
    @Override
    public int getItemViewType(int position){
        Task task = recyclerTaskList.get(position);
        int dailyChallenge = task.getDailyChallenge();
        if (dailyChallenge == 1) { return 1; }
        else { return 0;}
    }
    @Override
    public int getItemCount() {
        return recyclerTaskList.size();
    }

    // shows that there are currently no tasks to work on if there are no tasks in progress found in the database for this user
    public void updateEmptyView(){
        recyclerTaskList = new ArrayList<>();
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            for (Task task : taskList){
                if(task.getStatus().equals("In Progress")){
                    count+=1;
                    recyclerTaskList.add(task);
                }
            }
        }
        if (count > 0){ emptyTasktext.setVisibility(INVISIBLE); }
        else emptyTasktext.setVisibility(VISIBLE);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 1){
            return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge,parent, false));
        }
        else{
            return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent, false));
        }
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Log.i(THIS, "onbind");
        Task task = recyclerTaskList.get(position);
        Log.v("taskadapter", String.valueOf(task.getDailyChallenge()));
        holder.category.setText(task.getCategory());
        holder.name.setText(task.getTaskName());
        if (task.getDueDate() != null){
            holder.duedate.setText(task.getDueDate());
        }
        else holder.duedate.setVisibility(INVISIBLE);
        // view individual task
        if (task.getDailyChallenge() == 0){
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    Bundle info = new Bundle();
                    info.putParcelable("User", user);
                    info.putParcelable("Task", task); // send individual task
                    viewTask.putExtras(info);
                    context.startActivity(viewTask);
                }
            });
        }
        // edit individual task
        Dialog editTask = new Dialog(context);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask.setContentView(R.layout.edit_task);
                editTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editTask.setCancelable(false);
                editTask.getWindow().getAttributes().windowAnimations = R.style.animation;
                Button edit_Task = editTask.findViewById(R.id.button12);
                Button discard = editTask.findViewById(R.id.button13);
                TextView date = editTask.findViewById(R.id.textView24);
                if (task.getDueDate() != null){
                    date.setText(task.getDueDate());
                }
                else date.setText("no due date set");

                EditText etname = editTask.findViewById(R.id.editTextText);
                etname.setText(task.getTaskName());
                EditText etcat = editTask.findViewById(R.id.editTextText2);
                etcat.setText(task.getCategory());

                int seconds = task.getTargetSec();
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                hr = hours;
                min = minutes;
                sec = secs;
                NumberPicker ethr = (NumberPicker) editTask.findViewById(R.id.hourPicker3);
                ethr.setMaxValue(999);
                ethr.setMinValue(0);
                ethr.setValue(hours);
                ethr.setWrapSelectorWheel(true);

                NumberPicker etmin = (NumberPicker) editTask.findViewById(R.id.minPicker3);
                etmin.setMaxValue(999);
                etmin.setMinValue(0);
                etmin.setValue(minutes);
                etmin.setWrapSelectorWheel(true);

                NumberPicker etsec = (NumberPicker) editTask.findViewById(R.id.secPicker3);
                etsec.setMaxValue(999);
                etsec.setMinValue(0);
                etsec.setValue(secs);
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

                edit_Task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totalSeconds = (hr * 3600) + (min * 60) + sec;
                        // do not accept blank task name and category
                        if (etname.length() > 0 && etname.getText().charAt(0) != ' ') {
                            task.setTaskName(etname.getText().toString());
                            task.setStatus("In Progress");
                            task.setCategory(etcat.getText().toString());
                            if (etcat.length() == 0 || etcat.getText().charAt(0) == ' ') {
                                task.setCategory("Uncategorised");
                            }
                            task.setTargetSec(totalSeconds);
                            task.setDueDate(dueDate);
                            mDataBase.updateTask(task, user.getUsername());
                            editTask.dismiss();
                            Toast.makeText(context, "Edited Task", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Invalid Title", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTask.dismiss();
                        Toast.makeText(context, "Edit Discarded", Toast.LENGTH_SHORT).show();
                    }
                });

                editTask.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        notifyDataSetChanged();
                    }
                });
                editTask.show();
                //Intent editTask = new Intent(context, EditTask.class);
                //Bundle info = new Bundle();
                //info.putParcelable("User", user);
                //info.putParcelable("Task", task); // send individual task
                //.putExtras(info);
                //context.startActivity(editTask);
            }
        });
        // delete individual task
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Deleting Task: " + task.getTaskName());
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Log.v(THIS, "Deleting task " + task.getTaskName());
                        task.setStatus("Deleted");
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);
                        // notify adapter about changes to list
                        notifyItemRemoved(recyclerTaskList.indexOf(task) + 1);
                        notifyItemRangeChanged(recyclerTaskList.indexOf(task), recyclerTaskList.size());
                        notifyDataSetChanged();
                        if (recyclerTaskList.size() == 0) emptyTasktext.setVisibility(VISIBLE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        // complete task
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Complete: " + task.getTaskName());
                builder.setMessage("Is it really completed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Log.v(THIS, "Completed task " + task.getTaskName());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String newDayDate = formatter.format(new Date());
                        task.setStatus("Completed");
                        task.setDateComplete(newDayDate);
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);
                        // notify adapter about changes to list
                        notifyItemRemoved(recyclerTaskList.indexOf(task) + 1);
                        notifyItemRangeChanged(recyclerTaskList.indexOf(task), recyclerTaskList.size());
                        notifyDataSetChanged();
                        if (recyclerTaskList.size() == 0) emptyTasktext.setVisibility(VISIBLE);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        holder.complete.setChecked(false);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.complete.setChecked(false); // ensures all checkboxes are not checked before clicking it


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        String lastInDate = user.getLastLogInDate();
        if (!lastInDate.equals(newDayDate)) {
            if (task.getDailyChallenge() == 1){
                Log.i("TaskCardAdapter","new day, previous daily challenge removed");
                task.setStatus("Completed");
                mDataBase.updateTask(task, user.getUsername());
                recyclerTaskList.remove(task);
            }
            else{
                Log.i("TaskCardAdapter","new day, no previous daily challenge removed");
            }
            Log.i("TaskCardAdapter","new day, add new challenge");
            Intent intent = new Intent(context, DailyLogIn.class);
            intent.putExtra("User", user);
            intent.putExtra("tab", "home_tab");
            intent.putExtra("new_day",true);
            context.startActivity(intent);
        }
    }
}
