package sdmed.extra.cso.interfaces.services

import retrofit2.http.*
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.mqtt.MqttConnectModel
import sdmed.extra.cso.models.mqtt.MqttContentModel

interface IMqttService {
    @GET("${FConstants.REST_API_MQTT}/subscribe")
    suspend fun getSubscribe(): RestResultT<MqttConnectModel>
    @POST("${FConstants.REST_API_MQTT}/publish")
    suspend fun postPublish(@Query("topic") topic: String, @Body mqttContentModel: MqttContentModel): RestResult
}