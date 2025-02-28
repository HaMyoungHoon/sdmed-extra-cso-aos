package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.multidex.MultiDexApplication
import sdmed.extra.cso.bases.FBaseViewModel

class EDIRequestActivityVM(application: MultiDexApplication): FBaseViewModel(application) {

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}