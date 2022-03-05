package com.example.tablayout;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class VPAdapter extends FragmentStateAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private ArrayList<Fragment> fragmentArrayList ;
    private ArrayList<String> fragmentTitle ;

    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    public Fragment getRegisteredFragment(int position) {
        return fragmentArrayList.get(position);
    }



    public void setData(ArrayList<Fragment> fragments){
        this.fragmentArrayList = fragments;
    }
}
