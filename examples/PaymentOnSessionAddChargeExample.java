import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

public final class PaymentOnSessionAddChargeExample {
    private PaymentOnSessionAddChargeExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("Java SDK add charge")
                .build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://example.com/success")
                .setFailureUrl("https://example.com/failure")
                .build())
            .build();

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);
            ExampleHelper.printProto(response);
        }
    }
}
