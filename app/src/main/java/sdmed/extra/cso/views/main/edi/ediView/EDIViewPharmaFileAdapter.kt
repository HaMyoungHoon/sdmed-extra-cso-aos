package sdmed.extra.cso.views.main.edi.ediView

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiPharmaFileViewBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel

class EDIViewPharmaFileAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiPharmaFileViewBinding, EDIUploadPharmaFileModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_pharma_file_view

    companion object {
        @JvmStatic
        @BindingAdapter("viewPagerEDIPharmaFileList")
        fun setViewPagerEDIPharmaFileList(viewPager2: ViewPager2, item: EDIUploadPharmaModel?) {
            val adapter = viewPager2.adapter as? EDIViewPharmaFileAdapter ?: return
            viewPager2.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.let {
                    adapter.updateItems(it.fileList)
                }
            }
        }
    }
}