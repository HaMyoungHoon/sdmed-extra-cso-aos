package sdmed.extra.cso.models.adapter

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
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
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import sdmed.extra.cso.R
import sdmed.extra.cso.interfaces.command.IEventListener
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.utils.view.FAppCompatImageView

object FGlideBindingAdapters {
    @JvmStatic
    @BindingAdapter(value = ["glideBlobUrl", "glideBlobMimeType", "glideBlobDefImage", "glideBlobLoopCount", "animationStartListener", "animationEndListener"], requireAll = false)
    fun setGlideBlobSrc(imageView: AppCompatImageView, glideBlobUrl: String?, glideBlobMimeType: String?, @DrawableRes glideBlobDefImage: Int?,
                        glideBlobLoopCount: Int?, animationStartListener: IEventListener?, animationEndListener: IEventListener?) {
        val mimeType = FContentsType.getExtMimeType(glideBlobMimeType)
        if (!FImageUtils.isImage(mimeType) || glideBlobUrl.isNullOrEmpty()) {
            if (glideBlobDefImage != null && glideBlobDefImage != 0) {
                glideLoad(imageView, glideBlobDefImage)
                    .apply(defaultImageRequestOptions())
                    .into(imageView)
            } else {
                glideLoad(imageView, FImageUtils.getDefaultImage(glideBlobMimeType))
                    .apply(defaultImageRequestOptions())
                    .into(imageView)
            }
            return
        }
        if (mimeType == "webp") {
            glideAddWebp(imageView, glideBlobUrl, glideBlobMimeType)
            return
        }
        if (mimeType == "gif") {
            glideAddGif(imageView, glideBlobUrl, glideBlobLoopCount, animationStartListener, animationEndListener)
            return
        }
        glideLoad(imageView, glideBlobUrl)
            .apply(defaultImageRequestOptions())
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideBlobUrl", "glideBlobMimeType", "glideBlobDefImage", "glideBlobLoopCount", "animationStartListener", "animationEndListener"], requireAll = false)
    fun setGlideBlobSrc(imageView: FAppCompatImageView, glideBlobUrl: String?, glideBlobMimeType: String?, @DrawableRes glideBlobDefImage: Int?,
                        glideBlobLoopCount: Int?, animationStartListener: IEventListener?, animationEndListener: IEventListener?) {
        val mimeType = FContentsType.getExtMimeType(glideBlobMimeType)
        if (!FImageUtils.isImage(mimeType) || glideBlobUrl.isNullOrEmpty()) {
            if (glideBlobDefImage != null && glideBlobDefImage != 0) {
                glideLoad(imageView, glideBlobDefImage)
                    .apply(defaultImageRequestOptions())
                    .into(imageView)
            } else {
                glideLoad(imageView, FImageUtils.getDefaultImage(glideBlobMimeType))
                    .apply(defaultImageRequestOptions())
                    .into(imageView)
            }
            return
        }
        if (mimeType == "webp") {
            glideAddWebp(imageView, glideBlobUrl, glideBlobMimeType)
            return
        }
        if (mimeType == "gif") {
            glideAddGif(imageView, glideBlobUrl, glideBlobLoopCount, animationStartListener, animationEndListener)
            return
        }
        glideLoad(imageView, glideBlobUrl)
            .apply(defaultImageRequestOptions())
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideResSrc", "glideResSrcNullRemove", "glideResSrcCenter"], requireAll = false)
    fun setGlideResSrc(imageView: AppCompatImageView, @DrawableRes glideResSrc: Int?, glideResSrcNullRemove: Boolean?, glideResSrcCenter: Boolean?) {
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
            .into(imageView)
        return
    }
    @JvmStatic
    @BindingAdapter(value = ["glideResSrc", "glideResSrcNullRemove", "glideResSrcCenter"], requireAll = false)
    fun setGlideResSrc(imageView: AppCompatImageView, glideResSrc: Drawable?, glideResSrcNullRemove: Boolean?, glideResSrcCenter: Boolean?) {
        if (glideResSrc == null) {
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
            .into(imageView)
        return
    }
    @JvmStatic
    @BindingAdapter(value = ["glideResSrc", "glideResSrcNullRemove", "glideResSrcCenter"], requireAll = false)
    fun setGlideResSrc(imageView: FAppCompatImageView, @DrawableRes glideResSrc: Int?, glideResSrcNullRemove: Boolean?, glideResSrcCenter: Boolean?) {
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
    @BindingAdapter(value = ["glideSrcUri", "glideSrcUriWidth", "glideSrcUriHeight", "mediaFileType"], requireAll = false)
    fun setGlideSrcUri(imageView: AppCompatImageView, glideSrcUri: Uri?, glideSrcUriWidth: Int?, glideSrcUriHeight: Int?, mediaFileType: MediaFileType?) {
        when (mediaFileType) {
            MediaFileType.IMAGE -> {}
            MediaFileType.VIDEO -> {}
            MediaFileType.PDF -> {
                glideLoad(imageView, R.drawable.image_pdf)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL, glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            MediaFileType.EXCEL -> {
                glideLoad(imageView, R.drawable.image_excel)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL, glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            MediaFileType.ZIP -> {
                glideLoad(imageView, R.drawable.image_zip)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL, glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            else -> {
                glideLoad(imageView, R.drawable.image_no_image_1920)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL, glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
        }

        if (glideSrcUri == null) {
            imageView.setImageDrawable(null)
            return
        }

        glideUriLoad(imageView, glideSrcUri)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(glideSrcUriWidth ?: Target.SIZE_ORIGINAL, glideSrcUriHeight ?: Target.SIZE_ORIGINAL)
            .optionalCenterCrop()
            .priority(Priority.IMMEDIATE)
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
                    .override(glideThumbnailSrcUriWidth ?: Target.SIZE_ORIGINAL, glideThumbnailSrcUriHeight ?: Target.SIZE_ORIGINAL))
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
                    .override(glideThumbnailSrcUrlWidth ?: Target.SIZE_ORIGINAL, glideThumbnailSrcUrlHeight ?: Target.SIZE_ORIGINAL))
            .load(glideThumbnailSrcUrl)
            .into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["glideRoundSrcUri", "glideRoundSrcUrl", "glideRoundSrcCorners", "glideRoundSrcWidth", "glideRoundSrcHeight", "mediaFileType"], requireAll = false)
    fun setGlideRoundSrcUri(imageView: AppCompatImageView, glideRoundSrcUri: Uri?, glideRoundSrcUrl: String? = null, glideRoundSrcCorners: Int? = 20, glideRoundSrcWidth: Int?, glideRoundSrcHeight: Int?, mediaFileType: MediaFileType?) {
        when (mediaFileType) {
            MediaFileType.IMAGE -> {}
            MediaFileType.VIDEO -> {}
            MediaFileType.PDF -> {
                glideLoad(imageView, R.drawable.image_pdf)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            MediaFileType.EXCEL -> {
                glideLoad(imageView, R.drawable.image_excel)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            MediaFileType.ZIP -> {
                glideLoad(imageView, R.drawable.image_zip)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
            else -> {
                glideLoad(imageView, R.drawable.image_no_image_1920)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                    .optionalCenterCrop()
                    .priority(Priority.IMMEDIATE)
                    .format(DecodeFormat.DEFAULT)
                    .into(imageView)
                return
            }
        }
        if (glideRoundSrcUri != null) {
            glideUriLoad(imageView, glideRoundSrcUri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(glideRoundSrcWidth ?: Target.SIZE_ORIGINAL, glideRoundSrcHeight ?: Target.SIZE_ORIGINAL)
                .transform(CenterCrop(), RoundedCorners(glideRoundSrcCorners ?: 20))
                .priority(Priority.IMMEDIATE)
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
                .format(DecodeFormat.DEFAULT)
                .into(imageView)
            return
        }
        try {
            imageView.setImageDrawable(null)
        } catch (_: Exception) {
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
        }
    }

    fun glideAddWebp(imageView: AppCompatImageView, url: String, mimeType: String?) {
        glideLoad(imageView, url)
            .into(object: CustomViewTarget<AppCompatImageView, Drawable>(imageView) {
                override fun onResourceCleared(placeholder: Drawable?) {
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageView.setImageResource(FImageUtils.getDefaultImage(mimeType))
                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    imageView.setImageDrawable(resource)
                    if (resource is Animatable) {
                        resource.start()
                    }
                }
            })
    }
    fun glideAddWebp(imageView: FAppCompatImageView, url: String, mimeType: String?) {
        glideLoad(imageView, url)
            .into(object: CustomViewTarget<AppCompatImageView, Drawable>(imageView) {
                override fun onResourceCleared(placeholder: Drawable?) {
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageView.setImageResource(FImageUtils.getDefaultImage(mimeType))
                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    imageView.setImageDrawable(resource)
                    if (resource is Animatable) {
                        resource.start()
                    }
                }
            })
    }
    fun glideAddGif(imageView: AppCompatImageView, url: String, gifLoopCount: Int? = null, animationStartListener: IEventListener? = null, animationEndListener: IEventListener? = null) {
        Glide.with(imageView.context).asGif().load(url)
            .apply(RequestOptions()
                .disallowHardwareConfig()
                .fitCenter()
                .override(imageView.width, imageView.height)
            )
            .listener(object: RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean ): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    resource?.setLoopCount(gifLoopCount ?: -1)
                    resource?.registerAnimationCallback(object: Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            animationEndListener?.onEvent(imageView)
                        }
                        override fun onAnimationStart(drawable: Drawable?) {
                            animationStartListener?.onEvent(imageView)
                        }
                    })
                    resource?.start()
                    return false
                }
            }).into(imageView)
    }
    fun glideAddGif(imageView: FAppCompatImageView, url: String, gifLoopCount: Int? = null, animationStartListener: IEventListener? = null, animationEndListener: IEventListener? = null) {
        Glide.with(imageView.context).asGif().load(url)
            .apply(RequestOptions()
                .disallowHardwareConfig()
                .fitCenter()
                .override(imageView.width, imageView.height)
            )
            .listener(object: RequestListener<GifDrawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean ): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    resource?.setLoopCount(gifLoopCount ?: -1)
                    resource?.registerAnimationCallback(object: Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            animationEndListener?.onEvent(imageView)
                        }
                        override fun onAnimationStart(drawable: Drawable?) {
                            animationStartListener?.onEvent(imageView)
                        }
                    })
                    resource?.start()
                    return false
                }
            }).into(imageView)
    }
    private fun glideLoad(imageView: AppCompatImageView, url: String) = Glide.with(imageView.context).load(url)
    private fun glideLoad(imageView: AppCompatImageView, resId: Int) = Glide.with(imageView.context).load(resId)
    private fun glideLoad(imageView: AppCompatImageView, drawable: Drawable) = Glide.with(imageView.context).load(drawable)
    private fun glideLoad(imageView: FAppCompatImageView, drawable: Drawable) = Glide.with(imageView.context).load(drawable)
    private fun glideLoad(imageView: FAppCompatImageView, url: String) = Glide.with(imageView.context).load(url)
    private fun glideLoad(imageView: FAppCompatImageView, resId: Int) = Glide.with(imageView.context).load(resId)
    private fun glideBuilder(imageView: AppCompatImageView, url: String) = Glide.with(imageView.context).asDrawable().load(url)
    private fun glideUriLoad(imageView: AppCompatImageView, uri: Uri) = Glide.with(imageView.context).load(uri)
    private fun defaultImageRequestOptions() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().priority(
        Priority.IMMEDIATE).format(DecodeFormat.PREFER_ARGB_8888).skipMemoryCache(false).optionalCenterCrop()
    private fun fitCenterImageRequestOptions() = RequestOptions().diskCacheStrategy(
        DiskCacheStrategy.ALL).fitCenter().priority(Priority.IMMEDIATE).format(DecodeFormat.PREFER_ARGB_8888)
}