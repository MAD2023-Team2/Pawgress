package sg.edu.np.mad.pawgress.Analytics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import sg.edu.np.mad.pawgress.R;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<String> categories;
    private List<Integer> timeSpent;
    private List<Integer> completedTasks;
    private List<Integer> avgTime;


    public TableAdapter(List<String> categories, List<Integer> completedTasks, List<Integer> timeSpent, List<Integer> avgTime) {
        this.categories = categories;
        this.completedTasks = completedTasks;
        this.timeSpent = timeSpent;
        this.avgTime = avgTime;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pie_chart_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HashMap<String, Integer> categoryColorMap = new HashMap<>();
        categoryColorMap.put("School", Color.parseColor("#1f78b4")); // Blue
        categoryColorMap.put("Work", Color.parseColor("#ff7f00")); // Orange
        categoryColorMap.put("Lifestyle", Color.parseColor("#33a02c")); // Green
        categoryColorMap.put("Chores", Color.parseColor("#e31a1c")); // Red
        categoryColorMap.put("Others", Color.parseColor("#6a3d9a")); // Purple

        String category = categories.get(position);
        int color = Color.BLACK;
        if (category!=null){
            Integer categoryColor = categoryColorMap.get(category);
            if (categoryColor != null){
                color = categoryColor;
            }
        }

        int time = timeSpent.get(position);
        int tasks = completedTasks.get(position);
        int avg = avgTime.get(position);

        if (position == 0){
            holder.textCategory.setText("       Category");
            holder.textCompletedTasks.setText("  No. of\n   Task");
            holder.textTimeSpent.setText("    Time\n    Spent");
            holder.textAvgTime.setText("   Avg\n   Time");
            holder.colorCircle.setVisibility(View.GONE);
        }
        else{
            holder.textCategory.setText(category);
            holder.textCompletedTasks.setText(String.valueOf(tasks));
            String tTime = formatTime(time);
            holder.textTimeSpent.setText(tTime);
            String aTime = formatTime(avg);
            holder.textAvgTime.setText(aTime);


            GradientDrawable circleDrawable = new GradientDrawable();
            circleDrawable.setShape(GradientDrawable.OVAL);
            circleDrawable.setColor(color);
            holder.colorCircle.setBackground(circleDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position){
        return position == 0 ? 1 : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory, textTimeSpent, textCompletedTasks, textAvgTime;
        ImageView colorCircle;

        ViewHolder(View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textCategory);
            textCompletedTasks = itemView.findViewById(R.id.textCompletedTasks);
            textTimeSpent = itemView.findViewById(R.id.textTimeSpent);
            textAvgTime = itemView.findViewById(R.id.textAvgTime);
            colorCircle = itemView.findViewById(R.id.colorCircle);

            Typeface typeface = Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL);
            textCategory.setTypeface(typeface);
            textTimeSpent.setTypeface(typeface);
            textCompletedTasks.setTypeface(typeface);
            textAvgTime.setTypeface(typeface);
        }
    }

    private String formatTime(int secs){
        int hrs = secs/3600;
        int mins = (secs % 3600) / 60;

        return String.format(" %d hrs\n%d mins",hrs,mins);
    }

}
