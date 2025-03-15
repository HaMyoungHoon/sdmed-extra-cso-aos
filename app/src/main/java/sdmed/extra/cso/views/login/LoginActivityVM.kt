package sdmed.extra.cso.views.login

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel

class LoginActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val loginID = MutableStateFlow("")
    val loginPW = MutableStateFlow("")
    val fillDataState = MutableStateFlow(false)
    val idInputFilter = MutableStateFlow("[a-zA-Z가-힣0-9]+")
    val pwInputFilter = MutableStateFlow("[a-zA-Zㄱ-ㅎ가-힣0-9()!%*#?&]+")
    val multiSignItems = MutableStateFlow(mutableListOf<UserMultiLoginModel>())

    suspend fun signIn(): RestResultT<String> {
        return commonRepository.signIn(loginID.value, loginPW.value)
    }
    suspend fun multiSign(token: String): RestResultT<String> {
        return commonRepository.multiSign(token)
    }

    enum class ClickEvent(var index: Int) {
        FORGOT_ID(0),
        FORGOT_PW(1),
        SIGN_IN(2),
        MULTI_LOGIN(3)
    }
}