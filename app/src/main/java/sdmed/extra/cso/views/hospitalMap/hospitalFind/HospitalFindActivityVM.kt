package sdmed.extra.cso.views.hospitalMap.hospitalFind

import sdmed.extra.cso.R
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalFindActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val hospitalTempRepository: IHospitalTempRepository by kodein.instance(IHospitalTempRepository::class)

    val searchLoading = MutableStateFlow(false)
    val searchBuff = MutableStateFlow<String?>("")
    var searchString: String = ""
    val mapVisible = MutableStateFlow(true)
    val nearbyAble = MutableStateFlow(false)
    val hospitalTempItems = MutableStateFlow(mutableListOf<HospitalTempModel>())
    val selectedHospitalTemp: MutableStateFlow<HospitalTempModel?> = MutableStateFlow(null)

    suspend fun getSearch(): RestResultT<MutableList<HospitalTempModel>> {
        if (searchString.length < 3) {
            return RestResultT<MutableList<HospitalTempModel>>().setFail(-1, context.getString(R.string.search_range_desc))
        }
        val ret = hospitalTempRepository.getListSearch(searchString)
        if (ret.result == true) {
            hospitalTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }
    suspend fun getNearby(latitude: Double, longitude: Double): RestResultT<MutableList<HospitalTempModel>> {
        val ret = hospitalTempRepository.getListNearby(latitude, longitude)
        if (ret.result == true) {
            hospitalTempItems.value = ret.data ?: mutableListOf()
        }
        return ret
    }
    fun selectHospital(data: HospitalTempModel) {
        val buff = hospitalTempItems.value.toMutableList()
        buff.filter { it.isSelect.value }.forEach { it.isSelect.value = false }
        if (selectedHospitalTemp.value?.thisPK == data.thisPK) {
            selectedHospitalTemp.value = null
        } else {
            buff.find { it.thisPK == data.thisPK }?.isSelect?.value = true
            selectedHospitalTemp.value = data
        }
        hospitalTempItems.value = buff
    }

    enum class ClickEvent(var index: Int) {
        SELECT(0),
        CLOSE(1),
        MAP_TOGGLE(2),
        NEARBY(3)
    }
}