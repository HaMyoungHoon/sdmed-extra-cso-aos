package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPharmaFileCombinedBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.adapter.UploadBuffAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel

class EDIPharmaFileCombinedAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemPharmaFileCombinedBinding, EDIPharmaBuffModel>(relayCommand) {
    override var layoutId = R.layout.list_item_pharma_file_combined
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemPharmaFileCombinedBinding>, position: Int) {
        holder.binding?.let {
            it.rvUploadBuffList.adapter = UploadBuffAdapter(relayCommand)
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerEDIPharmaFileCombined")
        fun setRecyclerEDIPharmaFileCombined(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIPharmaBuffModel>>?) {
            val adapter = recyclerView.adapter as? EDIPharmaFileCombinedAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}