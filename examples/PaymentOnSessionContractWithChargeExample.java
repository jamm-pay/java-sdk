import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

public final class PaymentOnSessionContractWithChargeExample {
    private PaymentOnSessionContractWithChargeExample() {
    }

    public static void main(String[] args) throws Exception {
        String email = ExampleHelper.envOrDefault("EMAIL", "java.contract.with.charge@jamm-pay.jp");

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder()
                .setEmail(email)
                .setForceKyc(false)
                .build())
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("Java SDK contract with charge")
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
