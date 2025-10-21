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

import com.android.billingclient.api.QueryPurchasesParams;
import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.base.DynamicProduct;

/**
 * A base class to represent an in-app subscription.
 */
public abstract class DynamicSubscription extends DynamicProduct {

    /**
     * The product type for in-app subscription.
     */
    public static final @DynamicProduct.Type String TYPE = DynamicProduct.Type.SUBS;

    /**
     * The default purchase params to query products based on the type.
     */
    public static final QueryPurchasesParams QUERY_PURCHASES_PARAMS =
            QueryPurchasesParams.newBuilder().setProductType(TYPE).build();

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     */
    public DynamicSubscription(@NonNull String id) {
        this(id, TYPE);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The product id to be used.
     * @param type The product type to be used.
     */
    public DynamicSubscription(@NonNull String id, @DynamicProduct.Type String type) {
        super(id, type);
    }

    @Override
    public @DrawableRes int getIcon() {
        return R.drawable.adb_ic_subscription_alt;
    }
}
