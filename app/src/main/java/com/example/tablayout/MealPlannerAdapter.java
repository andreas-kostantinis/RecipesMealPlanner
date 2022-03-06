package com.example.tablayout;

import static com.example.tablayout.utils.Constants.enLang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablayout.model.MealType;
import com.example.tablayout.utils.LocaleHelper;

import java.util.ArrayList;


public class MealPlannerAdapter extends RecyclerView.Adapter<MealPlannerAdapter.MealPlannerViewwHolder>{

    private Context context;
    private ArrayList<MealType> recipes;

    public MealPlannerAdapter(ArrayList<MealType> recipes, Context context) {
        this.context = context;
        this.recipes = recipes;
    }


    @NonNull
    @Override
    public MealPlannerViewwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_planner_item, parent, false);

        MealPlannerViewwHolder myViewHolder = new MealPlannerViewwHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MealPlannerViewwHolder holder, int position) {
        if(position == 0) {
            holder.tvType.setText(LocaleHelper.getLanguage(context).equals(enLang) ? "Breakfast" : "Πρωινό");
        }
        else if(position == 1) {
            holder.tvType.setText(LocaleHelper.getLanguage(context).equals(enLang) ? "Lunch" : "Μεσημεριανό");
        }
        else if(position == 2) {
            holder.tvType.setText(LocaleHelper.getLanguage(context).equals(enLang) ? "Dinner" : "Βραδυνό");
        }

        holder.textViewName.setText(recipes.get(position).getRecipe().getName());
        holder.textViewVersion.setText(recipes.get(position).getRecipe().getDescription());
        Glide.with(context).load(recipes.get(position).getRecipe().getImage()).centerCrop().into(holder.imageView);


    }

    public void updateData(ArrayList<MealType> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class MealPlannerViewwHolder extends RecyclerView.ViewHolder{

        TextView tvType;
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageView;
        Button btnAdd;


        public MealPlannerViewwHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.tvType = itemView.findViewById(R.id.tv_type);
            this.textViewVersion = itemView.findViewById(R.id.textViewVersion);
            this.imageView = itemView.findViewById(R.id.image_View);
            this.btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}
