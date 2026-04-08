import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

/**
 * Creates an on-session payment for an existing customer on behalf of a merchant.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CUSTOMER
 * Optional:
 *   PLATFORM_FEE (platform fee in JPY, omitted if not set)
 */
public final class PlatformPaymentOnSessionExample {
    private PlatformPaymentOnSessionExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");
        String platformFeeStr = ExampleHelper.envOrDefault("PLATFORM_FEE", "");

        InitialCharge.Builder chargeBuilder = InitialCharge.newBuilder()
            .setPrice(1000)
            .setDescription("Platform payment from Java SDK");
        if (!platformFeeStr.isEmpty()) {
            chargeBuilder.setPlatformFee(Integer.parseInt(platformFeeStr));
        }

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(chargeBuilder.build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://jamm-pay.jp/success")
                .setFailureUrl("https://jamm-pay.jp/fail")
                .build())
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
