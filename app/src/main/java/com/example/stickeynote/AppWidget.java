package com.example.stickeynote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Correcting the Intent to launch MainActivity
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set up the RemoteViews
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set the click action for the TextView in the widget to open MainActivity
            remoteViews.setOnClickPendingIntent(R.id.idTVWidget, pendingIntent);

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
