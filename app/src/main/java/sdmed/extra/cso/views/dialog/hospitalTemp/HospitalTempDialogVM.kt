package sdmed.extra.cso.views.dialog.hospitalTemp

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val item = MutableStateFlow(HospitalTempModel())

    enum class ClickEvent(var index: Int) {
        THIS(0),
        WEBSITE(1),
        PHONE_NUMBER(2)
    }
}