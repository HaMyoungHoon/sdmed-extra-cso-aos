package sdmed.extra.cso.models.retrofit.edi

data class EDIPharmaDueDateModel(
    var thisPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var regDate: String = ""
) {
}