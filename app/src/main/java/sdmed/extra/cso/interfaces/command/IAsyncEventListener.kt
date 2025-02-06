package sdmed.extra.cso.interfaces.command

interface IAsyncEventListener {
    suspend fun onEvent(data: Any?)
}