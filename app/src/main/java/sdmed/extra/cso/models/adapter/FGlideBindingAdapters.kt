package sdmed.extra.cso.models.adapter

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object FGlideBindingAdapters {
    @JvmStatic
    @BindingAdapter(value = ["glideSrc", "glideSrcNullRemove", "glideSrcCenter"], requireAll = false)
    fun setGlideSrc(imageView: AppCompatImageView, glideSrc: String?, glideSrcNullRemove: Boolean?, glideSrcCenter: Boolean?) {
        if (glideSrc.isNullOrEmpty()) {
            if (glideSrcNullRemove == true) {
                imageView.setImageDrawable(null)
            }
            return
        }
        if (glideSrcCenter == true) {
            imageView.scaleType = ImageView.ScaleType.CENTER
        }
        glideLoad(imageView, glideSrc)
            .apply(defaultImageRequestOptions())
            .skipMemoryCache(false)
            .optionalCenterCrop()
            .into(imageView)
        return
    }
    @JvmStatic
    @BindingAdapter(value = ["glideResSrc", "glideResSrcNullRemove", "glideResSrcCenter"], requireAll = false)
    fun setGlideResSrc(imageView: AppCompatImageView, glideResSrc: Int?, glideResSrcNullRemove: Boolean?, glideResSrcCenter: Boolean?) {
        if (glideResSrc == null || glideResSrc == 0) {
            if (glideResSrcNullRemove == true) {
                imageView.setImageDrawable(null)
            }
            return
        }
        if (glideResSrcCenter == true) {
            imageView.scaleType = ImageView.ScaleType.CENTER
        }
        glideLoad(imageView, glideResSrc)
            .apply(defaultImageRequestOptions())
            .skipMemoryCache(false)
            .optionalCenterCrop()
            .into(imageView)
        return
    }
    @JvmStatic
    @BindingAdapter("glideCircleSrc")
    fun setGlideCircleSrc(imageView: AppCompatImageView, glideCircleSrc: String?) {
        if (glideCircleSrc.isNullOrEmpty()) {
            imageView.setImageDrawable(null)
            return
        }
        try {
            glideLoad(imageView, glideCircleSrc)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView)
        } catch (_: Exception) {
            Log.d("test mhha setGlideCircleSrc", "catch")
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["glideRoundSrc", "glideCorners"], requireAll = false)
    fun setGlideRoundSrc(imageView: AppCompatImageView, glideRoundSrc: String?, glideCorners: Int? = 20) {
        if (glideRoundSrc.isNullOrEmpty()) {
            imageView.setImageDrawable(null)
            return
        }

        glideBuilder(imageView, glideRoundSrc)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(glideCorners ?: 0)))
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter("glideSrcUri")
    fun setGlideSrcUri(imageView: AppCompatImageView, glideSrcUri: Uri?) {
        if (glideSrcUri == null) {
            imageView.setImageDrawable(null)
            return
        }
        glideUriLoad(imageView, glideSrcUri)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop())
            .skipMemoryCache(false)
            .optionalCenterCrop()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["glideSrcUri", "glideUriSrcNullRemove", "glideUriSrcFitCenter"], requireAll = false)
    fun setGlideSrcUri(imageView: androidx.appcompat.widget.AppCompatImageView, glideSrcUri: Uri?, glideUriSrcNullRemove: Boolean?, glideUriSrcFitCenter: Boolean?) {
        if (glideSrcUri == null) {
            if (glideUriSrcNullRemove == true) {
                imageView.setImageDrawable(null)
            }
            return
        }
        val ret = glideUriLoad(imageView, glideSrcUri)
        if (glideUriSrcFitCenter == true) {
            ret.apply(fitCenterImageRequestOptions()
                .override(imageView.width, imageView.height)
            ).into(imageView)
        } else {
            ret.apply(defaultImageRequestOptions()
                .override(imageView.width, imageView.height)
            ).into(imageView)
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["glideSrcUri", "glideSrcUriWidth", "glideSrcUriHeight"], requireAll = false)
    fun setGlideSrcUri(imageView: AppCompatImageView, glideSrcUri: Uri?, glideSrcUriWidth: Int?, glideSrcUriHeight: Int?) {
        if (glideSrcUri == null) {
            imageView.setImageDrawable(null)
            return
        }

        glideUriLoad(imageView, glideSrcUri)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL,glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
            .optionalCenterCrop()
            .priority(Priority.IMMEDIATE)
//                .encodeFormat(Bitmap.CompressFormat.PNG)
            .format(DecodeFormat.DEFAULT)
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideThumbnailSrcUri", "glideThumbnailSrcUriCorners", "glideThumbnailSrcUriWidth", "glideThumbnailSrcUriHeight"], requireAll = false)
    fun setGlideThumbnailUri(imageView: AppCompatImageView, glideThumbnailSrcUri: Uri?, glideThumbnailSrcUriCorners: Int? = 20, glideThumbnailSrcUriWidth: Int?, glideThumbnailSrcUriHeight: Int?) {
        if (glideThumbnailSrcUri == null) {
            imageView.setImageDrawable(null)
            return
        }
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000 * 1000)
                    .centerCrop()
                    .transform(CenterCrop(), RoundedCorners(glideThumbnailSrcUriCorners ?: 20))
                    .override(glideThumbnailSrcUriWidth ?: Target.SIZE_ORIGINAL,glideThumbnailSrcUriHeight ?: Target.SIZE_ORIGINAL))
            .load(glideThumbnailSrcUri)
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideThumbnailSrcUrl", "glideThumbnailSrcUrlCorners", "glideThumbnailSrcUrlWidth", "glideThumbnailSrcUrlHeight"], requireAll = false)
    fun setGlideThumbnailUrl(imageView: AppCompatImageView, glideThumbnailSrcUrl: String?, glideThumbnailSrcUrlCorners: Int? = 20, glideThumbnailSrcUrlWidth: Int?, glideThumbnailSrcUrlHeight: Int?) {
        if (glideThumbnailSrcUrl.isNullOrEmpty()) {
            imageView.setImageDrawable(null)
            return
        }
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000 * 1000)
                    .centerCrop()
                    .transform(CenterCrop(), RoundedCorners(glideThumbnailSrcUrlCorners ?: 20))
                    .override(glideThumbnailSrcUrlWidth ?: Target.SIZE_ORIGINAL,glideThumbnailSrcUrlHeight ?: Target.SIZE_ORIGINAL))
            .load(glideThumbnailSrcUrl)
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideRoundSrcUri", "glideRoundSrcUrl", "glideRoundSrcCorners", "glideRoundSrcWidth", "glideRoundSrcHeight"], requireAll = false)
    fun setGlideRoundSrcUri(imageView: AppCompatImageView, glideRoundSrcUri: Uri?, glideRoundSrcUrl: String? = null, glideRoundSrcCorners: Int? = 20, glideRoundSrcWidth: Int?, glideRoundSrcHeight: Int?) {
        if (glideRoundSrcUri != null) {
            glideUriLoad(imageView, glideRoundSrcUri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                .transform(CenterCrop(), RoundedCorners(glideRoundSrcCorners ?: 20))
                .priority(Priority.IMMEDIATE)
//                    .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .into(imageView)
            return
        }
        if (glideRoundSrcUrl != null) {
            glideLoad(imageView, glideRoundSrcUrl)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                .transform(CenterCrop(), RoundedCorners(glideRoundSrcCorners ?: 20))
                .priority(Priority.IMMEDIATE)
                //                    .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .into(imageView)
            return
        }
        try {
            imageView.setImageDrawable(null)
        } catch (_: Exception) {
            Log.d("test mhha setGlideRoundSrcUri", "catch setImageDrawable null")
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["gifAssetSrc", "gifAssetLoopCount"])
    fun setGifSrcAsset(imageView: AppCompatImageView, gifAssetSrc: String, gifAssetLoopCount: Int) {
        if (gifAssetSrc.isEmpty()) {
            return
        }

        try {
            Glide.with(imageView.context).asGif().load(gifAssetSrc)
                .listener(object: RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean ): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.setLoopCount(gifAssetLoopCount)
                        return false
                    }
                }).into(imageView)
        } catch (e: Exception) {
            Log.d("test mhha setGifSrcAsset", e.message ?: "")
        }
    }
    private fun glideLoad(imageView: AppCompatImageView, url: String) = Glide.with(imageView.context).load(url)
    private fun glideLoad(imageView: AppCompatImageView, resId: Int) = Glide.with(imageView.context).load(resId)
    private fun glideBuilder(imageView: AppCompatImageView, url: String) = Glide.with(imageView.context).asDrawable().load(url)
    private fun glideUriLoad(imageView: AppCompatImageView, uri: Uri) = Glide.with(imageView.context).load(uri)
    private fun defaultImageRequestOptions() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().priority(
        Priority.IMMEDIATE).format(DecodeFormat.PREFER_ARGB_8888)
    private fun fitCenterImageRequestOptions() = RequestOptions().diskCacheStrategy(
        DiskCacheStrategy.ALL).fitCenter().priority(Priority.IMMEDIATE).format(DecodeFormat.PREFER_ARGB_8888)
}