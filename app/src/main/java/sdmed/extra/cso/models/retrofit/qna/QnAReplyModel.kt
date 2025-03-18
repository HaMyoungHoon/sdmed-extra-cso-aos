package sdmed.extra.cso.models.retrofit.qna

import android.text.Html
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.EllipseItemModel
import java.util.Date

data class QnAReplyModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var content: String = "",
    var regDate: Date = Date(),
    var fileList: MutableList<QnAReplyFileModel> = mutableListOf()
): FDataModelClass<QnAReplyModel.ClickEvent>() {
    var open = MutableStateFlow(false)
    val htmlContentString get() = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
    val currentPosition = MutableStateFlow<Int>(1)
    val positionString: StateFlow<String> = currentPosition.map { "${it}/${fileList.size}" }
        .stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob()), SharingStarted.Lazily, "${currentPosition.value}/${fileList.size}")
    enum class ClickEvent(var index: Int) {
        OPEN(0),
    }
}