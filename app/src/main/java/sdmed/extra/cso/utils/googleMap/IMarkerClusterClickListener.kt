package sdmed.extra.cso.utils.googleMap

import com.google.maps.android.clustering.Cluster

interface IMarkerClusterClickListener {
    fun onMarkerClickListener(clusterItem: MarkerClusterDataModel)
    fun onClusterClickListener(cluster: Cluster<MarkerClusterDataModel>)
}