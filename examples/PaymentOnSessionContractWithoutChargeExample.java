import com.api.v1.Buyer;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.jamm.JammClient;

public final class PaymentOnSessionContractWithoutChargeExample {
    private PaymentOnSessionContractWithoutChargeExample() {
    }

    public static void main(String[] args) throws Exception {
        String email = ExampleHelper.envOrDefault("EMAIL", "java.contract.only@jamm-pay.jp");

        OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder()
                .setEmail(email)
                .setForceKyc(false)
                .build())
            .build();

        ExampleHelper.run((JammClient client) -> {
            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);
            ExampleHelper.printProto(response);
        });
    }
}
