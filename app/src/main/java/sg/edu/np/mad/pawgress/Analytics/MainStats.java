package sg.edu.np.mad.pawgress.Analytics;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import sg.edu.np.mad.pawgress.Fragments.Profile.Analytics;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainStats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainStats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyDBHandler myDBHandler;
    static UserData user;
    private ArrayList<Task> taskList;

    public MainStats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainStats.
     */
    // TODO: Rename and change types and number of parameters
    public static MainStats newInstance(String param1, String param2) {
        MainStats fragment = new MainStats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_main_stats, container, false);

        myDBHandler = new MyDBHandler(getActivity(), null, null, 1);

        user = myDBHandler.findUser((SaveSharedPreference.getUserName(getActivity())));

        TextView statsText = view.findViewById(R.id.statsText);

        taskList = myDBHandler.findTaskList(user);

        int count = 0;
        int totalTime = 0;
        int countMissedDaily = 0;
        int countDaily = 0;
        int totalTask = taskList.size();

        for (Task task : taskList){
            if (task.getCategory().equals("Daily Challenge")){
                if (task.getStatus().equals("Completed")){
                    countDaily++;
                }
                else if(task.getStatus().equals("Missed")){
                    countMissedDaily++;
                }
            }

            if (task.getStatus().equals("Completed")){
                count++;
                totalTime += task.getTimeSpent();
            }
            else if (task.getStatus().equals("Deleted")){
                totalTask--;
            }
        }

        int hrs = totalTime / 3600;
        int mins = (totalTime%3600) / 60;
        int secs = totalTime % 60;

        float completionRate = (count / (float)totalTask) * 100;

        statsText.setText("Count of completed lifetime task: \n" + count +
                "\n\nTotal Time Spent Completing Task: \n" + String.format("%d hrs %d mins %d secs",hrs,mins,secs) +
                "\n\nDaily Challenges Completed: \n" + countDaily +
                "\n\nDaily Challenges Missed: \n" + countMissedDaily +
                "\n\nTask Completion Rate: \n" + completionRate + "%");







        return view;

    }
}