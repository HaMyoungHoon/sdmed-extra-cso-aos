package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.mqtt.MqttConnectModel
import sdmed.extra.cso.models.mqtt.MqttContentModel

interface IMqttRepository {
    suspend fun getSubscribe(): RestResultT<MqttConnectModel>
    suspend fun postPublish(topic: String, mqttContentModel: MqttContentModel): RestResult
    suspend fun postQnA(thisPK: String, content: String): RestResult
    suspend fun postEDIRequest(thisPK: String, content: String): RestResult
    suspend fun postEDIFileAdd(thisPK: String, content: String): RestResult
}