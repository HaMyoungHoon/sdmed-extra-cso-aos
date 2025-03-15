package sdmed.extra.cso.views.dialog.pharmacyTemp

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

class PharmacyTempDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val item = MutableStateFlow(PharmacyTempModel())
    enum class ClickEvent(var index: Int) {
        THIS(0),
        PHONE_NUMBER(1)
    }
}