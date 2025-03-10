package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.utils.FExtensions

data class EDIAzureQueueModel(
    var uuid: String = "",
    var ediPK: String = "",
    var ediPharmaPK: String = "",
    var media: MediaPickerSourceModel = MediaPickerSourceModel(),
    var ediPharmaFileUploadModel: EDIUploadPharmaFileModel = EDIUploadPharmaFileModel(),
    var mediaIndex: Int = 0,
) {
    fun parse(pharma: EDIUploadPharmaModel, blobName: List<Pair<String, String>>, blobInfo: BlobStorageInfoModel): EDIAzureQueueModel? {
        val blobMediaName = blobName.find { y -> y.second == blobInfo.blobName }?.first
        val media = pharma.uploadItems.value.find { y -> y.mediaName == blobMediaName }
        this.media = media ?: return null
        ediPharmaFileUploadModel = EDIUploadPharmaFileModel().apply {
            this.pharmaPK = pharma.pharmaPK
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