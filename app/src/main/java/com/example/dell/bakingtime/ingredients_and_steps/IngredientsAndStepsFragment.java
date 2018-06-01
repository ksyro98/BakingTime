package com.example.dell.bakingtime.ingredients_and_steps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private boolean smallScreen;
    private Fragment fragment;
    private SendStepId callback;
    public static final String INTENT_RECIPE = "recipe";
    public static final String INTENT_POSITION = "position";
    public static final String DETAIL_FRAGMENT_TAG = "Detail_Fragment_Tag";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);


        //a RecyclerView is created
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsAndStepsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAndStepsRecyclerView.setHasFixedSize(false);

        IngredientsAndStepsAdapter ingredientsAndStepsAdapter = new IngredientsAndStepsAdapter(recipe, this);
        ingredientsAndStepsRecyclerView.setAdapter(ingredientsAndStepsAdapter);

        return view;
    }

    /**
     * setters
     */
    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }

    public void setSmallScreen(boolean smallScreen){
        this.smallScreen = smallScreen;
    }

    public void setFragment(Fragment fragment){
        this.fragment = fragment;
    }


    /**
     * On a small screen device, when an item is clicked the detail activity is launched.
     * On a large screen device, when an item is clicked the Fragment in the detail section of the master-detail flow changes.
     */
    @Override
    public void onItemClick(int position) {
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

    /**
     * A new Fragment replaces the previous Fragment based on the position of the item clicked.
     * In addition the id of this Fragment is passed to the activity that contains the Fragment.
     */
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
    }

    /**
     * An Interface used to pass the id of the step clicked to the activity that contains the Fragment.
     */
    public interface SendStepId{
        void setStepId(int stepId);
    }
}
