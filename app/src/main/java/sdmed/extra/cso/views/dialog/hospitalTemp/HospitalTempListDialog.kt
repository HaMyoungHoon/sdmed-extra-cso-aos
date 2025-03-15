package sdmed.extra.cso.views.dialog.hospitalTemp

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.HospitalTempListDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.views.hospitalMap.HospitalTempAdapter

class HospitalTempListDialog(private val items: List<HospitalTempModel>, private val relayCommand: ICommand): FBaseDialogFragment<HospitalTempListDialogBinding, HospitalTempListDialogVM>() {
    override var layoutId = R.layout.hospital_temp_list_dialog
    override val dataContext: HospitalTempListDialogVM by lazy {
        HospitalTempListDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        dataContext.items.value = items.toMutableList()
        setHospitalAdapter()
    }

    override fun setLayoutCommand(data: Any?) {
        relayCommand.execute(arrayListOf(data, this))
    }

    private fun setHospitalAdapter() = HospitalTempAdapter(dataContext.relayCommand).also { binding?.rvHospital?.adapter = it }
}