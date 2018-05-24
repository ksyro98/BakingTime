package com.example.dell.bakingtime;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.bakingtime.Recipe.Recipe;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsFragment;
import com.example.dell.bakingtime.ingredients_list.IngredientsFragment;



public class DetailsActivity extends AppCompatActivity implements
        IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep{

    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra(IngredientsAndStepsFragment.INTENT_POSITION, 0);
        recipe = intent.getParcelableExtra(IngredientsAndStepsFragment.INTENT_RECIPE);

        if(position == 0) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredients(recipe.getIngredients());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, ingredientsFragment)
                    .commit();
        }
        else{
            StepFragment stepFragment = new StepFragment();
            stepFragment.setPosition(position);
            stepFragment.setStep(recipe.getSteps().get(position-1));
            if(position == recipe.getSteps().size()) {
                stepFragment.setIsLastStep(true);
            }
            else {
                stepFragment.setIsLastStep(false);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, stepFragment)
                    .commit();
        }

    }

    @Override
    public void onButtonClickIngredients() {
        StepFragment stepFragment = new StepFragment();
        stepFragment.setPosition(1);
        stepFragment.setStep(recipe.getSteps().get(0));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, stepFragment)
                .commit();
    }

    @Override
    public void onButtonClickStep(int position) {
        if(position == 0){
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredients(recipe.getIngredients());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ingredientsFragment)
                    .commit();
        }
        else if(position == recipe.getSteps().size()){
            StepFragment stepFragment = new StepFragment();
            stepFragment.setPosition(position);
            stepFragment.setStep(recipe.getSteps().get(position - 1));
            stepFragment.setIsLastStep(true);
            //stepFragment.setStepNextButtonEnabled(false);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, stepFragment)
                    .commit();
        }
        else {
            StepFragment stepFragment = new StepFragment();
            stepFragment.setPosition(position);
            stepFragment.setStep(recipe.getSteps().get(position - 1));
            stepFragment.setIsLastStep(false);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, stepFragment)
                    .commit();
        }
    }
}
