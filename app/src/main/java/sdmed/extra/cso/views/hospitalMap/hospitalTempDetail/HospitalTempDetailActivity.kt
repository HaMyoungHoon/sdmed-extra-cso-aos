package sdmed.extra.cso.views.hospitalMap.hospitalTempDetail

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.Cluster
import sdmed.extra.cso.BuildConfig
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.HospitalTempDetailActivityBinding
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.googleMap.FGooglePlayMapSupport
import sdmed.extra.cso.utils.googleMap.GoogleMapOption
import sdmed.extra.cso.utils.googleMap.GoogleMapStyle
import sdmed.extra.cso.utils.googleMap.IMarkerClusterClickListener
import sdmed.extra.cso.utils.googleMap.MarkerClusterDataModel
import sdmed.extra.cso.views.dialog.hospitalTemp.HospitalTempDialog
import sdmed.extra.cso.views.dialog.pharmacyTemp.PharmacyTempDialog
import sdmed.extra.cso.views.dialog.pharmacyTemp.PharmacyTempDialogVM
import sdmed.extra.cso.views.dialog.pharmacyTemp.PharmacyTempListDialog
import sdmed.extra.cso.views.hospitalMap.PharmacyTempAdapter
import java.util.ArrayList

class HospitalTempDetailActivity: FBaseActivity<HospitalTempDetailActivityBinding, HospitalTempDetailActivityVM>(), OnMapReadyCallback {
    override var layoutId = R.layout.hospital_temp_detail_activity
    override val dataContext: HospitalTempDetailActivityVM by lazy {
        HospitalTempDetailActivityVM(multiDexApplication)
    }
    private var googleMap = FGooglePlayMapSupport()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleMap.setAPIKey(BuildConfig.googleApiKey)
        binding?.googleMap?.onCreate(null)
        binding?.googleMap?.getMapAsync(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding?.googleMap?.onDestroy()
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
    }

    override fun viewInit() {
        super.viewInit()
        setPharmacyAdapter()
        dataContext.hospitalPK = intent.getStringExtra(FConstants.HOSPITAL_PK) ?: ""
        getData()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setPharmacyTempCommand(data)
        setPharmacyTempDialogCommand(data)
        setPharmacyTempListDialogCommand(data)
    }

    private fun setPharmacyAdapter() = PharmacyTempAdapter(dataContext.relayCommand).also { binding?.rvPharmacy?.adapter = it }

    private fun getData() {
        FCoroutineUtil.coroutineScope({
            loading()
            val ret = dataContext.getData()
            if (ret.result == true) {
                getNearbyPharmacy()
                setLocation()
                return@coroutineScope
            }
            loading(false)
            toast(ret.msg)
        })
    }
    private fun getNearbyPharmacy() {
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getNearby()
            loading(false)
            if (ret.result == true) {
                setCluster()
                return@coroutineScope
            }
            toast(ret.msg)
        })
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? HospitalTempDetailActivityVM.ClickEvent ?: return
        when (eventName) {
            HospitalTempDetailActivityVM.ClickEvent.CLOSE -> finish()
            HospitalTempDetailActivityVM.ClickEvent.MAP_TOGGLE -> mapToggle()
            HospitalTempDetailActivityVM.ClickEvent.PHARMACY_TOGGLE -> pharmacyToggle()
        }
    }
    private fun setPharmacyTempCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? PharmacyTempModel.ClickEvent ?: return
        val dataBuff = data[1] as? PharmacyTempModel ?: return
        when (eventName) {
            PharmacyTempModel.ClickEvent.THIS -> selectPharmacy(dataBuff)
        }
    }
    private fun setPharmacyTempDialogCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? PharmacyTempDialogVM.ClickEvent ?: return
        val dataBuff = data[1] as? PharmacyTempModel ?: return
        when (eventName) {
            PharmacyTempDialogVM.ClickEvent.THIS -> selectPharmacy(dataBuff)
            PharmacyTempDialogVM.ClickEvent.PHONE_NUMBER -> openTelephony(dataBuff.phoneNumber)
        }
    }
    private fun setPharmacyTempListDialogCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val arrayBuff = data[0] as? ArrayList<*> ?: return
        val eventName = arrayBuff[0] as? PharmacyTempModel.ClickEvent ?: return
        val dataBuff = arrayBuff[1] as? PharmacyTempModel ?: return
        when (eventName) {
            PharmacyTempModel.ClickEvent.THIS -> pharmacyOpen(dataBuff)
        }
    }
    private fun setMarkerCommand(clusterItem: MarkerClusterDataModel) {
        if (clusterItem.websiteUrl.isNotEmpty()) {
            HospitalTempDialog(HospitalTempModel().parse(clusterItem), dataContext.relayCommand).show(supportFragmentManager, "")
        } else {
            PharmacyTempDialog(PharmacyTempModel().parse(clusterItem), dataContext.relayCommand).show(supportFragmentManager, "")
        }
    }
    private fun setClusterCommand(clusterItem: Cluster<MarkerClusterDataModel>) {
        val items = mutableListOf<PharmacyTempModel>()
        clusterItem.items.forEach { items.add(PharmacyTempModel().parse(it)) }
        PharmacyTempListDialog(items, dataContext.relayCommand).show(supportFragmentManager, "")
    }

    private fun setLocation() {
        googleMap.setLocation(dataContext.hospitalTempItem.value.latitude, dataContext.hospitalTempItem.value.longitude)
    }
    private fun mapToggle() {
        dataContext.mapVisible.value = !dataContext.mapVisible.value
    }
    private fun pharmacyToggle() {
        dataContext.pharmacyToggle.value = !dataContext.pharmacyToggle.value
        setCluster()
    }
    private fun setCluster() {
        googleMap.clearClusterMarkerOption()
        googleMap.addClusterMarkerOption(dataContext.hospitalTempItem.value.toMarkerClusterDataModel())
        if (dataContext.pharmacyToggle.value) {
            googleMap.addClusterMarkerOption(dataContext.pharmacyTempItems.value.map { it.toMarkerClusterDataModel() })
        }
        googleMap.addZoom(0.0001F)
    }
    private fun selectPharmacy(pharmacyTempModel: PharmacyTempModel) {
        googleMap.setLocation(pharmacyTempModel.latitude, pharmacyTempModel.longitude)
        googleMap.addZoom(0.0001F)
    }
    private fun pharmacyOpen(pharmacyTempModel: PharmacyTempModel) {
        PharmacyTempDialog(pharmacyTempModel, dataContext.relayCommand).show(supportFragmentManager, "")
    }
    private fun openTelephony(phoneNumber: String) {
        val buff = FExtensions.regexNumberReplace(phoneNumber)
        if (buff.isNullOrBlank()) {
            return
        }

        startActivity(Intent(Intent.ACTION_DIAL, "tel:$buff".toUri()))
    }
}