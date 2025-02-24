package sdmed.extra.cso.models.adapter

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEllipseListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.EllipseItemModel

class EllipseListAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemEllipseListBinding, EllipseItemModel>() {
    override var layoutId = R.layout.list_item_ellipse_list
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemEllipseListBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
}