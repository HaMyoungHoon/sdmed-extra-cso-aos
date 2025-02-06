package sdmed.extra.cso.models.common

enum class MediaResType(val index: Int) {
    UNKNOWN(-1),
    URI(0),
    URL(1);
    companion object {
        fun fromIndex(data: Int) = entries.firstOrNull { it.index == data } ?: UNKNOWN
        fun fromIndex(data: String): MediaResType {
            val index = try {
                data.toInt()
            } catch (_: Exception) {
                return UNKNOWN
            }
            return fromIndex(index)
        }
    }
}