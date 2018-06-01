package com.example.dell.bakingtime.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.BakingViewHolder>{

    //private int numberOfItems;
    private ArrayList<Recipe> recipeArrayList;
    //private Context context;
    private ClickListener clickListener;



    public BakingAdapter(ArrayList<Recipe> recipeArrayList, ClickListener clickListener){
        //this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.clickListener = clickListener;
    }

    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recycler_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new BakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BakingViewHolder holder, int position) {
        holder.recipeNameTextView.setText(recipeArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public interface ClickListener{
        void onItemClick(int recipeIndex);
    }

    class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recycler_view_text_view) TextView recipeNameTextView;

        public BakingViewHolder(View itemView) {
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
