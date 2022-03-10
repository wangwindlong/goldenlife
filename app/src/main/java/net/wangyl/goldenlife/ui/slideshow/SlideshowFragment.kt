package net.wangyl.goldenlife.ui.slideshow

import android.util.Log
import androidx.core.os.bundleOf
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.ItemTextViewBinding
import net.wangyl.goldenlife.extension.navTo
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.mvi.orbit.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SlideshowFragment : BaseListFragment<PostData>() {

    val repo: Repository by inject()

    override suspend fun loader(params: PageInfo): Status<List<PostData>> {
        return repo.getPosts()
    }

    override fun bindItem(holder: MyBaseViewHolder, item: PostData, payloads: List<Any>?) {
        (holder.dataBinding as ItemTextViewBinding).tv.text = item.getItemContent()
    }

    override fun sideEffect(event: Event) {
        when (event) {
            is DetailEvent<*> -> {
                Timber.d("navigateTo event ${event.value}")
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