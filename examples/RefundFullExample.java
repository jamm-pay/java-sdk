import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

public final class RefundFullExample {
    private RefundFullExample() {
    }

    public static void main(String[] args) throws Exception {
        String chargeId = ExampleHelper.requiredEnv("CHARGE_FULL_REFUND");

        RefundRequest request = RefundRequest.newBuilder()
            .setChargeId(chargeId)
            .build();

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            RefundResponse response = client.payments().refund(request);
            ExampleHelper.printProto(response);
        }
    }
}
