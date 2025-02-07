package sdmed.extra.cso.views.main.edi.ediList

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

class EDIListItemAdapter(val lifeCycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemEdiListBinding, EDIUploadModel>() {
    override var layoutId = R.layout.list_item_edi_list
    override var lifecycle = lifeCycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemEdiListBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifeCycleOwner
            it.dataContext = item
        }
    }
    fun updateItem(data: EDIUploadModel?) {
        val index = items.value.indexOfFirst { it == data }
        if (index < 0) {
            return
        }
        notifyItemChanged(index)
    }
}