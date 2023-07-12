package sg.edu.np.mad.pawgress.Fragments.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    Context context;

    public SearchAdapter(Context context){
        this.context = context;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_friend,
                parent,
                false);

        return new SearchViewHolder(item);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
