import com.api.v1.GetChargeResponse;
import com.jamm.JammClient;

public final class ChargeGetExample {
    private ChargeGetExample() {
    }

    public static void main(String[] args) throws Exception {
        String chargeId = ExampleHelper.requiredEnv("CHARGE");

        ExampleHelper.run((JammClient client) -> {
            GetChargeResponse response = client.payments().getCharge(chargeId);
            ExampleHelper.printProto(response);
        });
    }
}
