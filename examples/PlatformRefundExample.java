import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

/**
 * Refunds a charge on behalf of a merchant using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CHARGE
 */
public final class PlatformRefundExample {
    private PlatformRefundExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String chargeId = ExampleHelper.requiredEnv("CHARGE");

        RefundRequest request = RefundRequest.newBuilder()
            .setChargeId(chargeId)
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            RefundResponse response = client.payments().refund(request, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
