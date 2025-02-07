package sdmed.extra.cso.views.main.edi

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.EdiFragmentBinding
import sdmed.extra.cso.databinding.EdiTabItemBinding
import sdmed.extra.cso.models.common.WriteFontFamily
import sdmed.extra.cso.utils.FExtensions

class EDIFragment: FBaseFragment<EdiFragmentBinding, EDIFragmentVM>(), AppBarLayout.OnOffsetChangedListener {
    override var layoutId = R.layout.edi_fragment
    override val dataContext: EDIFragmentVM by lazy {
        EDIFragmentVM(multiDexApplication)
    }
    private var pagerAdapter: EDIFragmentAdapter? = null
    override fun viewInit() {
        binding?.dataContext = dataContext
        setViewPager()
        setTabLayout()
        setAppBarLayout()
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
    }
    override fun onDestroy() {
        binding?.alContent?.removeOnOffsetChangedListener(this)
        pagerAdapter?.destroyAdapter()
        super.onDestroy()
    }

    private fun setViewPager() {
        val binding = super.binding ?: return
        pagerAdapter = EDIFragmentAdapter(childFragmentManager, viewLifecycleOwner)
        binding.vpContent.adapter = pagerAdapter
        binding.vpContent.apply {
            setCurrentItem(0, false)
        }
    }
    private fun setTabLayout() {
        val binding = super.binding ?: return
        TabLayoutMediator(binding.tlContent, binding.vpContent) { tab, position ->
            when (position) {
                0 -> getTabView(tab, getResString(R.string.edi_list_desc))
                1 -> getTabView(tab, getResString(R.string.edi_request_desc))
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
    private fun getTabView(tab: TabLayout.Tab, text: String) {
        val binding = super.binding ?: return
        val context = contextBuff ?: return
        val inflater = LayoutInflater.from(context)
        val view: EdiTabItemBinding = DataBindingUtil.inflate(inflater, R.layout.edi_tab_item, binding.tlContent, false)
        view.tvText.text = text
        view.tvText.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
        tab.customView = view.root
    }
    private fun selectTab(tab: TabLayout.Tab?) {
        tab ?: return
        val context = contextBuff ?: return
        val customView = tab.customView as ViewGroup
        val textView = customView.getChildAt(0) as? AppCompatTextView ?: return
        textView.setTextColor(getResColor(R.color.def_foreground))
        textView.setTypeface(WriteFontFamily.getTypeface(context), Typeface.BOLD)
    }
    private fun unSelectTab(tab: TabLayout.Tab?) {
        tab ?: return
        val context = contextBuff ?: return
        val customView = tab.customView as ViewGroup
        val textView = customView.getChildAt(0) as? AppCompatTextView ?: return
        textView.setTextColor(getResColor(R.color.disable_fore_gray))
        textView.setTypeface(WriteFontFamily.getTypeface(context), Typeface.NORMAL)
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
}