package sdmed.extra.cso.views.hospitalMap.hospitalFind

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.HospitalFindActivityBinding

class HospitalFindActivity: FBaseActivity<HospitalFindActivityBinding, HospitalFindActivityVM>() {
    override var layoutId = R.layout.hospital_find_activity
    override val dataContext: HospitalFindActivityVM by lazy {
        HospitalFindActivityVM(multiDexApplication)
    }
}