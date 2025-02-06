package sdmed.extra.cso.views.dialog.message

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class MessageDialogVM(multiDexApplication: MultiDexApplication): FBaseViewModel(multiDexApplication) {
    val title = MutableStateFlow("")
    val leftBtnText = MutableStateFlow("")
    val rightBtnText = MutableStateFlow("")
    val leftBtnVisible = MutableStateFlow(true)
    val rightBtnVisible = MutableStateFlow(true)
    val leftBtnTextColor = MutableStateFlow(0)
    val rightBtnTextColor = MutableStateFlow(0)

    enum class ClickEvent {
        LEFT,
        RIGHT
    }
}