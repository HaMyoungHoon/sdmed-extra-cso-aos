package sdmed.extra.cso.views.login

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.models.RestResultT

class LoginActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val loginID = MutableStateFlow("")
    val loginPW = MutableStateFlow("")
    val fillDataState = MutableStateFlow(false)
    val idInputFilter = MutableStateFlow("[a-zA-Z가-힣0-9]+")
    val pwInputFilter = MutableStateFlow("[a-zA-Zㄱ-ㅎ가-힣0-9()!%*#?&]+")

    fun signIn(ret: (RestResultT<String>) -> Unit) {
        FCoroutineUtil.coroutineScope({
            commonRepository.signIn(loginID.value, loginPW.value)
        }, { ret(it) })
    }

    enum class ClickEvent(var index: Int) {
        FORGOT_ID(0),
        FORGOT_PW(1),
        SIGN_IN(2)
    }
}