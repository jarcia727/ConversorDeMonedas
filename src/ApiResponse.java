import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ApiResponse {

        @SerializedName("result")
        String result;

        @SerializedName("base_code")
        String baseCode;

        @SerializedName("conversion_rates")
        Map<String, Double> conversionRates;


}
