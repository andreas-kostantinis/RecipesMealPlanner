package com.example.tablayout.fragments;

import static com.example.tablayout.utils.Constants.enLang;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablayout.MealPlannerAdapter;
import com.example.tablayout.R;
import com.example.tablayout.model.MealDay;
import com.example.tablayout.model.MealType;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;


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
    private MealPlannerAdapter adapter;
    private ArrayList<MealType> recipes = new ArrayList<>();
    private RelativeLayout relNoMeals;
    private TextView tvNoMeals;

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
        relNoMeals = root.findViewById(R.id.rel_no_meals);
        tvNoMeals = root.findViewById(R.id.tv_no_meals);
        tvNoMeals.setText(LocaleHelper.getLanguage(requireContext()).equals(enLang) ? "No meals for this day" : "Δεν υπάρχουν γεύματα για αυτή τη μέρα");
        recyclerMealPlanner = root.findViewById(R.id.recycler_meal_planner);
        recyclerMealPlanner.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMealPlanner.setHasFixedSize(true);
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
                tvDate.setText(dateFormat.format(calendar.getTime()));

                //add to dataBase and eventually load items for the selected date!
                addToDBListener.onAddToDBCalled(selectedRecipe, mealType, dateFormat.format(calendar.getTime()));
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


                presentData();
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

                presentData();
            }



        });

        presentData();

        return root;
    }

    public void presentData() {

        Log.e("PRESENT","DATA");

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    try {
                        MealDay mealDay = realm.where(MealDay.class).equalTo(MealDay.PROPERTY_DATE, dateFormat.format(calendar.getTime())).findFirst();

                        recipes.clear();


                        if (mealDay == null) {
                            //NO MEALS FOR THIS DAY
                            Log.e("NO","MEALS");
                            recyclerMealPlanner.setVisibility(View.GONE);
                            relNoMeals.setVisibility(View.VISIBLE);
                        }
                        else{
                            for(int i = 0; i < mealDay.getMeals().size(); i ++){
                                MealType mealType = new MealType();
                                mealType.setType(mealDay.getMeals().get(i).getType());

                                Recipe recipe = new Recipe();
                                recipe.setName(mealDay.getMeals().get(i).getRecipe().getName());
                                recipe.setDescription(mealDay.getMeals().get(i).getRecipe().getDescription());
                                recipe.setImage(mealDay.getMeals().get(i).getRecipe().getImage());
                                recipe.setCalories(mealDay.getMeals().get(i).getRecipe().getCalories());
                                recipe.setInstructions(mealDay.getMeals().get(i).getRecipe().getInstructions());
                                recipe.setType(mealDay.getMeals().get(i).getRecipe().getType());
                                mealType.setRecipe(recipe);


                                recipes.add(mealType);
                            }
                            recyclerMealPlanner.setVisibility(View.VISIBLE);
                            relNoMeals.setVisibility(View.GONE);

                        }
                    } catch (RealmPrimaryKeyConstraintException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(adapter == null) {
            adapter = new MealPlannerAdapter(recipes, getContext());
            recyclerMealPlanner.setAdapter(adapter);
        }
        else{
            adapter.updateData(recipes);
        }
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
        datePickerDialog.show();

    }
}