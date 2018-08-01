package me.geeksploit.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import me.geeksploit.bakingapp.MainActivity;
import me.geeksploit.bakingapp.R;
import me.geeksploit.bakingapp.util.PrefUtils;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static String EXTRA_INGREDIENT = "extra_widget_ingredient";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = PrefUtils.getWidgetRecipeName(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setRemoteAdapter(R.id.appwidget_ingredient_gallery, new Intent(context,
                GridWidgetService.class));

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_root, pendingIntent);

        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_ingredient_gallery, clickPendingIntentTemplate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ingredient_gallery);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

