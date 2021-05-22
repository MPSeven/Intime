package kr.go.mapo.intime.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.content.ContentProviderCompat.requireContext
import kr.go.mapo.intime.R
import kr.go.mapo.intime.info.FragmentCpr
import kr.go.mapo.intime.setting.SettingAddContact
import kr.go.mapo.intime.setting.database.ContactsDatabase
import kr.go.mapo.intime.sos.SosFragment

/**
 * Implementation of App Widget functionality.
 */
class IntimeWidget : AppWidgetProvider() {
    private var db : ContactsDatabase? = null
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        db = ContactsDatabase.getInstance(context)
        appWidgetIds.forEach {
            updateAppWidget(context, appWidgetManager, it)

            val intent119 = PendingIntent.getActivity(
                context, 0,
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:119")), 0
            )
            val intent112 = PendingIntent.getActivity(
                context, 0,
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")), 0
            )
            val intentFav = PendingIntent.getActivity(
                context, 0,
                Intent(Intent.ACTION_DIAL, Uri.parse(
                    "tel:${db?.contactsDao()?.selectSms(check = true)?.phoneNumber.toString()}")), 0
            )

            val intentCpr: PendingIntent
            val views: RemoteViews = RemoteViews(context.packageName, R.layout.intime_widget)
            views.setOnClickPendingIntent(R.id.widget_119, intent119)
            views.setOnClickPendingIntent(R.id.widget_112, intent112)
            views.setOnClickPendingIntent(R.id.widget_fav, intentFav)
//            views.setOnClickPendingIntent(R.id.widget_fav, intentCpr)

            appWidgetManager.updateAppWidget(it, views)
        }
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.intime_widget)
    views.setTextViewText(R.id.widget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}