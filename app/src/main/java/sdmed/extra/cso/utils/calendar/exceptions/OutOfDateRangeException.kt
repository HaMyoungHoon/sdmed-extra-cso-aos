package sdmed.extra.cso.utils.calendar.exceptions

/**
 * Created by Applandeo Team.
 */

data class OutOfDateRangeException(override val message: String) : Exception(message)