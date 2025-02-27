package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.qna.QnAFileModel
import sdmed.extra.cso.utils.FExtensions

data class QnAAzureQueueModel(
    var uuid: String = "",
    var qnaPK: String = "",
    var media: MediaPickerSourceModel = MediaPickerSourceModel(),
    var qnaFileModel: QnAFileModel = QnAFileModel(),
    var mediaIndex: Int = 0
) {
    fun parse(keyQueue: QnASASKeyQueueModel, blobName: List<Pair<String, String>>, blobInfo: BlobStorageInfoModel): QnAAzureQueueModel? {
        val blobMediaName = blobName.find { y -> y.second == blobInfo.blobName }?.first
        val media = keyQueue.medias.find { y -> y.mediaName == blobMediaName }
        this.media = media ?: return null
        qnaFileModel = QnAFileModel().apply {
            this.blobUrl = "${blobInfo.blobUrl}/${blobInfo.blobContainerName}/${blobInfo.blobName}"
            this.sasKey = blobInfo.sasKey
            this.blobName = blobInfo.blobName
            this.originalFilename = media.mediaName
            this.mimeType = media.mediaMimeType
            this.regDate = FExtensions.getTodayString()
        }
        return this
    }
}