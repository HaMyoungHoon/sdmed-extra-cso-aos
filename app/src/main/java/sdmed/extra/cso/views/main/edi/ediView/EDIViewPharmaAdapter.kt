package sdmed.extra.cso.views.main.edi.ediView

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiPharmaListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.adapter.UploadBuffAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel

class EDIViewPharmaAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiPharmaListBinding, EDIUploadPharmaModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_pharma_list
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemEdiPharmaListBinding>, position: Int) {
        holder.binding?.let {
            it.vpEdiPharmaFileList.adapter = EDIViewPharmaFileAdapter(relayCommand)
            it.vpEdiPharmaFileList.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updatePagePosition(it, position)
                }
            })
            it.rvUploadBuffList.adapter = UploadBuffAdapter(relayCommand)
        }
    }

    fun updatePagePosition(binding: ListItemEdiPharmaListBinding, data: Int) {
        binding.dataContext?.currentPosition?.value = data + 1
        binding.executePendingBindings()
    }
}