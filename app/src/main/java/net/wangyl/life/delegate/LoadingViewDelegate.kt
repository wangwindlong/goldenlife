/*
 * Copyright (c) 2019. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wangyl.life.delegate

import android.view.ViewGroup
import android.view.LayoutInflater
import net.wangyl.base.widget.toolbar.LoadingStateView
import net.wangyl.base.widget.toolbar.ViewType
import net.wangyl.life.R


class LoadingViewDelegate : LoadingStateView.ViewDelegate<LoadingStateView.ViewHolder>(ViewType.LOADING) {

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
    LoadingStateView.ViewHolder(inflater.inflate(R.layout.loading, parent, false))
}