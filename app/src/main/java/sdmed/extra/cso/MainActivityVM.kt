package sdmed.extra.cso

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class MainActivityVM(application: MultiDexApplication): FBaseViewModel(application) {

    val ediMenuState = MutableStateFlow(false)
    val priceMenuState = MutableStateFlow(false)
    val qnaMenuState = MutableStateFlow(false)
    val myMenuState = MutableStateFlow(false)

    enum class ClickEvent(var index: Int) {
        EDI(0),
        PRICE(1),
        HOME(2),
        QNA(3),
        MY(4),
    }
}