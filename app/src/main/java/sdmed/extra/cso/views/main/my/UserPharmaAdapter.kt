package sdmed.extra.cso.views.main.my

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemUserPharmaBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel

class UserPharmaAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemUserPharmaBinding, PharmaModel>() {
    override var layoutId = R.layout.list_item_user_pharma
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemUserPharmaBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item

            val backgroundColor = it.root.context.getColor(if (position % 2 == 0) {
                R.color.recycler_even
            } else {
                R.color.recycler_odd
            })
            holder.itemView.setBackgroundColor(backgroundColor)
        }
    }
}