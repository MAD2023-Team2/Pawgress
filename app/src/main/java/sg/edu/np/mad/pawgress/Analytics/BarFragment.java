package sg.edu.np.mad.pawgress.Analytics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {

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
    private TextView weekText;
    private TextView timeSpentText;
    private Date today;
    private BarChart barChart;


    public BarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarFragment newInstance(String param1, String param2) {
        BarFragment fragment = new BarFragment();
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
        view = inflater.inflate(R.layout.fragment_bar, container, false);

        myDBHandler = new MyDBHandler(getActivity(), null, null, 1);

        user = myDBHandler.findUser((SaveSharedPreference.getUserName(getActivity())));
        taskList = myDBHandler.findTaskList(user);

        weekText = view.findViewById(R.id.tvWeekInfo);
        timeSpentText = view.findViewById(R.id.productiveTime);
        barChart = view.findViewById(R.id.barChart);
        today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        updateWeekInformation(today);
        plottingBarChart(barChart);

        view.findViewById(R.id.btnPrevWeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setTime(today);
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                today = calendar.getTime();
                updateWeekInformation(today);

                plottingBarChart(barChart);
            }
        });

        view.findViewById(R.id.btnNextWeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setTime(today);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                today = calendar.getTime();
                updateWeekInformation(today);

                plottingBarChart(barChart);
            }
        });

        return view;
    }

    private void updateWeekInformation(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int year = calendar.get(Calendar.YEAR);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date mondayOfWeek = calendar.getTime();

        calendar.add(Calendar.DATE, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date sundayOfWeek = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        String weekDates = String.format("%s - %s", dateFormat.format(mondayOfWeek), dateFormat.format(sundayOfWeek));
        String formattedText = String.format("%s\n %s", year, weekDates);
        weekText.setText(formattedText);

        int totalTime = calculateTotalProductiveTime(mondayOfWeek, sundayOfWeek);
        String formattedText2 = String.format("Total productive time: %d hrs %d mins", totalTime / 3600, (totalTime % 3600) / 60);
        timeSpentText.setText(formattedText2);
    }

    private int calculateTotalProductiveTime(Date start, Date end){
        int totalTime = 0;

        for (Task task : taskList){
            if (task.getStatus().equalsIgnoreCase("Completed") && isDateWithinCurrentWeek(task.getDateComplete(), start, end)){
                totalTime += task.getTimeSpent();
            }
        }
        return totalTime;
    }

    private boolean isDateWithinCurrentWeek(String dateStr, Date start, Date end){
        if (dateStr == null || dateStr.isEmpty()){
            return false;
        }

        try{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            if (dateStr.contains(",")){
                format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
            }

            Date dateComplete = format.parse(dateStr);
            return dateComplete.compareTo(start) >= 0 && dateComplete.compareTo(end) <=0;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void plottingBarChart(BarChart barChart){
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(today);


        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        // LOOPING THROUGH DAYS OF THE WEEK AND STORE IN ARRAYLIST<BARENTRY>
        for (int i = 0; i < 7; i++) {
            Calendar dayCalendar = (Calendar) calendar.clone();
            dayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + i);
            dayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            dayCalendar.set(Calendar.MINUTE, 0);
            dayCalendar.set(Calendar.SECOND, 0);
            dayCalendar.set(Calendar.MILLISECOND, 0);
            int dayOfWeekIndex = (dayCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7)%7;
            Date startOfDay = dayCalendar.getTime();
            dayCalendar.add(Calendar.HOUR_OF_DAY, 23);
            dayCalendar.add(Calendar.MINUTE, 59);
            dayCalendar.add(Calendar.SECOND, 59);
            Date endOfDay = dayCalendar.getTime();
            int totalTime = calculateTotalProductiveTime(startOfDay, endOfDay);

            Log.d("BarChart", "Day index: " + dayOfWeekIndex + ", Total time: " + totalTime);
            barEntries.add(new BarEntry(dayOfWeekIndex, totalTime / 60));
        }


        // SETTING BARDATASET USING BARENTRIES
        BarDataSet barDataSet = new BarDataSet(barEntries, "Minutes Spent Productive");
        barDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.colorBar));

        // SETTING DATA LABELS TO BE IN INT FORMAT
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format the data labels as integers
                return String.valueOf((int) value);
            }
        });

        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        float maxBarEntryValue = Float.MIN_VALUE;
        for (BarEntry entry : barEntries){
            if (entry.getY() > maxBarEntryValue){
                maxBarEntryValue = entry.getY();
            }
        }

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        Typeface customFont = Typeface.create("sans_serif_condensed_medium_regular", Typeface.BOLD);
        if (customFont == null) {
            Log.e("Font", "Custom Typeface is null");
        }



        // CHANGING Y AXIS
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(maxBarEntryValue + 10f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(12f);
        yAxis.setTypeface(customFont);

        // CHANGING X AXIS
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(daysOfWeek));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(7);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(14f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTypeface(customFont);

        // LEGEND
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(14f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Centralize the legend
        legend.setTypeface(customFont);

        // PLOTTING CHART
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getDescription().setEnabled(false);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
    }
}