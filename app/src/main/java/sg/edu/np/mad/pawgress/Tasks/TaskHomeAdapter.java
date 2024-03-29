package sg.edu.np.mad.pawgress.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;


// adapter for task recycler view shown in home page, only shows daily challenge, prioritised and urgent tasks (due today)
public class TaskHomeAdapter extends RecyclerView.Adapter<TaskHomeViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> recyclerTaskList;
    UserData user;
    Context context;
    String THIS = "Adapter";
    MyDBHandler mDataBase;
    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    ArrayList<Task> updatedList;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    String today = formatter.format(new Date());

    public TaskHomeAdapter(UserData userData, MyDBHandler mDatabase, Context context, RecyclerView recyclerView){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
        this.recyclerView = recyclerView;
    }
    @Override
    public int getItemCount() {
        return recyclerTaskList.size();
    }

    public void updateEmptyView(){
        // calculate how many task are in arraylist
        // if there are no task left, set text to no task
        // if there are task left, set text to empty
        recyclerTaskList = new ArrayList<>();
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            for (Task task : taskList){
                if(task.getStatus().equals("In Progress") && task.getDailyChallenge() == 1){
                    count+=1;
                    recyclerTaskList.add(task);
                }
            }
            for (Task task : taskList){
                if(task.getStatus().equals("In Progress") && task.getPriority() == 1 && !recyclerTaskList.contains(task)){
                    count+=1;
                    recyclerTaskList.add(task);
                }
            }
            for (Task task : taskList){
                if(task.getStatus().equals("In Progress") && task.getDueDate()!=null && task.getDueDate().equals(today) && !recyclerTaskList.contains(task)){
                    count+=1;
                    recyclerTaskList.add(task);
                }
            }
        }
        if (count > 0){ emptyTasktext.setText(""); }
        else emptyTasktext.setText("No tasks to work on for now :)");
    }

    @Override
    public TaskHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new TaskHomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_home,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskHomeViewHolder holder, int position){
        Task task = recyclerTaskList.get(position);
        // time icon to indicate urgent task, default visibility is GONE
        if (task.getDueDate()!=null && task.getDueDate().equals(today)){
            holder.name.setText(task.getTaskName() );
            holder.urgent.setVisibility(View.VISIBLE);
        }
        else holder.name.setText(task.getTaskName());
        // view individual task
        if (task.getDailyChallenge() == 0){
            holder.card2.setOnClickListener(new View.OnClickListener() {
                // if user clicks on task card, send user and task info to taskview
                @Override
                public void onClick(View v) {
                    Intent viewTask = new Intent(context, TaskView.class);
                    Bundle info = new Bundle();
                    info.putParcelable("User", user);
                    info.putParcelable("Task", task);
                    viewTask.putExtras(info);
                    context.startActivity(viewTask);
                }
            });
        }
        else if (task.getDailyChallenge() == 1) {
            Drawable background = context.getDrawable(R.drawable.rounded_corners_challenge);
            background.setTint(Color.parseColor(task.getColorCode().get(0)));
            holder.card2.setBackground(background);
            holder.name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(task.getColorCode().get(0))));
        }

        // complete task
        // if user clicks yes for complete, update database task status as complete
        // update amount of task in tasklist
        // if user click no, set checkbox back to false and do nothing
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Complete: " + task.getTaskName());
                builder.setMessage("Is it really completed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        task.setStatus("Completed");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, h:mm");
                        String newDayDate = formatter.format(new Date().getTime());
                        task.setDateComplete(newDayDate);
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);

                        // updating stats at homepage
                        // hardcoded in case database not updated in time

                        TextView taskLeft = v.getRootView().findViewById(R.id.taskLeft);
                        TextView productiveTime = v.getRootView().findViewById(R.id.productiveToday);
                        updatedList = mDataBase.findTaskList(user);

                        int totalTime = 0;
                        int inProgress = 0;
                        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat formatter3 = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

                        String todaysDate = formatter2.format(new Date());
                        String checkDate;
                        for (Task task:updatedList){
                            checkDate = task.getDateComplete();
                            if (task.getStatus().equals("In Progress")){
                                inProgress++;
                            }
                            else if (checkDate != null){
                                try{
                                    Date completedDate;
                                    if (checkDate.contains(",")){
                                        completedDate = formatter3.parse(checkDate);
                                    }
                                    else{
                                        completedDate = formatter3.parse(checkDate);
                                    }

                                    String completedDateString = formatter2.format(completedDate);
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
                        taskLeft.setText(String.valueOf(inProgress));
                        productiveTime.setText(String.format("%d hrs %d mins %d secs",hrs,mins,secs));;

                        // after completing any task, gain 5 currency
                        // for every 30 minutes spent on the task, an additional 1 currency is added
                        // if timespent > target time, an additional 1 currency is added
                        int additonal_currency;
                        int task_seconds = task.getTimeSpent();
                        int task_minutes = task_seconds / 60;
                        int task_minutes_30 = task_minutes / 30;
                        int current_currency = user.getCurrency();
                        int target_sec = task.getTargetSec();
                        if (task_seconds >= target_sec){
                            additonal_currency = 1;
                        }
                        else{
                            additonal_currency = 0;
                        }
                        int new_currency = current_currency + 5 + task_minutes_30 + additonal_currency;
                        user.setCurrency(new_currency);
                        mDataBase.updateCurrency(user.getUsername(), new_currency);

                        // notify adapter about changes to list
                        notifyItemRemoved(recyclerTaskList.indexOf(task) + 1);
                        notifyItemRangeChanged(recyclerTaskList.indexOf(task), recyclerTaskList.size());
                        notifyDataSetChanged();
                        if (recyclerTaskList.size() == 0) {
                            // if there is no task in tasklist, set recyclerview height as 0
                            // empty task text will fill up the space of recycler view
                            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                            layoutParams.height = Math.min(0, recyclerView.getHeight());
                            recyclerView.setLayoutParams(layoutParams);
                            emptyTasktext.setText("No tasks to work on for now :)");
                        }
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
        holder.complete.setChecked(false);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDayDate = formatter.format(new Date());
        String lastInDate = user.getLastLogInDate();
        if (!lastInDate.equals(newDayDate)) {
            Log.i("TaskHomeAdapter","new day, send to dalylogin");
            Intent intent = new Intent(context, DailyLogIn.class);
            intent.putExtra("User", user);
            intent.putExtra("tab", "home_tab");
            context.startActivity(intent);
        }
    }
}
