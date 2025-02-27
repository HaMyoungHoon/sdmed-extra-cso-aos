package sdmed.extra.cso.views.main.qna

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel

class QnAFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val qnaListRepository: IQnAListRepository by kodein.instance(IQnAListRepository::class)
    val searchBuff = MutableStateFlow<String?>(null)
    var searchString = ""
    val searchLoading = MutableStateFlow(false)
    val qnaModel = MutableStateFlow(mutableListOf<QnAHeaderModel>())
    val paginationModel = MutableStateFlow(PaginationModel())
    val page = MutableStateFlow(0)
    val size = MutableStateFlow(20)

    suspend fun getList(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        page.value = 0
        val ret = qnaListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun getLike(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        page.value = 0
        val ret = qnaListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
            paginationModel.value = PaginationModel().init(ret.data)
        }
        return ret
    }
    suspend fun addList(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        val ret = qnaListRepository.getList(page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
        }
        return ret
    }
    suspend fun addLike(): RestResultT<RestPage<MutableList<QnAHeaderModel>>> {
        page.value = 0
        val ret = qnaListRepository.getLike(searchString, page.value, size.value)
        if (ret.result == true) {
            qnaModel.value = ret.data?.content ?: mutableListOf()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        ADD(0)
    }
}