package sdmed.extra.cso.views.main.price

import androidx.multidex.MultiDexApplication
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.PriceFragmentBinding

class PriceFragment: FBaseFragment<PriceFragmentBinding, PriceFragmentVM>() {
    override var layoutId = R.layout.price_fragment
    override val dataContext: PriceFragmentVM by lazy {
        PriceFragmentVM(contextBuff!!.applicationContext as MultiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}