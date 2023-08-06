package sg.edu.np.mad.pawgress.tutorials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import sg.edu.np.mad.pawgress.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {

            R.drawable.pawgress_tut1,
            R.drawable.pawgress_tut2,
            R.drawable.pawgress_tut3

    };


    public ViewPagerAdapter(Context context){

        this.context = context;

    }

    @Override
    public int getCount() {
        // Return the number of slides in the tutorial
        return  images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // Check if the view and object represent the same page
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        // Inflate the layout for each slide
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);
        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.titleImage);

        // change the pictures on viewpager base on slide position
        // Add the view to the ViewPager container
        slidetitleimage.setImageResource(images[position]);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Remove the view from the container when it's no longer needed (destroyed)
        container.removeView((LinearLayout)object);

    }
}
