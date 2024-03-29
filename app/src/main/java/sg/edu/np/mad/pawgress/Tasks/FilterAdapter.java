package sg.edu.np.mad.pawgress.Tasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;
public class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder>{
    public TextView emptyCatText, clear;
    ArrayList<Task> taskList;
    ArrayList<String> categoryList;
    Context context;
    Boolean select = false;
    UserData user;
    MyDBHandler mDataBase;
    TasksFragment fragment;
    public ArrayList<Integer> positions;
    public FilterAdapter(UserData userData, MyDBHandler mDatabase, Context context, TasksFragment fragment, ArrayList<Integer> positions){
        this.user = userData;
        this.mDataBase = mDatabase;
        this.context = context;
        this.taskList = mDataBase.findTaskList(user);
        this.fragment = fragment;
        this.positions = positions;
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateEmptyView(){
        // add the categories to a list for display
        categoryList = new ArrayList<>();
        int count = 0;
        if (taskList.size() == 0){
            count = -1;
        }
        else{
            // loops through all tasks in the taskList saved to the user in database
            for (Task task : taskList){
                // if the category list does not contain the task's category and the task is in progress
                if (!categoryList.contains(task.getCategory()) && task.getStatus().equals("In Progress") && !task.getCategory().equals("Daily Challenge"))
                {
                    // if the task is a daily challenge, it adds this into the list first
                    // so that daily challenge will always be on top
                    categoryList.add(task.getCategory());
                    count+=1;
                }
            }
        }
        // shows that there are currently no categories if there are no tasks created in the database for this user
        if (count > 0){ emptyCatText.setVisibility(INVISIBLE); clear.setVisibility(VISIBLE); }
        else {emptyCatText.setVisibility(VISIBLE); clear.setVisibility(INVISIBLE);}
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new FilterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_filters, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        String category = categoryList.get(position);
        select = false;
        holder.name.setText(category);
        Drawable background = context.getDrawable(R.drawable.field_background);
        // set tags according to where users left it previously
        for (int i : positions){
            if (i == holder.getAdapterPosition()){
                background.setTint(Color.parseColor("#B9C498"));
                holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_clear_24,0);
                holder.name.setBackground(background);
                select = true;
                if (!fragment.categories.contains(category)){
                    fragment.categories.add(category);
                }
            }
        }
        // clicking on the filter tags
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // beginning of filter where none selected
                if (select == false && positions.isEmpty()){
                    background.setTint(Color.parseColor("#B9C498")); // changes it to green to indicate selected
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_clear_24,0);
                    holder.name.setBackground(background);
                    fragment.categories.add(category);
                    select = true;
                    positions.add(holder.getAdapterPosition());
                    fragment.refreshTaskRecyclerView();
                }
                // if this category is selected, so unselects it now
                else if(positions.contains(holder.getAdapterPosition())){
                    background.setTint(Color.parseColor("#EFDFCE")); // changes it back to beige to indicate unselected
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    holder.name.setBackground(background);
                    fragment.categories.remove(category);
                    positions.remove(positions.indexOf(holder.getAdapterPosition()));
                    fragment.refreshTaskRecyclerView();
                }
                // if this category has not been selected
                else if(!positions.contains(holder.getAdapterPosition())){
                    background.setTint(Color.parseColor("#B9C498")); // changes it to green to indicate selected
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_clear_24,0);
                    holder.name.setBackground(background);
                    fragment.categories.add(category);
                    select = true;
                    positions.add(holder.getAdapterPosition());
                    fragment.refreshTaskRecyclerView();
                }
                else Log.w("Filter Adapter On Bind", "Else");
            }
        });
    }
}
