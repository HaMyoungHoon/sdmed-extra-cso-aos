package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyFileModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel

data class QnAResultQueueModel(
    var uuid: String = "",
    var qnaPK: String = "",
    var currentMedia: QnAFileModel = QnAFileModel(),
    var itemIndex: Int = -1,
    var itemCount: Int = 0,
    var medias: MutableList<QnAFileModel> = mutableListOf(),
    var mediaIndexList: MutableList<Int> = mutableListOf(),
    var title: String = "",
    var content: String = ""
) {
    fun readyToSend() = itemCount <= 0
    fun appendItemPath(media: QnAFileModel, itemIndex: Int) {
        medias.add(QnAFileModel().copy(media))
        mediaIndexList.add(itemIndex)
        itemCount--
    }

    fun parsePostData(): QnAContentModel {
        val ret = QnAContentModel()
        ret.content = this.content
        ret.fileList.addAll(medias)
        return ret
    }
    fun parsePostReply() = QnAReplyModel(content = content).apply {
        fileList.addFile(medias)
    }

    fun MutableList<QnAReplyFileModel>.addFile(items: MutableList<QnAFileModel>): MutableList<QnAReplyFileModel> {
        val ret = this
        items.forEach { x ->
            ret.add(QnAReplyFileModel().apply {
                this.blobUrl = x.blobUrl
                this.originalFilename = x.originalFilename
                this.mimeType = x.mimeType
                this.regDate = x.regDate
            })
        }
        return ret
    }
}