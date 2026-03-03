import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

public final class RefundPartialExample {
    private RefundPartialExample() {
    }

    public static void main(String[] args) throws Exception {
        String chargeId = ExampleHelper.requiredEnv("CHARGE_PARTIAL_REFUND");
        String amountString = ExampleHelper.envOrDefault("PARTIAL_REFUND_AMOUNT", "50");
        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Environment variable PARTIAL_REFUND_AMOUNT must be a valid integer but was: " + amountString,
                e
            );
        }

        RefundRequest request = RefundRequest.newBuilder()
            .setChargeId(chargeId)
            .setAmount(amount)
            .build();

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            RefundResponse response = client.payments().refund(request);
            ExampleHelper.printProto(response);
        }
    }
}
