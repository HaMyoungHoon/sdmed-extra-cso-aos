package sdmed.extra.cso.models.retrofit.edi

data class EDIApplyDateModel(
    var thisPK: String = "",
    var year: String = "",
    var month: String = "",
    var userPK: String = "",
    var applyDateState: EDIApplyDateState = EDIApplyDateState.None
) {
}