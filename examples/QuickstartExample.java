import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

public final class QuickstartExample {
    private QuickstartExample() {
    }

    public static void main(String[] args) throws Exception {
        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            String email = ExampleHelper.envOrDefault("EMAIL", "java.test@jamm-pay.jp");

            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                .setBuyer(Buyer.newBuilder().setEmail(email).setForceKyc(false).build())
                .setCharge(InitialCharge.newBuilder().setPrice(100).setDescription("Java SDK quickstart").build())
                .setRedirect(URL.newBuilder()
                    .setSuccessUrl("https://example.com/success")
                    .setFailureUrl("https://example.com/failure")
                    .build())
                .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);
            ExampleHelper.printProto(response);
        }
    }
}
