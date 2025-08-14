/*
 * Copyright 2022-2025 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.billing.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.adapter.DynamicFeaturesAdapter;
import com.pranavpandey.android.dynamic.billing.model.DynamicFeature;
import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame;
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewNested;
import com.pranavpandey.android.dynamic.support.util.DynamicLayoutUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DynamicRecyclerViewFrame} to display the list of {@link DynamicFeature}.
 */
public abstract class DynamicFeaturesView extends DynamicRecyclerViewNested {

    public DynamicFeaturesView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicFeaturesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setAdapter();
    }

    public DynamicFeaturesView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setAdapter();
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.adb_features_view;
    }

    @Override
    public @Nullable RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return DynamicLayoutUtils.getLinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL);
    }

    /**
     * Returns a list of dynamic features to be displayed by this view.
     *
     * @return The list of dynamic features to be displayed by this view.
     */
    public abstract @NonNull List<DynamicFeature> getFeatures();

    public @NonNull DynamicFeaturesView setAdapter() {
        List<DynamicInfo> features = new ArrayList<>();
        for (DynamicFeature feature : getFeatures()) {
            features.add(new DynamicInfo()
                    .setIcon(DynamicResourceUtils.getDrawable(
                            getContext(), feature.getIcon()))
                    .setTitle(getContext().getString(feature.getTitle()))
                    .setDescription(getContext().getString(feature.getSubtitle()))
                    .setIconBig(DynamicResourceUtils.getDrawable(
                            getContext(), R.drawable.adb_ic_feature)));
        }

        setAdapter(new DynamicFeaturesAdapter(features));

        return this;
    }
}
