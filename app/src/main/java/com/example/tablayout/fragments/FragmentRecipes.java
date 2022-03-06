package com.example.tablayout.fragments;

import static com.example.tablayout.utils.Constants.Meals;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tablayout.R;
import com.example.tablayout.RecipesAdapter;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentRecipes extends Fragment implements RecipesAdapter.AddListener{

    private FirebaseFirestore firestore;
    private RecyclerView recyclerRecipes;
    private ArrayList<Recipe> recipes =  new ArrayList<>();
    private int type_selected = 0;
    private int selected_recipe = -1;
    private MealAddedListener listener;

    public interface MealAddedListener {
        void onMealAdded(Recipe selectedRecipe, int mealType);
    }

    public FragmentRecipes(MealAddedListener listener){
        this.listener = listener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        recyclerRecipes =  rootView.findViewById(R.id.recycler_recipes);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRecipes.setHasFixedSize(true);

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
                        recyclerRecipes.setAdapter(new RecipesAdapter(recipes, getContext(), FragmentRecipes.this));

                    }
                });


        return rootView;

    }

    private void presentDialogForType() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setTitle(requireContext().getResources().getString(R.string.select_a_language))
                .setSingleChoiceItems(Meals, type_selected, (dialogInterface, i) -> {
                    type_selected = i;
                })
                .setPositiveButton(requireContext().getResources().getString(R.string.ok), (dialogInterface, i) -> {
                    if(selected_recipe != -1) {
                        addRecipeToMeal(recipes.get(selected_recipe), type_selected);
                    }
                    dialogInterface.dismiss();
                });
        dialogBuilder.create().show();
    }

    private void addRecipeToMeal(Recipe selectedRecipe, int mealType) {
        //kanw add sti vasi edw! oxi edw telika giati theloume kai date!!!!
        listener.onMealAdded(selectedRecipe, mealType);

    }

    @Override
    public void onAddClick(int position) {
        //theloume to position gia na valoume auto to recipe sto meal pou tha epileksei o xristis
        selected_recipe = position;
        presentDialogForType();
    }

}