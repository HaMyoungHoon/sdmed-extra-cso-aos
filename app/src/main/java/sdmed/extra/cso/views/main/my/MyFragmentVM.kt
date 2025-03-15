package sdmed.extra.cso.views.main.my

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.models.retrofit.users.UserDataModel

class MyFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val myInfoRepository: IMyInfoRepository by kodein.instance(IMyInfoRepository::class)
    val thisData = MutableStateFlow(UserDataModel())
    val hosList = MutableStateFlow(mutableListOf<HospitalModel>())
    val selectedHos = MutableStateFlow<HospitalModel>(HospitalModel())
    val pharmaList = MutableStateFlow(mutableListOf<PharmaModel>())

    suspend fun getData(): RestResultT<UserDataModel> {
        val ret = myInfoRepository.getData()
        if (ret.result == true) {
            thisData.value = ret.data ?: UserDataModel()
        }
        return ret
    }

    enum class ClickEvent(var index: Int) {
        LOGOUT(0),
        PASSWORD_CHANGE(1),
        MULTI_LOGIN(2),
        IMAGE_TAXPAYER(3),
        IMAGE_BANK_ACCOUNT(4),
        IMAGE_CSO_REPORT(5),
        IMAGE_MARKETING_CONTRACT(6),
    }
}