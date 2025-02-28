package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiRequestApplyDateBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel

class EDIRequestApplyDateAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiRequestApplyDateBinding, EDIApplyDateModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_request_apply_date

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerEDIRequestApplyDate")
        fun setRecyclerEDIRequestApplyDate(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIApplyDateModel>>?) {
            val adapter = recyclerView.adapter as? EDIRequestApplyDateAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}