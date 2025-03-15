package sdmed.extra.cso.utils.googleMap

import java.util.EnumSet

enum class GoogleMapOption {
    NONE,
    SET_LOCATION_AND_MOVE,
    MOVE_AND_CENTER,
    CENTER_AND_ZOOM,
    MOVE_AND_INFO_SHOW;
    infix fun and(rhs: GoogleMapOption): EnumSet<GoogleMapOption> = GoogleMapOptions.of(this, rhs)
    infix fun GoogleMapOptions.allOf(rhs: GoogleMapOptions) = this.containsAll(rhs)
    infix fun GoogleMapOptions.and(rhs: GoogleMapOption): EnumSet<GoogleMapOption> = GoogleMapOptions.of(rhs, *this.toTypedArray())
}
typealias GoogleMapOptions = EnumSet<GoogleMapOption>