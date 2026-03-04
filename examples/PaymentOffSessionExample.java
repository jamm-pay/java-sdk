import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentRequest;
import com.api.v1.OffSessionPaymentResponse;
import com.jamm.JammClient;

public final class PaymentOffSessionExample {
    private PaymentOffSessionExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        OffSessionPaymentRequest request = OffSessionPaymentRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(InitialCharge.newBuilder().setPrice(100).setDescription("Test charge from Java SDK").build())
            .build();

        ExampleHelper.run((JammClient client) -> {
            OffSessionPaymentResponse response = client.payments().offSessionPayment(request);
            ExampleHelper.printProto(response);
        });
    }
}
