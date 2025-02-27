package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.bases.FDataModelClass

data class QnAReplyFileModel(
    var thisPK: String = "",
    var replyPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
): FDataModelClass<QnAReplyFileModel.ClickEvent>() {
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}