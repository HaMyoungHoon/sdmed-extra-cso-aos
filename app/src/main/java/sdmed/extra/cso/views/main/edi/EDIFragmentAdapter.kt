package sdmed.extra.cso.views.main.edi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import sdmed.extra.cso.models.adapter.FViewPagerFragmentAdapter
import sdmed.extra.cso.views.main.edi.ediList.EDIListFragment
import sdmed.extra.cso.views.main.edi.ediRequest.EDIRequestFragment

class EDIFragmentAdapter(fragmentManager: FragmentManager, lifecycleOwner: LifecycleOwner): FViewPagerFragmentAdapter(fragmentManager, lifecycleOwner.lifecycle) {
    override val items = mutableListOf<Fragment>()
    override var bundle = Bundle()
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        items.add(EDIListFragment())
        items.add(EDIRequestFragment())
        super.onAttachedToRecyclerView(recyclerView)
    }
}