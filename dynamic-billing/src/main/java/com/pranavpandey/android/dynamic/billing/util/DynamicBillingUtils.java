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
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.ProductDetails;
import com.pranavpandey.android.dynamic.billing.R;
import com.pranavpandey.android.dynamic.billing.model.base.DynamicFeature;
import com.pranavpandey.android.dynamic.billing.model.base.DynamicProduct;
import com.pranavpandey.android.dynamic.support.util.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.util.DynamicDeviceUtils;
import com.pranavpandey.android.dynamic.util.DynamicDrawableUtils;
import com.pranavpandey.android.dynamic.util.DynamicLinkUtils;

import java.util.List;

/**
 * Helper class to perform billing related operations.
 */
public class DynamicBillingUtils {

    /**
     * Intent action constant to launch the billing activity.
     */
    public static final String ACTION_ACTIVITY =
            "com.pranavpandey.android.dynamic.billing.intent.action.ACTIVITY";

    /**
     * Intent extra constant for the in-app products.
     */
    public static final String EXTRA_PRODUCTS_INAPP =
            "com.pranavpandey.android.dynamic.billing.intent.extra.PRODUCTS_INAPP";

    /**
     * Intent extra constant for the subscription products.
     */
    public static final String EXTRA_PRODUCTS_SUBS =
            "com.pranavpandey.android.dynamic.billing.intent.extra.PRODUCTS_SUBS";

    /**
     * Intent extra constant for the external products.
     */
    public static final String EXTRA_PRODUCTS_EXTERNAL =
            "com.pranavpandey.android.dynamic.billing.intent.extra.PRODUCTS_EXTERNAL";

    /**
     * Intent extra constant for the products to disable subscriptions.
     *
     * @see DynamicProduct#isSubscriptions()
     */
    public static final String EXTRA_PRODUCTS_DISABLE_SUBS =
            "com.pranavpandey.android.dynamic.billing.intent.extra.PRODUCTS_DISABLE_SUBS";

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
     * URL constant for Google Payments history.
     */
    public static final String URL_GOOGLE_PLAY_ORDER_HISTORY =
            "https://play.google.com/store/account/orderhistory";

    /**
     * Build a feature string based on the supplied collection including
     * icons, titles and subtitles.
     *
     * @param context The context to retrieve resources.
     * @param features The features collection to be processed.
     * @param color The color to tint the icon drawables.
     * @param icons {@code true} to include the icons.
     * @param withSubtitle {@code true} to include the subtitles.
     *
     * @return The feature string based on the supplied collection including icons,
     *         titles and subtitles.
     */
    public static @Nullable SpannableStringBuilder buildFeaturesString(
            @Nullable Context context, @Nullable List<DynamicFeature> features,
            @ColorInt int color, boolean icons, boolean withSubtitle) {
        if (context == null || features == null) {
            return null;
        }

        SpannableStringBuilder iconString = new SpannableStringBuilder();
        SpannableStringBuilder textString = new SpannableStringBuilder();

        for (DynamicFeature feature : features) {
            if (iconString.length() > 0) {
                iconString.append("    ");
            }
            if (textString.length() > 0) {
                textString.append(withSubtitle ? "\n\n" : "\n");
            }

            Spannable title = new SpannableString(context.getString(feature.getTitle()));
            Spannable subtitle = new SpannableString(context.getString(feature.getSubtitle()));
            Drawable drawable = DynamicResourceUtils.getDrawable(context, feature.getIcon());
            drawable = DynamicDrawableUtils.colorizeDrawable(drawable, color);

            if (drawable != null) {
                String span = "[icon]";
                int size = context.getResources()
                        .getDimensionPixelOffset(R.dimen.ads_icon_header);
                drawable.setBounds(0, 0, size, size);

                iconString.append(span);
                iconString.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE),
                        iconString.length() - span.length(), iconString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textString.append(title);
            if (withSubtitle) {
                textString.append("\n").append(subtitle);
            }
        }

        return icons ? iconString.append("\n\n").append(textString) : textString;
    }

