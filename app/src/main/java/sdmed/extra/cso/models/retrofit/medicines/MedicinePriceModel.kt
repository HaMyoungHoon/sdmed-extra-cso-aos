package sdmed.extra.cso.models.retrofit.medicines

import java.util.Date

data class MedicinePriceModel(
    var thisPK: String = "",
    var kdCode: String = "",
    var maxPrice: Int = 0,
    var ancestorCode: String = "",
    var applyDate: Date = Date(),
) {
}