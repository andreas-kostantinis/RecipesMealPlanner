package com.example.tablayout.activities;

import static com.example.tablayout.utils.Constants.Languages;
import static com.example.tablayout.utils.Constants.Meals;
import static com.example.tablayout.utils.Constants.enLang;
import static com.example.tablayout.utils.Constants.english;
import static com.example.tablayout.utils.Constants.grLang;
import static com.example.tablayout.utils.Constants.greek;
import static com.example.tablayout.utils.SharedPreferenceManager.TOKEN;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tablayout.R;
import com.example.tablayout.VPAdapter;
import com.example.tablayout.fragments.FragmentCalories;
import com.example.tablayout.fragments.FragmentMealPlanner;
import com.example.tablayout.fragments.FragmentRecipes;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;
import com.example.tablayout.utils.SharedPreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        TabLayoutMediator.TabConfigurationStrategy,
        FragmentRecipes.MealAddedListener,
        FragmentMealPlanner.AddToDBListener{

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    ArrayList<String> titles;

    private Resources resources;
    private int lang_selected = 0;
    private VPAdapter vpAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        titles = new ArrayList<>();

        setViewPagerAdapter();
        configureLanguage();

        SharedPreferenceManager.getStringValue(this,TOKEN);

        new TabLayoutMediator(tabLayout,viewPager,  this).attach();

    }

    private void configureLanguage() {
        if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase(enLang)) {
            resources = LocaleHelper.setLocale(MainActivity.this, enLang).getResources();
            lang_selected = 0;
        }else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase(grLang)){
            resources = LocaleHelper.setLocale(MainActivity.this, grLang).getResources();
            lang_selected = 1;
        }

        applyTranslation();
    }

    private void applyTranslation() {
        titles.clear();
        titles.add(resources.getString(R.string.recipes));
        titles.add(resources.getString(R.string.meal_planner));
        titles.add(resources.getString(R.string.calories));

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(titles.get(0));
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(titles.get(1));
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(titles.get(2));

        setViewPagerAdapter();


        setTitle(resources.getString(R.string.app_name));
    }

    public void setViewPagerAdapter(){
        vpAdapter = new VPAdapter(this);
        ArrayList<Fragment>  fragmentList = new ArrayList<>();

        fragmentList.add(new FragmentRecipes(this));
        fragmentList.add(new FragmentMealPlanner(this));
        fragmentList.add(new FragmentCalories());

        vpAdapter.setData(fragmentList);
        viewPager.setAdapter(vpAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.translate_menu) {
            presentDialogForLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void presentDialogForLanguage() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(resources.getString(R.string.select_a_language))
                .setSingleChoiceItems(Languages, lang_selected, (dialogInterface, i) -> {
                    if(Languages[i].equals(english)){
                        LocaleHelper.setLocale(MainActivity.this, enLang);
                    }
                    if(Languages[i].equals(greek)) {
                        LocaleHelper.setLocale(MainActivity.this, grLang);
                    }

                })
                .setPositiveButton(resources.getString(R.string.ok), (dialogInterface, i) -> {
                    configureLanguage();
                    dialogInterface.dismiss();
                });
        dialogBuilder.create().show();
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }


    @Override
    public void onMealAdded(Recipe selectedRecipe, int mealType) {
        viewPager.setCurrentItem(1);
        ((FragmentMealPlanner) vpAdapter.getRegisteredFragment(1)).selectDateAndAddToDB(selectedRecipe, mealType);
    }

    @Override
    public void onAddToDBCalled(Recipe selectedRecipe, int mealType, String date) {
        //exw recipe, meal type kai date opote kanw add sti vasi
        Log.e("ADD RECIPE", ": "+selectedRecipe.getName());
        Log.e("MEAL TYPE", ": "+Meals[mealType]);
        Log.e("DATE", ": "+date);
    }
}