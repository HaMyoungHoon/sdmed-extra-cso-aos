package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.users.UserFileType

data class UserFileResultQueueModel(
    var uuid: String = "",
    var currentMedia: BlobUploadModel = BlobUploadModel(),
    var itemIndex: Int = -1,
    var itemCount: Int = 0,
    var medias: MutableList<BlobUploadModel> = mutableListOf(),
    var mediaIndexList: MutableList<Int> = mutableListOf(),
    var mediaTypeIndex: Int = 0,
) {
    fun readyToSend() = itemCount <= 0
    fun appendItemPath(media: BlobUploadModel, itemIndex: Int) {
        medias.add(BlobUploadModel().copy(media))
        mediaIndexList.add(itemIndex)
        itemCount--
    }
    fun parseBlobUploadModel(): BlobUploadModel {
        return medias.first()
    }
    fun userFileType() = UserFileType.parseIndex(mediaTypeIndex)
}