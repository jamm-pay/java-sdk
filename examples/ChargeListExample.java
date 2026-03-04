import com.api.v1.GetChargesRequest;
import com.api.v1.GetChargesResponse;
import com.jamm.JammClient;

public final class ChargeListExample {
    private ChargeListExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        GetChargesRequest request = GetChargesRequest.newBuilder()
            .setCustomer(customerId)
            .build();

        ExampleHelper.run((JammClient client) -> {
            GetChargesResponse response = client.payments().getCharges(request);
            ExampleHelper.printProto(response);
        });
    }
}
