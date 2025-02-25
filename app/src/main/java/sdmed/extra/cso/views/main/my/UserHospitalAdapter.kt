package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemUserHospitalBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel

class UserHospitalAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemUserHospitalBinding, HospitalModel>(relayCommand) {
    override var layoutId = R.layout.list_item_user_hospital
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemUserHospitalBinding>, position: Int) {
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