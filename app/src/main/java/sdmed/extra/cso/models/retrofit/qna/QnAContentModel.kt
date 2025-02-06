package sdmed.extra.cso.models.retrofit.qna

data class QnAContentModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var userPK: String = "",
    var content: String = "",
    var fileList: MutableList<QnAFileModel> = mutableListOf(),
    var replyList: MutableList<QnAReplyModel> = mutableListOf()
) {
}