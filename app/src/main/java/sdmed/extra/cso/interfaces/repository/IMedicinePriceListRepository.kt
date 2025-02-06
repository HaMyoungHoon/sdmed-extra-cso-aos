package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

interface IMedicinePriceListRepository {
    suspend fun getList(): RestResultT<List<MedicineModel>>
}