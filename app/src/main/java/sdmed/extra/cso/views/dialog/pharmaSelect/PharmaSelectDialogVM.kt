package sdmed.extra.cso.views.dialog.pharmaSelect

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel

class PharmaSelectDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    val items = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val viewItems = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val searchString = MutableStateFlow<String>("")

    fun selectPharma(data: EDIPharmaBuffModel) {
        data.isSelect.value = !data.isSelect.value
        items.value.find { x -> x.thisPK == data.thisPK }?.isSelect?.value = data.isSelect.value
    }
    fun filterItem() {
        val searchBuff = searchString.value
        if (searchBuff.isEmpty()) {
            viewItems.value = items.value.toMutableList()
            return
        }

        viewItems.value = items.value.filter { x -> x.orgName.contains(searchBuff, true) }.toMutableList()
    }
    fun getSelectItems(): List<EDIPharmaBuffModel> {
        return items.value.filter { x -> x.isSelect.value }.toMutableList()
    }
    enum class ClickEvent(var index: Int) {
        CANCEL(0),
        CONFIRM(1)
    }
}