package sdmed.extra.cso.views.dialog.pharmacyTemp

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

class PharmacyTempListDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val items = MutableStateFlow(mutableListOf<PharmacyTempModel>())

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}