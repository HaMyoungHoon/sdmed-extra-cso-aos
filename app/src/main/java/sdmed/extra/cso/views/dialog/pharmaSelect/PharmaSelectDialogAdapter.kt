package sdmed.extra.cso.views.dialog.pharmaSelect

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPharmaSelectBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel

class PharmaSelectDialogAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemPharmaSelectBinding, EDIPharmaBuffModel>(relayCommand) {
    override var layoutId = R.layout.list_item_pharma_select

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerPharmaSelect")
        fun setRecyclerPharmaSelect(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIPharmaBuffModel>>?) {
            val adapter = recyclerView.adapter as? PharmaSelectDialogAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}