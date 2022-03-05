package com.example.tablayout.fragments;

import static com.example.tablayout.utils.Constants.Meals;
import static com.squareup.okhttp.internal.http.HttpDate.format;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablayout.R;
import com.example.tablayout.activities.MainActivity;
import com.example.tablayout.model.Recipe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class FragmentMealPlanner extends Fragment {

    private ImageView imageArrowLeft, imageArrowRight;
    private TextView tvDate;
    private RecyclerView recyclerMealPlanner;
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar calendar;
    private int counter = 0;
    private AddToDBListener addToDBListener;
    private DatePickerDialog datePickerDialog;
    private Recipe selectedRecipe;
    private int mealType;

    public interface AddToDBListener {
        void onAddToDBCalled(Recipe selectedRecipe, int mealType, String date);
    }


    public FragmentMealPlanner(AddToDBListener addToDBListener){
        this.addToDBListener = addToDBListener;
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

        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //get date selected from datePicker
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                //calculate difference in days and update counter-arrows
                long diff = newDate.getTimeInMillis() - calendar.getTimeInMillis();
                counter = (int) TimeUnit.MILLISECONDS.toDays(diff);
                updateArrows();

                //assign new calendar and update textView
                calendar = newDate;
                tvDate.setText(dateFormat.format(newDate.getTime()));

                //add to dataBase and eventually load items for the selected date!
                addToDBListener.onAddToDBCalled(selectedRecipe, mealType, dateFormat.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


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


    private void updateArrows(){
        if(counter <= 0) {
            imageArrowLeft.setImageResource(R.drawable.baseline_keyboard_arrow_left_black_24dp);
        }
        else{
            imageArrowLeft.setImageResource(R.drawable.baseline_keyboard_arrow_left_white_24dp);
        }
    }

    public void selectDateAndAddToDB(Recipe selectedRecipe, int mealType) {
        this.selectedRecipe = selectedRecipe;
        this.mealType = mealType;
        Toast.makeText(requireContext(), "Select Date!!!", Toast.LENGTH_SHORT).show();
        datePickerDialog.show();

    }
}