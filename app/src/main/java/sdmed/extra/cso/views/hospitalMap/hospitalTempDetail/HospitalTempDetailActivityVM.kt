package sdmed.extra.cso.views.hospitalMap.hospitalTempDetail

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import kotlin.getValue

class HospitalTempDetailActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val hospitalTempRepository: IHospitalTempRepository by kodein.instance(IHospitalTempRepository::class)
    var hospitalPK: String = ""
    val mapVisible = MutableStateFlow(true)
    val pharmacyToggle = MutableStateFlow(true)
    val hospitalTempItem = MutableStateFlow(HospitalTempModel())
    val pharmacyTempItems = MutableStateFlow(mutableListOf<PharmacyTempModel>())

    suspend fun getData(): RestResultT<HospitalTempModel> {
        val ret = hospitalTempRepository.getData(hospitalPK)
        if (ret.result == true) {
            hospitalTempItem.value = ret.data ?: HospitalTempModel()
        }
        return ret
    }
    suspend fun getNearby(): RestResultT<MutableList<PharmacyTempModel>> {
        val ret = hospitalTempRepository.getPharmacyListNearby(hospitalTempItem.value.latitude, hospitalTempItem.value.longitude)
        if (ret.result == true) {
            pharmacyTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        MAP_TOGGLE(1),
        PHARMACY_TOGGLE(2),
    }
}