package sdmed.extra.cso.views.main.price

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

class PriceFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val priceListRepository: IMedicinePriceListRepository by kodein.instance(IMedicinePriceListRepository::class)
    var searchString = ""
    val searchBuff = MutableStateFlow<String?>(null)
    val searchLoading = MutableStateFlow(false)
    val page = MutableStateFlow(0)
    val size = MutableStateFlow(20)
    val medicineModel = MutableStateFlow(mutableListOf<MedicineModel>())
    val paginationModel = MutableStateFlow(PaginationModel())

    suspend fun getList(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        page.value = 0
        val ret = priceListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun getLike(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        page.value = 0
        val ret = priceListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun addList(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        val ret = priceListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
//            val medicineBuff = medicineModel.value.toMutableList()
//            medicineBuff.addAll(ret.data?.content ?: mutableListOf())
//            medicineBuff.distinctBy { it.thisPK }
//            medicineModel.value = medicineBuff
        }
        return ret
    }
    suspend fun addLike(): RestResultT<RestPage<MutableList<MedicineModel>>> {
        val ret = priceListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            medicineModel.value = ret.data?.content ?: mutableListOf()
//            val medicineBuff = medicineModel.value.toMutableList()
//            medicineBuff.addAll(ret.data?.content ?: mutableListOf())
//            medicineBuff.distinctBy { it.thisPK }
//            medicineModel.value = medicineBuff
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        SEARCH(0)
    }
}