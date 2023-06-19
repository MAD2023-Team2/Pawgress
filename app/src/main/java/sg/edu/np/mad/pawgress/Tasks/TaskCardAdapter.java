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
        holder.name.setText(task.getTaskName());;
        // view individual task
        holder.card2.setOnClickListener(new View.OnClickListener() {
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
                        mDataBase.updateTask(task, user.getUsername());
                        recyclerTaskList.remove(task);
                        notifyItemRemoved(recyclerTaskList.indexOf(task) + 1);
                        notifyItemRangeChanged(recyclerTaskList.indexOf(task), recyclerTaskList.size());
                        if (recyclerTaskList.size() == 0) {
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
    }
}
