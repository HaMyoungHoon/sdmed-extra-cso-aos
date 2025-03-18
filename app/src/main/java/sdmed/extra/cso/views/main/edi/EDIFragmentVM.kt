package sdmed.extra.cso.views.main.edi

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import kotlin.getValue

class EDIFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediListRepository: IEDIListRepository by kodein.instance(IEDIListRepository::class)

    val isRefreshing = MutableStateFlow(false)
    val startDate = MutableStateFlow(FExtensions.getToday().addMonth(-1).toString("yyyy-MM-dd"))
    val endDate = MutableStateFlow(FExtensions.getTodayString())
    val ediUploadModel = MutableStateFlow(mutableListOf<EDIUploadModel>())

    suspend fun getList(): RestResultT<List<EDIUploadModel>> {
        isRefreshing.value = true
        val ret = ediListRepository.getList(startDate.value, endDate.value)
        if (ret.result == true) {
            ediUploadModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        isRefreshing.value = false
        return ret
    }
    fun refreshingTemp() {
        FCoroutineUtil.coroutineScope({
            getList()
        })
    }

    enum class ClickEvent(var index: Int) {
        START_DATE(0),
        END_DATE(1),
        SEARCH(2),
    }
}