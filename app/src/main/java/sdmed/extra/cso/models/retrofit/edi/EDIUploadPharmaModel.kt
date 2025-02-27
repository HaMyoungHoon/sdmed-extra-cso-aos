package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.bases.FDataModelClass

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
): FDataModelClass<EDIUploadPharmaModel.ClickEvent>() {

    fun getYearMonth() = "${year}-${month}"
    fun getEdiColor() = ediState.parseEDIColor()
    fun getEdiBackColor() = ediState.parseEDIBackColor()
    enum class ClickEvent(var index: Int) {
    }
}