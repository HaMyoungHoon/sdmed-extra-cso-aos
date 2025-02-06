package sdmed.extra.cso.models.retrofit.common

enum class ContractType(var index: Int, var desc: String) {
    None(0, "미지정"),
    Veterinary(1, "수의계약"),
    Competitive(2, "경쟁입찰");

    companion object {
        fun parseString(data: String?) = entries.find { it.desc == data } ?: None
    }
}