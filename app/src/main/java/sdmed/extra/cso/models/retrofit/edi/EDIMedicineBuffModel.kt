package sdmed.extra.cso.models.retrofit.edi

data class EDIMedicineBuffModel(
    var thisPK: String = "",
    var code: String = "",
    var pharma: String = "",
    var name: String = "",
    var pharmaPK: String = "",
    var hosPK: String = "",
) {
}