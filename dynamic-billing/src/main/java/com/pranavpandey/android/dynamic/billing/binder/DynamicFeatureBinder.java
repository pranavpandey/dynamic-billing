/*
 * Copyright 2022 Pranav Pandey
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

package com.pranavpandey.android.dynamic.billing.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.adapter.DynamicFeaturesAdapter;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.model.DynamicInfo;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.InfoBigBinder;
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.InfoBinder;

/**
 * An {@link InfoBigBinder} to bind the {@link DynamicInfo} inside a
 * {@link androidx.cardview.widget.CardView} that can be used with the
 * {@link DynamicFeaturesAdapter}.
 */
public class DynamicFeatureBinder extends InfoBinder<String> {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param binderAdapter The dynamic features adapter for the recycler view.
     */
    public DynamicFeatureBinder(@NonNull DynamicFeaturesAdapter binderAdapter) {
        super(binderAdapter);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adb_layout_feature_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Dynamic.setColorType(holder.getDynamicInfo().getIconView(),
                Defaults.ADS_COLOR_TYPE_ICON);
    }
}
