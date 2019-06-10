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
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import android.media.ToneGenerator
import android.media.AudioManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


interface repo

class repository: repo {
    var mMap:GoogleMap? = null

    var map:MutableMap<Date, ArrayList<Event>> = mutableMapOf()
    var currentLoc:LatLng? = null
    var currentPoi:String? = null
    var currentName:String? = null
    var currentDate:String? = null
    var currentUsername:String? = null
    var currentTime:String? = null
    var currentTitle:String? = null
    var currentDescription: String? = null
    var currentDaily:Int? = null
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
        repo = repository()
        instance = this
        val locationListener: LocationListener = object : LocationListener {
            var you:Int = 0
            fun checkLoc(loc1:LatLng, loc2:LatLng):Boolean {
                return abs(loc1.latitude-loc2.latitude) < 0.001 && abs(loc1.latitude-loc2.latitude) < 0.001
            }

            @SuppressLint("MissingPermission")
            override fun onLocationChanged(location: Location) {
                if (App.instance.repo.currentName != null) {
                    val db = DatabaseHelper(this@App)
                    val lm2 = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
                    val location = lm2.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val longitude = location.longitude
                    val latitude = location.latitude
                    val mLocation = LatLng(latitude, longitude)
                    val c:Calendar = Calendar.getInstance();
                    val formattedDate:String = SimpleDateFormat("HH:mm").format(c.time)
                    val formatMM:String = SimpleDateFormat("MM-dd").format(c.time)
                    val formatYY:String = formatMM + "-" + SimpleDateFormat("yyyy").format(c.time)
                    val targetLoc:LatLng? = db.checkAlarm(formatYY, formattedDate)
                    if (targetLoc != null && !checkLoc(targetLoc, mLocation)) {
                        if (you == 6) {

                        }else {
                            val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 50000)
                            you++
                        }
                    } else {
                        you = 0
                    }
                }
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
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
            lm!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        }
    }
}