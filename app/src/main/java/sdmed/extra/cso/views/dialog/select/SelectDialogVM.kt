package sdmed.extra.cso.views.dialog.select

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.SelectListModel

class SelectDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val items = MutableStateFlow(mutableListOf<SelectListModel>())

    enum class ClickEvent(var index: Int) {
        SELECT(0)
    }
}