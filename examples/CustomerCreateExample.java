import com.api.v1.Buyer;
import com.api.v1.MerchantCustomer;
import com.api.v1.CreateCustomerRequest;
import com.jamm.JammClient;

public final class CustomerCreateExample {
    private CustomerCreateExample() {
    }

    public static void main(String[] args) throws Exception {
        String email = ExampleHelper.envOrDefault("EMAIL", "java.create@jamm-pay.jp");

        CreateCustomerRequest request = CreateCustomerRequest.newBuilder()
            .setBuyer(Buyer.newBuilder()
                .setEmail(email)
                .setForceKyc(false)
                .build())
            .build();

        ExampleHelper.run((JammClient client) -> {
            MerchantCustomer response = client.customers().create(request);
            ExampleHelper.printProto(response);
        });
    }
}
