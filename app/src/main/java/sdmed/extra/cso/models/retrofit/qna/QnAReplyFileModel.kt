package sdmed.extra.cso.models.retrofit.qna

data class QnAReplyFileModel(
    var thisPK: String = "",
    var replyPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
) {
}