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

package com.pranavpandey.android.dynamic.billing.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.billing.R;

/**
 * A {@link DynamicSubscription} product to represent a base plan.
 */
public abstract class DynamicPlan extends DynamicSubscription {

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     */
    public DynamicPlan(@NonNull String id) {
        super(id);
    }

    @Override
    public boolean isBasePlan() {
        return true;
    }

    @Override
    public @DrawableRes int getIcon() {
        return R.drawable.adb_ic_product_alt;
    }
}
