package sdmed.extra.cso.views.main.qna.qnaView

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EllipseItemModel
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel

class QnAViewActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val qnaListRepository: IQnAListRepository by kodein.instance(IQnAListRepository::class)
    var thisPK: String = ""
    val headerModel = MutableStateFlow(QnAHeaderModel())
    val contentModel = MutableStateFlow(QnAContentModel())
    val ellipseList = MutableStateFlow(mutableListOf<EllipseItemModel>())
    val collapseContent = MutableStateFlow(true)
    suspend fun getData(): RestResultT<QnAHeaderModel> {
        val ret = qnaListRepository.getHeaderData(thisPK)
        if (ret.result == true) {
            headerModel.value = ret.data ?: QnAHeaderModel()
            val contentRet = qnaListRepository.getContentData(thisPK)
            if (contentRet.result == true) {
                val contentBuff = contentRet.data ?: QnAContentModel()
                contentModel.value = contentBuff
                val ellipseBuff = mutableListOf<EllipseItemModel>()
                contentModel.value.fileList.forEach { ellipseBuff.add(EllipseItemModel()) }
                ellipseList.value = ellipseBuff
            } else {
                ret.result = contentRet.result
                ret.msg = contentRet.msg
            }
        }
        return ret
    }
    suspend fun postData(): RestResultT<QnAHeaderModel> {
        val ret = qnaListRepository.putData(thisPK)
        if (ret.result == true) {
            headerModel.value = ret.data ?: QnAHeaderModel()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        COLLAPSE(1),
        ADD(2),
        COMP(3),
    }
}