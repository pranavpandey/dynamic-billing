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

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.android.billingclient.api.BillingClient;
import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.util.DynamicBillingUtils;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class to represent the in-app product or subscription.
 */
public class DynamicProduct implements Parcelable {

    /**
     * Interface to hold the product type constants.
     */
    public @interface Type {

        /**
         * String constant for the unknown product type.
         */
        String UNKNOWN = "unknown";

        /**
         * String constant for the in-app product type.
         */
        String INAPP = BillingClient.ProductType.INAPP;

        /**
         * String constant for the subscription product type.
         */
        String SUBS = BillingClient.ProductType.SUBS;

        /**
         * String constant for the external product type.
         */
        String EXTERNAL = "external";
    }
    
    /**
     * Interface to hold the period constants.
     */
    public @interface Period {

        /**
         * Constant for the hourly period.
         */
        String HOUR = "H";

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
     * Constant id for the unknown product.
     */
    public static final String UNKNOWN = "adb_product_unknown";

    /**
     * Unique id of the product.
     */
    private final String id;

    /**
     * Product type of this product.
     */
    private final @DynamicProduct.Type String type;

    /**
     * {@code false} to disable subscriptions if this product is already purchased.
     */
    private final boolean subscriptions;

    /**
     * Dynamic features offered by this product.
     */
    private List<DynamicFeature> features;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicProduct() {
        this(UNKNOWN, DynamicProduct.UNKNOWN);
    }
    
    /**
     * Constructor to initialize an object of this class.
     * 
     * @param id The unique id for this product.
     * @param type The type for this product.
     */
    public DynamicProduct(@NonNull String id, @DynamicProduct.Type String type) {
        this(id, type, true);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this product.
     * @param type The type for this product.
     * @param subscriptions {@code false} to disable subscriptions along with this product.
     */
    public DynamicProduct(@NonNull String id,
            @DynamicProduct.Type String type, boolean subscriptions) {
        this.id = id;
        this.type = type;
        this.subscriptions = subscriptions;
        this.features = new ArrayList<>();
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    public DynamicProduct(@NonNull Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.subscriptions = in.readByte() != 0;
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
        dest.writeByte((byte) (subscriptions ? 1 : 0));
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
    public @DynamicProduct.Type String getType() {
        return type;
    }

    /**
     * Returns the features offered by this product.
     *
     * @return The features offered by this product.
     */
    public List<DynamicFeature> getFeatures() {
        return features;
    }

    /**
     * Add feature offered by this product.
     *
     * @param feature The feature to be offered.
     */
    public void addFeature(@Nullable DynamicFeature feature) {
        if (feature != null && !features.contains(feature)) {
            getFeatures().add(feature);
        }
    }

    /**
     * Add features offered by this product.
     *
     * @param features The features to be offered.
     */
    public void addFeatures(@Nullable List<DynamicFeature> features) {
        if (features == null) {
            return;
        }

        for (DynamicFeature feature : features) {
            addFeature(feature);
        }
    }

    /**
     * Returns whether this product is a base plan.
     *
     * @return {@code true} if this product is a base plan.
     */
    public boolean isBasePlan() {
        return false;
    }

    /**
     * Returns whether is subscriptions are enabled along with this product.
     *
     * @return {@code true} if the subscriptions are enabled along with this product.
     */
    public boolean isSubscriptions() {
        return subscriptions;
    }

    /**
     * Returns the icon for this product.
     *
     * @return The icon for this product.
     */
    public @DrawableRes int getIcon() {
        return R.drawable.adb_ic_premium;
    }

    /**
     * Returns the title string resource used by this product.
     *
     * @return The title string resource used by this product.
     *
     * @see #resolveTitle(Context, String)
     */
    public @StringRes int getTitle() {
        return DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID;
    }

    /**
     * Resolve the product title based on the supplied billing title.
     *
     * @param context The context to retrieve resources.
     * @param billingTitle The title returned by the billing library.
     *
     * @return The resolved product title based on the supplied billing title.
     */
    public @Nullable String resolveTitle(@Nullable Context context,
            @Nullable String billingTitle) {
        if (context == null || getTitle() == DynamicResourceUtils.ADS_DEFAULT_RESOURCE_ID) {
            return billingTitle;
        }

        return context.getString(getTitle());
    }

    /**
     * Build a feature string based on the offered features including
     * icons, titles and subtitles.
     *
     * @param context The context to retrieve resources.
     * @param color The color to tint the icon drawables.
     * @param withSubtitle {@code true} to include the subtitles.
     *
     * @return The feature feature string based on the offered features including
     *         icons, titles and subtitles.
     *
     * @see DynamicBillingUtils#buildFeaturesString(Context, List, int, boolean, boolean)
     */
    public @Nullable SpannableStringBuilder buildFeaturesString(@Nullable Context context,
            @ColorInt int color, boolean icons, boolean withSubtitle) {
        return DynamicBillingUtils.buildFeaturesString(context,
                getFeatures(), color, icons, withSubtitle);
    }
}
