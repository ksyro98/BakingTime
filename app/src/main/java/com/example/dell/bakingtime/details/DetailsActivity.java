package com.example.dell.bakingtime.details;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Ingredient;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsFragment;


public class DetailsActivity extends AppCompatActivity implements
        IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep {

    private Recipe recipe;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();
        if(intent != null) {
            int position = intent.getIntExtra(IngredientsAndStepsFragment.INTENT_POSITION, 0);
            recipe = intent.getParcelableExtra(IngredientsAndStepsFragment.INTENT_RECIPE);

            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
                actionBar.setTitle(recipe.getName());
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

            if (position == 0) {
                fragment = new IngredientsFragment();
                ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
                ((IngredientsFragment) fragment).setSmallScreen(true);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            } else {
                fragment = new StepFragment();
                ((StepFragment) fragment).setPosition(position);
                ((StepFragment) fragment).setStep(recipe.getSteps().get(position - 1));
                ((StepFragment) fragment).setIsLastStep(position == recipe.getSteps().size());
                ((StepFragment) fragment).setSmallScreen(true);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            }
        }

    }

    @Override
    public void onButtonClickIngredients() {
        StepFragment stepFragment = new StepFragment();
        stepFragment.setPosition(1);
        stepFragment.setStep(recipe.getSteps().get(0));
        stepFragment.setSmallScreen(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, stepFragment)
                .commit();
    }

    @Override
    public void onButtonClickStep(int position) {
        if(position == 0){
            fragment = new IngredientsFragment();
            ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
            ((IngredientsFragment) fragment).setSmallScreen(true);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else if(position == recipe.getSteps().size()){
            fragment = new StepFragment();
            ((StepFragment) fragment).setPosition(position);
            ((StepFragment) fragment).setStep(recipe.getSteps().get(position - 1));
            ((StepFragment) fragment).setSmallScreen(true);
            ((StepFragment) fragment).setIsLastStep(true);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else {
            fragment = new StepFragment();
            ((StepFragment) fragment).setPosition(position);
            ((StepFragment) fragment).setStep(recipe.getSteps().get(position - 1));
            ((StepFragment) fragment).setSmallScreen(true);
            ((StepFragment) fragment).setIsLastStep(false);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        super.onSaveInstanceState(outState);
    }
}
