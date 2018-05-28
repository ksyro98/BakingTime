package com.example.dell.bakingtime.ingredients_and_steps;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dell.bakingtime.IngredientsWidgetProvider;
import com.example.dell.bakingtime.ListWidgetService;
import com.example.dell.bakingtime.MainActivity;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.Recipe.Recipe;
import com.example.dell.bakingtime.StepFragment;
import com.example.dell.bakingtime.ingredients_list.IngredientsFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IngredientsAndStepsActivity extends AppCompatActivity
        implements IngredientsFragment.OnButtonClickListenerIngredients,
        StepFragment.OnButtonClickListenerStep{

    private static final String TAG = IngredientsAndStepsActivity.class.getSimpleName();
    private Recipe recipe;
    private boolean isFavorite = false;
    public static final String FILE_NAME = "favorite_recipe_file";

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

            IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();
            ingredientsAndStepsFragment.setRecipe(recipe);
            ingredientsAndStepsFragment.setSmallScreen(findViewById(R.id.detail_container) == null);

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
                    .add(R.id.ingredients_steps_list_fragment, ingredientsAndStepsFragment)
                    .commit();
        }
        else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }


        if(findViewById(R.id.detail_container) != null){
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredients(recipe.getIngredients());
            ingredientsFragment.setSmallScreen(false);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, ingredientsFragment)
                    .commit();
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

    @Override
    public void onButtonClickIngredients() {

    }

    @Override
    public void onButtonClickStep(int position) {

    }
}
