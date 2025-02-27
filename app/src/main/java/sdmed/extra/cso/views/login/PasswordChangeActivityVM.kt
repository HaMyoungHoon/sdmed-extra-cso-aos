package sdmed.extra.cso.views.login

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserDataModel

class PasswordChangeActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val myInfoRepository: IMyInfoRepository by kodein.instance(IMyInfoRepository::class)
    val currentPW = MutableStateFlow<String>("")
    val afterPW = MutableStateFlow<String>("")
    val confirmPW = MutableStateFlow<String>("")
    val changeAble = MutableStateFlow(false)
    val afterPWRuleVisible = MutableStateFlow(false)
    val confirmPWRuleVisible = MutableStateFlow(false)
    val pwUnMatchVisible = MutableStateFlow(false)
    val pwInputFilter = MutableStateFlow("[a-zA-Zㄱ-ㅎ가-힣0-9()!%*#?&]+")

    suspend fun putPasswordChange(): RestResultT<UserDataModel> {
        val ret = myInfoRepository.putPasswordChange(currentPW.value, afterPW.value, confirmPW.value)
        return ret
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        CHANGE(1)
    }
}