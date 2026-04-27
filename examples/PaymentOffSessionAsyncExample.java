import com.api.v1.GetChargeResponse;
import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentAsyncRequest;
import com.api.v1.OffSessionPaymentAsyncResponse;
import com.jamm.JammClient;

public final class PaymentOffSessionAsyncExample {
    private PaymentOffSessionAsyncExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        // Setting idempotency_key makes retries safe — submitting again with the same
        // key returns the existing charge instead of creating a duplicate. Omit the
        // field to let the SDK auto-fill a UUID for one-off charges.
        OffSessionPaymentAsyncRequest request = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer(customerId)
            .setCharge(InitialCharge.newBuilder().setPrice(100).setDescription("Test async charge from Java SDK").build())
            .setIdempotencyKey("order-2024-001")
            .build();

        ExampleHelper.run((JammClient client) -> {
            OffSessionPaymentAsyncResponse asyncResponse = client.payments().offSessionPaymentAsync(request);
            ExampleHelper.printProto(asyncResponse);

            // Fetch the latest charge snapshot using the async response charge ID.
            GetChargeResponse chargeResponse = client.payments().getCharge(asyncResponse.getChargeId());
            ExampleHelper.printProto(chargeResponse);
        });
    }
}
