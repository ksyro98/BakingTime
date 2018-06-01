package com.example.dell.bakingtime.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dell.bakingtime.recipe.Ingredient;
import com.example.dell.bakingtime.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsFragment extends Fragment {


    @BindView(R.id.ingredients_recycler_view) RecyclerView ingredientsRecyclerView;
    @BindView(R.id.ingredients_previous_button) Button ingredientPreviousButton;
    @BindView(R.id.ingredients_next_button) Button ingredientsNextButton;
    @BindView(R.id.linear_layout) LinearLayout linearLayout;

    private OnButtonClickListenerIngredients callbackActivity;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private boolean smallScreen = true;


    /**
     * An Interface used to change the Fragment in the Activity that contains the current Fragment
     * when the next or previous button is clicked
     */
    public interface OnButtonClickListenerIngredients {
        void onButtonClickIngredients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container,false);

        ButterKnife.bind(this, view);

        //a RecyclerView is created
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setHasFixedSize(false);

        IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientsListAdapter);


        //in large screen devices the next and previous buttons shouldn't be visible
        //in small screen devices the next and previous buttons should be visible
        if(!smallScreen){
            hideButtons();
        }
        else {
            showButtons();
            ingredientPreviousButton.setEnabled(false);

            ingredientsNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackActivity.onButtonClickIngredients();
                }
            });
        }

        return view;
    }


    /**
     * Checks if the Activity that contains the Fragment implements OnButtonClickListenerIngredients
     */
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


    /**
     * setters
     */
    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public void setSmallScreen(boolean smallScreen){
        this.smallScreen = smallScreen;
    }


    /**
     * These methods are used to change the visibility of the next and previous buttons.
     */
    private void hideButtons(){
        ingredientsNextButton.setVisibility(View.GONE);
        ingredientPreviousButton.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    private void showButtons(){
        ingredientsNextButton.setVisibility(View.VISIBLE);
        ingredientPreviousButton.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }
}
