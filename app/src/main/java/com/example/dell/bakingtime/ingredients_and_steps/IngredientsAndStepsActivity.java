package com.example.dell.bakingtime.ingredients_and_steps;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class IngredientsAndStepsActivity extends AppCompatActivity
        implements IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep,
        IngredientsAndStepsFragment.SendStepId{

    private static final String TAG = IngredientsAndStepsActivity.class.getSimpleName();
    private Recipe recipe;
    private boolean isFavorite = false;
    private IngredientsAndStepsFragment ingredientsAndStepsFragment;
    private Fragment fragment;
    private int stepId = -1;    //stepId = -1 represents the ingredients
    private static boolean smallScreen;
    //used for the savedInstance bundle
    private static final String SAVED_INSTANCE_STRING_KEY = "string_key";
    private static final String ROTATION = "rotation is done";
    private static final String SAVED_INSTANCE_INT_KEY = "int_key";
    public static final String FILE_NAME = "favorite_recipe_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

         smallScreen = findViewById(R.id.detail_container) == null;

        //if the intent that started this activity is not null we get the recipe it contained
        Intent intent = getIntent();
        if (intent != null) {
            recipe = intent.getParcelableExtra(MainActivity.INTENT_RECIPE);
            if (recipe == null) {
                recipe = intent.getParcelableExtra(ListWidgetService.INTENT_RECIPE);
            }

            //an ingredientsAndStepsFragment is created
            ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
            ingredientsAndStepsFragment.setRecipe(recipe);
            ingredientsAndStepsFragment.setSmallScreen(smallScreen);
            ingredientsAndStepsFragment.setFragment(fragment);

            //the title of the ActionBar is changed to display the recipe name and the back button is added
            if (recipe != null) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
                    actionBar.setTitle(recipe.getName());
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }

            //checking if this recipe is the favorite recipe
            Recipe favoriteRecipe = readFile(FILE_NAME);
            isFavorite = favoriteRecipe != null && favoriteRecipe.checkIfEqual(recipe);


            if(!smallScreen){
                //because master-detail flow is used when the app is running on large devices one more fragment is loaded
                //this is an IngredientFragment or a StepFragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                //savedInstanceState == null when the activity is first lunched, in this case the IngredientFragment is displayed
                if(savedInstanceState == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                            .commit();

                    fragment = new IngredientsFragment();
                    ((IngredientsFragment) fragment).setIngredients(recipe.getIngredients());
                    ((IngredientsFragment) fragment).setSmallScreen(false);

                    fragmentManager.beginTransaction()
                            .add(R.id.detail_container, fragment)
                            .commit();
                }
                //if the activity was destroyed and the recreated then the Fragment that was visible before the destruction is displayed
                //this is happens because the id of the current step is stored on the savedInstanceState (-1 is stored if IngredientsFragment was the Fragment displayed before destruction)
                else{
                    stepId = savedInstanceState.getInt(SAVED_INSTANCE_INT_KEY);

                    fragmentManager.beginTransaction()
                            .add(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                            .commit();

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
            else{
                Log.d(TAG, "panda");
                if(savedInstanceState == null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                            .commit();
                }
                else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                            .commit();
                }
            }
        }
        else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The Fragment is replaced in onStart because when we navigate to this activity from another activity onCreate isn't called.
     * This case can only occur in small screen devices.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(smallScreen) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_steps_list_container, ingredientsAndStepsFragment)
                    .commit();
        }
    }

    /**
     * Favorite activity has a white star.
     */
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

    /**
     * The favorite activity is written on a file.
     * This way when we set a new activity as favorite the old one is no longer favorite.
     */
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


    /**
     * Methods to write and read a file
     */
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

    /**
     * This method is used to update the widget every time the favorite activity changes
     */
    private void updateWidget(){
        ComponentName thisWidget = new ComponentName(this, IngredientsWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(!smallScreen){
            outState.putInt(SAVED_INSTANCE_INT_KEY, stepId);
        }

        outState.putString(SAVED_INSTANCE_STRING_KEY, ROTATION);

        super.onSaveInstanceState(outState);
    }

    /**
     * Overriding methods of IngredientsFragment.OnButtonClickListenerIngredients,
     * StepFragment.OnButtonClickListenerStep and
     * IngredientsAndStepsFragment.SendStepId.
     */
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
