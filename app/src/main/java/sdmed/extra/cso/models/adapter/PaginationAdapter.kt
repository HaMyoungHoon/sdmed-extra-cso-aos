package sdmed.extra.cso.models.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.databinding.IncludePaginationBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.PageNumberModel
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.utils.FCoroutineUtil

class PaginationAdapter @JvmOverloads constructor(context: Context,
                        attrs: AttributeSet? = null,
                        defStyleAttr: Int = 0): androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr) {
    var binding: IncludePaginationBinding = IncludePaginationBinding.inflate(LayoutInflater.from(context), this, true)
    fun afterInit(lifecycleOwner: LifecycleOwner, dataModel: StateFlow<PaginationModel>, relayCommand: ICommand) {
        binding.lifecycleOwner = lifecycleOwner
        binding.rvPage.adapter = PageNumberAdapter(lifecycleOwner, relayCommand)
        FCoroutineUtil.coroutineScope({
            dataModel.collectLatest {
                it.relayCommand = relayCommand
                binding.dataContext = it
                updateSelect(0)
            }
        })
    }
    fun updateSelect(position: Int) {
        val buff = binding.dataContext?.pages?.value?.toMutableList() ?: return
        if (position < 0 || position >= buff.size) {
            return
        }
        binding.dataContext?.first?.value = position == 0
        binding.dataContext?.last?.value = position == buff.size - 1
        val findIndex = buff.indexOfFirst { it.isSelect }
        if (findIndex >= 0) {
            buff[findIndex].unSelectThis()
        }
        buff[position].selectThis()
        binding.dataContext?.pages?.value = buff
        getPageAdapter()?.notifyItemChanged(findIndex)
        getPageAdapter()?.notifyItemChanged(position)
    }
    fun getPageAdapter(): PageNumberAdapter? {
        return binding.rvPage.adapter as? PageNumberAdapter
    }
    companion object {
        @JvmStatic
        @BindingAdapter("recyclerPageNumberItem")
        fun setRecyclerPageNumberItem(recyclerView: RecyclerView, listItem: StateFlow<MutableList<PageNumberModel>>?) {
            val adapter = recyclerView.adapter as? PageNumberAdapter ?: return
            adapter.lifecycleOwner.lifecycleScope.launch {
                listItem?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}