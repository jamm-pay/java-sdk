import com.api.v1.GetChargeResponse;
import com.jamm.JammClient;

/**
 * Gets a charge on behalf of a merchant using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CHARGE
 */
public final class PlatformChargeGetExample {
    private PlatformChargeGetExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String chargeId = ExampleHelper.requiredEnv("CHARGE");

        ExampleHelper.runPlatform((JammClient client) -> {
            GetChargeResponse response = client.payments().getCharge(chargeId, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
