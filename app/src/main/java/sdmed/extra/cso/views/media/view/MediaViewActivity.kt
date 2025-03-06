package sdmed.extra.cso.views.media.view

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.MediaViewActivityBinding
import sdmed.extra.cso.models.common.MediaViewModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.utils.FStorage

class MediaViewActivity: FBaseActivity<MediaViewActivityBinding, MediaViewActivityVM>() {
    override var layoutId = R.layout.media_view_activity
    override val dataContext: MediaViewActivityVM by lazy {
        MediaViewActivityVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        val buffItem = FStorage.getParcel<MediaViewParcelModel>(intent, "mediaItem")
        dataContext.setItemData(buffItem)
        setWebView()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setAdapterCommand(data)
    }
    private fun setWebView() {
        val webView = binding?.wvDoc ?: return
        setWebClient(webView)
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebClient(data: WebView) {
        data.webViewClient = WebViewClient()
        val webSettings: WebSettings = data.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaViewActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaViewActivityVM.ClickEvent.CLOSE -> close()
        }
    }
    private fun setAdapterCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaViewModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaViewModel ?: return
        when (eventName) {
            MediaViewModel.ClickEvent.THIS -> { }
        }
    }
    private fun close() {
        finish()
    }
}