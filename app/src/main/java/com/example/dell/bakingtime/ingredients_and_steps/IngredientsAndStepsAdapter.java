package com.example.dell.bakingtime.ingredients_and_steps;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.Recipe.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAndStepsAdapter
        extends RecyclerView.Adapter<IngredientsAndStepsAdapter.IngredientsAndStepsViewHolder>{

    private Context context;
    private Recipe recipe;
    private ClickListener clickListener;
    private static final String TAG = IngredientsAndStepsAdapter.class.getSimpleName();


    public IngredientsAndStepsAdapter(Context context, Recipe recipe, ClickListener clickListener){
        this.context = context;
        this.recipe = recipe;
        this.clickListener = clickListener;
    }

    @Override
    public IngredientsAndStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recycler_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientsAndStepsAdapter.IngredientsAndStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAndStepsViewHolder holder, int position) {
        if(position == 0)
            holder.ingredientsAndStepsTextView.setText(R.string.ingredients);
        else
            holder.ingredientsAndStepsTextView.setText(recipe.getSteps().get(position-1).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (recipe == null)
            return 0;

        return recipe.getSteps().size() + 1;
    }

    public interface ClickListener{
        void onItemClick(int position);
    }

    class IngredientsAndStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recycler_view_text_view) TextView ingredientsAndStepsTextView;

        public IngredientsAndStepsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

}
