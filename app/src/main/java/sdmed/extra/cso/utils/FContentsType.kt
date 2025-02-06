package sdmed.extra.cso.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.Locale

object FContentsType {
    const val type_aac   : String = "audio/aac"
    const val type_abw   : String = "application/x-abiword"
    const val type_arc   : String = "application/octet-stream"
    const val type_avi   : String = "video/x-msvideo"
    const val type_azw   : String = "application/vnd.amazon.ebook"
    const val type_bin   : String = "application/octet-stream"
    const val type_bz    : String = "application/x-bzip"
    const val type_bz2   : String = "application/x-bzip2"
    const val type_csh   : String = "application/x-csh"
    const val type_css   : String = "text/css"
    const val type_csv   : String = "text/csv"
    const val type_doc   : String = "application/msword"
    const val type_epub  : String = "application/epub+zip"
    const val type_gif   : String = "image/gif"
    const val type_htm   : String = "text/html"
    const val type_html  : String = "text/html"
    const val type_heic  : String = "image/heic"
    const val type_heif  : String = "image/heif"
    const val type_ico   : String = "image/x-icon"
    const val type_ics   : String = "text/calendar"
    const val type_jar   : String = "application/java-archive"
    const val type_jpeg  : String = "image/jpeg"
    const val type_jpg   : String = "image/jpeg"
    const val type_js    : String = "application/js"
    const val type_json  : String = "application/json"
    const val type_mid   : String = "audio/midi"
    const val type_midi  : String = "audio/midi"
    const val type_mpeg  : String = "video/mpeg"
    const val type_mpkg  : String = "application/vnd.apple.installer+xml"
    const val type_odp   : String = "application/vnd.oasis.opendocument.presentation"
    const val type_ods   : String = "application/vnd.oasis.opendocument.spreadsheet"
    const val type_odt   : String = "application/vnd.oasis.opendocument.text"
    const val type_oga   : String = "audio/ogg"
    const val type_ogv   : String = "video/ogg"
    const val type_ogx   : String = "application/ogg"
    const val type_png   : String = "image/png"
    const val type_pdf   : String = "application/pdf"
    const val type_ppt   : String = "application/vnd.ms-powerpoint"
    const val type_rar   : String = "application/x-rar-compressed"
    const val type_rtf   : String = "application/rtf"
    const val type_sh    : String = "application/x-sh"
    const val type_svg   : String = "image/svg+xml"
    const val type_swf   : String = "application/x-shockwave-flash"
    const val type_tar   : String = "application/x-tar"
    const val type_tif   : String = "image/tiff"
    const val type_tiff  : String = "image/tiff"
    const val type_ttf   : String = "application/x-font-ttf"
    const val type_txt   : String = "plain/text"
    const val type_vsd   : String = "application/vnd.visio"
    const val type_wav   : String = "audio/x-wav"
    const val type_weba  : String = "audio/webm"
    const val type_webm  : String = "video/webm"
    const val type_webp  : String = "image/webp"
    const val type_woff  : String = "application/x-font-woff"
    const val type_xhtml : String = "application/xhtml+xml"
    const val type_xls   : String = "application/vnd.ms-excel"
    const val type_xlsx  : String = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    const val type_xlsm  : String = "application/vnd.ms-excel.sheet.macroEnabled.12"
    const val type_xml   : String = "application/xml"
    const val type_xul   : String = "application/vnd.mozilla.xul+xml"
    const val type_zip   : String = "application/zip"
    const val type_3gp   : String = "video/3gpp"
    const val type_3g2   : String = "video/3gpp2"
    const val type_7z    : String = "application/x-7z-compressed"

