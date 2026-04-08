import com.api.v1.Buyer;
import com.api.v1.MerchantCustomer;
import com.api.v1.CreateCustomerRequest;
import com.jamm.JammClient;

/**
 * Creates a customer on behalf of a merchant using platform mode.
 *
 * Required env vars:
 *   PLATFORM_CLIENT_ID, PLATFORM_CLIENT_SECRET, ENV, MERCHANT
 * Optional:
 *   EMAIL (defaults to "java.platform.create@jamm-pay.jp")
 */
public final class PlatformCustomerCreateExample {
    private PlatformCustomerCreateExample() {
    }

    public static void main(String[] args) throws Exception {
        String merchant = ExampleHelper.requiredEnv("MERCHANT");
        String email = ExampleHelper.envOrDefault("EMAIL", "java.platform.create@jamm-pay.jp");

        CreateCustomerRequest request = CreateCustomerRequest.newBuilder()
            .setBuyer(Buyer.newBuilder()
                .setEmail(email)
                .setForceKyc(false)
                .build())
            .build();

        ExampleHelper.runPlatform((JammClient client) -> {
            MerchantCustomer response = client.customers().create(request, merchant);
            ExampleHelper.printProto(response);
        });
    }
}
