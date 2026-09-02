import com.api.v1.ChargeResult;
import com.api.v1.ChargeStatus;
import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentAsyncRequest;
import com.api.v1.OffSessionPaymentAsyncResponse;
import com.jamm.JammClient;

/**
 * Starts an async off-session charge for an existing customer on behalf of a merchant
 * using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CUSTOMER
 * Optional:
 *   PLATFORM_FEE (platform fee in JPY, omitted if not set)
 */
public final class PlatformPaymentOffSessionAsyncExample {
    private PlatformPaymentOffSessionAsyncExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");
        String platformFeeStr = ExampleHelper.envOrDefault("PLATFORM_FEE", "");

        InitialCharge.Builder chargeBuilder = InitialCharge.newBuilder()
            .setPrice(1000)
            .setDescription("Platform off-session async payment from Java SDK");
        if (!platformFeeStr.isEmpty()) {
            chargeBuilder.setPlatformFee(Integer.parseInt(platformFeeStr));
        }

        OffSessionPaymentAsyncRequest request = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(chargeBuilder.build())
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            OffSessionPaymentAsyncResponse response = client.payments().offSessionPaymentAsync(request, merchant);
            ExampleHelper.printProto(response);

            // The charge is pending above, so poll it to a terminal state before printing the result.
            ChargeResult charge = awaitCharge(client, response.getChargeId(), merchant);
            if (charge == null) {
                System.out.println("charge " + response.getChargeId()
                    + " still pending — unresolved, not unpaid; wait for the charge webhook");
                return;
            }
            ExampleHelper.printProto(charge);
        });
    }

    /** Polls until the charge leaves CHARGE_STATUS_PENDING; null if it is still pending after ~60s. */
    private static ChargeResult awaitCharge(JammClient client, String chargeId, String merchant)
            throws InterruptedException {
        for (int attempt = 0; attempt < 30; attempt++) {
            ChargeResult charge = client.payments().getCharge(chargeId, merchant).getCharge();
            if (charge.getChargeStatus() != ChargeStatus.CHARGE_STATUS_PENDING) {
                return charge;
            }
            Thread.sleep(2000);
        }
        return null;
    }
}
