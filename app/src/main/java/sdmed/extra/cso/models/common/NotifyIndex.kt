package sdmed.extra.cso.models.common

enum class NotifyIndex(var index: Int) {
    UNKNOWN(0),
    EDI_UPLOAD(1),
    EDI_FILE_UPLOAD(2),
    EDI_FILE_REMOVE(3),
    EDI_RESPONSE(4),
    QNA_UPLOAD(5),
    QNA_FILE_UPLOAD(6),
    QNA_RESPONSE(7),
    USER_FILE_UPLOAD(8);
    companion object {
        fun parseIndex(index: Int?) = entries.find { x -> x.index == index } ?: UNKNOWN
    }
}