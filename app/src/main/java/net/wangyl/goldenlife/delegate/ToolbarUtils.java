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

package net.wangyl.goldenlife.delegate;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import net.wangyl.base.widget.toolbar.LoadingStateView;
import net.wangyl.base.widget.toolbar.SimpleToolbarViewDelegate;
import net.wangyl.base.widget.toolbar.ToolbarConfig;
import net.wangyl.base.widget.toolbar.ToolbarViewDelegate;

import kotlin.jvm.functions.Function1;

/**
 * @author Dylan Cai
 */
@SuppressWarnings("UnusedReturnValue")
public class ToolbarUtils {
//  public static LoadingStateView setToolbar(Activity activity, String title, NavIconType type) {
//    return setToolbar(activity, title, type, 0, null);
//  }
//
//  public static LoadingStateView setToolbar(Activity activity, String title, NavIconType type, int menuId,
//                                            Function1<? super MenuItem, Boolean> onMenuItemClick) {
//    LoadingStateView loadingStateView = new LoadingStateView(activity);
//    loadingStateView.setDecorHeader(new ToolbarViewDelegate(title, type, onMenuItemClick) {
//      @NonNull
//      @Override
//      public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        return null;
//      }
//
//      @Override
//      public void onBindView(@NonNull View view, @NonNull ToolbarConfig config) {
//
//      }
//    });
//    return loadingStateView;
//  }
//
//  public static LoadingStateView setCustomToolbar(Activity activity, View.OnClickListener onMessageClick,
//                                                  int firstDrawableId, View.OnClickListener onFirstBtnClick,
//                                                  int secondDrawableId, View.OnClickListener onSecondBtnClick) {
//    LoadingStateView loadingStateView = new LoadingStateView(activity);
//    loadingStateView.setDecorHeader(new CustomHeaderViewDelegate(onMessageClick,
//        firstDrawableId, onFirstBtnClick, secondDrawableId, onSecondBtnClick));
//    return loadingStateView;
//  }
//
//  public static LoadingStateView setScrollingToolbar(Activity activity, String title) {
//    LoadingStateView loadingStateView = new LoadingStateView(activity);
//    loadingStateView.setDecorView(new ScrollingDecorViewDelegate(title));
//    return loadingStateView;
//  }
}

enum NavIconType {
  BACK, NONE
}
