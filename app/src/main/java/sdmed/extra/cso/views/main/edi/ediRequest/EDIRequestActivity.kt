package sdmed.extra.cso.views.main.edi.ediRequest

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.EdiRequestActivityBinding
import sdmed.extra.cso.databinding.EdiTabItemBinding
import sdmed.extra.cso.models.common.WriteFontFamily
import sdmed.extra.cso.models.eventbus.EDIUploadEvent
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles

class EDIRequestActivity: FBaseActivity<EdiRequestActivityBinding, EDIRequestActivityVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)), AppBarLayout.OnOffsetChangedListener {
    override var layoutId = R.layout.edi_request_activity
    override val dataContext: EDIRequestActivityVM by lazy {
        EDIRequestActivityVM(multiDexApplication)
    }
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
    }
    override fun viewInit() {
        super.viewInit()
        setViewPager()
        setTabLayout()
        setAppBarLayout()
    }
    override fun setLayoutCommand(data: Any?) {
        val eventName = data as? EDIRequestActivityVM.ClickEvent ?: return
        when (eventName) {
            EDIRequestActivityVM.ClickEvent.CLOSE -> close()
        }
    }
    override fun onDestroy() {
        binding?.alContent?.removeOnOffsetChangedListener(this)
        getViewPager()?.destroyAdapter()
        super.onDestroy()
    }

    private fun setViewPager() {
        binding?.vpContent?.adapter = EDIRequestAdapter(supportFragmentManager, this)
        binding?.vpContent?.apply {
            setCurrentItem(0, false)
        }
    }
    private fun setTabLayout() {
        val binding = super.binding ?: return
        TabLayoutMediator(binding.tlContent, binding.vpContent) { tab, position ->
            when (position) {
                0 -> getTabView(tab, getString(R.string.edi_request_desc), true)
                1 -> getTabView(tab, getString(R.string.edi_new_desc), false)
            }
        }.attach()
        binding.tlContent.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = selectTab(tab)
            override fun onTabUnselected(tab: TabLayout.Tab?) = unSelectTab(tab)
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    private fun setAppBarLayout() = binding?.alContent?.addOnOffsetChangedListener(this)
    private fun getTabView(tab: TabLayout.Tab, text: String, isSelect: Boolean = false) {
        val binding = super.binding ?: return
        val inflater = LayoutInflater.from(this)
        val view: EdiTabItemBinding = DataBindingUtil.inflate(inflater, R.layout.edi_tab_item, binding.tlContent, false)
        view.tvText.text = text
        view.tvText.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
        if (isSelect) {
            view.tvText.setTextColor(getColor(R.color.def_foreground))
            view.tvText.setTypeface(WriteFontFamily.getTypeface(this), Typeface.BOLD)
        } else {
            view.tvText.setTextColor(getColor(R.color.disable_fore_gray))
            view.tvText.setTypeface(WriteFontFamily.getTypeface(this), Typeface.NORMAL)
        }
        tab.customView = view.root
    }
    private fun selectTab(tab: TabLayout.Tab?) {
        tab ?: return
        val customView = tab.customView as ViewGroup
        val textView = customView.getChildAt(0) as? AppCompatTextView ?: return
        textView.setTextColor(getColor(R.color.def_foreground))
        textView.setTypeface(WriteFontFamily.getTypeface(this), Typeface.BOLD)
    }
    private fun unSelectTab(tab: TabLayout.Tab?) {
        tab ?: return
        val customView = tab.customView as ViewGroup
        val textView = customView.getChildAt(0) as? AppCompatTextView ?: return
        textView.setTextColor(getColor(R.color.disable_fore_gray))
        textView.setTypeface(WriteFontFamily.getTypeface(this), Typeface.NORMAL)
    }
    private fun setTabText(index: Int, text: String) {
        val binding = super.binding ?: return
        if (binding.tlContent.tabCount <= index) {
            return
        }
        val tab = binding.tlContent.getTabAt(index) ?: return
        val customView = tab.customView as? ViewGroup
        if (customView == null) {
            tab.text = text
        } else {
            for (i in 0 until customView.childCount) {
                val textView = customView.getChildAt(i) as? AppCompatTextView ?: continue
                textView.text = text
                tab.text = text
                return
            }
        }
    }
    private fun close() {
        setResult(RESULT_CANCELED)
        finish()
    }
    private fun ok() {
        setResult(RESULT_OK)
        finish()
    }
    private fun getViewPager() = binding?.vpContent?.adapter as? EDIRequestAdapter

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ediUploadEvent(ediUploadEvent: EDIUploadEvent) {
        ok()
    }
}