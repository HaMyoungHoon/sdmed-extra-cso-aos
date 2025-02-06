package sdmed.extra.cso.views.dialog.loading

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel

class LoadingDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val msg = MutableStateFlow(" ")
}