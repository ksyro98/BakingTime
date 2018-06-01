package com.example.dell.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsActivity;


public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        remoteViews.setEmptyView(R.id.list_view, R.id.empty_text_view);

        //adding intents to launch the app when the widget is clicked
        Intent intent = new Intent(context, IngredientsAndStepsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.list_view, pendingIntent);

        Intent adapterIntent = new Intent(context, ListWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.list_view, adapterIntent);


        //instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
    }


    /**
     * Updating all the widgets
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //there may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

