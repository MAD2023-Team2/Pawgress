package sg.edu.np.mad.pawgress.Tasks;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    String title = "ViewHolder!";
    TextView name, category, none;
    ConstraintLayout card;
    ImageButton edit,delete;
    CheckBox complete;

    public TaskViewHolder(View itemView){
        super(itemView);
        category = itemView.findViewById(R.id.textView3);
        name = itemView.findViewById(R.id.textView4);
        none = itemView.findViewById(R.id.textView2);
        card = itemView.findViewById(R.id.taskCard); // individual task
        edit = itemView.findViewById(R.id.button3);
        delete = itemView.findViewById(R.id.button4);
        complete = itemView.findViewById(R.id.checkBox);
        Log.i(title, "viewholder");
    }
}
