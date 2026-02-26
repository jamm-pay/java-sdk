import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

public final class RefundExample {
    private RefundExample() {
    }

    public static void main(String[] args) throws Exception {
        String chargeId = ExampleHelper.requiredEnv("CHARGE");

        RefundRequest.Builder builder = RefundRequest.newBuilder()
            .setChargeId(chargeId);

        String amount = ExampleHelper.envOrDefault("AMOUNT", "");
        if (!amount.isEmpty()) {
            builder.setAmount(Integer.parseInt(amount));
        }

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            RefundResponse response = client.payments().refund(builder.build());
            ExampleHelper.printProto(response);
        }
    }
}
