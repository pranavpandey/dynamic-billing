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

package com.pranavpandey.android.dynamic.billing.model.factory.plan;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.DynamicPlan;

/**
 * A {@link DynamicPlan} product to represent {@code half-yearly (6 months)} validity.
 *
 * @see #ID
 */
public class HalfYearly extends DynamicPlan {

    /**
     * The {@code half-yearly (6 months)} subscription product id.
     */
    public static final String ID = "subs-plan-base-month-6";

    /**
     * Constructor to initialize an object of this class.
     */
    public HalfYearly() {
        this(ID);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     */
    public HalfYearly(@NonNull String id) {
        super(id);
    }

    @Override
    public @StringRes int getTitle() {
        return R.string.adb_subs_half_yearly;
    }
}
