package com.example.tablayout.activities;

import static com.example.tablayout.utils.Constants.Languages;
import static com.example.tablayout.utils.Constants.Meals;
import static com.example.tablayout.utils.Constants.enLang;
import static com.example.tablayout.utils.Constants.english;
import static com.example.tablayout.utils.Constants.grLang;
import static com.example.tablayout.utils.Constants.greek;
import static com.example.tablayout.utils.SharedPreferenceManager.TOKEN;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.example.tablayout.model.MealDay;
import com.example.tablayout.model.MealType;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;
import com.example.tablayout.utils.SharedPreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import timber.log.Timber;

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

    private SensorManager sm = null;
    private Sensor brightness, accelerometer, gyroscope;

    SensorEventListener sel = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float light = event.values[0];
                brightness(light);
            }
            else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER || event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                Timber.e("X: "+event.values[0]+" Y: "+event.values[1]+" Z: "+event.values[2]);
            }
        }
    };

    private void brightness(float light) {

        if(light == 0) {
            Timber.e("Pitch black");
        }
        else if(light >=1 && light<=10) {
            Timber.e("Dark");
        }
        else if(light >=11 && light<=50) {
            Timber.e("Grey");
        }
        else if(light >=51 && light<=5000) {
            Timber.e("Normal");
        }
        else if(light >=5001 && light<=25000) {
            Timber.e("Incredibly bright");
        }
        else{
            Timber.e("This light will blind you");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sel, brightness, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(sel, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(sel, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

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

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        brightness = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


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
        //lew ston viewpager na paei ston mealplanner
        viewPager.setCurrentItem(1);
        ((FragmentMealPlanner) vpAdapter.getRegisteredFragment(1)).selectDateAndAddToDB(selectedRecipe, mealType);
    }

    //einai to teliko stadio exw recipe typo kai date
    @Override
    public void onAddToDBCalled(Recipe selectedRecipe, int selectedMealType, String date) {
        //exw recipe, meal type kai date opote kanw add sti vasi
        Log.e("ADD RECIPE", ": "+selectedRecipe.getName());
        Log.e("MEAL TYPE", ": "+Meals[selectedMealType]);
        Log.e("DATE", ": "+date);

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    try {
                        //fere mou (an yparxei) to mealday me vash to date pou exw
                        MealDay mealDay = realm.where(MealDay.class).equalTo(MealDay.PROPERTY_DATE, date).findFirst();

                        //an den yparxei kane create kai vale hmeromhnia. Auto to object tha mpei sth vash
                        if(mealDay == null) {
                            mealDay = realm.createObject(MealDay.class);
                            mealDay.setDate(date);
                        }
                        // thetw to null gia na yparxei h synthkh if giati an den yparxei xerw oti einai null
                        MealType mealType = null;
                        //for gia na vrw an yparxei typos geymatos gia thn sygkekrimenh hmera
                        for(int i = 0; i < mealDay.getMeals().size() ; i++) {
                            if(mealDay.getMeals().get(i).getType().equals(Meals[selectedMealType])) {
                                mealType = mealDay.getMeals().get(i);
                            }
                        }
                       //an den yparxei to dhmioyrgw kai tou thetoume ton typo
                        if(mealType == null){
                            mealType = realm.createObject(MealType.class);
                            mealType.setType(Meals[selectedMealType]);
                        }

                        Recipe recipe = realm.createObject(Recipe.class);
                        recipe.setName(selectedRecipe.getName());
                        recipe.setDescription(selectedRecipe.getDescription());
                        recipe.setImage(selectedRecipe.getImage());
                        recipe.setCalories(selectedRecipe.getCalories());
                        recipe.setInstructions(selectedRecipe.getInstructions());
                        recipe.setType(selectedRecipe.getType());
                        //vazw to recipe mesa sto mealType
                        mealType.setRecipe(recipe);

                        mealDay.addMeal(mealType);
                        // save sth vash
                        realm.copyToRealm(mealType);
                        realm.copyToRealm(mealDay);


                        //DEBUG
//                        MealDay mealDayTest = realm.where(MealDay.class).equalTo(MealDay.PROPERTY_DATE, date).findFirst();
//
//                        Log.e("TEST: ",""+mealDayTest.getDate());
//                        for(int i = 0 ; i < mealDayTest.getMeals().size() ; i++) {
//                            Log.e("Type: ",""+mealDayTest.getMeals().get(i).getType());
//                            Log.e("Name: ",""+mealDayTest.getMeals().get(i).getRecipe().getName());
//                        }



                    } catch (RealmPrimaryKeyConstraintException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        ((FragmentMealPlanner) vpAdapter.getRegisteredFragment(1)).presentData();
        ((FragmentCalories) vpAdapter.getRegisteredFragment(2)).setData();


    }


}