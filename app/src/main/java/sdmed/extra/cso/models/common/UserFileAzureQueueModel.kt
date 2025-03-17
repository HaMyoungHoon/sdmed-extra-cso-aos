package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.utils.FExtensions

data class UserFileAzureQueueModel(
    var uuid: String = "",
    var media: MediaPickerSourceModel = MediaPickerSourceModel(),
    var userFileModel: BlobUploadModel = BlobUploadModel(),
    var mediaTypeIndex: Int = 0
) {
    fun parse(keyQueue: UserFileSASKeyQueueModel, blobName: List<Pair<String, String>>, blobInfo: BlobStorageInfoModel): UserFileAzureQueueModel? {
        val blobMediaName = blobName.find { y -> y.second == blobInfo.blobName }?.first
        val media = keyQueue.medias.find { y -> y.mediaName == blobMediaName }
        this.media = media ?: return null
        userFileModel = BlobUploadModel().apply {
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