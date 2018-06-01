package com.example.dell.bakingtime.ingredients_and_steps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.bakingtime.details.DetailsActivity;
import com.example.dell.bakingtime.details.IngredientsFragment;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.details.StepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAndStepsFragment extends Fragment implements IngredientsAndStepsAdapter.ClickListener{


    @BindView(R.id.ingredients_and_steps_recycler_view) RecyclerView ingredientsAndStepsRecyclerView;

    private Recipe recipe;
    private static final String TAG = IngredientsAndStepsFragment.class.getSimpleName();
    public static final String INTENT_RECIPE = "recipe";
    public static final String INTENT_POSITION = "position";
    private boolean smallScreen;
    private Fragment fragment;
    public static final String DETAIL_FRAGMENT_TAG = "Detail_Fragment_Tag";
    private SendStepId callback;
    //private Bundle fragmentBundle;
    //private int position;
    //private static final String SAVED_INSTANCE_INT_KEY = "int_key";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsAndStepsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAndStepsRecyclerView.setHasFixedSize(false);

        if(recipe != null)
            Log.d(TAG, recipe.getName());

        IngredientsAndStepsAdapter ingredientsAndStepsAdapter = new IngredientsAndStepsAdapter(recipe, this);
        ingredientsAndStepsRecyclerView.setAdapter(ingredientsAndStepsAdapter);

        //if(savedInstanceState != null){
            //addDetailFragment(position);
        //}

        return view;
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }

    public void setSmallScreen(boolean smallScreen){
        this.smallScreen = smallScreen;
    }

    public void setFragment(Fragment fragment){
        this.fragment = fragment;
    }

    //public void setFragmentBundle(Bundle fragmentBundle){
        //this.fragmentBundle = fragmentBundle;
    //}

    @Override
    public void onItemClick(int position) {
        //this.position = position;

        if(smallScreen) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(INTENT_RECIPE, (Parcelable) recipe);
            intent.putExtra(INTENT_POSITION, position);
            getActivity().startActivity(intent);
        }
        else{
            addDetailFragment(position);
        }
    }

    private void addDetailFragment(int position){
        if(position == 0){
            fragment = new IngredientsFragment();
            ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
            ((IngredientsFragment) fragment).setSmallScreen(false);
        }
        else{
            fragment = new StepFragment();
            ((StepFragment) fragment).setPosition(position);
            ((StepFragment) fragment).setStep(recipe.getSteps().get(position-1));
            ((StepFragment) fragment).setIsLastStep(position == recipe.getSteps().size());
            ((StepFragment) fragment).setSmallScreen(false);
        }


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.detail_container, fragment, DETAIL_FRAGMENT_TAG)
                .commit();

        try{
            callback = (SendStepId) getActivity();
            callback.setStepId(position-1);
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
        //((IngredientsAndStepsActivity) getActivity()).setStepId(position-1);
    }

    public interface SendStepId{
        void setStepId(int stepId);
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();

        Log.d(TAG, "onSaveInstanceState in fragment");

        outState.putInt(SAVED_INSTANCE_INT_KEY, position);

        super.onSaveInstanceState(outState);
    }*/
}
