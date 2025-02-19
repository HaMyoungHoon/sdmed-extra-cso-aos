package sdmed.extra.cso.models.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import sdmed.extra.cso.utils.FExtensions

class ImageViewAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageViewWidth", "imageViewHeight"], requireAll = false)
        fun imageViewSize(imageView: AppCompatImageView, imageViewWidth: Int?, imageViewHeight: Int?) {
            val params = imageView.layoutParams
            imageViewWidth?.let { params.width = FExtensions.dpToPx(imageView.context, imageViewWidth) }
            imageViewHeight?.let { params.height = FExtensions.dpToPx(imageView.context, imageViewHeight) }
            imageView.layoutParams = params
        }
    }
}