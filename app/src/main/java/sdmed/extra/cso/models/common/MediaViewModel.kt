package sdmed.extra.cso.models.common

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FImageUtils

data class MediaViewModel(
    var blobUrl: MutableStateFlow<String> = MutableStateFlow(""),
    var originalFilename: MutableStateFlow<String> = MutableStateFlow(""),
    var mimeType: MutableStateFlow<String> = MutableStateFlow(""),
): FDataModelClass<MediaViewModel.ClickEvent>() {
    val getFileExt get() = FContentsType.getExtMimeType(mimeType.value)
    val isImage get() = FImageUtils.isImage(getFileExt)
    val isPdf get() = FImageUtils.isPdf(getFileExt)
    val isExcel get() = FImageUtils.isExcel(getFileExt)

    fun parse(data: MediaViewParcelModel): MediaViewModel {
        this.blobUrl.value = data.blobUrl
        this.originalFilename.value = data.originalFilename
        this.mimeType.value = data.mimeType

        return this
    }

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}