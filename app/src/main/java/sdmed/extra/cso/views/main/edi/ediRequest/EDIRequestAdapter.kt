package sdmed.extra.cso.views.main.edi.ediRequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import sdmed.extra.cso.models.adapter.FViewPagerFragmentAdapter

class EDIRequestAdapter(fragmentManager: FragmentManager, lifecycleOwner: LifecycleOwner): FViewPagerFragmentAdapter(fragmentManager, lifecycleOwner.lifecycle) {
    override val items = mutableListOf<Fragment>()
    override var bundle = Bundle()
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        items.add(EDIRequestFragment())
        items.add(EDIRequestNewFragment())
        super.onAttachedToRecyclerView(recyclerView)
    }
}