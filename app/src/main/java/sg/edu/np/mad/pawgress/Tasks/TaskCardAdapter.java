package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.edu.np.mad.pawgress.DailyLogIn;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class TaskCardAdapter extends RecyclerView.Adapter<TaskCardViewHolder>{
    public TextView emptyTasktext;
    ArrayList<Task> recyclerTaskList;
    UserData user;
    Context context;
    String THIS = "Adapter";
    MyDBHandler mDataBase;
    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    public TaskCardAdapter(UserData userData, MyDBHandler mDatabase, Context context, RecyclerView recyclerView){
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
                if(task.getStatus().equals("In Progress")){
                    count+=1;
                    recyclerTaskList.add(task);
                }
            }
        }
        if (count > 0){ emptyTasktext.setText(""); }
        else emptyTasktext.setText("No tasks to work on for now :)");
    }

    @Override
    public TaskCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new TaskCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.taskcard,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskCardViewHolder holder, int position){
        Log.i(THIS, "onbind");
        Task task = recyclerTaskList.get(position);
        holder.name.setText(task.getTaskName());
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
            holder.card2.setBackgroundColor(Color.parseColor("#B9C498"));
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
                        Log.v(THIS, "Completed task " + task.getTaskName());
                        task.setStatus("Completed");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String newDayDate = formatter.format(new Date());
                        task.setDateComplete(newDayDate);
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);

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
            if (task.getDailyChallenge() == 1){
                Log.w("TaskCardAdapter","new day, previous daily challenge removed");
                task.setStatus("Completed");
                mDataBase.updateTask(task, user.getUsername());
                recyclerTaskList.remove(task);
            }
            else{
                Log.w("TaskCardAdapter","new day, no previous daily challenge to be removed");
            }
            Log.w("TaskCardAdapter","new day, adding new daily challenge");
            Intent intent = new Intent(context, DailyLogIn.class);
            intent.putExtra("User", user);
            intent.putExtra("tab", "home_tab");
            intent.putExtra("new_day",true);
            context.startActivity(intent);
        }
    }
}
