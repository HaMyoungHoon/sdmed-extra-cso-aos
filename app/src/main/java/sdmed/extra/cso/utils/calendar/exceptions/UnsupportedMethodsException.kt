package sdmed.extra.cso.utils.calendar.exceptions

/**
 * Created by Applandeo Team.
 */

data class UnsupportedMethodsException(override val message: String) : RuntimeException(message)