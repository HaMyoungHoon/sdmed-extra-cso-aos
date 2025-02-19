package sdmed.extra.cso.views.dialog.select

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemSelectDialogListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.common.SelectListModel

class SelectDialogAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemSelectDialogListBinding, SelectListModel>() {
    override var layoutId = R.layout.list_item_select_dialog_list
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemSelectDialogListBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
}