package sdmed.extra.cso.models.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class FRecyclerViewHolder<T: ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {
    private var _binding: T?
    val binding get() = _binding
    init {
        DataBindingUtil.bind<T>(view).let {
            _binding = it
        }
    }
}