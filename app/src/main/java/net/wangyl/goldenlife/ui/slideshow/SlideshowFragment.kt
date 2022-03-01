package net.wangyl.goldenlife.ui.slideshow

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.PagingSource
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.ItemTextViewBinding
import net.wangyl.goldenlife.extension.getK
import net.wangyl.goldenlife.extension.navTo
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.mvi.base.*

class SlideshowFragment : BaseListFragment<PostData>() {

    val repo: Repository = getK()

    override suspend fun loader(params: PageInfo): Status<List<PostData>> {
        return repo.getPosts()
    }

    override fun bindItem(holder: MyBaseViewHolder, item: PostData, payloads: List<Any>?) {
        (holder.dataBinding as ItemTextViewBinding).tv.text = item.getItemContent()
    }

    override fun sideEffect(event: Event) {
        when (event) {
            is DetailEvent<*> -> {
                Log.d(TAG, "navigateTo event ${event.value}")
                navTo(R.id.nav_settings, bundleOf("itemId" to (event.value as PostData).getItemId(),
                "item" to event.value)//.apply { putParcelable("item", event.value)  }
                )
//                findNavController().navigate(
//                    SettingsFragmentDi.actionListFragmentToDetailFragment(
//                        sideEffect.post
//                    )
//                )
            }
        }
    }

}