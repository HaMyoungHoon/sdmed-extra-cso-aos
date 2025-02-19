package sdmed.extra.cso.views.main.edi.ediView

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiPharmaListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel

class EDIViewPharmaAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemEdiPharmaListBinding, EDIUploadPharmaModel>() {
    override var layoutId = R.layout.list_item_edi_pharma_list
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemEdiPharmaListBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
}