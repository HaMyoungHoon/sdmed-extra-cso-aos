package sdmed.extra.cso.views.main.price

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.PriceFragmentBinding

class PriceFragment: FBaseFragment<PriceFragmentBinding, PriceFragmentVM>() {
    override var layoutId = R.layout.price_fragment
    override val dataContext: PriceFragmentVM by lazy {
        PriceFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}