package sdmed.extra.cso.views.dialog.pharmacyTemp

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.PharmacyTempDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel
import sdmed.extra.cso.views.dialog.hospitalTemp.HospitalTempDialogVM

class PharmacyTempDialog(private val item: PharmacyTempModel, private val relayCommand: ICommand): FBaseDialogFragment<PharmacyTempDialogBinding, PharmacyTempDialogVM>() {
    override var layoutId = R.layout.pharmacy_temp_dialog
    override val dataContext: PharmacyTempDialogVM by lazy {
        PharmacyTempDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        dataContext.item.value = item
    }

    override fun setLayoutCommand(data: Any?) {
        relayCommand.execute(arrayListOf(data, dataContext.item.value))
        if (data == HospitalTempDialogVM.ClickEvent.THIS) {
            dismiss()
        }
    }
}