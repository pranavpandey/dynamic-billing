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

package com.pranavpandey.android.dynamic.billing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.android.billingclient.api.QueryPurchasesParams;
import com.pranavpandey.android.dynamic.billing.listener.DynamicBillingListener;
import com.pranavpandey.android.dynamic.billing.model.base.DynamicFeature;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to handle the billing related operations including subscriptions.
 * <p>It must be initialized once before accessing its methods.
 */
public class DynamicBilling {

    /**
     * URL constant to manage billing inside Google Play.
     */
    public static final String URL_GOOGLE_PLAY =
            "http://play.google.com/store/account/subscriptions?package=%1$s";

    /**
     * URL constant to manage subscription inside Google Play.
     */
    public static final String URL_GOOGLE_PLAY_SUB =
            "http://play.google.com/store/account/subscriptions?sku=%1$s&package=%2$s";

    /**
     * Singleton instance of {@link DynamicBilling}.
     */
    @SuppressLint("StaticFieldLeak")
    private static DynamicBilling sInstance;

    /**
     * Context to retrieve the resources.
     */
    private Context mContext;

    /**
     * The billing client.
     */
    private BillingClient mBillingClient;

    /**
     * Listener to listen the billing client state events.
     */
    private final BillingClientStateListener mBillingStateListener;

    /**
     * Listener to listen the purchase update events.
     */
    private final PurchasesUpdatedListener mPurchasesUpdatedListener;

    /**
     * Listener to listen the product details event.
     */
    private final ProductDetailsResponseListener mProductDetailsResponseListener;

    /**
     * Listener to listen the purchase response.
     */
    private final PurchasesResponseListener mPurchasesResponseListener;

    /**
     * Listener to listen the purchase consumption.
     */
    private final ConsumeResponseListener mConsumeResponseListener;

    /**
     * Listener to listen the purchase acknowledgement.
     */
    private final AcknowledgePurchaseResponseListener mAcknowledgePurchaseResponseListener;

    /**
     * List of listeners to receive billing callbacks.
     */
    private final List<DynamicBillingListener> mBillingListeners;

    /**
     * The billing state result.
     */
    private BillingResult mBillingResult;

    /**
     * Main thread handler to publish results.
     */
    private final Handler mHandler;

    /**
     * Making default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #initializeInstance(Context)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    private DynamicBilling() {
        this.mBillingListeners = new ArrayList<>();
        this.mHandler = new Handler(Looper.getMainLooper());

        this.mBillingStateListener = new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (BillingClientStateListener listener : getPurchaseListeners()) {
                            listener.onBillingServiceDisconnected();
                        }
                    }
                });
            }

            @Override
            public void onBillingSetupFinished(final @NonNull BillingResult billingResult) {
                mBillingResult = billingResult;

                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    onBillingServiceDisconnected();

                    return;
                }

                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (BillingClientStateListener listener : getPurchaseListeners()) {
                            listener.onBillingSetupFinished(billingResult);
                        }
                    }
                });
            }
        };

        this.mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(final @NonNull BillingResult billingResult,
                    final @Nullable List<Purchase> purchases) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (PurchasesUpdatedListener listener : getPurchaseListeners()) {
                            listener.onPurchasesUpdated(billingResult, purchases);
                        }
                    }
                });
            }
        };

        this.mProductDetailsResponseListener = new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(final @NonNull BillingResult billingResult,
                    final @NonNull QueryProductDetailsResult queryProductDetailsResult) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (ProductDetailsResponseListener listener : getPurchaseListeners()) {
                            listener.onProductDetailsResponse(billingResult,
                                    queryProductDetailsResult);
                        }
                    }
                });
            }
        };

        this.mPurchasesResponseListener = new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(final @NonNull BillingResult billingResult,
                    final @NonNull List<Purchase> purchases) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (PurchasesResponseListener listener : getPurchaseListeners()) {
                            listener.onQueryPurchasesResponse(billingResult, purchases);
                        }
                    }
                });
            }
        };

        this.mConsumeResponseListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult,
                    @NonNull String purchaseToken) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (ConsumeResponseListener listener : getPurchaseListeners()) {
                            listener.onConsumeResponse(billingResult, purchaseToken);
                        }
                    }
                });
            }
        };

        this.mAcknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (AcknowledgePurchaseResponseListener listener
                                : getPurchaseListeners()) {
                            listener.onAcknowledgePurchaseResponse(billingResult);
                        }
                    }
                });
            }
        };
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context to be used.
     */
    private DynamicBilling(@NonNull Context context) {
        this();

        this.mContext = context;

        this.mBillingClient = BillingClient.newBuilder(getContext())
                .setListener(mPurchasesUpdatedListener)
                .enablePendingPurchases(PendingPurchasesParams.newBuilder()
                        .enablePrepaidPlans().enableOneTimeProducts().build())
                .enableAutoServiceReconnection()
                .build();

        if (getContext() instanceof DynamicBillingListener) {
            addListener((DynamicBillingListener) getContext());
        }

        startConnection();
    }

