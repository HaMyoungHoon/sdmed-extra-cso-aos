package sdmed.extra.cso.models.retrofit.medicines

data class MedicineModel(
    var thisPK: String = "",
    var code: String = "",
    var mainIngredientCode: String = "",
    var kdCode: String = "",
    var standardCode: Long = 0L,
    var makerCode: String = "",
    var name: String = "",
    var customPrice: Int = 0,
    var charge: Int = 50,
    var inVisible: Boolean = false,
    var maxPrice: Int = 0,
    var medicineSubModel: MedicineSubModel = MedicineSubModel(),
    var medicineIngredientModel: MedicineIngredientModel = MedicineIngredientModel(),
    var medicinePriceModel: MutableList<MedicinePriceModel> = mutableListOf(),
) {
}