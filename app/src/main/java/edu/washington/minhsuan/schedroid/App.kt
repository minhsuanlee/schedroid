package edu.washington.minhsuan.schedroid

import android.app.Application
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.Date

interface repo {
}

class repository: repo {
    var mMap:GoogleMap? = null
    val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {

        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
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
    }
}