package sg.edu.np.mad.pawgress.Tasks;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class ParentTaskViewHolder extends RecyclerView.ViewHolder {

    String title = "ViewHolder!";
    TextView category;
    RelativeLayout card;
    RecyclerView childList;

    public ParentTaskViewHolder(View itemView){
        super(itemView);
        category = itemView.findViewById(R.id.taskCat);
        card = itemView.findViewById(R.id.CategoryCard); // individual task
        childList = itemView.findViewById(R.id.childList); // child recyclerview to display tasks under the specified category
    }
}
