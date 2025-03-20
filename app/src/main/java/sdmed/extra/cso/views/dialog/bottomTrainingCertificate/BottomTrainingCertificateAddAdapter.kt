package sdmed.extra.cso.views.dialog.bottomTrainingCertificate

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemBottomTrainingCertificateBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel

class BottomTrainingCertificateAddAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemBottomTrainingCertificateBinding, UserTrainingModel>(relayCommand) {
    override var layoutId = R.layout.list_item_bottom_training_certificate

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerTrainingCertificate")
        fun setRecyclerTrainingCertificate(recyclerView: RecyclerView, listItem: StateFlow<MutableList<UserTrainingModel>>?) {
            val adapter = recyclerView.adapter as? BottomTrainingCertificateAddAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItem?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}