package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

data class EDIFileResultQueueModel(
    var uuid: String = "",
    var ediPK: String = "",
    var currentMedia: EDIUploadFileModel = EDIUploadFileModel(),
    var itemIndex: Int = -1,
    var itemCount: Int = 0,
    var medias: MutableList<EDIUploadFileModel> = mutableListOf(),
    var mediaIndexList: MutableList<Int> = mutableListOf(),
    var ediUploadModel: EDIUploadModel = EDIUploadModel()
) {
    fun readyToSend() = itemCount <= 0
    fun appendItemPath(media: EDIUploadFileModel, itemIndex: Int) {
        medias.add(EDIUploadFileModel().copy(media))
        mediaIndexList.add(itemIndex)
        itemCount--
    }
    fun getMergeEDIUploadModel(): EDIUploadModel {
        ediUploadModel.fileList = medias
        return ediUploadModel
    }
}