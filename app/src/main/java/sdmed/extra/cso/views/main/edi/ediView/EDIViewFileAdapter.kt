package sdmed.extra.cso.views.main.edi.ediView

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiFileViewBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.adapter.FViewPagerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel

class EDIViewFileAdapter(val lifecycleOwner: LifecycleOwner, val dataModel: MutableList<EDIUploadFileModel>, val relayCommand: ICommand): FViewPagerAdapter<ListItemEdiFileViewBinding>() {
    override var layoutId = R.layout.list_item_edi_file_view
    override fun getItemCount() = dataModel.size
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemEdiFileViewBinding>, position: Int) {
        holder.binding?.let {
            val item = dataModel[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
    fun updateItems(items: MutableList<EDIUploadFileModel>) {
        dataModel.clear()
        dataModel.addAll(items)
        notifyItemRangeChanged(0, items.size)
    }

}