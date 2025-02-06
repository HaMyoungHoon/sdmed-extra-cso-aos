package sdmed.extra.cso.models.common

enum class MediaFileType(val index: Int) {
    UNKNOWN(-1),
    IMAGE(0),
    VIDEO(1);
    companion object {
        fun fromIndex(data: Int) = entries.firstOrNull { it.index == data } ?: IMAGE
    }
}