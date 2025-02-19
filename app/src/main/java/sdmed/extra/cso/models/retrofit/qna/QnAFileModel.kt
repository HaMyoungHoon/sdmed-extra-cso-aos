package sdmed.extra.cso.models.retrofit.qna

data class QnAFileModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
) {
}