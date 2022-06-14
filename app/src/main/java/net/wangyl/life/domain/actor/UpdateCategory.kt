package net.wangyl.life.domain.actor

import com.dropbox.android.external.store4.get
import net.wangyl.life.domain.Interactor
import net.wangyl.life.store.CategoryStore

class UpdateCategory : Interactor<Unit>() {
    override suspend fun doWork(params: Unit) {
        CategoryStore.categoryStore.get(0)
    }

}