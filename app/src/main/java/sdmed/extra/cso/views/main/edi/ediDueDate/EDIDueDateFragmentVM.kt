package sdmed.extra.cso.views.main.edi.ediDueDate

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.fDate.FCalendar2
import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel
import sdmed.extra.cso.utils.FExtensions

class EDIDueDateFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediDueDateRepository: IEDIDueDateRepository by kodein.instance(IEDIDueDateRepository::class)
    val startDate = MutableStateFlow(FExtensions.getTodayString())
    val endDate = MutableStateFlow(FCalendar2().setThis(startDate.value).getMonthLastFromCalendar().getDate())
    val dateModel = MutableStateFlow(mutableListOf<EDIPharmaDueDateModel>())

    suspend fun getList(): RestResultT<List<EDIPharmaDueDateModel>> {
        val ret = ediDueDateRepository.getListRange(startDate.value, endDate.value)
        if (ret.result == true) {
            dateModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        START_DATE(0),
        END_DATE(1),
        SEARCH(2),
    }
}