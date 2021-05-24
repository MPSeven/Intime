package kr.go.mapo.intime.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.widget.RemoteViews
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kr.go.mapo.intime.R
import kr.go.mapo.intime.setting.database.ContactsDatabase
import java.util.*

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
            val intentRefresh: PendingIntent
            val views: RemoteViews = RemoteViews(context.packageName, R.layout.intime_widget)
            views.setOnClickPendingIntent(R.id.widget_119, intent119)
            views.setOnClickPendingIntent(R.id.widget_112, intent112)
            views.setOnClickPendingIntent(R.id.widget_fav, intentFav)
//            views.setOnClickPendingIntent(R.id.widget_cpr, intentCpr)
//            views.setOnClickPendingIntent(R.id.widget_refresh, intentRefresh)

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

@SuppressLint("MissingPermission")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    var latitude: Double?
    var longitude: Double?
    val mGeoCoder = Geocoder(context, Locale.KOREAN)
    var address: String?
    var currentAddress: List<Address>?

    var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
        // Got last known location. In some rare situations this can be null.
        val views = RemoteViews(context.packageName, R.layout.intime_widget)

        if(location == null){
            views.setTextViewText(R.id.widget_currentLoc, "gps 연결이 불안정합니다")
        } else {
            latitude = location?.latitude
            longitude = location?.longitude
            currentAddress = mGeoCoder.getFromLocation(latitude!!, longitude!!, 1)
            address = (currentAddress as MutableList<Address>)?.get(0).getAddressLine(0).substring(5)

            views.setTextViewText(R.id.widget_currentLoc, address)
        }

        val widgetText = context.getString(R.string.app_name)
        views.setTextViewText(R.id.widget_text, widgetText)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}