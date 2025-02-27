package sdmed.extra.cso.models.adapter

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEllipseListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.EllipseItemModel

class EllipseListAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEllipseListBinding, EllipseItemModel>(relayCommand) {
    override var layoutId = R.layout.list_item_ellipse_list
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemEllipseListBinding>, position: Int) {
        holder.binding?.executePendingBindings()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerEllipseList")
        fun setRecyclerViewEllipseList(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EllipseItemModel>>?) {
            val adapter = recyclerView.adapter as? EllipseListAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}