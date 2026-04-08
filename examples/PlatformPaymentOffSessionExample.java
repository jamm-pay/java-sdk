import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentRequest;
import com.api.v1.OffSessionPaymentResponse;
import com.jamm.JammClient;

/**
 * Charges an existing customer off-session on behalf of a merchant using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CUSTOMER
 * Optional:
 *   PLATFORM_FEE (platform fee in JPY, omitted if not set)
 */
public final class PlatformPaymentOffSessionExample {
    private PlatformPaymentOffSessionExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");
        String platformFeeStr = ExampleHelper.envOrDefault("PLATFORM_FEE", "");

        InitialCharge.Builder chargeBuilder = InitialCharge.newBuilder()
            .setPrice(1000)
            .setDescription("Platform off-session payment from Java SDK");
        if (!platformFeeStr.isEmpty()) {
            chargeBuilder.setPlatformFee(Integer.parseInt(platformFeeStr));
        }

        OffSessionPaymentRequest request = OffSessionPaymentRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(chargeBuilder.build())
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            OffSessionPaymentResponse response = client.payments().offSessionPayment(request, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
