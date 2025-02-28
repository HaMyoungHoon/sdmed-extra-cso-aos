package sdmed.extra.cso.views.dialog.pharmaSelect

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.PharmaSelectDialogBinding
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import java.util.ArrayList

class PharmaSelectDialog(val items: List<EDIPharmaBuffModel>, val ret: (List<EDIPharmaBuffModel>) -> Unit): FBaseDialogFragment<PharmaSelectDialogBinding, PharmaSelectDialogVM>() {
    override var layoutId = R.layout.pharma_select_dialog
    override val dataContext: PharmaSelectDialogVM by lazy {
        PharmaSelectDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        dataContext.items.value = items.toMutableList()
        dataContext.viewItems.value = items.toMutableList()
        super.viewInit()
        setPharmaAdapter()
        observeText()
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setPharmaCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? PharmaSelectDialogVM.ClickEvent ?: return
        when (eventName) {
            PharmaSelectDialogVM.ClickEvent.CANCEL -> { dismiss() }
            PharmaSelectDialogVM.ClickEvent.CONFIRM -> {
                ret(dataContext.getSelectItems())
                dismiss()
            }
        }
    }
    private fun setPharmaCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIPharmaBuffModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIPharmaBuffModel ?: return
        when (eventName) {
            EDIPharmaBuffModel.ClickEvent.THIS -> selectPharma(dataBuff)
        }
    }

    private fun setPharmaAdapter() = binding?.rvPharma?.adapter = PharmaSelectDialogAdapter(dataContext.relayCommand)
    private fun selectPharma(data: EDIPharmaBuffModel) {
        dataContext.selectPharma(data)
    }
    private fun observeText() {
        lifecycleScope.launch {
            dataContext.searchString.collectLatest {
                dataContext.filterItem()
            }
        }
    }
}