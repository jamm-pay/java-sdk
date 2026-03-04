import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;

public final class PaymentOnSessionExample {
    private PaymentOnSessionExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(InitialCharge.newBuilder().setPrice(100).setDescription("Test charge from Java SDK").build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://jamm-pay.jp/success")
                .setFailureUrl("https://jamm-pay.jp/fail")
                .build())
            .build();

        ExampleHelper.run((JammClient client) -> {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);
            ExampleHelper.printProto(response);
        });
    }
}
