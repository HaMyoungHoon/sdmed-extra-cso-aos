package sdmed.extra.cso.models.retrofit.medicines

data class MedicineSubModel(
    var thisPK: String = "",
    var code: String = "",
    var standard: String = "",
    var accountUnit: Double = 0.0,
    var medicineType: MedicineType = MedicineType.General,
    var medicineMethod: MedicineMethod = MedicineMethod.ETC,
    var medicineCategory: MedicineCategory = MedicineCategory.ETC,
    var medicineGroup: MedicineGroup = MedicineGroup.Medicine,
    var medicineDiv: MedicineDiv = MedicineDiv.Open,
    var medicineRank: MedicineRank = MedicineRank.None,
    var medicineStorageTemp: MedicineStorageTemp = MedicineStorageTemp.RoomTemp,
    var medicineStorageBox: MedicineStorageBox = MedicineStorageBox.Confidential,
    var packageUnit: Int = 0,
    var unit: String = "",
    var etc1: String = "",
    var etc2: String = "",
) {
}