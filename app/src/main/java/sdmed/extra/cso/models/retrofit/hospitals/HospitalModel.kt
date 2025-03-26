package sdmed.extra.cso.models.retrofit.hospitals

import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel

data class HospitalModel(
    var thisPK: String = "",
    var code: String = "",
    var innerName: String = "",
    var orgName: String = "",
    var ownerName: String = "",
    var taxpayerNumber: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var faxNumber: String = "",
    var zipCode: String = "",
    var businessType: String = "",
    var businessItem: String = "",
    var nursingHomeNumber: String = "",
    var etc1: String = "",
    var etc2: String = "",
    var imageUrl: String = "",
    var inVisible: Boolean = false,
    var pharmaList: MutableList<PharmaModel> = mutableListOf(),
): FDataModelClass<HospitalModel.ClickEvent>() {

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}