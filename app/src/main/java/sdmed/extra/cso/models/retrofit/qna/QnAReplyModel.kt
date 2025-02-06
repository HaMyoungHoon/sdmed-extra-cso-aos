package sdmed.extra.cso.models.retrofit.qna

import java.util.Date

data class QnAReplyModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var userPK: String = "",
    var name: String = "",
    var content: String = "",
    var regDate: Date = Date(),
    var fileList: MutableList<QnAReplyFileModel> = mutableListOf()
) {
}