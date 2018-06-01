package com.example.dell.bakingtime.details;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.bakingtime.recipe.Ingredient;
import com.example.dell.bakingtime.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsListViewHolder>{

    private ArrayList<Ingredient> ingredients;

    public IngredientsListAdapter(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public IngredientsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredients_recycler_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientsListAdapter.IngredientsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsListViewHolder holder, int position) {
        holder.ingredientNameTextView.setText(ingredients.get(position).getName());
        String quantityAndMeasure = ingredients.get(position).getQuantity() + " " + ingredients.get(position).getMeasure();
        holder.ingredientQuantityTextView.setText(quantityAndMeasure);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    class IngredientsListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredient_name_text_view) TextView ingredientNameTextView;
        @BindView(R.id.ingredient_quantity_text_view) TextView ingredientQuantityTextView;

        public IngredientsListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
