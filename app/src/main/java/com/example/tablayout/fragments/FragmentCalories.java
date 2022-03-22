package com.example.tablayout.fragments;

import static com.example.tablayout.utils.Constants.enLang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablayout.R;
import com.example.tablayout.activities.MainActivity;
import com.example.tablayout.model.MealDay;
import com.example.tablayout.model.MealType;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


public class FragmentCalories extends Fragment {

    private final int caloriesLimit = 1800;
    private int caloriesCounter = 0;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private PieChart chartCalories;
    private TextView tvCalories;
    private Calendar calendar;

    public FragmentCalories(){
        //this is a constructor- andreas comment dsa
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calories, container, false);

        chartCalories = root.findViewById(R.id.chart_calories);
        tvCalories = root.findViewById(R.id.tv_calories);
        calendar = Calendar.getInstance();

        presentData();
        return root;
    }

    private void presentData() {
         chartCalories.setUsePercentValues(true);
        chartCalories.getDescription().setEnabled(false);
        chartCalories.setExtraOffsets(5, 10, 5, 5);

        chartCalories.setDragDecelerationFrictionCoef(0.95f);

        chartCalories.setDrawHoleEnabled(true);
        chartCalories.setHoleColor(R.color.purple_500);

        chartCalories.setTransparentCircleColor(Color.WHITE);
        chartCalories.setTransparentCircleAlpha(110);

        chartCalories.setHoleRadius(58f);
        chartCalories.setTransparentCircleRadius(61f);

        chartCalories.setDrawCenterText(true);

        chartCalories.setRotationAngle(0);
        chartCalories.setRotationEnabled(true);
        chartCalories.setHighlightPerTapEnabled(true);

        chartCalories.animateY(1400, Easing.EaseInOutQuad);


        chartCalories.setEntryLabelColor(Color.WHITE);
        chartCalories.setEntryLabelTextSize(12f);

        setData();
    }

    public void setData() {
        caloriesCounter = 0;

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    try {
                        //erwthma sth vash gia ta geumata ths sygkekrimenhs hmeras
                        MealDay mealDay = realm.where(MealDay.class).equalTo(MealDay.PROPERTY_DATE, dateFormat.format(calendar.getTime())).findFirst();


                        if (mealDay != null) {
                            for(int i = 0; i < mealDay.getMeals().size(); i ++){
                                caloriesCounter = caloriesCounter + mealDay.getMeals().get(i).getRecipe().getCalories();
                            }
                        }
                    } catch (RealmPrimaryKeyConstraintException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        float remainingValue = caloriesLimit - caloriesCounter;

        entries.add(new PieEntry((caloriesCounter*100) / caloriesLimit));

        if(remainingValue > 0){
            entries.add(new PieEntry((remainingValue*100) / caloriesLimit));
        }


        PieDataSet dataSet = new PieDataSet(entries, "Calories");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chartCalories.setData(data);

        // undo all highlights
        chartCalories.getLegend().setEnabled(false);
        chartCalories.highlightValues(null);

        chartCalories.invalidate();

        tvCalories.setTextColor(remainingValue < 0 ? Color.RED : Color.WHITE);
        tvCalories.setText(caloriesCounter + " / " + caloriesLimit + (LocaleHelper.getLanguage(getContext()).equals(enLang) ? " Calories for today" : " Θερμίδες για σήμερα"));
    }

}