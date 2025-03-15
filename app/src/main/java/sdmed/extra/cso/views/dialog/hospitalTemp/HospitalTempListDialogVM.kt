package sdmed.extra.cso.views.dialog.hospitalTemp

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempListDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val items = MutableStateFlow(mutableListOf<HospitalTempModel>())

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}