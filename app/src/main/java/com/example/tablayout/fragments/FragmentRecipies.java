package com.example.tablayout.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tablayout.R;
import com.example.tablayout.RecipesAdapter;
import com.example.tablayout.model.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentRecipies extends Fragment {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerRecipes;
    private ArrayList<Recipe> recipes =  new ArrayList<>();
    private Spinner spinner;


    public FragmentRecipies(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        spinner = rootView.findViewById(R.id.spinner_tags);
        recyclerRecipes =  rootView.findViewById(R.id.recycler_recipes);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRecipes.setHasFixedSize(true);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);




        firestore = FirebaseFirestore.getInstance();


        firestore.collection("Recipe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc : task.getResult()){
                            Recipe recipe = new Recipe();
                            recipe.setName(doc.getString("Name"));
                            recipe.setDescription(doc.getString("description"));
                            recipe.setImage(doc.getString("imageUrl"));


                            recipes.add(recipe);

                        }
                        recyclerRecipes.setAdapter(new RecipesAdapter(recipes,getContext()));

                    }
                });






        return rootView;

    }
}