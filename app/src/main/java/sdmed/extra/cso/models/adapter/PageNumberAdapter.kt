package sdmed.extra.cso.models.adapter

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPageNumberBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.PageNumberModel

class PageNumberAdapter(val lifecycleOwner: LifecycleOwner, private val relayCommand: ICommand): FRecyclerAdapter<ListItemPageNumberBinding, PageNumberModel>() {
    override var layoutId = R.layout.list_item_page_number
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemPageNumberBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
}