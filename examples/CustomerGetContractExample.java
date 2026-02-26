import com.api.v1.GetContractResponse;
import com.jamm.JammClient;

public final class CustomerGetContractExample {
    private CustomerGetContractExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            GetContractResponse response = client.customers().getContract(customerId);
            ExampleHelper.printProto(response);
        }
    }
}
