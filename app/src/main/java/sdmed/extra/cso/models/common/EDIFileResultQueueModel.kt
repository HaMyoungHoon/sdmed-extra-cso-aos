package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel

data class EDIFileResultQueueModel(
    var uuid: String = "",
    var ediPK: String = "",
    var ediPharmaPK: String = "",
    var currentMedia: EDIUploadPharmaFileModel = EDIUploadPharmaFileModel(),
    var itemIndex: Int = -1,
    var itemCount: Int = 0,
    var medias: MutableList<EDIUploadPharmaFileModel> = mutableListOf(),
    var mediaIndexList: MutableList<Int> = mutableListOf(),
    var ediUploadModel: EDIUploadModel = EDIUploadModel()
) {
    fun readyToSend() = itemCount <= 0
    fun appendItemPath(media: EDIUploadPharmaFileModel, itemIndex: Int) {
        medias.add(EDIUploadPharmaFileModel().copy(media))
        mediaIndexList.add(itemIndex)
        itemCount--
        ediUploadModel.pharmaList.find { x -> x.pharmaPK == media.pharmaPK }?.fileList?.add(media)
    }
    fun parseEDIUploadModel(): EDIUploadModel {
        return ediUploadModel
    }
}