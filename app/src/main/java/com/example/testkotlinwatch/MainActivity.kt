package com.example.testkotlinwatch

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import com.example.testkotlinwatch.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import java.util.*

class MainActivity : Activity() {
    private val LOG_TAG = MainActivity::class.java.simpleName
    private val LOC_PROVIDER = "WATCH"
    private val REQUEST_COARSE_AND_FINE_LOCATION_RESULT_CODE = 101
    val LOCATION_UPDATE_INTERVAL: Long = 3000 // duration in milliseconds
    private val MAX_LOCATION_RECORDED = 10

    var locations = ArrayList<Location>()
    var adapter: GpsLocationAdaptater? = null

    private lateinit var binding: ActivityMainBinding
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(LOG_TAG, "onLocationResult(" + locationResult.locations.size + ")")
            if (locationResult.locations.size > 0) {
                for (location in locationResult.locations) {
                    val length = locations.size
                    if (length >= MAX_LOCATION_RECORDED) {
                        locations.removeAt(length - 1) // remove last element
                    }
                    locations.add(0, location!!) // insert new element
                }
                //adapter.notifyItemRangeChanged(0, locations.size());
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate()")

        // Init view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
         * Attach an ambient mode controller, which will be used by
         * the activity to determine if the current mode is ambient.
         */
//        val ambientController = AmbientModeSupport.attach(this)
        /*
         * activity's task will be moved to the front if it was the last activity to be running
         *  when ambient started, depending on how much time the system spent in ambient mode.
         */
//        ambientController.setAutoResumeEnabled(true)

        // Ensure watch has an embedded physical GPS for the application to work
        if (hasGps()) {
            Log.d(LOG_TAG, "Found standalone GPS hardware")
            // dummy init for test purpose only
            locations.add(makeLoc(0.0, 0.0))
            /* locations.add(makeLoc(42.12812921d, 6.22987212d));
            locations.add(makeLoc(43.24648328d, 7.53783836d));
            locations.add(makeLoc(44.39800122d, 8.40192765d));
            locations.add(makeLoc(45.83232627d, 9.12763238d));*/

            // Wearable recycler view init
            binding.gpsLocationWrv.isEdgeItemsCenteringEnabled = true
            binding.gpsLocationWrv.layoutManager = WearableLinearLayoutManager(this)
            adapter = GpsLocationAdaptater(locations, this)
            binding.gpsLocationWrv.adapter = adapter

            // Location init
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            // Check for coarse and fine location permission
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(LOG_TAG, "All required location permission are already granted")
                startLocationUpdates()
            } else {
                Log.d(LOG_TAG, "ask for coarse and fine location permission")
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_COARSE_AND_FINE_LOCATION_RESULT_CODE
                )
            }
        } else {
            Log.e(LOG_TAG, "This hardware doesn't have GPS.")
            noGpsExitConfirmation()
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        Log.d(LOG_TAG, "startLocationUpdates()")

        // create location request
        val locationRequest = LocationRequest.create()
        locationRequest.interval = LOCATION_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        Log.d(LOG_TAG, "stopLocationUpdates()")
        fusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(LOG_TAG, "onRequestPermissionsResult(): " + Arrays.toString(permissions))
        if (requestCode == REQUEST_COARSE_AND_FINE_LOCATION_RESULT_CODE) {
            Log.i(LOG_TAG, "Received response for GPS permission request")
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(LOG_TAG, "GPS permission granted.")
                startLocationUpdates()
            } else {
                Log.i(LOG_TAG, "GPS permission NOT granted.")
            }
        } else {
            Log.e(LOG_TAG, "Unhandled Request Permissions Result code")
        }
    }

    /* *********************************************************************************************
     * HELPER METHODS
     * *********************************************************************************************
     */
    private fun hasGps(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
    }

    fun noGpsExitConfirmation() {
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra(
            ConfirmationActivity.EXTRA_ANIMATION_TYPE,
            ConfirmationActivity.FAILURE_ANIMATION
        )
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS, 3000)
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.no_gps_message))
        startActivity(intent)
        finish()
    }

    private fun makeLoc(lat: Double, lon: Double): Location {
        val location: Location = Location(LOC_PROVIDER)
        val now = Date()
        location.latitude = lat
        location.longitude = lon
        location.time = now.time
        return location
    }

    fun getAmbientCallback(): AmbientModeSupport.AmbientCallback? {
        Log.d(LOG_TAG, "getAmbientCallback")
        return CustomAmbientCallback(this)
    }

    /*
     * Inner class for ambient mde call back implementation
     */
    class CustomAmbientCallback  // Ctor
    internal constructor(var activity: MainActivity) :
        AmbientModeSupport.AmbientCallback() {
        override fun onEnterAmbient(ambientDetails: Bundle) {
            super.onEnterAmbient(ambientDetails)
            Log.d(
                LOG_TAG,
                "onEnterAmbient() $ambientDetails"
            )
            activity.stopLocationUpdates()
        }

        override fun onExitAmbient() {
            super.onExitAmbient()
            Log.d(LOG_TAG, "onExitAmbient()")
            activity.startLocationUpdates()
        }

        companion object {
            private val LOG_TAG = CustomAmbientCallback::class.java.simpleName
        }
    }
}