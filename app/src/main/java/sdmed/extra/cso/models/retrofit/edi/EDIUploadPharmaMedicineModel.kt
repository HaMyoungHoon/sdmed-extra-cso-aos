package sdmed.extra.cso.models.retrofit.edi

data class EDIUploadPharmaMedicineModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var makerCode: String = "",
    var medicinePK: String = "",
    var name: String = "",
    var count: Int = 0,
    var price: Int = 0,
    var charge: Int = 50,
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var inVisible: Boolean = false
) {
}