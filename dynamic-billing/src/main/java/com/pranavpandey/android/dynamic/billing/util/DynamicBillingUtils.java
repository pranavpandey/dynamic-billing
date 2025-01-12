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

package com.pranavpandey.android.dynamic.billing.util;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.billingclient.api.ProductDetails;
import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.DynamicProduct;

import java.util.List;

/**
 * Helper class to perform billing related operations.
 */
public class DynamicBillingUtils {

    /**
     * Returns the user friendly string to display the trial information.
     *
     * @param context The context to retrieve resources.
     * @param period The ISO 8601 formatted pricing phase period.
     *
     * @return The user friendly string to display the trial information.
     */
    public static @Nullable String getStringForFreeTrial(
            @Nullable Context context, @Nullable String period) {
        if (context == null || period == null) {
            return null;
        }

        int periodCount = Integer.parseInt(period.replaceAll(
                DynamicProduct.Patterns.NUMBER.pattern(), ""));

        if (period.contains(DynamicProduct.Period.DAY)) {
            return String.format(context.getString(
                    R.string.adb_offer_free_trial_day), periodCount);
        } else if (period.contains(DynamicProduct.Period.WEEK)) {
            return String.format(context.getString(
                    R.string.adb_offer_free_trial_week), periodCount);
        } else if (period.contains(DynamicProduct.Period.MONTH)) {
            return String.format(context.getString(
                    R.string.adb_offer_free_trial_month), periodCount);
        } else if (period.contains(DynamicProduct.Period.YEAR)) {
            return String.format(context.getString(
                    R.string.adb_offer_free_trial_year), periodCount);
        }

        return period;
    }

