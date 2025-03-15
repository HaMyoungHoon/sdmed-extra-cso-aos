package sdmed.extra.cso.utils.googleMap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MarkerClusterDataModel(
    var thisPK: String = "",
    var orgName: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var websiteUrl: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var zIndex: Float = 10F,
    var resDrawableId: Int = 0,
): ClusterItem {
    override fun getPosition() = LatLng(latitude, longitude)
    override fun getTitle() = orgName
    override fun getSnippet() = address.takeIf { it.isNotEmpty() }
    override fun getZIndex() = zIndex
}