package sdmed.extra.cso.models.retrofit.qna

import java.util.Date

data class QnAReplyFileModel(
    var thisPK: String = "",
    var replyPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: Date = Date()
) {
}