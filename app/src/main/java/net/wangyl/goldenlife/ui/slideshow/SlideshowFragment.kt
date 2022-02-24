package net.wangyl.goldenlife.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.ui.BaseListFragment
import net.wangyl.goldenlife.ui.getK

class SlideshowFragment : BaseListFragment<PostData>() {

    val repo: Repository = getK()

    override suspend fun loader(): Status<List<PostData>> {
        return repo.getPosts()
    }

    override fun bindItem(holder: BaseViewHolder, item: Any, payloads: List<Any>?) {

    }

    override fun getItemLayouts(): List<Int> {
        return emptyList()
    }
}