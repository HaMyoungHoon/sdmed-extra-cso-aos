package sdmed.extra.cso.utils.googleMap

import sdmed.extra.cso.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.model.GeocodingResult
import sdmed.extra.cso.databinding.CustomClusterViewBinding
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FGlideSupport
import sdmed.extra.cso.utils.FImageUtils
import java.util.Locale

class FGooglePlayMapSupport {
    private var _mapOptions: GoogleMapOptions = GoogleMapOptions.of(GoogleMapOption.NONE)
    private var _googleMap: GoogleMap? = null
    private var _clusterManager: ClusterManager<MarkerClusterDataModel>? = null
    private var _googleMarker: Marker? = null
    private var _subMarker: MutableList<Marker> = arrayListOf()
    private var _googleCircle: Circle? = null
    private var _currentData: MarkerClusterDataModel = MarkerClusterDataModel()
    private var _zoom: Float = 15F
    private var _apiKey: String = ""
    private var _customLocale: Locale? = null
    var title = ""
        get() = _currentData.orgName
        private set
    var snippet = ""
        get() = _currentData.address
        private set
    val latitude
        get() = _currentData.latitude
    val longitude
        get() = _currentData.longitude
    val address get() = _currentData.address

    // region config
    fun setConfig(mapOptions: GoogleMapOptions) {
        _mapOptions.clear()
        _mapOptions.addAll(mapOptions)
    }
    fun setMyLocationEnable(context: Context, enable: Boolean) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        _googleMap?.isMyLocationEnabled = enable
    }
    fun setZoom(zoom: Float) {
        if (zoom > 0F) {
            _zoom = zoom
        }
        _googleMap?.moveCamera(CameraUpdateFactory.zoomTo(_zoom))
    }
    fun addZoom(zoom: Float) {
        _zoom += zoom
        _googleMap?.moveCamera(CameraUpdateFactory.zoomTo(_zoom))
    }
    // endregion

    // region set map
    fun setGoogleMap(p0: GoogleMap) {
        _googleMap = p0
        _googleMap?.uiSettings?.let { x ->
            x.isMapToolbarEnabled = false
            x.isMyLocationButtonEnabled = false
        }
    }
    fun setClusterManager(context: Context, markerClusterClickListener: IMarkerClusterClickListener) {
        val buff = _googleMap ?: return
        _clusterManager = ClusterManager(context, buff)
        _clusterManager?.let { x ->
            x.renderer = FClusterRenderer(context, buff, x, markerClusterClickListener)
            buff.setOnCameraIdleListener(x)
        }
    }
    fun setClusterMarkerClickListener(callback: (Cluster<MarkerClusterDataModel>) -> Unit) {
        _clusterManager?.renderer?.setOnClusterClickListener {
            callback(it)
            true
        }
    }
    @SuppressLint("PotentialBehaviorOverride")
    fun setMarkClickListener(callback: (Marker) -> Unit) {
        _googleMap?.setOnMarkerClickListener {
            callback(it)
            true
        }
    }
    @SuppressLint("PotentialBehaviorOverride")
    fun setInfoClickListener(callback: (Marker) -> Unit) {
        _googleMap?.setOnInfoWindowClickListener {
            callback(it)
        }
    }
    fun setMapClickListener(callback: (LatLng) -> Unit) {
        _googleMap?.setOnMapClickListener {
            callback(it)
        }
    }
    fun setCameraIdleListener(listener: GoogleMap.OnCameraIdleListener) {
        _googleMap?.setOnCameraIdleListener(listener)
    }
    fun setCameraMoveListener(listener: GoogleMap.OnCameraMoveListener) {
        _googleMap?.setOnCameraMoveListener(listener)
    }
    fun setCameraMoveCanceledListener(listener: GoogleMap.OnCameraMoveCanceledListener) {
        _googleMap?.setOnCameraMoveCanceledListener(listener)
    }
    fun setCameraMoveStartedListener(listener: GoogleMap.OnCameraMoveStartedListener) {
        _googleMap?.setOnCameraMoveStartedListener(listener)
    }
    //endregion

    // region set marker
    fun setMarkerOption(lat: Double, lng: Double, icon: Bitmap? = null): String? {
        return setMarkerOption(LatLng(lat, lng), icon)
    }
    fun setMarkerOption(lat: Double, lng: Double, icon: Int? = null): String? {
        return setMarkerOption(LatLng(lat, lng), icon)
    }
    fun setMarkerOption(lat: Double, lng: Double, icon: String): String? {
        return setMarkerOption(LatLng(lat, lng), icon)
    }
    fun setMarkerOption(latLng: LatLng, icon: Bitmap? = null): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(latLng)
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(it))
            }
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setMarkerOption(latLng: LatLng, icon: Int? = null): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(latLng)
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromResource(it))
            }
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setMarkerOption(latLng: LatLng, iconUrl: String): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(latLng)
            val icon = FImageUtils.urlToBitmap(iconUrl)
            markerOption.icon(icon)
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setMarkerOption(icon: Bitmap? = null): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(LatLng(latitude, longitude))
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(it))
            }
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setMarkerOption(icon: Int? = null): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(LatLng(latitude, longitude))
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromResource(it))
            }
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setMarkerOption(iconUrl: String): String? {
        if (_googleMap == null) return "map is null"
        _googleMarker?.remove()
        try {
            val markerOption = MarkerOptions().position(LatLng(latitude, longitude))
            val icon = FImageUtils.urlToBitmap(iconUrl)
            markerOption.icon(icon)
            _googleMarker = _googleMap?.addMarker(markerOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun addMarkerOption(lat: Double, lng: Double, icon: Bitmap? = null): String? {
        return addMarkerOption(LatLng(lat, lng), icon)
    }
    fun addMarkerOption(lat: Double, lng: Double, icon: Int? = null): String? {
        return addMarkerOption(LatLng(lat, lng), icon)
    }
    fun addMarkerOption(lat: Double, lng: Double, icon: String): String? {
        return addMarkerOption(LatLng(lat, lng), icon)
    }
    fun addMarkerCustomView(context: Context, lat: Double, lng: Double, name: String, resDrawableId: Int): String? {
        return addMarkerCustomView(context, LatLng(lat, lng), name, resDrawableId)
    }
    fun addMarkerOption(latLng: LatLng, icon: Bitmap? = null): String? {
        val findBuff = _subMarker.find { x -> x.position == latLng }
        if (findBuff != null) {
            return "already add"
        }
        try {
            val markerOption = MarkerOptions().position(latLng)
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(it))
            }
            _googleMap?.addMarker(markerOption)?.let { x ->
                _subMarker.add(x)
            }
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun addMarkerOption(latLng: LatLng, icon: Int? = null): String? {
        val findBuff = _subMarker.find { x -> x.position == latLng }
        if (findBuff != null) {
            return "already add"
        }
        try {
            val markerOption = MarkerOptions().position(latLng)
            icon?.let {
                markerOption.icon(BitmapDescriptorFactory.fromResource(it))
            }
            _googleMap?.addMarker(markerOption)?.let { x ->
                _subMarker.add(x)
            }
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun addMarkerOption(latLng: LatLng, iconUrl: String): String? {
        val findBuff = _subMarker.find { x -> x.position == latLng }
        if (findBuff != null) {
            return "already add"
        }
        try {
            val markerOption = MarkerOptions().position(latLng)
            val icon = FImageUtils.urlToBitmap(iconUrl)
            markerOption.icon(icon)
            _googleMap?.addMarker(markerOption)?.let { x ->
                _subMarker.add(x)
            }
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun addMarkerCustomView(context: Context, latLng: LatLng, name: String, resDrawableId: Int): String? {
        val findBuff = _subMarker.find { x -> x.position == latLng }
        if (findBuff != null) {
            return "already add"
        }
        try {
            val markerOption = MarkerOptions().position(latLng)
            val inflater = LayoutInflater.from(context)
            val view: CustomClusterViewBinding = DataBindingUtil.inflate(inflater, R.layout.custom_cluster_view, null, false)
            view.tvName.text = name
            view.ivImage.setImageDrawable(context.getDrawable(resDrawableId))
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(FImageUtils.getBitmapFromView(view.root)))
            _googleMap?.addMarker(markerOption)?.let { x ->
                _subMarker.add(x)
            }
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun addClusterMarkerOption(data: List<MarkerClusterDataModel>): String? {
        val buff = data.filter { x -> _clusterManager?.algorithm?.items?.map { y -> y.thisPK }?.contains(x.thisPK) == false  }
        if (buff.isEmpty()) {
            return ""
        }
        _clusterManager?.addItems(buff)
        return ""
    }
    fun addClusterMarkerOption(data: MarkerClusterDataModel): String? {
        val findBuff = _clusterManager?.algorithm?.items?.find { it.thisPK == data.thisPK }
        if (findBuff != null) {
            return "already add"
        }
        _clusterManager?.addItem(data)
        return ""
    }
    fun removeClusterMarkerOption(thisPK: String) {
        val findBuff = _clusterManager?.algorithm?.items?.find { it.thisPK == thisPK }
        if (findBuff != null) {
            _clusterManager?.removeItem(findBuff)
        }
    }
    fun clearClusterMarkerOption() {
        _clusterManager?.clearItems()
    }
    //endregion

    // region set circle
    fun setCircleOption(lat: Double, lng: Double, radius: Double, strokeColor: Int, fillColor: Int): String? {
        return setCircleOption(LatLng(lat, lng), radius, strokeColor, fillColor)
    }
    fun setCircleOption(latLng: LatLng, radius: Double, strokeColor: Int, fillColor: Int, strokeWidth: Float = 0F): String? {
        if (_googleMap == null) return "map is null"
        _googleCircle?.remove()
        try {
            val circleOption = CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
                .strokeWidth(strokeWidth)
            _googleCircle = _googleMap?.addCircle(circleOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    fun setCircleOption(radius: Double, strokeColor: Int, fillColor: Int, strokeWidth: Float = 0F): String? {
        if (_googleMap == null) return "map is null"
        _googleCircle?.remove()
        try {
            val circleOption = CircleOptions()
                .center(LatLng(latitude, longitude))
                .radius(radius)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
                .strokeWidth(strokeWidth)
            _googleCircle = _googleMap?.addCircle(circleOption)
        } catch (e: Exception) {
            return e.message
        }
        return ""
    }
    //endregion

    // region common command
    fun setLocation(lat: Double, lng: Double, zoom: Float? = null): String {
        return setLocation(LatLng(lat, lng), zoom)
    }
    fun setLocation(latLng: LatLng, zoom: Float? = null): String {
        if (_googleMap == null) return "map is null"
        if (zoom != null && zoom > 0F) _zoom = zoom
        _currentData.latitude = latLng.latitude
        _currentData.longitude = latLng.longitude
        if (_mapOptions.contains(GoogleMapOption.SET_LOCATION_AND_MOVE)) {
            return moveLocation(latitude, longitude, _zoom)
        }

        return ""
    }
    fun moveLocation(lat: Double, lng: Double, zoom: Float? = null): String {
        return moveLocation(LatLng(lat, lng), zoom)
    }
    fun moveLocation(latLng: LatLng, zoom: Float? = null): String {
        if (_googleMap == null) return "map is null"
        if (zoom != null && zoom > 0F) _zoom = zoom
        _currentData.latitude = latLng.latitude
        _currentData.longitude = latLng.longitude
        return moveLocation()
    }
    fun moveLocation(): String {
        val latLng = LatLng(latitude, longitude)
        return try {
            _googleMarker?.position = latLng
            _googleCircle?.center = latLng
            if (_mapOptions.contains(GoogleMapOption.MOVE_AND_INFO_SHOW)) {
                showInfo()
            }
            if (_mapOptions.contains(GoogleMapOption.MOVE_AND_CENTER)) {
                return setCenter()
            } else {
                ""
            }
        } catch (e: Exception) {
            e.message ?: "exception"
        }
    }
    fun setCenter(): String {
        if (_googleMap == null) return "map is null"
        return try {
            if (_mapOptions.contains(GoogleMapOption.CENTER_AND_ZOOM)) {
                _googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), _zoom))
            } else {
                _googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            }
            ""
        } catch (e: Exception) {
            e.message ?: "exception"
        }
    }
    fun setRadius(radius: Int): String {
        return setRadius(radius.toDouble())
    }
    fun setRadius(radius: Double): String {
        if (_googleMap == null) return "map is null"
        if (_googleCircle == null) {
            _googleCircle = _googleMap?.addCircle(CircleOptions().radius(radius))
        } else {
            _googleCircle?.radius = radius
        }
        return ""
    }
    fun setInfo(title: String, snippet: String) {
        this.title = title
        this.snippet = snippet
    }
    fun showInfo() {
        if (_googleMarker != null) {
            _googleMarker?.showInfoWindow()
        }
    }
    fun hideInfo() {
        if (_googleMarker != null) {
            _googleMarker?.hideInfoWindow()
        }
    }
    // endregion

    // region search
    fun setAPIKey(apiKey: String) {
        _apiKey = apiKey
    }
    fun setCustomLocal(lang: String, script: String? = null, region: String? = null) {
        if (lang.isEmpty()) {
            return
        }
        _customLocale = Locale.Builder().setLanguage(lang).apply {
            if (!script.isNullOrEmpty()) {
                setScript(script)
            }
            if (!region.isNullOrEmpty()) {
                setRegion(region)
            }
        }.build()
    }
    fun getLocale(): Locale {
        return _customLocale ?: Locale.getDefault()
    }
    fun getLanguage(): String {
        return _customLocale?.language ?: Locale.getDefault().language
    }
    fun searchGeocoding(lat: Double, lng: Double, lang: String = getLanguage(), callback: (Array<GeocodingResult>) -> Unit, error: (String?) -> Unit) {
        searchGeocoding(LatLng(lat, lng), lang, callback, error)
    }
    fun searchGeocoding(latLng: LatLng, lang: String = getLanguage(), callback: (Array<GeocodingResult>) -> Unit, error: (String?) -> Unit) {
        if (_apiKey.isEmpty()) {
            error("api key is empty")
            return
        }
       FCoroutineUtil.coroutineScope({
            try {
                val geoContext = GeoApiContext.Builder().apiKey(_apiKey).build()
                GeocodingApi.newRequest(geoContext).language(lang).latlng(com.google.maps.model.LatLng(latLng.latitude, latLng.longitude)).await()
            } catch (e: Exception) {
                error(e.message)
                arrayOf()
            }
        }, {
            callback(it)
        })
    }
    fun searchGeocoding(lang: String = getLanguage(), callback: (Array<GeocodingResult>) -> Unit, error: (String?) -> Unit) {
        if (_apiKey.isEmpty()) {
            error("api key is empty")
            return
        }
        FCoroutineUtil.coroutineScope({
            try {
                val geoContext = GeoApiContext.Builder().apiKey(_apiKey).build()
                GeocodingApi.newRequest(geoContext).language(lang).latlng(com.google.maps.model.LatLng(latitude, longitude)).await()
            } catch (e: Exception) {
                error(e.message)
                arrayOf()
            }
        }, {
            callback(it)
        })
    }
    fun searchGeocoding(placeName: String, lang: String = getLanguage(), callback: (Array<GeocodingResult>) -> Unit, error: (String?) -> Unit) {
        if (_apiKey.isEmpty()) {
            error("api key is empty")
            return
        }
        FCoroutineUtil.coroutineScope({
            try {
                val geoContext = GeoApiContext.Builder().apiKey(_apiKey).build()
                GeocodingApi.newRequest(geoContext).language(lang).address(placeName).await()
            } catch (e: Exception) {
                error(e.message)
                arrayOf()
            }
        }, {
            callback(it)
        })
    }
    fun searchGeocoder(context: Context, lat: Double, lng: Double, locale: Locale = getLocale(), callback: (MutableList<Address>) -> Unit, error: (String?) -> Unit) {
        val geocoder = Geocoder(context, locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(lat, lng, 5, object: Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        callback(addresses)
                    }
                    override fun onError(errorMessage: String?) {
                        error(errorMessage)
                    }
                })
            } catch (e: Exception) {
                error(e.message)
            }
        } else {
            try {
                @Suppress("DEPRECATION")
                val ret = geocoder.getFromLocation(lat, lng, 5)
                callback(ret!!)
            } catch (e: Exception) {
                error(e.message)
            }
        }
    }
    fun searchGeocoder(context: Context, latLng: LatLng, locale: Locale = getLocale(), callback: (MutableList<Address>) -> Unit, error: (String?) -> Unit) {
        searchGeocoder(context, latLng.latitude, latLng.longitude, locale, callback, error)
    }
    fun searchGeocoder(context: Context, locale: Locale = getLocale(), callback: (MutableList<Address>) -> Unit, error: (String?) -> Unit) {
        val geocoder = Geocoder(context, locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(latitude, longitude, 5, object: Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        callback(addresses)
                    }
                    override fun onError(errorMessage: String?) {
                        error(errorMessage)
                    }
                })
            } catch (e: Exception) {
                error(e.message)
            }
        } else {
            try {
                @Suppress("DEPRECATION")
                val ret = geocoder.getFromLocation(latitude, longitude, 5)
                callback(ret!!)
            } catch (e: Exception) {
                error(e.message)
            }
        }
    }
    fun searchGeocoder(context: Context, placeName: String, locale: Locale = getLocale(), callback: (MutableList<Address>) -> Unit, error: (String?) -> Unit) {
        val geocoder = Geocoder(context, locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocationName(placeName, 5, object: Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        callback(addresses)
                    }
                    override fun onError(errorMessage: String?) {
                        error(errorMessage)
                    }
                })
            } catch (e: Exception) {
                error(e.message)
            }
        } else {
            try {
                @Suppress("DEPRECATION")
                val ret = geocoder.getFromLocationName(placeName, 5)
                callback(ret!!)
            } catch (e: Exception) {
                error(e.message)
            }
        }
    }
    // endregion
}