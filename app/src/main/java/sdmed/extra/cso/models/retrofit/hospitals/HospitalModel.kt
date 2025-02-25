package sdmed.extra.cso.models.retrofit.hospitals

import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.retrofit.common.BillType
import sdmed.extra.cso.models.retrofit.common.ContractType
import sdmed.extra.cso.models.retrofit.common.DeliveryDiv
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import java.sql.Date
import java.util.*

data class HospitalModel(
    var thisPK: String = "",
    var code: String = "",
    var orgName: String = "",
    var innerName: String = "",
    var ownerName: String = "",
    var taxpayerNumber: String = "",
    var phoneNumber: String = "",
    var faxNumber: String = "",
    var zipCode: String = "",
    var address: String = "",
    var addressDetail: String = "",
    var businessType: String = "",
    var businessItem: String = "",
    var billType: BillType = BillType.None,
    var contractType: ContractType = ContractType.None,
    var deliveryDiv: DeliveryDiv = DeliveryDiv.None,
    var licenseNumber: String = "",
    var nursingHomeNumber: String = "",
    var mail: String = "",
    var mobilePhone: String = "",
    var openDate: Date? = null,
    var closeDate: Date? = null,
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