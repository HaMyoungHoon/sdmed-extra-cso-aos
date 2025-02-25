package sdmed.extra.cso.models.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.interfaces.command.ICommand

abstract class FRecyclerAdapter<T1: ViewDataBinding, T2: FDataModelClass<*>>(val relayCommand: ICommand): RecyclerView.Adapter<FRecyclerViewHolder<T1>>() {
    protected var items = MutableStateFlow(mutableListOf<T2>())
    protected abstract var layoutId: Int
    override fun getItemCount() = items.value.size
    override fun onBindViewHolder(holder: FRecyclerViewHolder<T1>, position: Int) {
        holder.binding?.let {
            val item = items.value[position]
            item.relayCommand = relayCommand
            val method = it::class.java.getMethod("setDataContext", item::class.java)
            method.invoke(it, item)
            onBindAfter(holder, position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FRecyclerViewHolder<T1> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = FRecyclerViewHolder<T1>(inflater.inflate(layoutId, parent, false))
        viewHolder.binding?.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return viewHolder
    }
    open fun onBindAfter(holder: FRecyclerViewHolder<T1>, position: Int) {
    }

    fun clearItems() {
        val callback = ListItemDiffCallback(items.value, arrayListOf())
        val result = DiffUtil.calculateDiff(callback)
        items.value = arrayListOf()
        result.dispatchUpdatesTo(this)
    }
    fun updateItems(data: MutableList<T2>?) {
        if (data == null) {
            return
        }
        val callback = ListItemDiffCallback(items.value, data)
        val result = DiffUtil.calculateDiff(callback)
        items.value = data
        result.dispatchUpdatesTo(this)
    }
    open fun updateItem(data: T2?) {
        val index = items.value.indexOfFirst { it == data }
        if (index < 0) {
            return
        }
        notifyItemChanged(index)
    }
    fun addItems(data: ArrayList<T2>) {
        val itemsBuff = items.value.toMutableList()
        itemsBuff.addAll(data)
        val callback = ListItemDiffCallback(items.value, itemsBuff)
        val result = DiffUtil.calculateDiff(callback)
        items.value = itemsBuff
        result.dispatchUpdatesTo(this)
    }
    fun notifyItemChanged() {
        notifyItemRangeChanged(0, items.value.size)
    }
}