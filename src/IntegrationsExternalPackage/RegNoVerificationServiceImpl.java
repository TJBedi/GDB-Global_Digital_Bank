package IntegrationsExternalPackage;

import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegNoVerificationServiceImpl {
	private List<String> companyRegistrationNumbers;
	
	public RegNoVerificationServiceImpl() {
		this.companyRegistrationNumbers = new ArrayList<>();
		companyRegistrationNumbers.add("234567890123");
		companyRegistrationNumbers.add("345678901234");
		companyRegistrationNumbers.add("456789012345");
		companyRegistrationNumbers.add("567890123456");
		companyRegistrationNumbers.add("678901234567");
		companyRegistrationNumbers.add("789012345678");
		companyRegistrationNumbers.add("890123456789");
		companyRegistrationNumbers.add("901234567890");
		companyRegistrationNumbers.add("234567123456");
		companyRegistrationNumbers.add("345678234567");
		companyRegistrationNumbers.add("456789345678");
		companyRegistrationNumbers.add("567890456789");
        companyRegistrationNumbers.add("678901567890");
        companyRegistrationNumbers.add("789012678901");
        companyRegistrationNumbers.add("890123789012");
        companyRegistrationNumbers.add("901234890123");
        companyRegistrationNumbers.add("234567901234");
        companyRegistrationNumbers.add("345678012345");
        companyRegistrationNumbers.add("456789123456");
        companyRegistrationNumbers.add("567890234567");
	}
	
	public boolean checkRegNo(String companyRegNum) {
		if(verifyCompanyRegistration(companyRegNum))
			return true;
		else
		{
			if(companyRegistrationNumbers.contains(companyRegNum)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	public boolean verifyCompanyRegistration(String registrationNumber) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL("https://api.companyverification.com/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"registrationNumber\": \"" + registrationNumber + "\"}";

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