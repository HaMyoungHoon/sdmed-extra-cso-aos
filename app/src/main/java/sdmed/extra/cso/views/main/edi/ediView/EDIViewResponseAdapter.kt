package sdmed.extra.cso.views.main.edi.ediView

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiResponseListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.edi.EDIUploadResponseModel

class EDIViewResponseAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemEdiResponseListBinding, EDIUploadResponseModel>() {
    override var layoutId = R.layout.list_item_edi_response_list
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemEdiResponseListBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
    fun updateItem(data: EDIUploadResponseModel) {
        val index = items.value.indexOfFirst { it == data }
        if (index < 0) {
            return
        }
        notifyItemChanged(index)
    }
}