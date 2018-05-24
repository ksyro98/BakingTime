package com.example.dell.bakingtime.ingredients_and_steps;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dell.bakingtime.MainActivity;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.Recipe.Recipe;

public class IngredientsAndStepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

        Intent intent = getIntent();
        if (intent != null) {
            Recipe recipe = intent.getParcelableExtra(MainActivity.INTENT_RECIPE);
            IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
            ingredientsAndStepsFragment.setRecipe(recipe);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_steps_list_fragment, ingredientsAndStepsFragment)
                    .commit();
        }
        else
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();

    }
}
