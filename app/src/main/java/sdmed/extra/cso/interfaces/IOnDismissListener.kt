package sdmed.extra.cso.interfaces

interface IOnDismissListener {
    fun onDismiss()
    operator fun invoke(function: () -> Unit) {
    }
}