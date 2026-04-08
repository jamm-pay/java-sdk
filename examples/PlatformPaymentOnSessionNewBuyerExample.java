import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

/**
 * Creates an on-session payment for a new buyer on behalf of a merchant.
 * This flow does not require an existing customer — a new one is created during onboarding.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT
 * Optional:
 *   PLATFORM_FEE (platform fee in JPY, omitted if not set)
 */
public final class PlatformPaymentOnSessionNewBuyerExample {
    private PlatformPaymentOnSessionNewBuyerExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String platformFeeStr = ExampleHelper.envOrDefault("PLATFORM_FEE", "");

        InitialCharge.Builder chargeBuilder = InitialCharge.newBuilder()
            .setPrice(1000)
            .setDescription("Platform new buyer payment from Java SDK");
        if (!platformFeeStr.isEmpty()) {
            chargeBuilder.setPlatformFee(Integer.parseInt(platformFeeStr));
        }

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setCharge(chargeBuilder.build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://jamm-pay.jp/success")
                .setFailureUrl("https://jamm-pay.jp/fail")
                .build())
            .setBuyer(Buyer.newBuilder()
                .setEmail("new-buyer-" + System.currentTimeMillis() + "@jamm-pay.jp")
                .setName("Platform Buyer")
                .setKatakanaLastName("バイヤー")
                .setKatakanaFirstName("テスト")
                .setAddress("Tokyo, Minato-ku, 1-2-3")
                .setBirthDate("1990-01-01")
                .setGender("male")
                .build())
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
