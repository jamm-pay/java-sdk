import com.api.v1.MerchantCustomer;
import com.api.v1.UpdateCustomerRequest;
import com.jamm.JammClient;

public final class CustomerUpdateExample {
    private CustomerUpdateExample() {
    }

    public static void main(String[] args) throws Exception {
        String customerId = ExampleHelper.requiredEnv("CUSTOMER");

        UpdateCustomerRequest request = UpdateCustomerRequest.newBuilder()
            .setCustomer(customerId)
            .setEmail("updated@jamm-pay.jp")
            .setName("Yamada Taro")
            .setKatakanaLastName("ヤマダ")
            .setKatakanaFirstName("タロウ")
            .setAddress("Tokyo")
            .setBirthDate("2000-01-02")
            .setGender("male")
            .putMetadata("storeId", "usr12345")
            .build();

        ExampleHelper.run((JammClient client) -> {
            MerchantCustomer response = client.customers().update(request);
            ExampleHelper.printProto(response);
        });
    }
}
