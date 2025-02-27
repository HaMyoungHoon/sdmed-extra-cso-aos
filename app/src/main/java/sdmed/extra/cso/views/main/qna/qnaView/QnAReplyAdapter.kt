package sdmed.extra.cso.views.main.qna.qnaView

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemQnaReplyBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.EllipseListAdapter
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel

class QnAReplyAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemQnaReplyBinding, QnAReplyModel>(relayCommand) {
    override var layoutId = R.layout.list_item_qna_reply
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemQnaReplyBinding>, position: Int) {
        holder.binding?.let {
            it.rvEllipseList.adapter = EllipseListAdapter(relayCommand)
            it.vpReplyFile.adapter = QnAReplyFileAdapter(relayCommand)
            it.vpReplyFile.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateEllipseList(it, position)
                }
            })
            if (position == itemCount - 1) {
                it.dataContext?.open?.value = true
            }
        }
    }
    fun updateEllipseList(binding: ListItemQnaReplyBinding, position: Int) {
        val buff = binding.dataContext?.ellipseList?.value ?: return
        if (position < 0 || position >= buff.size) {
            return
        }
        buff.forEach { x -> x.initThis() }
        buff[position].selectThis()
        buff.getOrNull(position - 3)?.tinyThis()
        buff.getOrNull(position - 2)?.visibleThis()
        buff.getOrNull(position - 1)?.visibleThis()
        buff.getOrNull(position + 1)?.visibleThis()
        buff.getOrNull(position + 2)?.visibleThis()
        buff.getOrNull(position + 3)?.tinyThis()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("viewPagerQnAReplyFileList")
        fun setViewPagerQnAReplyFileList(viewPager2: ViewPager2, item: QnAReplyModel?) {
            val adapter = viewPager2.adapter as? QnAReplyFileAdapter ?: return
            viewPager2.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.let {
                    adapter.updateItems(it.fileList)
                }
            }
        }
    }
}