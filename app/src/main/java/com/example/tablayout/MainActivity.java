package com.example.tablayout;

import static com.example.tablayout.SharedPreferenceManager.TOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

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

        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment3());

        vpAdapter.setData(fragmentList);
        viewPager.setAdapter(vpAdapter);

    }


    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}