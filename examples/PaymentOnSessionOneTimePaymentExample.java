import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

public final class PaymentOnSessionOneTimePaymentExample {
    private PaymentOnSessionOneTimePaymentExample() {
    }

    public static void main(String[] args) throws Exception {
        String email = ExampleHelper.envOrDefault("EMAIL", "java.one.time@jamm-pay.jp");

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder()
                .setEmail(email)
                .setForceKyc(false)
                .build())
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("Java SDK one-time payment")
                .build())
            .setOneTime(true)
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://example.com/success")
                .setFailureUrl("https://example.com/failure")
                .build())
            .build();

        ExampleHelper.run((JammClient client) -> {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);
            ExampleHelper.printProto(response);
        });
    }
}
