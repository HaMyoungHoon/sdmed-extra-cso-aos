package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiRequestHospitalBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel

class EDIRequestHospitalAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiRequestHospitalBinding, EDIHosBuffModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_request_hospital

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerEDIRequestHospital")
        fun setRecyclerEDIRequestHospital(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIHosBuffModel>>?) {
            val adapter = recyclerView.adapter as? EDIRequestHospitalAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}