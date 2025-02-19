package sdmed.extra.cso.models.repository

//import com.hivemq.client.mqtt.MqttClient
//import com.hivemq.client.mqtt.datatypes.MqttQos
//import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import sdmed.extra.cso.interfaces.repository.IMqttRepository
import sdmed.extra.cso.interfaces.services.IMqttService
import sdmed.extra.cso.models.mqtt.MqttContentModel
import sdmed.extra.cso.models.mqtt.MqttContentType
import sdmed.extra.cso.utils.FExtensions

class MqttRepository(private val _service: IMqttService): IMqttRepository {
    override suspend fun getSubscribe() = FExtensions.restTryT { _service.getSubscribe() }
    override suspend fun postPublish(topic: String, mqttContentModel: MqttContentModel) = FExtensions.restTry { _service.postPublish(topic, mqttContentModel) }

    override suspend fun postQnA(thisPK: String, content: String) = FExtensions.restTry {
        postPublish("aos-extra-cso", MqttContentModel().apply {
            this.contentType = MqttContentType.QNA_REQUEST
            this.content = content
            this.targetItemPK = thisPK
        })
    }
    override suspend fun postEDIRequest(thisPK: String, content: String) = FExtensions.restTry {
        postPublish("aos-extra-cso", MqttContentModel().apply {
            this.contentType = MqttContentType.EDI_REQUEST
            this.content = content
            this.targetItemPK = thisPK
        })
    }
    override suspend fun postEDIFileAdd(thisPK: String, content: String) = FExtensions.restTry {
        postPublish("aos-extra-cso", MqttContentModel().apply {
            this.contentType = MqttContentType.EDI_FILE_ADD
            this.content = content
            this.targetItemPK = thisPK
        })
    }
}