    fun findContentType(fileName: String) =
        when (fileName.substring(fileName.indexOfLast { it == '.' } + 1).lowercase(Locale.getDefault())) {
            "aac" ->    type_aac
            "abw" ->    type_abw
            "arc" ->    type_arc
            "avi" ->    type_avi
            "azw" ->    type_azw
            "bin" ->    type_bin
            "bz" ->     type_bz
            "bz2" ->    type_bz2
            "csh" ->    type_csh
            "css" ->    type_css
            "csv" ->    type_csv
            "doc" ->    type_doc
            "epub" ->   type_epub
            "gif" ->    type_gif
            "heic" ->   type_heic
            "heif" ->   type_heif
            "htm" ->    type_htm
            "html" ->   type_html
            "ico" ->    type_ico
            "ics" ->    type_ics
            "jar" ->    type_jar
            "jpeg" ->   type_jpeg
            "jpg" ->    type_jpg
            "js" ->     type_js
            "json" ->   type_json
            "mid" ->    type_mid
            "midi" ->   type_midi
            "mpeg" ->   type_mpeg
            "mpkg" ->   type_mpkg
            "odp" ->    type_odp
            "ods" ->    type_ods
            "odt" ->    type_odt
            "oga" ->    type_oga
            "ogv" ->    type_ogv
            "ogx" ->    type_ogx
            "png" ->    type_png
            "pdf" ->    type_pdf
            "ppt" ->    type_ppt
            "rar" ->    type_rar
            "rtf" ->    type_rtf
            "sh" ->     type_sh
            "svg" ->    type_svg
            "swf" ->    type_swf
            "tar" ->    type_tar
            "tif" ->    type_tif
            "tiff" ->   type_tiff
            "ttf" ->    type_ttf
            "txt" ->    type_txt
            "vsd" ->    type_vsd
            "wav" ->    type_wav
            "weba" ->   type_weba
            "webm" ->   type_webm
            "webp" ->   type_webp
            "woff" ->   type_woff
            "xhtml" ->  type_xhtml
            "xls" ->    type_xls
            "xlsx" ->   type_xlsx
            "xlsm" ->   type_xlsm
            "xml" ->    type_xml
            "xul" ->    type_xul
            "zip" ->    type_zip
            "3gp" ->    type_3gp
            "3g2" ->    type_3g2
            "7z" ->     type_7z
            else ->     "application/octet-stream"
        }
    fun getExtMimeTypeString(ext: String) = when (ext) {
        "aac" ->    type_aac
        "abw" ->    type_abw
        "arc" ->    type_arc
        "avi" ->    type_avi
        "azw" ->    type_azw
        "bin" ->    type_bin
        "bz" ->     type_bz
        "bz2" ->    type_bz2
        "csh" ->    type_csh
        "css" ->    type_css
        "csv" ->    type_csv
        "doc" ->    type_doc
        "epub" ->   type_epub
        "gif" ->    type_gif
        "heic" ->   type_heic
        "heif" ->   type_heif
        "htm" ->    type_htm
        "html" ->   type_html
        "ico" ->    type_ico
        "ics" ->    type_ics
        "jar" ->    type_jar
        "jpeg" ->   type_jpeg
        "jpg" ->    type_jpg
        "js" ->     type_js
        "json" ->   type_json
        "mid" ->    type_mid
        "midi" ->   type_midi
        "mpeg" ->   type_mpeg
        "mpkg" ->   type_mpkg
        "odp" ->    type_odp
        "ods" ->    type_ods
        "odt" ->    type_odt
        "oga" ->    type_oga
        "ogv" ->    type_ogv
        "ogx" ->    type_ogx
        "png" ->    type_png
        "pdf" ->    type_pdf
        "ppt" ->    type_ppt
        "rar" ->    type_rar
        "rtf" ->    type_rtf
        "sh" ->     type_sh
        "svg" ->    type_svg
        "swf" ->    type_swf
        "tar" ->    type_tar
        "tif" ->    type_tif
        "tiff" ->   type_tiff
        "ttf" ->    type_ttf
        "txt" ->    type_txt
        "vsd" ->    type_vsd
        "wav" ->    type_wav
        "weba" ->   type_weba
        "webm" ->   type_webm
        "webp" ->   type_webp
        "woff" ->   type_woff
        "xhtml" ->  type_xhtml
        "xls" ->    type_xls
        "xlsx" ->   type_xlsx
        "xlsm" ->   type_xlsm
        "xml" ->    type_xml
        "xul" ->    type_xul
        "zip" ->    type_zip
        "3gp" ->    type_3gp
        "3g2" ->    type_3g2
        "7z" ->     type_7z
        else ->     "application/octet-stream"
    }
    fun getMediaType(mimeType: String) = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaType()
}