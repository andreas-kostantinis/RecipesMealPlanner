package com.example.tablayout.activities;

import static com.example.tablayout.SharedPreferenceManager.TOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tablayout.R;
import com.example.tablayout.SharedPreferenceManager;
import com.example.tablayout.VPAdapter;
import com.example.tablayout.fragments.FragmentCalories;
import com.example.tablayout.fragments.FragmentMealPlanner;
import com.example.tablayout.fragments.FragmentRecipies;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    ArrayList<String> titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        titles = new ArrayList<>();

        titles.add("recipes");
        titles.add("meal planner");
        titles.add("calories");
        setViewPagerAdapter();

        SharedPreferenceManager.getStringValue(this,TOKEN);

        new TabLayoutMediator(tabLayout,viewPager,  this).attach();

    }

    public void setViewPagerAdapter(){
        VPAdapter vpAdapter = new VPAdapter(this);
        ArrayList<Fragment>  fragmentList = new ArrayList<>();

        fragmentList.add(new FragmentRecipies());
        fragmentList.add(new FragmentMealPlanner());
        fragmentList.add(new FragmentCalories());

        vpAdapter.setData(fragmentList);
        viewPager.setAdapter(vpAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.options_menu,menu);
         MenuItem item = menu.findItem(R.id.translate_menu);


        return true;
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}