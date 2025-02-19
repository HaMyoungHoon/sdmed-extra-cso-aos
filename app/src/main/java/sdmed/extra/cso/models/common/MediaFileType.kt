package sdmed.extra.cso.models.common

import sdmed.extra.cso.utils.FContentsType

enum class MediaFileType(val index: Int) {
    UNKNOWN(-1),
    IMAGE(0),
    VIDEO(1),
    PDF(2),
    EXCEL(3),
    ZIP(4);
    fun isVisible(): Boolean {
        if (this != VIDEO) return true

        return false
    }
    companion object {
        fun fromIndex(data: Int) = entries.firstOrNull { it.index == data } ?: IMAGE
        fun fromMimeType(mimeType: String): MediaFileType {
            return when (mimeType) {
                FContentsType.type_pdf -> PDF
                FContentsType.type_xlsx -> EXCEL
                FContentsType.type_xls -> EXCEL
                else -> UNKNOWN
            }
        }
    }
}