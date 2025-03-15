package sdmed.extra.cso.views.dialog.bottomLogin

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel

class BottomLoginDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val items = MutableStateFlow(mutableListOf<UserMultiLoginModel>())
    val isAddVisible = MutableStateFlow(false)

    suspend fun multiSign(token: String): RestResultT<String> {
        return commonRepository.multiSign(token)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        ADD(1)
    }
}