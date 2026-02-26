import com.api.v1.Customer;
import com.jamm.JammClient;

public final class CustomerGetExample {
    private CustomerGetExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            Customer response = client.customers().get(customerId);
            ExampleHelper.printProto(response);
        }
    }
}
