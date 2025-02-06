package sdmed.extra.cso.models.retrofit.edi

data class EDIUploadPharmaModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var isCarriedOver: Boolean = false,
    var ediState: EDIState = EDIState.None,
    var medicineList: MutableList<EDIUploadPharmaMedicineModel> = mutableListOf(),
) {
}