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

package com.pranavpandey.android.dynamic.billing.model.factory;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.DynamicSubscription;

/**
 * A {@link DynamicSubscription} product to represent {@code premium} validity.
 *
 * @see #ID
 */
public class Premium extends DynamicSubscription {

    /**
     * The {@code premium} subscription product id.
     */
    public static final String ID = "subs_app_premium";

    /**
     * Constructor to initialize an object of this class.
     */
    public Premium() {
        this(ID);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     */
    public Premium(@NonNull String id) {
        super(id);
    }

    @Override
    public @StringRes int getTitle() {
        return R.string.adb_premium;
    }
}
