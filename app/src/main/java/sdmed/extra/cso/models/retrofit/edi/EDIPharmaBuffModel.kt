package sdmed.extra.cso.models.retrofit.edi

data class EDIPharmaBuffModel(
    var thisPK: String = "",
    var hosPK: String = "",
    var code: String = "",
    var orgName: String = "",
    var innerName: String = "",
) {
    var medicineList: MutableList<EDIMedicineBuffModel> = mutableListOf()
}