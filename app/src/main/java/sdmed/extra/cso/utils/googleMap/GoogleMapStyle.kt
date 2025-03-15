package sdmed.extra.cso.utils.googleMap

import sdmed.extra.cso.R

enum class GoogleMapStyle(val index: Int, val resId: Int) {
    STANDARD(0, R.raw.map_style_standard),
    SILVER(1, R.raw.map_style_silver),
    RETRO(2, R.raw.map_style_retro),
    DARK(3, R.raw.map_style_dark),
    NIGHT(4, R.raw.map_style_night),
    AUBERGINE(5, R.raw.map_style_aubergine);
    companion object {
        fun getDefault() = RETRO
        fun getFromIndex(index: Int?) = entries.find { it.index == index } ?: getDefault()
    }
}