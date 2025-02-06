package sdmed.extra.cso.models.retrofit.pharmas

import sdmed.extra.cso.models.retrofit.common.BillType
import sdmed.extra.cso.models.retrofit.common.ContractType
import sdmed.extra.cso.models.retrofit.common.DeliveryDiv
import sdmed.extra.cso.models.retrofit.common.PharmaGroup
import sdmed.extra.cso.models.retrofit.common.PharmaType
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import java.sql.Date
import java.util.*

data class PharmaModel(
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
    var pharmaType: PharmaType = PharmaType.None,
    var pharmaGroup: PharmaGroup = PharmaGroup.None,
    var contractType: ContractType = ContractType.None,
    var deliveryDiv: DeliveryDiv = DeliveryDiv.None,
    var mail: String = "",
    var mobilePhone: String = "",
    var openDate: Date? = null,
    var closeDate: Date? = null,
    var etc1: String = "",
    var etc2: String = "",
    var imageUrl: String = "",
    var inVisible: Boolean = false,
    var medicineList: MutableList<MedicineModel> = mutableListOf(),
    var relationMedicineList: MutableList<MedicineModel> = mutableListOf(),
) {
}