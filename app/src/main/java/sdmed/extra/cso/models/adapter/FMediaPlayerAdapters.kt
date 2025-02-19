package sdmed.extra.cso.models.adapter

import android.media.MediaPlayer
import android.net.Uri
import androidx.databinding.BindingAdapter
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import sdmed.extra.cso.utils.FExtensions

object FMediaPlayerAdapters {
    @JvmStatic
    @BindingAdapter(value = ["playerUri", "playerUriWidth", "playerUriHeight", "playerAutoPlay"], requireAll = false)
    fun setMediaPlayerUriSource(mediaPlayer: PlayerView, playerUri: Uri?, playerUriWidth: Int?, playerUriHeight: Int?, playerAutoPlay: Boolean?) {
        val player = mediaPlayer.player ?: return
        player.clearMediaItems()
        playerUri ?: return
        val width = playerUriWidth?.let { FExtensions.dpToPx(mediaPlayer.context, it) } ?: 0
        val height = playerUriHeight?.let { FExtensions.dpToPx(mediaPlayer.context, it) } ?: 0
        player.addMediaItem(MediaItem.fromUri(playerUri))
        player.volume = 0F
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.playWhenReady = false
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .setMaxVideoSize(width, height)
            .build()
        player.addListener(object: Player.Listener {
        })
        if (playerAutoPlay == true) {
            player.prepare()
            player.play()
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["playerUrl", "playerUrlWidth", "playerUrlHeight", "playerAutoPlay"], requireAll = false)
    fun setMediaPlayerUrlSource(mediaPlayer: PlayerView, playerUrl: String?, playerUriWidth: Int?, playerUriHeight: Int?, playerAutoPlay: Boolean?) {
        val player = mediaPlayer.player ?: return
        player.clearMediaItems()
        playerUrl ?: return
        val width = playerUriWidth?.let { FExtensions.dpToPx(mediaPlayer.context, it) } ?: 0
        val height = playerUriHeight?.let { FExtensions.dpToPx(mediaPlayer.context, it) } ?: 0
        player.addMediaItem(MediaItem.fromUri(playerUrl))
        player.volume = 0F
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.playWhenReady = false
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .setMaxVideoSize(width, height)
            .build()
        player.addListener(object: Player.Listener {
        })
        if (playerAutoPlay == true) {
            player.prepare()
            player.play()
        }
    }
}