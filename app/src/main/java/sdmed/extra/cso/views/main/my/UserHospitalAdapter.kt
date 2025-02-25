package sdmed.extra.cso.views.main.my

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemUserHospitalBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel

class UserHospitalAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemUserHospitalBinding, HospitalModel>() {
    override var layoutId = R.layout.list_item_user_hospital
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemUserHospitalBinding>, position: Int) {
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