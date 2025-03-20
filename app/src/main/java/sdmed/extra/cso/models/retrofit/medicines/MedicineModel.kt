package sdmed.extra.cso.models.retrofit.medicines

import sdmed.extra.cso.bases.FDataModelClass

data class MedicineModel(
    var thisPK: String = "",
    var makerName: String? = null,
    var orgName: String = "",
    var innerName: String = "",
    var kdCode: String = "",
    var customPrice: Int = 0,
    var charge: Int = 50,
    var standard: String = "",
    var etc1: String = "",
    var mainIngredientCode: String = "",
    var code: String = "",
    var makerCode: String = "",
    var medicineDiv: MedicineDiv = MedicineDiv.Open,
    var inVisible: Boolean = false,
    var clientName: String? = null,
    var maxPrice: Int = 0,
    var medicineIngredientModel: MedicineIngredientModel = MedicineIngredientModel(),
    var medicinePriceModel: MutableList<MedicinePriceModel> = mutableListOf(),
): FDataModelClass<MedicineModel.ClickEvent>() {
    val maxPriceString: String get() = "$maxPrice"

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}