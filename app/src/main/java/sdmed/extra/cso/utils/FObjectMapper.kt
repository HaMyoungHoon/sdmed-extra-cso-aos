package sdmed.extra.cso.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import sdmed.extra.cso.models.mqtt.MqttContentModel

object FObjectMapper {
    val objectMapper by lazy {
        ObjectMapper().registerKotlinModule()
    }
    fun readValue(byteArray: ByteArray, clazz: Class<*>) = objectMapper.readValue(byteArray, clazz)
    fun mqttRead(byteArray: ByteArray) = objectMapper.readValue<MqttContentModel>(byteArray, MqttContentModel::class.java)
}