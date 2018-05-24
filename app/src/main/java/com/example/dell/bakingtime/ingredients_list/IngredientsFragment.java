package com.example.dell.bakingtime.ingredients_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.Recipe.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsFragment extends Fragment {


    @BindView(R.id.ingredients_recycler_view) RecyclerView ingredientsRecyclerView;
    @BindView(R.id.ingredients_previous_button) Button ingredientPreviousButton;
    @BindView(R.id.ingredients_next_button) Button ingredientsNextButton;

    private OnButtonClickListenerIngredients callbackActivity;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();


    public interface OnButtonClickListenerIngredients {
        void onButtonClickIngredients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container,false);

        ButterKnife.bind(this, view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setHasFixedSize(false);

        IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientsListAdapter);


        ingredientPreviousButton.setEnabled(false);

        ingredientsNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackActivity.onButtonClickIngredients();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbackActivity = (OnButtonClickListenerIngredients) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnButtonClickListener");
        }
    }

    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
}
