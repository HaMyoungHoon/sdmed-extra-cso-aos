package sdmed.extra.cso.interfaces.services

import retrofit2.http.GET
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

interface IMedicinePriceListService {
    @GET("${FConstants.REST_API_MEDICINE_PRICE_LIST}/list")
    suspend fun getList(): RestResultT<List<MedicineModel>>
}