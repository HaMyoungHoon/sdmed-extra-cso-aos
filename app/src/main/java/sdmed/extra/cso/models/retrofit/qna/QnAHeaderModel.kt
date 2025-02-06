package sdmed.extra.cso.models.retrofit.qna

import java.util.Date

data class QnAHeaderModel(
    var thisPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var title: String = "",
    var regDate: Date = Date(),
    var qnaState: QnAState = QnAState.None,
) {
}