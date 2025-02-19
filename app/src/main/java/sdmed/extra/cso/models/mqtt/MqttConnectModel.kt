package sdmed.extra.cso.models.mqtt

data class MqttConnectModel(
    var brokerUrl: MutableList<String> = mutableListOf(),
    var topic: MutableList<String> = mutableListOf(),
    var userName: String = "",
    var password: String = ""
) {
}