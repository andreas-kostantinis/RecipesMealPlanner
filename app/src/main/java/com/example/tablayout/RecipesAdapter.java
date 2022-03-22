package com.example.tablayout;

import static com.example.tablayout.utils.Constants.enLang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tablayout.model.Recipe;
import com.example.tablayout.utils.LocaleHelper;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context context;
    private AddListener listener;

    public RecipesAdapter(ArrayList<Recipe> recipes, Context context, AddListener listener){
        this.recipes = recipes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        RecipesViewHolder myViewHolder = new RecipesViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageView;
        Button btnAdd = holder.btnAdd;

        textViewName.setText(recipes.get(position).getName());
        textViewVersion.setText(recipes.get(position).getDescription());
        Glide.with(imageView.getContext()).load(recipes.get(position).getImage()).centerCrop().into(imageView);

        btnAdd.setText(LocaleHelper.getLanguage(context).equals(enLang) ? "Add" : "Πρόσθεσε");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddClick(position);
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ProductPriceQuantity.class);
//
//                intent.putExtra(PRODUCT,dataSet.get(holder.getAdapterPosition()));
//                context.startActivity(intent);
//
//            }
//        });

        //imageView.setImageResource(dataSet.get(listPosition).getImageUrl().toString());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageView;
        Button btnAdd;


        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewVersion = itemView.findViewById(R.id.textViewVersion);
            this.imageView = itemView.findViewById(R.id.image_View);
            this.btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }

    // o adapter milaei me ta fragment
    public interface AddListener {
        void onAddClick(int position);
    }



}
