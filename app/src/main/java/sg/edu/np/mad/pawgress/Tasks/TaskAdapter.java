package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
    public TaskAdapter(UserData userData, MyDBHandler mDatabase, Context context){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
    }
    @Override
    public int getItemCount() {
        return recyclerTaskList.size();
    }

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
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position){
        Log.i(THIS, "onbind");
        Task task = recyclerTaskList.get(position);
        holder.category.setText(task.getCategory());
        holder.name.setText(task.getTaskName());;
        // view individual task
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
        // edit individual task
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editTask = new Intent(context, EditTask.class);
                Bundle info = new Bundle();
                info.putParcelable("User", user);
                info.putParcelable("Task", task); // send individual task
                editTask.putExtras(info);
                context.startActivity(editTask);
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
                        mDataBase.updateTask(task);
                        recyclerTaskList.remove(task);
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
                        task.setStatus("Completed");
                        mDataBase.updateTask(task);
                        recyclerTaskList.remove(task);
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
    }
}
