package com.example.dell.bakingtime.details;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsFragment;


public class DetailsActivity extends AppCompatActivity implements
        IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep {

    private static final String SAVED_INSTANCE_INT_KEY = "int_key";
    private Recipe recipe;
    private Fragment fragment;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        //if the intent that started this activity is not null we get the recipe it contained
        Intent intent = getIntent();
        if(intent != null) {
            position = intent.getIntExtra(IngredientsAndStepsFragment.INTENT_POSITION, 0);
            recipe = intent.getParcelableExtra(IngredientsAndStepsFragment.INTENT_RECIPE);

            //the title of the ActionBar is changed to display the recipe name
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
                actionBar.setTitle(recipe.getName());
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
            }

            //if position == 0 the IngredientsFragment is added, if position != 0 the StepFragment is added
            if (position == 0) {
                fragment = new IngredientsFragment();
                ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
                ((IngredientsFragment) fragment).setSmallScreen(true);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(savedInstanceState == null) {
                    fragmentTransaction.add(R.id.container, fragment);
                }
                else{
                    fragmentTransaction.replace(R.id.container, fragment);
                }
                fragmentTransaction.commit();

            } else {
                fragment = new StepFragment();
                ((StepFragment) fragment).setPosition(position);
                ((StepFragment) fragment).setStep(recipe.getSteps().get(position - 1));
                ((StepFragment) fragment).setIsLastStep(position == recipe.getSteps().size());
                ((StepFragment) fragment).setSmallScreen(true);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(savedInstanceState == null) {
                    fragmentTransaction.add(R.id.container, fragment);
                }
                else{
                    fragmentTransaction.replace(R.id.container, fragment);
                }
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * If the IngredientsFragment is displayed the nextButton replaces the current fragment with a StepFragment
     * that contains information about the first step.
     */
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


    /**
     * If the StepFragment is displayed the nextButton or previousButton replaces the current fragment with a StepFragment
     * that contains information about the next or the previous step respectively (or with an IngredientFragment if the
     * previous button is clicked from a StepFragment that contains information about the first step).
     */
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


    /**
     * The fragments are removed to avoid having the same Fragment many times when the device is rotated.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction()
          //      .remove(fragment)
            //    .commit();
        outState.putInt(SAVED_INSTANCE_INT_KEY, 0);
        super.onSaveInstanceState(outState);
    }
}
