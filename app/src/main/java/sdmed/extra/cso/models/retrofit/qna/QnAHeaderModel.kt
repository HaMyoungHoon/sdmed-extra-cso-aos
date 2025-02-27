package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.bases.FDataModelClass
import java.sql.Timestamp
import java.util.Date

data class QnAHeaderModel(
    var thisPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var title: String = "",
    var regDate: Timestamp = Timestamp(Date().time),
    var qnaState: QnAState = QnAState.None,
): FDataModelClass<QnAHeaderModel.ClickEvent>() {
    val qnaColor: Int get() = qnaState.parseQnAColor()
    val qnaBackColor: Int get() = qnaState.parseQnABackColor()

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}