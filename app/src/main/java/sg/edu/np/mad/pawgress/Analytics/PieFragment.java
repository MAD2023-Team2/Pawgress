package sg.edu.np.mad.pawgress.Analytics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyDBHandler myDBHandler;
    private UserData user;
    private ArrayList<Task> taskList;
    private PieChart pieChart;

    public PieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieFragment newInstance(String param1, String param2) {
        PieFragment fragment = new PieFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_pie, container, false);

        myDBHandler = new MyDBHandler(getActivity(), null, null, 1);
        user = myDBHandler.findUser((SaveSharedPreference.getUserName(getActivity())));
        taskList = myDBHandler.findTaskList(user);

        pieChart = view.findViewById(R.id.pie_chart);

        List<String> categories = new ArrayList<>();

        categories.add("School");
        categories.add("Work");
        categories.add("Lifestyle");
        categories.add("Chores");
        categories.add("Others");

        HashMap<String, Integer> categoryColorMap = new HashMap<>();
        categoryColorMap.put("School", Color.parseColor("#1f78b4")); // Blue
        categoryColorMap.put("Work", Color.parseColor("#ff7f00")); // Orange
        categoryColorMap.put("Lifestyle", Color.parseColor("#33a02c")); // Green
        categoryColorMap.put("Chores", Color.parseColor("#e31a1c")); // Red
        categoryColorMap.put("Others", Color.parseColor("#6a3d9a")); // Purple


        List<Integer> completedTaskCounts = new ArrayList<>(Arrays.asList(0,0,0,0,0));
        List<Integer> timeSpentCategory = new ArrayList<>(Arrays.asList(0,0,0,0,0));

        for (Task task : taskList){
            if (task.getStatus().equals("Completed")){
                switch (task.getCategory()){
                    case "School":
                        completedTaskCounts.set(0, completedTaskCounts.get(0) + 1);
                        timeSpentCategory.set(0,timeSpentCategory.get(0) + task.getTimeSpent());
                        break;
                    case "Work":
                        completedTaskCounts.set(1, completedTaskCounts.get(1) + 1);
                        timeSpentCategory.set(1,timeSpentCategory.get(0) + task.getTimeSpent());
                        break;
                    case "Lifestyle":
                        completedTaskCounts.set(2, completedTaskCounts.get(2) + 1);
                        timeSpentCategory.set(2,timeSpentCategory.get(0) + task.getTimeSpent());
                        break;
                    case "Chores":
                        completedTaskCounts.set(3, completedTaskCounts.get(3) + 1);
                        timeSpentCategory.set(3,timeSpentCategory.get(0) + task.getTimeSpent());
                        break;
                    case "Others":
                        completedTaskCounts.set(4, completedTaskCounts.get(4) + 1);
                        timeSpentCategory.set(4,timeSpentCategory.get(0) + task.getTimeSpent());
                        break;
                    default:
                        break;
                }
            }
        }

        List<Integer> averageTimeSpent = new ArrayList<>();

        for (int i = 0; i<completedTaskCounts.size(); i++){
            int count = completedTaskCounts.get(i);
            int totalTimeSpent = timeSpentCategory.get(i);
            int avg;
            if (count == 0){
                avg = 0;
            }
            else{
                double avgD = (double) totalTimeSpent / count;
                avg = (int) Math.round(avgD);
            }
            averageTimeSpent.add(avg);
        }


        List<Integer> timeSpentCategoryCopy = new ArrayList<>(timeSpentCategory);
        List<Integer> completedTaskCountsCopy = new ArrayList<>(completedTaskCounts);
        List<Integer> averageTimeSpentCopy = new ArrayList<>(averageTimeSpent);
        List<String> categoriesCopy = new ArrayList<>(categories);

        completedTaskCountsCopy.add(0, -1);
        timeSpentCategoryCopy.add(0, -1);
        averageTimeSpentCopy.add(0, -1);
        categoriesCopy.add(0, "");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_pie);
        TableAdapter tableAdapter = new TableAdapter(categoriesCopy, completedTaskCountsCopy, timeSpentCategoryCopy, averageTimeSpentCopy);
        recyclerView.setAdapter(tableAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Integer> indicesWithNonZeroValues = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < completedTaskCounts.size(); i++) {
            int count = completedTaskCounts.get(i);
            if (count > 0) {
                indicesWithNonZeroValues.add(i);
                String category = categories.get(i);
                int color = categoryColorMap.get(category);
                pieEntries.add(new PieEntry(count, category));
                colors.add(color);
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Task Categories");

        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setColors(colors);
        IntegerValueFormatter valueFormatter = new IntegerValueFormatter();
        pieDataSet.setValueFormatter(valueFormatter);
        Typeface customTypeface = Typeface.create("sans-serif-condensed-medium", Typeface.BOLD);
        pieDataSet.setValueTypeface(customTypeface);



        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        // pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawSlicesUnderHole(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.animateY(250);
        pieChart.invalidate();

        boolean noValuesRecorded = true;
        for (int count:completedTaskCounts){
            if (count > 0){
                noValuesRecorded = false;
                break;
            }
        }

        if (noValuesRecorded){
            pieChart.setVisibility(View.GONE);
            TextView noDataText = view.findViewById(R.id.no_data_text);
            noDataText.setVisibility(View.VISIBLE);
            return view;
        }
        else {
            pieChart.setVisibility(View.VISIBLE);
            TextView noDataText = view.findViewById(R.id.no_data_text);
            noDataText.setVisibility(View.GONE);
        }
        return view;
    }

    public class IntegerValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value); // Format the value as an integer (without decimal places)
        }
    }
}