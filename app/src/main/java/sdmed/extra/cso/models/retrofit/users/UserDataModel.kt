package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import java.sql.Timestamp
import java.util.Date

data class UserDataModel(
    var thisPK: String = "",
    var id: String = "",
    var pw: String = "",
    var name: String = "",
    var mail: String = "",
    var phoneNumber: String = "",
    var role: Int = 0,
    var dept: Int = 0,
    var status: UserStatus = UserStatus.None,
    var companyName: String = "",
    var companyNumber: String = "",
    var companyAddress: String = "",
    var bankAccount: String = "",
    var regDate: Timestamp = Timestamp(Date().time),
    var lastLoginDate: Timestamp? = null,
    var motherPK: String = "",
    var children: MutableList<UserDataModel> = mutableListOf(),
    var hosList: MutableList<HospitalModel> = mutableListOf(),
    var fileList: MutableList<UserFileModel> = mutableListOf(),
) {
}