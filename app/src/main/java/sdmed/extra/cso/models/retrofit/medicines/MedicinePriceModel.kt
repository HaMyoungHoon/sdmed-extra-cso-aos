package sdmed.extra.cso.models.retrofit.medicines

data class MedicinePriceModel(
    var thisPK: String = "",
    var kdCode: String = "",
    var maxPrice: Int = 0,
    var ancestorCode: String = "",
    var applyDate: String = ""
) {
}