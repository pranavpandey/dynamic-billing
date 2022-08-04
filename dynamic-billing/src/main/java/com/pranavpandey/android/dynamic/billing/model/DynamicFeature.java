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

package com.pranavpandey.android.dynamic.billing.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.listener.DynamicBillingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent the dynamic feature.
 */
public class DynamicFeature implements Parcelable, DynamicBillingListener {

    /**
     * Constant id for the unknown feature.
     */
    public static final String UNKNOWN = "adb_feature_unknown";

    /**
     * Constant value for the unknown resource.
     */
    public static final int UNKNOWN_RES = -1;

    /**
     * Unique id of the feature.
     */
    private final String id;

    /**
     * Icon drawable resource used by this feature.
     */
    private final @DrawableRes int icon;

    /**
     * Title string resource used by this feature.
     */
    private final @StringRes int title;

    /**
     * Subtitle string resource used by this feature.
     */
    private final @StringRes int subtitle;

    /**
     * List of products to enable this feature.
     */
    private final List<DynamicProduct> products;

    /**
     * {@code true} if this feature is enabled.
     */
    private boolean enabled;

    /**
     * Constructor to initialize an object of this class.
     */
    public DynamicFeature() {
        this(UNKNOWN, new ArrayList<>());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this feature.
     */
    public DynamicFeature(@NonNull String id) {
        this(id, new ArrayList<>());
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this feature.
     * @param products The list of products to enable this feature.
     */
    public DynamicFeature(@NonNull String id, @NonNull List<DynamicProduct> products) {
        this(id, products, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this feature.
     * @param products The list of products to enable this feature.
     * @param enabled {@code true} if this feature is enabled.
     */
    public DynamicFeature(@NonNull String id,
            @NonNull List<DynamicProduct> products, boolean enabled) {
        this(id, R.drawable.adb_ic_product, R.string.adb_product, UNKNOWN_RES, products, enabled);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this feature.
     * @param icon The icon drawable resource for this feature.
     * @param title The title string resource for this feature.
     * @param subtitle The subtitle string resource for this feature.
     * @param products The list of products to enable this feature.
     */
    public DynamicFeature(@NonNull String id, @DrawableRes int icon, @StringRes int title,
            @StringRes int subtitle, @NonNull List<DynamicProduct> products) {
        this(id, icon, title, subtitle, products, false);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param id The unique id for this feature.
     * @param icon The icon drawable resource for this feature.
     * @param title The title string resource for this feature.
     * @param subtitle The subtitle string resource for this feature.
     * @param products The list of products to enable this feature.
     * @param enabled {@code true} if this feature is enabled.
     */
    public DynamicFeature(@NonNull String id, @DrawableRes int icon, @StringRes int title,
            @StringRes int subtitle, @NonNull List<DynamicProduct> products, boolean enabled) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.products = products;
        this.enabled = enabled;
    }

    /**
     * Read an object of this class from the parcel.
     *
     * @param in The parcel to read the values.
     */
    @SuppressWarnings("unchecked")
    public DynamicFeature(@NonNull Parcel in) {
        this.id = in.readString();
        this.icon = in.readInt();
        this.title = in.readInt();
        this.subtitle = in.readInt();
        this.products = in.readArrayList(DynamicProduct.class.getClassLoader());
        this.enabled = in.readByte() != 0;
    }

    /**
     * Parcelable creator to create from parcel.
     */
    public static final Creator<DynamicFeature> CREATOR =
            new Creator<DynamicFeature>() {
        @Override
        public DynamicFeature createFromParcel(Parcel in) {
            return new DynamicFeature(in);
        }

        @Override
        public DynamicFeature[] newArray(int size) {
            return new DynamicFeature[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(icon);
        dest.writeInt(title);
        dest.writeInt(subtitle);
        dest.writeList(products);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    /**
     * Returns the unique id for this feature.
     *
     * @return The unique id for this feature.
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * Returns the icon drawable resource used by this feature.
     * 
     * @return The icon drawable resource used by this feature.
     */
    public @DrawableRes int getIcon() {
        return icon;
    }

    /**
     * Returns the title string resource used by this feature.
     *
     * @return The title string resource used by this feature.
     */
    public @StringRes int getTitle() {
        return title;
    }

    /**
     * Returns the subtitle string resource used by this feature.
     *
     * @return The subtitle string resource used by this feature.
     */
    public @StringRes int getSubtitle() {
        return subtitle;
    }

    /**
     * The list of products to enable this feature.
     *
     * @return The list of products to enable this feature.
     */
    public @NonNull List<DynamicProduct> getProducts() {
        return products;
    }

    /**
     * Returns whether this feature is enabled.
     *
     * @return {@code true} if this feature is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this feature is enabled.
     *
     * @param enabled {@code true} to enable this feature.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * This method will be called to verify the status for this feature.
     *
     * @param billingResult The billing result to be used.
     * @param purchases The purchases to be verified.
     */
    public void onVerifyStatus(@NonNull BillingResult billingResult,
            @Nullable List<Purchase> purchases) {
        if (purchases == null || getProducts().isEmpty()
                || billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            return;
        }

        setEnabled(false);

        for (DynamicProduct product : getProducts()) {
            for (Purchase purchase : purchases) {
                if (purchase.getProducts().contains(product.getId())) {
                    setEnabled(true);

                    break;
                }
            }
        }
    }

    @Override
    public void onBillingServiceDisconnected() { }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) { }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
            @Nullable List<Purchase> purchases) {
        onVerifyStatus(billingResult, purchases);
    }

    @Override
    public void onProductDetailsResponse(@NonNull BillingResult billingResult,
            @NonNull List<ProductDetails> productDetails) { }

    @Override
    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult,
            @NonNull List<Purchase> purchases) {
        onVerifyStatus(billingResult, purchases);
    }

    @Override
    public void onConsumeResponse(@NonNull BillingResult billingResult,
            @NonNull String purchaseToken) { }

    @Override
    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) { }
}
