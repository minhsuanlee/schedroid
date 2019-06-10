package edu.washington.minhsuan.schedroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.media.ToneGenerator
import android.media.AudioManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.lang.Math.abs
import java.security.AccessController.getContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


interface repo {

    //return a list of Events
    fun getEvents(date:Date):ArrayList<Event>?
    fun addEvent(date:Date, event:Event)
    fun removeEvent(date:Date, num:Int)

}

class repository(): repo {
    var mMap:GoogleMap? = null

    var map:MutableMap<Date, ArrayList<Event>> = mutableMapOf()
    var currentLoc:LatLng? = null
    var currentPoi:String? = null
    var currentName:String? = null
    var currentTime:String? = null
    var currentTitle:String? = null
    var currentDescription: String? = null
    var currentDaily:Int? = null
    var currentDate:String? = null
    var currentUsername:String? = null


    // return all events on the given date
    override fun getEvents(date:Date):ArrayList<Event>? {
        if (map != null) {
            if (map!!.containsKey(date)) {
                return map.get(date)
            }
            //wenren
        }
        return null
    }

    // add a new event to the given date
    override fun addEvent(date: Date, event:Event) {
            if (map.get(date) != null) {
                (map.get(date))!!.add(event)
            } else {
                map.put(date, arrayListOf<Event>(event))
            }
    }

    // Use hashcode to remove an event on a specific day
    override fun removeEvent(date: Date, num: Int) {
        if (map.get(date) != null) {
            val events = map.get(date)
            for (i in 0 until (events!!.size-1)) {
                if (events[i].hashCode() == num) {
                    events.remove(events[i])
                }
            }
        }
    }
}


class App: Application() {

    lateinit var repo: repository
        private set

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()


        Log.e("BonanKou", "Success")
        super.onCreate()
        repo = repository()
        instance = this
        val locationListener: LocationListener = object : LocationListener {
            var you:Int = 0
            fun checkLoc(loc1:LatLng, loc2:LatLng):Boolean {
                Log.e("Bonan", "Yes")
                val end:Boolean = abs(loc1.latitude-loc2.latitude) < 0.001 && abs(loc1.latitude-loc2.latitude) < 0.001
                if (end) {
                    Log.e("Bonan", "Yesyes")
                }
                return end
            }

            @SuppressLint("MissingPermission")
            override fun onLocationChanged(location: Location) {
                // val db = DatabaseHelper(getContext())
//            val time =
//
//            if (db.checkAlarm(App.instance.repo.currentDate, )) {
//                val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
//                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 3000)
//            }
                if (App.instance.repo.currentName != null) {
                    val something = getContext()
                    val db = DatabaseHelper(this@App)
                    val lm2 = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
                    val location = lm2.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val longitude = location.longitude
                    val latitude = location.latitude
                    val mLocation = LatLng(latitude, longitude)
                    val c:Calendar = Calendar.getInstance();
                    val df:SimpleDateFormat = SimpleDateFormat("HH:mm");
                    val formattedDate:String = df.format(c.getTime());
                    Log.e("Bonan", formattedDate)
                    val df2:SimpleDateFormat = SimpleDateFormat("MM-dd")
                    val df3 = SimpleDateFormat("yyyy")
                    val formatMM:String = df2.format(c.time)
                    val formatYY:String = formatMM + "-" + df3.format(c.time)
                    val targetLoc:LatLng? = db.checkAlarm(formatYY, formattedDate)
                    Log.e("Bonan", formatYY)
                    if (targetLoc != null ) {
                        Log.e("Bonan", targetLoc!!.latitude.toString())
                    }
                    if (targetLoc != null && !checkLoc(targetLoc, mLocation)) {
                        if (you==6) {

                        }else {
                            val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 50000)
                            you++
                        }
                    } else {
                        you = 0
                    }
                }
                //mMap!!.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)).title((location.latitude).toString()))
                //Toast.makeText(getContext() as Context, "changed", Toast.LENGTH_LONG).show()
                Log.e("Bonan", "Ilovexumiao")
//            val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
//            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }

        } else {
            Log.e("Bonan", "Nonononono")
            val locationRequest = LocationRequest.create()?.apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            lm!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        }

           }
}