package edu.washington.minhsuan.schedroid

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest as Manifest
import android.Manifest.permission
import android.Manifest.permission.READ_CONTACTS
import android.location.*
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.model.PointOfInterest
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener ,
    GoogleMap.OnMapClickListener, GoogleMap.OnPoiClickListener {

    private lateinit var mMap: GoogleMap

    val TAG = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
//        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        val longitude = location.longitude
//        val latitude = location.latitude
//        val mLocation = LatLng(latitude, longitude)
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 10f))


        try {
            // Request location updates
         //   lm!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f,
                //App.instance.repo.locationListener)
        } catch(ex: SecurityException) {
            Log.v(TAG, "Security Exception, no location available")
        }
    }

    fun onMapSearch(view: View) {
        val locationSearch = findViewById<EditText>(R.id.editText)
        val location = locationSearch.text.toString()
        var addressList = mutableListOf<Address>()

        if (location != null || location != "") {
            val geocoder = Geocoder(this)
            try {
                addressList = geocoder.getFromLocationName(location, 1)
                    geocoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                val latLng = LatLng(address.latitude, address.longitude)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(latLng).title("Marker"))
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                App.instance.repo.currentLoc = latLng
            } else {
                Toast.makeText(applicationContext, "You might have entered an invalid location", Toast.LENGTH_LONG)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.setOnPoiClickListener(this)
        mMap.setOnMapLongClickListener(this)
        App.instance.repo.mMap = googleMap
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION), 1)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            mMap.isMyLocationEnabled = true
        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val longitude = location.longitude
        val latitude = location.latitude
        val mLocation = LatLng(latitude, longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15f))


        try {
            // Request location updates
           // lm?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f,
                //App.instance.repo.locationListener)
        } catch(ex: SecurityException) {
            Log.v(TAG, "Security Exception, no location available")
        }


    }

    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults:IntArray) {
            if (requestCode == 1/*whatever you set in requestPermissions()*/) {
                if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    if (ContextCompat.checkSelfPermission(this,READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED)
                    mMap.isMyLocationEnabled = true
                }
            }
        }

    override fun onMapLongClick(p0: LatLng?) {
        App.instance.repo.currentLoc = p0
        mMap.clear()
        val marker = mMap!!.addMarker(MarkerOptions().position(p0!!).title("Selected Location"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(p0, 15f))
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Selected Location")
            setMessage("Set this as the desired destination?")
            setPositiveButton("Yes") {dialog, id ->
                App.instance.repo.currentLoc=p0
                Toast.makeText(this@MapsActivity, "Location selected", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") {dialog, which ->
                Toast.makeText(this@MapsActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                marker.remove()
                val lm = getSystemService(LOCATION_SERVICE) as LocationManager
                if (ContextCompat.checkSelfPermission(this@MapsActivity, permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val longitude = location.longitude
                    val latitude = location.latitude
                    val mLocation = LatLng(latitude,longitude)
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15f))
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onMapClick(p0: LatLng?) {
        Log.v(TAG, "Map Clicked!")
    }

    override fun onPoiClick(poi: PointOfInterest?) {
        Toast.makeText(applicationContext, "Clicked: " + poi!!.name + "\nPlace ID:" + poi!!.placeId +
                "\nLatitude:" + poi!!.latLng.latitude + " Longitude:" + poi!!.latLng.longitude,
            Toast.LENGTH_SHORT).show()
        mMap.clear()
        val marker = mMap!!.addMarker(MarkerOptions().position(poi.latLng!!).title("Selected Location"))
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle(poi!!.name)
            setMessage("Set this as the desired destination?")
            setPositiveButton("Yes") { _, _ ->
                App.instance.repo.currentLoc=poi!!.latLng
                App.instance.repo.currentPoi=poi!!.name
                Toast.makeText(this@MapsActivity, App.instance.repo.currentPoi + " selected",
                    Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No") { _, _ ->
                Toast.makeText(this@MapsActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                marker.remove()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
