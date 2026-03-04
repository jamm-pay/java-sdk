import com.api.v1.DeleteCustomerResponse;
import com.jamm.JammClient;

public final class CustomerDeleteExample {
    private CustomerDeleteExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        ExampleHelper.run((JammClient client) -> {
            DeleteCustomerResponse response = client.customers().delete(customerId);
            ExampleHelper.printProto(response);
        });
    }
}
