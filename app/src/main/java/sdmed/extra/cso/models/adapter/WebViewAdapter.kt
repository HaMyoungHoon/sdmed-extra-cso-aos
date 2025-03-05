package sdmed.extra.cso.models.adapter

import android.webkit.WebView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.utils.FAmhohwa

class WebViewAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("webViewDoc")
        fun setWebViewDoc(webView: WebView, item: StateFlow<String>?) {
            webView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest { x ->
                    if (webView.isVisible) {
//                        webView.loadUrl("${FConstants.GOOGLE_DOC_PREFIX}${FAmhohwa.urlEncoder(x)}")
                        webView.loadUrl("${FConstants.WEB_VIEW_PREFIX}${FAmhohwa.urlEncoder(x)}")
                    }
                }
            }
        }
    }
}