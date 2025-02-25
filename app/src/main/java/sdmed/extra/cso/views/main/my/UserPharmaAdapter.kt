package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemUserPharmaBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel

class UserPharmaAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemUserPharmaBinding, PharmaModel>(relayCommand) {
    override var layoutId = R.layout.list_item_user_pharma
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemUserPharmaBinding>, position: Int) {
        holder.binding?.let {
            val backgroundColor = it.root.context.getColor(if (position % 2 == 0) {
                R.color.recycler_even
            } else {
                R.color.recycler_odd
            })
            holder.itemView.setBackgroundColor(backgroundColor)
        }
    }
}