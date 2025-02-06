package sdmed.extra.cso.models.retrofit.edi

data class EDIHosBuffModel(
    var thisPK: String = "",
    var orgName: String = "",
    var pharmaList: MutableList<EDIPharmaBuffModel> = mutableListOf()
) {
}