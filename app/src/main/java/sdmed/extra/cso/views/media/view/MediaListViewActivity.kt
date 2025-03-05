package sdmed.extra.cso.views.media.view

import android.os.Build
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.MediaListViewActivityBinding
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel

class MediaListViewActivity: FBaseActivity<MediaListViewActivityBinding, MediaListViewActivityVM>() {
    override var layoutId = R.layout.media_list_view_activity
    override val dataContext: MediaListViewActivityVM by lazy {
        MediaListViewActivityVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        setMediaAdapter()
        val buffList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("mediaList", MediaViewParcelModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<MediaViewParcelModel>("mediaList")
        }
        dataContext.setItemData(buffList)
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setAdapterCommand(data)
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaListViewActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaListViewActivityVM.ClickEvent.CLOSE -> close()
        }
    }
    private fun setAdapterCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaViewModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaViewModel ?: return
        when (eventName) {
            MediaViewModel.ClickEvent.THIS -> { }
        }
    }
    private fun close() {
        finish()
    }

    private fun setMediaAdapter() = MediaListViewActivityAdapter(dataContext.relayCommand).also { binding?.vpMedia?.adapter = it }
    companion object {
        @JvmStatic
        @BindingAdapter("viewPagerMediaView")
        fun setViewPager2MediaView(viewPager2: ViewPager2, listItems: StateFlow<MutableList<MediaViewModel>>?) {
            val adapter = viewPager2.adapter as? MediaListViewActivityAdapter ?: return
            viewPager2.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}