import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

public final class RefundCancelOnlyExample {
    private RefundCancelOnlyExample() {
    }

    public static void main(String[] args) throws Exception {
        String chargeId = ExampleHelper.requiredEnv("CHARGE");

        RefundRequest request = RefundRequest.newBuilder()
            .setChargeId(chargeId)
            .setCancelOnly(true)
            .build();

        ExampleHelper.run((JammClient client) -> {
            RefundResponse response = client.payments().refund(request);
            ExampleHelper.printProto(response);
        });
    }
}