    /**
     * Build a feature string based on the supplied collection including
     * icons, titles and subtitles.
     *
     * @param context The context to retrieve resources.
     * @param features The features collection to be processed.
     * @param color The color to tint the icon drawables.
     * @param withSubtitle {@code true} to include the subtitles.
     *
     * @return The feature string based on the supplied collection including icons,
     *         titles and subtitles.
     *
     * @see #buildFeaturesString(Context, List, int, boolean, boolean)
     */
    public static @Nullable SpannableStringBuilder buildFeaturesString(
            @Nullable Context context, @Nullable List<DynamicFeature> features,
            @ColorInt int color, boolean withSubtitle) {
        return buildFeaturesString(context, features, color, true, withSubtitle);
    }

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
     * Returns the user friendly string to display the validity period.
     *
     * @param context The context to retrieve resources.
     * @param period The ISO 8601 formatted pricing phase period.
     *
     * @return The user friendly string to display the validity period.
     */
    public static @Nullable String getStringForValidityPeriod(
            @Nullable Context context, @Nullable String period) {
        if (context == null || period == null) {
            return null;
        }

        int periodCount = Integer.parseInt(period.replaceAll(
                DynamicProduct.Patterns.NUMBER.pattern(), ""));

        if (period.contains(DynamicProduct.Period.HOUR)) {
            return context.getResources().getQuantityString(
                    R.plurals.adb_validity_hours, periodCount, periodCount);
        } else if (period.contains(DynamicProduct.Period.DAY)) {
            return context.getResources().getQuantityString(
                    R.plurals.adb_validity_days, periodCount, periodCount);
        } else if (period.contains(DynamicProduct.Period.WEEK)) {
            return context.getResources().getQuantityString(
                    R.plurals.adb_validity_weeks, periodCount, periodCount);
        } else if (period.contains(DynamicProduct.Period.MONTH)) {
            return context.getResources().getQuantityString(
                    R.plurals.adb_validity_months, periodCount, periodCount);
        } else if (period.contains(DynamicProduct.Period.YEAR)) {
            return context.getResources().getQuantityString(
                    R.plurals.adb_validity_years, periodCount, periodCount);
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
     * Returns a string based on the offer details.
     *
     * @param context The context to retrieve resources.
     * @param offer The offer to retrieve the offer pricing.
     * @param withBase {@code true} to include the base price.
     * @param withValidity {@code true} to include the offer validity.
     *
     * @return A string based on the offer details.
     */
    public static @Nullable String getOfferDetails(@Nullable Context context,
            @Nullable ProductDetails.OneTimePurchaseOfferDetails offer,
            boolean withBase, boolean withValidity) {
        if (context == null || offer == null) {
            return null;
        }

        StringBuilder offerDetailsBuilder = new StringBuilder();
        if (offer.getDiscountDisplayInfo() != null) {
            String discount;
            if (offer.getDiscountDisplayInfo().getDiscountAmount() != null) {
                discount = offer.getDiscountDisplayInfo()
                        .getDiscountAmount().getFormattedDiscountAmount();
            } else if (offer.getDiscountDisplayInfo().getPercentageDiscount() != null) {
                discount = Integer.toString(offer
                        .getDiscountDisplayInfo().getPercentageDiscount());
            } else {
                discount = null;
            }

            if (discount != null) {
                offerDetailsBuilder.replace(0, offerDetailsBuilder.length(),
                        String.format(context.getString(R.string.adb_offer_discount), discount));
            }

            if (withBase) {
                offerDetailsBuilder.replace(0, offerDetailsBuilder.length(),
                        String.format(context.getString(R.string.ads_format_blank_space),
                        offer.getFormattedPrice(), offerDetailsBuilder));
            }

            if (withValidity) {
                offerDetailsBuilder.replace(0, offerDetailsBuilder.length(),
                        String.format(context.getString(R.string.ads_format_next_line),
                        offerDetailsBuilder, getOfferDetailsValidity(context, offer)));
            }
        } else if (withBase) {
            offerDetailsBuilder.append(offer.getFormattedPrice());
        }

        return offerDetailsBuilder.toString();
    }

    /**
     * Returns a string based on the offer validity.
     *
     * @param context The context to retrieve resources.
     * @param offer The offer to retrieve the validity.
     *
     * @return A string based on the offer validity.
     */
    public static @Nullable String getOfferDetailsValidity(@Nullable Context context,
            @Nullable ProductDetails.OneTimePurchaseOfferDetails offer) {
        if (context == null || offer == null) {
            return null;
        }

        StringBuilder offerDetailsBuilder = new StringBuilder();

        if (offer.getValidTimeWindow() != null
                && offer.getValidTimeWindow().getEndTimeMillis() != null) {
            offerDetailsBuilder.append(String.format(
                    context.getString(R.string.adb_offer_validity),
                    DynamicDeviceUtils.getDate(context,
                            offer.getValidTimeWindow().getEndTimeMillis())));
        }

        return offerDetailsBuilder.toString();
    }

    /**
     * Returns a string based on the rental validity.
     *
     * @param context The context to retrieve resources.
     * @param offer The offer to retrieve the rental validity.
     * @param withExpiration {@code true} to include rental expiration period.
     *
     * @return A string based on the rental validity.
     */
    public static @Nullable String getRentalDetails(@Nullable Context context,
            @Nullable ProductDetails.OneTimePurchaseOfferDetails offer, boolean withExpiration) {
        if (context == null || offer == null) {
            return null;
        }

        StringBuilder offerDetailsBuilder = new StringBuilder();

        if (offer.getRentalDetails() != null) {
            offerDetailsBuilder.append(String.format(context.getString(R.string.adb_offer_rent),
                    offer.getFormattedPrice(), getStringForValidityPeriod(context,
                            offer.getRentalDetails().getRentalPeriod())));

            if (withExpiration && offer.getRentalDetails().getRentalExpirationPeriod() != null) {
                offerDetailsBuilder.replace(0, offerDetailsBuilder.length(),
                        String.format(context.getString(R.string.adb_offer_rent_expiration),
                                offerDetailsBuilder, getStringForValidityPeriod(context,
                                        offer.getRentalDetails().getRentalExpirationPeriod())));
            }
        }

        return offerDetailsBuilder.toString();
    }

    /**
     * Try to launch manage account flow for Google Play.
     *
     * @param context The context to build and launch the intent.
     */
    public static void manageGooglePlaySubscriptions(@NonNull Context context) {
        DynamicLinkUtils.viewUrl(context, String.format(
                URL_GOOGLE_PLAY, context.getPackageName()));
    }

    /**
     * Try to launch manage subscription flow for Google Play.
     *
     * @param context The context to build and launch the intent.
     * @param productId The product id for the subscription.
     */
    public static void manageGooglePlaySubscription(
            @NonNull Context context, @Nullable String productId) {
        if (productId == null) {
            manageGooglePlaySubscriptions(context);

            return;
        }

        DynamicLinkUtils.viewUrl(context, String.format(URL_GOOGLE_PLAY_SUB,
                productId, context.getPackageName()));
    }

    /**
     * Try to launch Google Play order history.
     *
     * @param context The context to build and launch the intent.
     */
    public static void viewGooglePlayOrderHistory(@NonNull Context context) {
        DynamicLinkUtils.viewUrl(context, URL_GOOGLE_PLAY_ORDER_HISTORY);
    }
}