    /**
     * Initialize the billing client when application starts.
     * <p>Must be initialized once.
     *
     * @param context The context to retrieve resources.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null.");
        }

        if (sInstance == null) {
            sInstance = new DynamicBilling(!(context instanceof Application)
                    ? context.getApplicationContext() : context);
        }
    }

    /**
     * Retrieves the singleton instance of {@link DynamicBilling}.
     * <p>Must be called before accessing the public methods.
     *
     * @return The singleton instance of {@link DynamicBilling}.
     */
    public static synchronized @NonNull DynamicBilling getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicBilling.class.getSimpleName() +
                    " is not initialized, call initializeInstance(...) method first.");
        }

        return sInstance;
    }

    /**
     * Returns the context used by this instance.
     *
     * @return The context to retrieve the resources.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * The initialized billing client.
     *
     * @return The initialized billing client.
     */
    public @NonNull BillingClient getBillingClient() {
        return mBillingClient;
    }

    /**
     * Returns the list of billing listeners handled by this handler.
     *
     * @return The list of billing listeners handled by this handler.
     */
    public @NonNull List<DynamicBillingListener> getPurchaseListeners() {
        return mBillingListeners;
    }

    /**
     * Add a billing listener to receive the various callbacks.
     *
     * @param listener The billing listener to be added.
     *
     * @return The {@link DynamicBilling} object to allow for chaining of calls to set methods.
     *
     * @see DynamicBillingListener
     */
    public @NonNull DynamicBilling addListener(@Nullable DynamicBillingListener listener) {
        if (listener != null && !getPurchaseListeners().contains(listener)) {
            getPurchaseListeners().add(listener);

            if (isConnected()) {
                listener.onBillingSetupFinished(mBillingResult);
            } else {
                startConnection();
            }
        }

        return this;
    }

    /**
     * Remove a billing listener.
     *
     * @param listener The billing listener to be removed.
     *
     * @return The {@link DynamicBilling} object to allow for chaining of calls to set methods.
     *
     * @see DynamicBillingListener
     */
    public @NonNull DynamicBilling removeListener(@Nullable DynamicBillingListener listener) {
        getPurchaseListeners().remove(listener);

        return this;
    }

    /**
     * Try to start connection with the billing service.
     */
    public void startConnection() {
        if (mBillingResult != null && (mBillingResult.getResponseCode()
                == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE
                || mBillingResult.getResponseCode()
                == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE)) {
            if (mBillingStateListener != null) {
                mBillingStateListener.onBillingServiceDisconnected();
            }

            return;
        }

        if (isInitialized()) {
            mBillingClient.startConnection(mBillingStateListener);
        }
    }

    /**
     * Query product details for the supplied params.
     *
     * @param params The params to query the product details.
     */
    public void queryProductDetailsAsync(@NonNull QueryProductDetailsParams params) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        mBillingClient.queryProductDetailsAsync(params, mProductDetailsResponseListener);
    }

    /**
     * Query purchases for the supplied params.
     *
     * @param params The params to query the purchases.
     */
    public void queryPurchasesAsync(@NonNull QueryPurchasesParams params) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        mBillingClient.queryPurchasesAsync(params, mPurchasesResponseListener);
    }

    /**
     * Try to consume the supplied purchase.
     *
     * @param billingResult The billing result to be used.
     * @param purchase The purchase to be consumed.
     */
    public void consumePurchase(@NonNull BillingResult billingResult,
            @Nullable Purchase purchase) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        if (purchase != null && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            mBillingClient.consumeAsync(ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken()).build(),
                    mConsumeResponseListener);
        }
    }

    /**
     * Try to consume the supplied purchases.
     *
     * @param billingResult The billing result to be used.
     * @param purchases The purchases to be acknowledged.
     */
    public void consumePurchases(@NonNull BillingResult billingResult,
            @NonNull List<Purchase> purchases) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            for (Purchase purchase : purchases) {
                consumePurchase(billingResult, purchase);
            }
        }
    }

    /**
     * Try to acknowledge the supplied purchase.
     *
     * @param billingResult The billing result to be used.
     * @param purchase The purchase to be acknowledged.
     */
    public void acknowledgePurchase(@NonNull BillingResult billingResult,
            @Nullable Purchase purchase) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        if (purchase != null && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                mBillingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken()).build(),
                        mAcknowledgePurchaseResponseListener);
            }
        }
    }

    /**
     * Try to acknowledge the supplied purchases.
     *
     * @param billingResult The billing result to be used.
     * @param purchases The purchases to be acknowledged.
     */
    public void acknowledgePurchases(@NonNull BillingResult billingResult,
            @Nullable List<Purchase> purchases) {
        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                acknowledgePurchase(billingResult, purchase);
            }
        }
    }

    /**
     * Launch the billing flow for the supplied params.
     *
     * @param activity The activity to be used.
     * @param params The billing flow params to be used.
     */
    public void launchBillingFlow(@Nullable Activity activity, @NonNull BillingFlowParams params) {
        if (activity == null) {
            return;
        }

        if (!isInitialized() || !isConnected()) {
            startConnection();

            return;
        }

        mBillingClient.launchBillingFlow(activity, params);
    }

    /**
     * Returns the main thread handler to publish results.
     *
     * @return The main thread handler to publish results.
     */
    public @NonNull Handler getHandler() {
        return mHandler;
    }

    /**
     * Returns whether the billing client has been initialized.
     *
     * @return {@code true} if the billing client have been initialized.
     */
    public boolean isInitialized() {
        return mBillingStateListener != null && mPurchasesUpdatedListener != null
                && mProductDetailsResponseListener != null && mBillingClient != null;
    }

    /**
     * Returns whether the billing client is connected.
     *
     * @return {@code true} if the billing client is connected.
     */
    public boolean isConnected() {
        return isInitialized() && mBillingClient.getConnectionState()
                == BillingClient.ConnectionState.CONNECTED;
    }

    /**
     * Try to find the dynamic feature for the supplied id.
     *
     * @param id The feature id to be used.
     *
     * @return The dynamic feature for the supplied id if found,
     *         otherwise {@link DynamicFeature#UNKNOWN} feature.
     */
    public @NonNull DynamicFeature getFeatureById(@NonNull String id) {
        for (DynamicBillingListener listener : getPurchaseListeners()) {
            if (listener instanceof DynamicFeature
                    && id.equals(((DynamicFeature) listener).getId())) {
                return (DynamicFeature) listener;
            }
        }

        return new DynamicFeature();
    }

    /**
     * Try to launch manage account flow for Google Play.
     */
    public void manageGooglePlay() {
        DynamicLinkUtils.viewUrl(getContext(), String.format(
                URL_GOOGLE_PLAY, getContext().getPackageName()));
    }

    /**
     * Try to launch manage subscription flow for Google Play.
     *
     * @param productId The product id for the subscription.
     */
    public void manageGooglePlaySubscription(@Nullable String productId) {
        if (productId == null) {
            manageGooglePlay();

            return;
        }

        DynamicLinkUtils.viewUrl(getContext(), String.format(
                URL_GOOGLE_PLAY_SUB, productId, getContext().getPackageName()));
    }
}
