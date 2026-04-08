package com.jamm.http;

/**
 * Per-request options for API calls.
 * Currently supports the merchant parameter for platform mode.
 *
 * <p>In platform mode, the merchant parameter allows calling the API on behalf
 * of a specific merchant by setting the "Jamm-Merchant" header.
 */
public final class RequestOptions {

    private final String merchant;

    private RequestOptions(String merchant) {
        this.merchant = merchant;
    }

    /**
     * Gets the merchant ID for this request.
     *
     * @return the merchant ID, or null if not set
     */
    public String getMerchant() {
        return merchant;
    }

    /**
     * Creates options with a merchant ID for platform mode.
     *
     * @param merchant the merchant ID (format: "mer-*")
     * @return new RequestOptions with the merchant set
     */
    public static RequestOptions withMerchant(String merchant) {
        if (merchant == null) {
            throw new IllegalArgumentException(
                    "merchant must not be null; use RequestOptions.none() for no merchant");
        }
        return new RequestOptions(merchant);
    }

    /**
     * Creates empty options (no merchant context).
     *
     * @return new RequestOptions with no merchant set
     */
    public static RequestOptions none() {
        return new RequestOptions(null);
    }
}
