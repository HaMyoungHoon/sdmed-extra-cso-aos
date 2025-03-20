package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.utils.FExtensions

data class UserTrainingFileAzureQueueModel(
    var uuid: String = "",
    var media: MediaPickerSourceModel = MediaPickerSourceModel(),
    var userFileModel: BlobUploadModel = BlobUploadModel(),
    var trainingDate: String = "",
) {
    fun parse(keyQueue: UserTrainingFileSASKeyQueueModel, blobInfo: BlobStorageInfoModel): UserTrainingFileAzureQueueModel? {
        this.media = keyQueue.media
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