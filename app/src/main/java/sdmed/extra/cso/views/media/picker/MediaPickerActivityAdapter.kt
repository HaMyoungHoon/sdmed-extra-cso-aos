package sdmed.extra.cso.views.media.picker

import androidx.lifecycle.LifecycleOwner
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemMediaPickerBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.common.MediaPickerSourceModel

class MediaPickerActivityAdapter(val lifecycleOwner: LifecycleOwner, val relayCommand: ICommand): FRecyclerAdapter<ListItemMediaPickerBinding, MediaPickerSourceModel>() {
    override var layoutId = R.layout.list_item_media_picker
    override var lifecycle = lifecycleOwner.lifecycle
    override fun onBindViewHolder(holder: FRecyclerViewHolder<ListItemMediaPickerBinding>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            it.lifecycleOwner = lifecycleOwner
            it.dataContext = item
        }
    }
    fun updateItem(data: MediaPickerSourceModel?) {
        val index = items.value.indexOfFirst { it == data }
        if (index < 0) {
            return
        }
        notifyItemChanged(index)
    }
}