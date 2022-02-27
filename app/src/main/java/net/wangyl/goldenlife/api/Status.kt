/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package net.wangyl.goldenlife.api

import net.wangyl.goldenlife.model.BaseListModel
import net.wangyl.goldenlife.model.ListModel
import net.wangyl.goldenlife.model.PostData

sealed class Status<T : Any>: ListModel<T> {
    data class Success<T : Any>(val data: T) : Status<T>() {
        override val displayList: T
            get() = data
        override val nextKey: Int
            get() = 0
    }

    data class Failure<T : Any>(val exception: Exception) : Status<T>() {
        override val displayList: T?
            get() = null
        override val nextKey: Int
            get() = 0
    }
}
