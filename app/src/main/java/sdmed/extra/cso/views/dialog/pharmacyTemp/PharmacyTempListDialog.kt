package sdmed.extra.cso.views.dialog.pharmacyTemp

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.PharmacyTempListDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.views.hospitalMap.PharmacyTempAdapter

class PharmacyTempListDialog(private val items: List<PharmacyTempModel>, private val relayCommand: ICommand): FBaseDialogFragment<PharmacyTempListDialogBinding, PharmacyTempListDialogVM>() {
    override var layoutId = R.layout.pharmacy_temp_list_dialog
    override val dataContext: PharmacyTempListDialogVM by lazy {
        PharmacyTempListDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        dataContext.items.value = items.toMutableList()
        setPharmacyAdapter()
    }

    override fun setLayoutCommand(data: Any?) {
        relayCommand.execute(arrayListOf(data, this))
    }

    private fun setPharmacyAdapter() = PharmacyTempAdapter(dataContext.relayCommand).also { binding?.rvPharmacy?.adapter = it }
}