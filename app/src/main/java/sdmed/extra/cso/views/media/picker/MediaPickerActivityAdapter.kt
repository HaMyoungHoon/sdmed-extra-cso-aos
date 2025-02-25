package sdmed.extra.cso.views.media.picker

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemMediaPickerBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.common.MediaPickerSourceModel

class MediaPickerActivityAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemMediaPickerBinding, MediaPickerSourceModel>(relayCommand) {
    override var layoutId = R.layout.list_item_media_picker
}