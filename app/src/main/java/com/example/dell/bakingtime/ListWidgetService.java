package com.example.dell.bakingtime;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dell.bakingtime.Recipe.Recipe;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


public class ListWidgetService extends RemoteViewsService {
    public static final String INTENT_RECIPE = "recipe_widget";


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ListRemoteViewFactory.class.getSimpleName();

    private Context context;
    private Recipe recipe;

    public ListRemoteViewFactory(Context context){
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        try {
            FileInputStream fileInputStream = context.openFileInput(IngredientsAndStepsActivity.FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            recipe = (Recipe) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipe == null) {
            Log.d(TAG, "null");
            return 0;
        }
        //Log.d(TAG, String.valueOf(recipe.getIngredients().size()));
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        if(recipe != null) {
            remoteViews.setTextViewText(R.id.appwidget_text, recipe.getIngredients().get(position).toString());
            //Intent fillIntent = new Intent(context, IngredientsAndStepsActivity.class);
            Intent fillIntent = new Intent();
            fillIntent.putExtra(ListWidgetService.INTENT_RECIPE, (Parcelable) recipe);
            remoteViews.setOnClickFillInIntent(R.id.appwidget_text, fillIntent);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