    /**
     * Returns the user friendly string to display the first cycle information.
     *
     * @param context The context to retrieve resources.
     * @param formattedPrice The formatted price along with the currency symbol.
     * @param period The ISO 8601 formatted pricing phase period.
     *
     * @return The user friendly string to display the first cycle information.
     */
    public static @Nullable String getStringForFirstCycle(@Nullable Context context,
            @Nullable String formattedPrice, @Nullable String period) {
        if (context == null || period == null) {
            return null;
        }

        int periodCount = Integer.parseInt(period.replaceAll(
                DynamicProduct.Patterns.NUMBER.pattern(), ""));
        String formattedPeriod = period;

        if (period.contains(DynamicProduct.Period.DAY)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_offer_first_days), periodCount)
                    : context.getString(R.string.adb_offer_first_day);
        } else if (period.contains(DynamicProduct.Period.WEEK)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_offer_first_weeks), periodCount)
                    : context.getString(R.string.adb_offer_first_week);
        } else if (period.contains(DynamicProduct.Period.MONTH)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_offer_first_months), periodCount)
                    : context.getString(R.string.adb_offer_first_month);
        } else if (period.contains(DynamicProduct.Period.YEAR)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_offer_first_years), periodCount)
                    : context.getString(R.string.adb_offer_first_year);
        }

        return String.format(context.getString(R.string.ads_format_blank_space),
                formattedPrice, formattedPeriod);
    }

    /**
     * Returns the user friendly string to display the base cycle information.
     *
     * @param context The context to retrieve resources.
     * @param formattedPrice The formatted price along with the currency symbol.
     * @param period The ISO 8601 formatted pricing phase period.
     *
     * @return The user friendly string to display the base cycle information.
     */
    public static @Nullable String getStringForBaseCycle(@Nullable Context context,
            @Nullable String formattedPrice, @Nullable String period) {
        if (context == null || period == null) {
            return null;
        }

        int periodCount = Integer.parseInt(period.replaceAll(
                DynamicProduct.Patterns.NUMBER.pattern(), ""));
        String formattedPeriod = period;

        if (period.contains(DynamicProduct.Period.DAY)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_price_days), periodCount)
                    : context.getString(R.string.adb_price_day);
        } else if (period.contains(DynamicProduct.Period.WEEK)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_price_weeks), periodCount)
                    : context.getString(R.string.adb_price_week);
        } else if (period.contains(DynamicProduct.Period.MONTH)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_price_months), periodCount)
                    : context.getString(R.string.adb_price_month);
        } else if (period.contains(DynamicProduct.Period.YEAR)) {
            formattedPeriod = periodCount > 1 ? String.format(
                    context.getString(R.string.adb_price_years), periodCount)
                    : context.getString(R.string.adb_price_year);
        }

        return String.format(context.getString(R.string.ads_format_blank_space),
                formattedPrice, formattedPeriod);
    }

    /**
     * Returns a string based on the formatted price, period and cycle count.
     *
     * @param context The context to retrieve resources.
     * @param formattedPrice The formatted price along with the currency symbol.
     * @param period The ISO 8601 formatted pricing phase period.
     * @param cycleCount The period cycle count.
     *
     * @return The string based on the formatted price, period and cycle count.
     */
    public static @Nullable String getStringForPeriod(@Nullable Context context,
            @Nullable String formattedPrice, @Nullable String period, int cycleCount) {
        if (context == null || formattedPrice == null || period == null) {
            return null;
        }

        if (DynamicProduct.Patterns.DIGIT.matcher(formattedPrice).matches()) {
            if (cycleCount == 1) {
                return getStringForFirstCycle(
                        context, formattedPrice, period);
            } else {
                return getStringForBaseCycle(
                        context, formattedPrice, period);
            }
        } else {
            return getStringForFreeTrial(context, period);
        }
    }

    /**
     * Returns a string based on the offer pricing phase(s).
     *
     * @param context The context to retrieve resources.
     * @param pricingPhases The pricing phases.
     * @param withBase {@code true} to include base pricing phase.
     *
     * @return A string based on the offer pricing phase(s).
     *
     * @see #getStringForPeriod(Context, String, String, int)
     */
    public static @Nullable String getPricingPhasesDetails(@Nullable Context context,
            @Nullable List<ProductDetails.PricingPhase> pricingPhases, boolean withBase) {
        if (context == null || pricingPhases == null) {
            return null;
        }

        StringBuilder offerDetailsBuilder = new StringBuilder();
        for (ProductDetails.PricingPhase offerPricingPhase : pricingPhases) {
            if (!withBase && offerPricingPhase.getBillingCycleCount() <= 0) {
                continue;
            }

            String pricingPhaseInfo = getStringForPeriod(context,
                    offerPricingPhase.getFormattedPrice(), offerPricingPhase.getBillingPeriod(),
                    offerPricingPhase.getBillingCycleCount());

            if (offerDetailsBuilder.length() > 0) {
                offerDetailsBuilder.replace(0, offerDetailsBuilder.length(),
                        String.format(context.getString(R.string.ads_format_next_line),
                                offerDetailsBuilder, pricingPhaseInfo));
            } else {
                offerDetailsBuilder.append(pricingPhaseInfo);
            }
        }

        return offerDetailsBuilder.toString();
    }

    /**
     * Returns a string based on the offer pricing phase(s).
     *
     * @param context The context to retrieve resources.
     * @param offer The offer to retrieve the pricing phases.
     * @param withBase {@code true} to include base pricing phase.
     *
     * @return A string based on the offer pricing phase(s).
     *
     * @see #getPricingPhasesDetails(Context, List, boolean)
     */
    public static @Nullable String getOfferDetails(@Nullable Context context,
            @Nullable ProductDetails.SubscriptionOfferDetails offer, boolean withBase) {
        if (context == null || offer == null) {
            return null;
        }

        return getPricingPhasesDetails(context,
                offer.getPricingPhases().getPricingPhaseList(), withBase);
    }

    /**
     * Returns a string based on the base offer pricing.
     *
     * @param context The context to retrieve resources.
     * @param offer The offer to retrieve the pricing phases.
     *
     * @return A string based on the base offer pricing.
     *
     * @see #getPricingPhasesDetails(Context, List, boolean)
     */
    public static @Nullable String getOfferDetailsBase(@Nullable Context context,
            @Nullable ProductDetails.SubscriptionOfferDetails offer) {
        if (context == null || offer == null) {
            return null;
        }

        int size = offer.getPricingPhases().getPricingPhaseList().size();
        return getPricingPhasesDetails(context, offer.getPricingPhases()
                .getPricingPhaseList().subList(size - 1, size), true);
    }
}
