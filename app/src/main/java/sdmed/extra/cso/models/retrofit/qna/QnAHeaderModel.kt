package sdmed.extra.cso.models.retrofit.qna

data class QnAHeaderModel(
    var thisPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var title: String = "",
    var regDate: String = "",
    var qnaState: QnAState = QnAState.None,
) {
}