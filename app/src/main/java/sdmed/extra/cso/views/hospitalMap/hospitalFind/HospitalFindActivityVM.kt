package sdmed.extra.cso.views.hospitalMap.hospitalFind

import androidx.multidex.MultiDexApplication
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository

class HospitalFindActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val hospitalTempRepository: IHospitalTempRepository by kodein.instance(IHospitalTempRepository::class)

    enum class ClickEvent(var index: Int) {
        SELECT(0),
        CLOSE(1),
        MAP_TOGGLE(2),
    }
}