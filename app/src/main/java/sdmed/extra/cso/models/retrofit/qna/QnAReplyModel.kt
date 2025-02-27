package sdmed.extra.cso.models.retrofit.qna

import android.text.Html
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.EllipseItemModel
import java.sql.Timestamp
import java.util.Date

data class QnAReplyModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var content: String = "",
    var regDate: Timestamp = Timestamp(Date().time),
    var fileList: MutableList<QnAReplyFileModel> = mutableListOf()
): FDataModelClass<QnAReplyModel.ClickEvent>() {
    var open = MutableStateFlow(false)
    val htmlContentString get() = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
    val ellipseList = MutableStateFlow(mutableListOf<EllipseItemModel>())
    fun initEllipseList() {
        val ellipseBuff = mutableListOf<EllipseItemModel>()
        fileList.forEach { ellipseBuff.add(EllipseItemModel()) }
        ellipseList.value = ellipseBuff
    }
    enum class ClickEvent(var index: Int) {
        OPEN(0),
    }
}