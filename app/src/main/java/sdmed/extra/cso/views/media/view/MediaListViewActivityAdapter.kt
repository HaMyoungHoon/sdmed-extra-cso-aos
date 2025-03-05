package sdmed.extra.cso.views.media.view

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemMediaViewBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.adapter.FRecyclerViewHolder
import sdmed.extra.cso.models.common.MediaViewModel

class MediaListViewActivityAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemMediaViewBinding, MediaViewModel>(relayCommand) {
    override var layoutId = R.layout.list_item_media_view
    override fun onBindAfter(holder: FRecyclerViewHolder<ListItemMediaViewBinding>, position: Int) {
        holder.binding?.let { it ->
            setWebClient(it.wvDoc)
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebClient(data: WebView) {
        data.webViewClient = WebViewClient()
        val webSettings: WebSettings = data.settings
        webSettings.domStorageEnabled = true
        webSettings.javaScriptEnabled = true
    }
}