package sdmed.extra.cso.utils.googleMap

import sdmed.extra.cso.R
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import sdmed.extra.cso.databinding.CustomClusterViewBinding
import sdmed.extra.cso.utils.FImageUtils

class FClusterRenderer(val context: Context, val map: GoogleMap, clusterManager: ClusterManager<MarkerClusterDataModel>, val markerClusterClickListener: IMarkerClusterClickListener): DefaultClusterRenderer<MarkerClusterDataModel>(context, map, clusterManager) {
    override fun onBeforeClusterItemRendered(item: MarkerClusterDataModel, markerOptions: MarkerOptions) {
        val inflater = LayoutInflater.from(context)
        val view: CustomClusterViewBinding = DataBindingUtil.inflate(inflater, R.layout.custom_cluster_view, null, false)
        // 아 이거 왜 안됨
//            view.dataContext = item
        view.tvName.text = item.orgName
        if (item.resDrawableId != 0) {
            view.ivImage.setImageDrawable(context.getDrawable(item.resDrawableId))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(FImageUtils.getBitmapFromView(view.root)))
        }
        map.setOnMarkerClickListener { x ->
            val markerItem = getClusterItem(x)
            if (markerItem == null) {
                val cluster = getCluster(x)
                if (cluster != null) {
                    markerClusterClickListener.onClusterClickListener(cluster)
                }
            } else {
                markerClusterClickListener.onMarkerClickListener(markerItem)
            }
            true
        }
    }
    override fun onBeforeClusterRendered(cluster: Cluster<MarkerClusterDataModel>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
    }
}