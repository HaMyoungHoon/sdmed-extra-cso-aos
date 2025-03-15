package sdmed.extra.cso.utils

import sdmed.extra.cso.R
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.gun0912.tedpermission.coroutine.TedPermission

class FLocationUtil(val context: Context, val once: Boolean, val listener: ILocationListener): LocationListener, LocationCallback() {
    var fusedLocationClient: FusedLocationProviderClient? = null
    var locationManager: LocationManager? = null
    var googlePlay = false
    private var minTime = 500L
    private var minDistance = 1F
    companion object {
        fun getDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Int {
            return Location("point A").apply {
                latitude = latitude1
                longitude = longitude1
            }.distanceTo(Location("point B").apply {
                latitude = latitude2
                longitude = longitude2
            }).toInt()
        }
        const val notValidLatitude: Double = -91.0
        const val notValidLongitude: Double = -181.0
        fun isValidGPS(latitude: Double, longitude: Double): Boolean {
            if (latitude < -90.0 || latitude > 90) {
                return false
            }
            if (longitude < -180.0 || longitude > 180.0) {
                return false
            }
            return true
        }
    }
    init {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initThis()
        } else {
            FCoroutineUtil.coroutineScope({
                TedPermission.create()
                    .setRationaleTitle(context.getString(R.string.check_location_desc))
                    .setRationaleMessage(context.getString(R.string.permission_location_for_map_desc))
                    .setDeniedTitle(context.getString(R.string.cancel_desc))
                    .setDeniedMessage(context.getString(R.string.permit_require))
                    .setGotoSettingButtonText(context.getString(R.string.permit_setting))
                    .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check()
            }, {
                if (it.isGranted) {
                    initThis()
                    getLocation()
                } else {
                    listener.onLocationFail(context.getString(R.string.permit_require))
                }
            })
        }
    }
    private fun initThis() {
        if (isGooglePlayServicesAvailable()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            googlePlay = true
        } else {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }
    fun setUpdateConfig(minTime: Long, minDistance: Float) {
        this.minTime = if (minTime > 100L) minTime else 100L
        this.minDistance = if (minDistance > 0.5F) minDistance else 0.5F
    }
    fun stopWatching() {
        if (googlePlay) {
            fusedLocationClient?.removeLocationUpdates(this)
        } else {
            locationManager?.removeUpdates(this)
        }
    }
    fun getLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, minTime).build()
        val locationSettingRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(locationSettingRequest.build())
        task.addOnSuccessListener {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (googlePlay) {
                    getFused()
                } else {
                    getDefault()
                }
            }
        }
        task.addOnFailureListener {
            listener.onLocationFail(it)
        }
    }
    private fun getFused() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, minTime).build()
            fusedLocationClient?.requestLocationUpdates(locationRequest, this, Looper.getMainLooper())
        }
    }
    private fun getDefault() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this)
        }
    }

    override fun onLocationChanged(p0: Location) {
        if (once) {
            stopWatching()
        }
        listener.onLocationEvent(p0.latitude, p0.longitude)
    }
    override fun onLocationResult(p0: LocationResult) {
        if (once) {
            stopWatching()
        }
        val location = p0.lastLocation ?: return
        listener.onLocationEvent(location.latitude, location.longitude)
    }
    fun isGooglePlayServicesAvailable(): Boolean {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }



    interface ILocationListener {
        fun onLocationEvent(latitude: Double, longitude: Double)
        fun onLocationFail(exception: Exception)
        fun onLocationFail(exception: String)
    }
}