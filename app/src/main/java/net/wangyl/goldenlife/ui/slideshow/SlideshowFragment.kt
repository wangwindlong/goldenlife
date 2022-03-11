package net.wangyl.goldenlife.ui.slideshow

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.ItemTextViewBinding
import net.wangyl.goldenlife.extension.goActivity
import net.wangyl.goldenlife.extension.navTo
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.mvi.orbit.*
import net.wangyl.goldenlife.ui.settings.SettingsFragment
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

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        Timber.d("initData args=${savedInstanceState?.get("args_1")}")
    }

    override fun initView(v: View?, savedInstanceState: Bundle?) {
        super.initView(v, savedInstanceState)
    }

    override fun sideEffect(event: Event) {
        when (event) {
            is DetailEvent<*> -> {
                Timber.d("navigateTo event ${event.value}")
                navTo(
                    R.id.nav_settings, bundleOf(
                        "itemId" to (event.value as PostData).getItemId(),
                        "item" to event.value
                    )//.apply { putParcelable("item", event.value)  }
                ) {
                    goActivity(SettingsFragment::class.java.name)
                }
//                findNavController().navigate(
//                    SettingsFragmentDi.actionListFragmentToDetailFragment(
//                        sideEffect.post
//                    )
//                )
            }
        }
    }

}