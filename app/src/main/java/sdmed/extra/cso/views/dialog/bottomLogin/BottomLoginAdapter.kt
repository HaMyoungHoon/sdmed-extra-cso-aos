package sdmed.extra.cso.views.dialog.bottomLogin

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemMultiLoginBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel

class BottomLoginAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemMultiLoginBinding, UserMultiLoginModel>(relayCommand) {
    override var layoutId = R.layout.list_item_multi_login

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerBottomLogin")
        fun setRecyclerBottomLogin(recyclerView: RecyclerView, listItems: StateFlow<MutableList<UserMultiLoginModel>>?) {
            val adapter = recyclerView.adapter as? BottomLoginAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}