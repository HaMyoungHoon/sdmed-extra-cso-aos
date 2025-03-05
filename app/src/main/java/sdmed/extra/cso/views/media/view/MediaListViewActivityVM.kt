package sdmed.extra.cso.views.media.view

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel

class MediaListViewActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val title = MutableStateFlow<String>("")
    val items = MutableStateFlow(mutableListOf<MediaViewModel>())

    fun setItemData(data: ArrayList<MediaViewParcelModel>?) {
        data ?: return
        val ret = mutableListOf<MediaViewModel>()
        data.toMutableList().forEach {
            ret.add(MediaViewModel().parse(it))
        }
        items.value = ret
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}