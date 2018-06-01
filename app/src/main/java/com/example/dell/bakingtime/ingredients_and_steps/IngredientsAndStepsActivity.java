package com.example.dell.bakingtime.ingredients_and_steps;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dell.bakingtime.details.IngredientsFragment;
import com.example.dell.bakingtime.widget.IngredientsWidgetProvider;
import com.example.dell.bakingtime.widget.ListWidgetService;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.details.StepFragment;
import com.example.dell.bakingtime.main.MainActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

public class IngredientsAndStepsActivity extends AppCompatActivity
        implements IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep,
        IngredientsAndStepsFragment.SendStepId{

    private static final String TAG = IngredientsAndStepsActivity.class.getSimpleName();
    private Recipe recipe;
    private boolean isFavorite = false;
    private IngredientsAndStepsFragment ingredientsAndStepsFragment;
    private Fragment fragment;
    private static final String SAVED_INSTANCE_STRING_KEY = "string_key";
    private static final String ROTATION = "rotation is done";
    private static final String SAVED_INSTANCE_INT_KEY = "int_key";
    public static final String FILE_NAME = "favorite_recipe_file";
    private int stepId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

        Intent intent = getIntent();
        if (intent != null) {
            recipe = intent.getParcelableExtra(MainActivity.INTENT_RECIPE);
            if (recipe == null) {
                recipe = intent.getParcelableExtra(ListWidgetService.INTENT_RECIPE);
            }

            ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
            ingredientsAndStepsFragment.setRecipe(recipe);
            ingredientsAndStepsFragment.setSmallScreen(isSmallScreen());
            ingredientsAndStepsFragment.setFragment(fragment);

            if (recipe != null) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
                    actionBar.setTitle(recipe.getName());
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
            Recipe favoriteRecipe = readFile(FILE_NAME);
            isFavorite = favoriteRecipe != null && favoriteRecipe.compare(recipe);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                    .commit();


            if(!isSmallScreen()){
                if(savedInstanceState == null) {
                    fragment = new IngredientsFragment();
                    ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
                    ((IngredientsFragment) fragment).setSmallScreen(false);

                    fragmentManager.beginTransaction()
                            .add(R.id.detail_container, fragment)
                            .commit();
                }
                else{
                    stepId = savedInstanceState.getInt(SAVED_INSTANCE_INT_KEY);
                    if(stepId == -1) {
                        fragment = new IngredientsFragment();
                        ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
                        ((IngredientsFragment) fragment).setSmallScreen(false);

                        fragmentManager.beginTransaction()
                                .replace(R.id.detail_container, fragment)
                                .commit();
                    }
                    else{
                        fragment = new StepFragment();
                        ((StepFragment) fragment).setPosition(stepId);
                        ((StepFragment) fragment).setStep(recipe.getSteps().get(stepId));
                        ((StepFragment) fragment).setIsLastStep(stepId == recipe.getSteps().size()-1);
                        ((StepFragment) fragment).setSmallScreen(false);
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_recipe, menu);

        if(isFavorite){
            menu.getItem(0).setIcon(R.drawable.star_white);
        }
        else{
            menu.getItem(0).setIcon(R.drawable.star_red);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (android.R.id.home):
                NavUtils.navigateUpFromSameTask(this);
                break;
            case (R.id.favorite):
                if(isFavorite){
                    deleteFile(FILE_NAME);
                    item.setIcon(R.drawable.star_red);
                    isFavorite = false;

                    updateWidget();
                }
                else{
                    writeFile(FILE_NAME);
                    item.setIcon(R.drawable.star_white);
                    isFavorite = true;

                    updateWidget();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void writeFile(String fileName){
        try {
            FileOutputStream fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(recipe);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            Log.d(TAG, "FileNotFoundException when writing");
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException when writing");
        }
    }

    private Recipe readFile(String fileName){
        try {
            FileInputStream fileInputStream = this.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Recipe recipeFromFile = (Recipe) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return recipeFromFile;
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "IOException when reading");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "ClassNotFoundException when reading");
        }

        return null;
    }

    private void updateWidget(){
        ComponentName thisWidget = new ComponentName(this, IngredientsWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);//.updateAppWidget(thisWidget, remoteViews);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }

    private boolean isSmallScreen(){
        return findViewById(R.id.detail_container) == null;
    }

    //public void setStepId(int stepId){
        //this.stepId = stepId;
    //}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(ingredientsAndStepsFragment)
                .commit();

        if(!isSmallScreen()){
            outState.putInt(SAVED_INSTANCE_INT_KEY, stepId);

            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        outState.putString(SAVED_INSTANCE_STRING_KEY, ROTATION);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onButtonClickIngredients() {

    }

    @Override
    public void onButtonClickStep(int position) {

    }

    @Override
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
}
