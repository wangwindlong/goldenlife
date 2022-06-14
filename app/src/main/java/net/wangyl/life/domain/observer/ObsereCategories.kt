package net.wangyl.life.domain.observer

import kotlinx.coroutines.flow.Flow
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.life.domain.DaoInteractor
import net.wangyl.life.utils.DaoManager

class ObsereCategories: DaoInteractor<Unit, List<CategoryEntity>>() {
    override fun createFlow(params: Unit): Flow<List<CategoryEntity>> {
        return DaoManager.categoryDao.entriesCategory()
    }
}