package sg.edu.np.mad.pawgress.Tasks;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class FilterViewHolder extends RecyclerView.ViewHolder{
    TextView name;

    public FilterViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.filterName); // name of category
    }
}
