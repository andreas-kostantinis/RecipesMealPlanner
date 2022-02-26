package com.example.tablayout.fragments;

import static com.squareup.okhttp.internal.http.HttpDate.format;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tablayout.R;
import com.example.tablayout.model.Recipe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentMealPlanner extends Fragment {

    private ImageView imageArrowLeft, imageArrowRight;
    private TextView tvDate;
    private RecyclerView recyclerMealPlanner;
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
    private Calendar calendar;
    private int counter = 0;




    public FragmentMealPlanner(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meal_planner, container, false);

        imageArrowLeft = root.findViewById(R.id.img_arrow_left);
        imageArrowRight = root.findViewById(R.id.img_arrow_right);
        tvDate = root.findViewById(R.id.tv_date);
        recyclerMealPlanner = root.findViewById(R.id.recycler_meal_planner);
        calendar =  Calendar.getInstance();


        tvDate.setText(dateFormat.format(calendar.getTime()));

        imageArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                tvDate.setText(dateFormat.format(calendar.getTime()));
                imageArrowLeft.setImageResource(R.drawable.baseline_keyboard_arrow_left_white_24dp);

            }
        });

        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                if (counter <= 0) {
                    imageArrowLeft.setImageResource(R.drawable.baseline_keyboard_arrow_left_black_24dp);
                    if(counter == 0){
                        calendar.add(Calendar.DAY_OF_YEAR, -1);
                    }else{
                        counter = 0;
                    }


                } else {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);

                }
                tvDate.setText(dateFormat.format(calendar.getTime()));
            }


        });


        return root;
    }
}