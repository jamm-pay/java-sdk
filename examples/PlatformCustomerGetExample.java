import com.api.v1.Customer;
import com.jamm.JammClient;

/**
 * Gets a customer on behalf of a merchant using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT, CUSTOMER
 */
public final class PlatformCustomerGetExample {
    private PlatformCustomerGetExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        ExampleHelper.runPlatform((JammClient client) -> {
            Customer response = client.customers().get(customerId, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
