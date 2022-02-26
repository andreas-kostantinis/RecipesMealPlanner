package com.example.tablayout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayout.R;


public class FragmentMealPlanner extends Fragment {

    public FragmentMealPlanner(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meal_planner, container, false);

        return root;
    }
}