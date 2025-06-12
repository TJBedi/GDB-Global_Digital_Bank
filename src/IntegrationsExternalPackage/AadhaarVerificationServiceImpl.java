package IntegrationsExternalPackage;

import java.util.List;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AadhaarVerificationServiceImpl {
private List<String> aadhaarNumbers;
	
	public AadhaarVerificationServiceImpl() {
		this.aadhaarNumbers = new ArrayList<>();
		aadhaarNumbers.add("234567890123");
		aadhaarNumbers.add("345678901234");
		aadhaarNumbers.add("456789012345");
		aadhaarNumbers.add("567890123456");
		aadhaarNumbers.add("678901234567");
		aadhaarNumbers.add("789012345678");
		aadhaarNumbers.add("890123456789");
		aadhaarNumbers.add("901234567890");
		aadhaarNumbers.add("234567123456");
		aadhaarNumbers.add("345678234567");
		aadhaarNumbers.add("456789345678");
        aadhaarNumbers.add("567890456789");
        aadhaarNumbers.add("678901567890");
        aadhaarNumbers.add("789012678901");
        aadhaarNumbers.add("890123789012");
        aadhaarNumbers.add("901234890123");
        aadhaarNumbers.add("234567901234");
        aadhaarNumbers.add("345678012345");
        aadhaarNumbers.add("456789123456");
        aadhaarNumbers.add("567890234567");
        aadhaarNumbers.add("221234567890");
	}
	
	public boolean checkAadhaar(String aadhaarNum) {
		if(verifyAadhaar(aadhaarNum))
			return true;
		else
		{
			if(aadhaarNumbers.contains(aadhaarNum)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	public boolean verifyAadhaar(String aadhaarNumber) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL("https://api.aadhaarverification.com/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"aadhaarNumber\": \"" + aadhaarNumber + "\"}";

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }

            try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Assuming the API returns a JSON with a field "valid"
                return response.toString().contains("\"valid\":true");
            }
        } catch (Exception e) {
            return false;
        }
    }
}

