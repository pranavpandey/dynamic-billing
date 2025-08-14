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

package com.pranavpandey.android.dynamic.billing.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;

import java.util.regex.Pattern;

/**
 * A class to represent the in-app product or subscription.
 */
public class DynamicProduct implements Parcelable {

    /**
     * Unique id of the product.
     */
    private final String id;

    /**
     * Product type of this product.
     */
    private final @BillingClient.ProductType String type;

    /**
     * Interface to hold the period constants.
     */
    public @interface Period {

        /**
         * Constant for the daily period.
         */
        String DAY = "D";

        /**
         * Constant for the weekly period.
         */
        String WEEK = "W";

        /**
         * Constant for the monthly period.
         */
        String MONTH = "M";

        /**
         * Constant for the yearly period.
         */
        String YEAR = "Y";
    }

    /**
     * Interface to hold the pricing and period patterns.
     */
    public @interface Patterns {

        /**
         * Pattern to match a digit.
         */
        Pattern DIGIT = Pattern.compile(".*\\d.*");

        /**
         * Pattern to match a number.
         */
        Pattern NUMBER = Pattern.compile("[^0-9]");
    }

    /**
     * Constructor to initialize an object of this class.
     * 
     * @param id The unique id for this product.
     * @param type The type for this product.
     */
    public DynamicProduct(@NonNull String id, @BillingClient.ProductType String type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicProduct(@NonNull Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Parcelable.Creator<DynamicProduct> CREATOR =
            new Parcelable.Creator<DynamicProduct>() {
        @Override
        public DynamicProduct createFromParcel(Parcel in) {
            return new DynamicProduct(in);
        }

        @Override
        public DynamicProduct[] newArray(int size) {
            return new DynamicProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
    }

    /**
     * Returns the unique id for this product.
     *
     * @return The unique id for this product.
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * Returns the type for this product.
     *
     * @return The type for this product.
     */
    public @BillingClient.ProductType String getType() {
        return type;
    }
}
