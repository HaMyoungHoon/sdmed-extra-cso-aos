package sdmed.extra.cso.views.main.landing

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel

class LandingActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val startVisible = MutableStateFlow(false)
    fun versionCheck(ret: (RestResultT<List<VersionCheckModel>>) -> Unit) {
        FCoroutineUtil.coroutineScope({
            commonRepository.versionCheck()
        }, { ret(it) })
    }
    enum class ClickEvent {
        START
    }
}