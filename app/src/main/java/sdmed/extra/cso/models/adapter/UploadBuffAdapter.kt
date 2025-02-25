package sdmed.extra.cso.models.adapter

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemUploadBuffBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.MediaPickerSourceModel

class UploadBuffAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemUploadBuffBinding, MediaPickerSourceModel>(relayCommand) {
    override var layoutId = R.layout.list_item_upload_buff

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerUploadBuffList")
        fun setUploadBuffList(recyclerView: RecyclerView, listItems: StateFlow<MutableList<MediaPickerSourceModel>>?) {
            val adapter = recyclerView.adapter as? UploadBuffAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}