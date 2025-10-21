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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.DynamicInApp;
import com.pranavpandey.android.dynamic.billing.model.base.DynamicProduct;

/**
 * A {@link DynamicInApp} product to represent an {@code external} product.
 */
public abstract class External extends DynamicInApp {

    /**
     * The {@code external} in-app product id.
     */
    public static final String ID = "inapp_app_external";

    /**
     * The type for the in-app external product.
     */
    public static final @DynamicProduct.Type String TYPE = Type.EXTERNAL;

    /**
     * Constructor to initialize an object of this class.
     */
    public External() {
        this(ID);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     */
    public External(@NonNull String id) {
        super(id, TYPE);
    }

    /**
     * Returns the package name for this external product.
     *
     * @return The package name for this external product.
     */
    public abstract @NonNull String getPackageName();

    /**
     * Returns the subtitle string resource used by this product.
     *
     * @return The subtitle string resource used by this product.
     */
    public @StringRes int getSubtitle() {
        return R.string.adb_product_external_google_play;
    }

    /**
     * Checks whether this product is active.
     *
     * @param context The context to match the signatures (if required).
     *
     * @return {@code true} if this product is active.
     */
    public abstract boolean isActive(@NonNull Context context);
}
