package sdmed.extra.cso.views.hospitalMap.hospitalTempFind

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.Cluster
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import sdmed.extra.cso.BuildConfig
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.HospitalTempFindActivityBinding
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FLocationUtil
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.googleMap.FGooglePlayMapSupport
import sdmed.extra.cso.utils.googleMap.GoogleMapOption
import sdmed.extra.cso.utils.googleMap.GoogleMapStyle
import sdmed.extra.cso.utils.googleMap.IMarkerClusterClickListener
import sdmed.extra.cso.utils.googleMap.MarkerClusterDataModel
import sdmed.extra.cso.views.dialog.hospitalTemp.HospitalTempDialog
import sdmed.extra.cso.views.dialog.hospitalTemp.HospitalTempDialogVM
import sdmed.extra.cso.views.dialog.hospitalTemp.HospitalTempListDialog
import sdmed.extra.cso.views.hospitalMap.HospitalTempAdapter
import java.util.ArrayList
import androidx.core.net.toUri
import com.gun0912.tedpermission.coroutine.TedPermission

class HospitalTempFindActivity: FBaseActivity<HospitalTempFindActivityBinding, HospitalTempFindActivityVM>(), OnMapReadyCallback, FLocationUtil.ILocationListener {
    override var layoutId = R.layout.hospital_temp_find_activity
    override val dataContext: HospitalTempFindActivityVM by lazy {
        HospitalTempFindActivityVM(multiDexApplication)
    }
    private var googleMap = FGooglePlayMapSupport()
    private var locationUtil: FLocationUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleMap.setAPIKey(BuildConfig.googleApiKey)
        binding?.googleMap?.onCreate(null)
        binding?.googleMap?.getMapAsync(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding?.googleMap?.onDestroy()
        locationUtil = null
    }
    override fun onResume() {
        super.onResume()
        binding?.googleMap?.onResume()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.googleMap?.onSaveInstanceState(outState)
    }
    override fun onMapReady(p0: GoogleMap) {
        locationUtil = FLocationUtil(this, true, this)
        try {
            val mapStyle = GoogleMapStyle.getFromIndex(FStorage.getGoogleMapStyleIndex(this))
            p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, mapStyle.resId))
        } catch (_: Exception) {

        }
        googleMap.setGoogleMap(p0)
        googleMap.setMyLocationEnable(this, true)
        googleMap.setClusterManager(this, object: IMarkerClusterClickListener {
            override fun onMarkerClickListener(clusterItem: MarkerClusterDataModel) {
                setMarkerCommand(clusterItem)
            }
            override fun onClusterClickListener(cluster: Cluster<MarkerClusterDataModel>) {
                setClusterCommand(cluster)
            }
        })
        googleMap.setAPIKey(BuildConfig.googleApiKey)
        googleMap.setConfig(GoogleMapOption.SET_LOCATION_AND_MOVE and GoogleMapOption.MOVE_AND_CENTER)
        googleMap.setZoom(15F)
        loading()
        locationUtil?.getLocation()
    }
    override fun onLocationEvent(latitude: Double, longitude: Double) {
        loading(false)
        googleMap.setLocation(latitude, longitude)
        dataContext.nearbyAble.value = true
        getNearbyHospital()
    }
    override fun onLocationFail(exception: String) {
        loading(false)
        toast(exception)
    }
    override fun onLocationFail(exception: Exception) {
        loading(false)
        toast(exception.message)
    }

    override fun viewInit() {
        super.viewInit()
        setHospitalAdapter()
        observeText()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setHospitalTempCommand(data)
        setHospitalTempDialogCommand(data)
        setHospitalTempListDialogCommand(data)
    }

    private fun setHospitalAdapter() = HospitalTempAdapter(dataContext.relayCommand).also { binding?.rvHospital?.adapter = it }
    @OptIn(FlowPreview::class)
    private fun observeText() {
        lifecycleScope.launch {
            dataContext.searchBuff.debounce(1000).collectLatest {
                it ?: return@collectLatest
                if (dataContext.searchString != it) {
                    dataContext.searchString = it
                    getListSearch()
                }
                dataContext.searchLoading.value = false
            }
        }
        lifecycleScope.launch {
            dataContext.searchBuff.collectLatest {
                it ?: return@collectLatest
                dataContext.searchLoading.value = true
            }
        }
    }
    private fun getListSearch() {
        if (dataContext.searchString.length < 3) {
            toast(R.string.search_length_too_low)
            return
        }
        FCoroutineUtil.coroutineScope({
            loading()
            val ret = dataContext.getSearch()
            loading(false)
            if (ret.result == true) {
                googleMap.clearClusterMarkerOption()
                googleMap.addClusterMarkerOption(dataContext.hospitalTempItems.value.map { it.toMarkerClusterDataModel() })
                return@coroutineScope
            }
            toast(ret.msg)
        })
    }
    private fun getNearbyHospital() {
        if (!dataContext.nearbyAble.value) {
            toast(R.string.permission_location_for_map_desc)
            FCoroutineUtil.coroutineScope({
                TedPermission.create()
                    .setRationaleTitle(getString(R.string.check_location_desc))
                    .setRationaleMessage(getString(R.string.permission_location_for_map_desc))
                    .setDeniedTitle(getString(R.string.cancel_desc))
                    .setDeniedMessage(getString(R.string.permit_require))
                    .setGotoSettingButtonText(getString(R.string.permit_setting))
                    .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check()
            }, {
                if (it.isGranted) {
                    locationUtil?.getLocation()
                }
            })
            return
        }
        FCoroutineUtil.coroutineScope({
            loading()
            val ret = dataContext.getNearby(googleMap.latitude, googleMap.longitude)
            loading(false)
            if (ret.result == true) {
                googleMap.clearClusterMarkerOption()
                googleMap.addClusterMarkerOption(dataContext.hospitalTempItems.value.map { it.toMarkerClusterDataModel() })
                googleMap.addZoom(0.0001F)
                return@coroutineScope
            }
            toast(ret.msg)
        })
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? HospitalTempFindActivityVM.ClickEvent ?: return
        when (eventName) {
            HospitalTempFindActivityVM.ClickEvent.SELECT -> selectHospitalFinish()
            HospitalTempFindActivityVM.ClickEvent.CLOSE -> finish()
            HospitalTempFindActivityVM.ClickEvent.MAP_TOGGLE -> mapToggle()
            HospitalTempFindActivityVM.ClickEvent.NEARBY -> getNearbyHospital()
        }
    }
    private fun setHospitalTempCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? HospitalTempModel.ClickEvent ?: return
        val dataBuff = data[1] as? HospitalTempModel ?: return
        when (eventName) {
            HospitalTempModel.ClickEvent.THIS -> selectHospital(dataBuff)
        }
    }
    private fun setHospitalTempDialogCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? HospitalTempDialogVM.ClickEvent ?: return
        val dataBuff = data[1] as? HospitalTempModel ?: return
        when (eventName) {
            HospitalTempDialogVM.ClickEvent.THIS -> selectHospital(dataBuff)
            HospitalTempDialogVM.ClickEvent.WEBSITE -> openWebsite(dataBuff.websiteUrl)
            HospitalTempDialogVM.ClickEvent.PHONE_NUMBER -> openTelephony(dataBuff.phoneNumber)
        }
    }
    private fun setHospitalTempListDialogCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val arrayBuff = data[0] as? ArrayList<*> ?: return
        val eventName = arrayBuff[0] as? HospitalTempModel.ClickEvent ?: return
        val dataBuff = arrayBuff[1] as? HospitalTempModel ?: return
        when (eventName) {
            HospitalTempModel.ClickEvent.THIS -> hospitalOpen(dataBuff)
        }
    }
    private fun setMarkerCommand(clusterItem: MarkerClusterDataModel) {
        HospitalTempDialog(HospitalTempModel().parse(clusterItem), dataContext.relayCommand).show(supportFragmentManager, "")
    }
    private fun setClusterCommand(clusterItem: Cluster<MarkerClusterDataModel>) {
        val items = mutableListOf<HospitalTempModel>()
        clusterItem.items.forEach { items.add(HospitalTempModel().parse(it)) }
        HospitalTempListDialog(items, dataContext.relayCommand).show(supportFragmentManager, "")
    }

    private fun selectHospitalFinish() {
        val item = dataContext.selectedHospitalTemp.value ?: return
        setResult(RESULT_OK, Intent().apply {
            putParcelable(FConstants.HOSPITAL_TEMP, item)
        })
        Glide.get(this).clearMemory()
        finish()
    }
    private fun mapToggle() {
        dataContext.mapVisible.value = !dataContext.mapVisible.value
    }

    private fun selectHospital(hospitalTempModel: HospitalTempModel) {
        dataContext.selectHospital(hospitalTempModel)
        googleMap.setLocation(hospitalTempModel.latitude, hospitalTempModel.longitude)
        googleMap.addZoom(0.0001F)
    }
    private fun hospitalOpen(hospitalTempModel: HospitalTempModel) {
        HospitalTempDialog(hospitalTempModel, dataContext.relayCommand).show(supportFragmentManager, "")
    }
    private fun openWebsite(url: String) {
        if (url.isEmpty()) {
            return
        }

        val buff = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
        startActivity(Intent(Intent.ACTION_VIEW, buff.toUri()))
    }
    private fun openTelephony(phoneNumber: String) {
        val buff = FExtensions.regexNumberReplace(phoneNumber)
        if (buff.isNullOrBlank()) {
            return
        }

        startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()))
    }
}