package sdmed.extra.cso.views.media.view

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel

class MediaViewActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val title = MutableStateFlow<String>("")
    val item = MutableStateFlow(MediaViewModel())

    fun setItemData(data: MediaViewParcelModel?) {
        data ?: return
        item.value = MediaViewModel().parse(data)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}