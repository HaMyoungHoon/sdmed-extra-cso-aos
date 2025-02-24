package sdmed.extra.cso.models.retrofit.medicines

import sdmed.extra.cso.bases.FDataModelClass

data class MedicineModel(
    var thisPK: String = "",
    var code: String = "",
    var mainIngredientCode: String = "",
    var kdCode: String = "",
    var standardCode: Long = 0L,
    var clientName: String? = null,
    var makerName: String? = null,
    var makerCode: String = "",
    var orgName: String = "",
    var innerName: String = "",
    var customPrice: Int = 0,
    var charge: Int = 50,
    var inVisible: Boolean = false,
    var maxPrice: Int = 0,
    var medicineSubModel: MedicineSubModel = MedicineSubModel(),
    var medicineIngredientModel: MedicineIngredientModel = MedicineIngredientModel(),
    var medicinePriceModel: MutableList<MedicinePriceModel> = mutableListOf(),
): FDataModelClass<MedicineModel.ClickEvent>() {
    val maxPriceString: String get() = "$maxPrice"

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}