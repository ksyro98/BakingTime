package com.example.dell.bakingtime.ingredients_and_steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.bakingtime.DetailsActivity;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.Recipe.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAndStepsFragment extends Fragment implements IngredientsAndStepsAdapter.ClickListener{


    @BindView(R.id.ingredients_and_steps_recycler_view) RecyclerView ingredientsAndStepsRecyclerView;

    private Recipe recipe;
    private static final String TAG = IngredientsAndStepsFragment.class.getSimpleName();
    public static final String INTENT_RECIPE = "recipe";
    public static final String INTENT_POSITION = "position";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsAndStepsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAndStepsRecyclerView.setHasFixedSize(false);

        if(recipe != null)
            Log.d(TAG, recipe.getName());

        IngredientsAndStepsAdapter ingredientsAndStepsAdapter = new IngredientsAndStepsAdapter(getContext(), recipe, this);
        ingredientsAndStepsRecyclerView.setAdapter(ingredientsAndStepsAdapter);

        return view;
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(INTENT_RECIPE, recipe);
        intent.putExtra(INTENT_POSITION, position);
        getActivity().startActivity(intent);
    }
}
