package sdmed.extra.cso.models.retrofit.qna

import android.text.Html

data class QnAContentModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var userPK: String = "",
    var content: String = "",
    var fileList: MutableList<QnAFileModel> = mutableListOf(),
    var replyList: MutableList<QnAReplyModel> = mutableListOf()
) {
    val htmlContentString get() = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
}