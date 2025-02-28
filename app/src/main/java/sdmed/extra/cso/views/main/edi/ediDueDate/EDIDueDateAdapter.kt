package sdmed.extra.cso.views.main.edi.ediDueDate

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiDueDateBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel

class EDIDueDateAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiDueDateBinding, EDIPharmaDueDateModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_due_date

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerViewEDIDueDate")
        fun setRecyclerViewEDIDueDate(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIPharmaDueDateModel>>?) {
            val adapter = recyclerView.adapter as? EDIDueDateAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}