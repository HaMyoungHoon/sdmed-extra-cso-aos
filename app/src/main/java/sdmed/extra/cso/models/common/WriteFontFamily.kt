package sdmed.extra.cso.models.common

import android.content.Context
import android.graphics.Typeface

enum class WriteFontFamily(val fontFileName: String, val fontName: String) {
    DEFAULT("nanum_gothic", "나눔고딕");
    companion object {
        fun getDefault() = DEFAULT
        fun fromFontFileName(value: String?) = entries.firstOrNull { it.fontFileName.lowercase() == value?.lowercase() } ?: DEFAULT
        fun fromFontName(value: String?) = entries.firstOrNull { it.fontName.lowercase() == value?.lowercase() } ?: DEFAULT
        fun fromFontFileNameToFontName(value: String?) = fromFontFileName(value).fontName
        fun fromFontNameToFontFileName(value: String?) = fromFontName(value).fontFileName
        fun getTypeface(context: Context, fontFileName: String? = null): Typeface {
            return Typeface.createFromAsset(context.assets, "font/${fromFontFileName(fontFileName).fontFileName}.ttf")
        }
    }
}