package sdmed.extra.cso.models.retrofit.qna

import java.util.Date

data class QnAFileModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: Date = Date()
) {
}