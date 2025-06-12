package IntegrationsExternalPackage;

import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GstVerificationServiceImpl {
	private List<String> gstNumbers;
	
	public GstVerificationServiceImpl() {
		this.gstNumbers = new ArrayList<>();
		gstNumbers.add("22ABCDE1234F1Z5");
        gstNumbers.add("07XYZPQ6789M1Z8");
        gstNumbers.add("29QWERT1234H1Z2");
        gstNumbers.add("09ZXCVB4567P2Z3");
        gstNumbers.add("33POIUY2345L1Z9");
        gstNumbers.add("19LKJHG9876B1Z1");
        gstNumbers.add("27MNOPQ1234K3Z6");
        gstNumbers.add("23GHJKL6789D1Z4");
        gstNumbers.add("10BVCXZ3456N2Z7");
        gstNumbers.add("08ASDFG7890Q1Z0");
        gstNumbers.add("03HGFDS1234J2Z3");
        gstNumbers.add("26YUIOP3456R1Z5");
        gstNumbers.add("32PLKJH6789C2Z9");
        gstNumbers.add("11QAZWS1234X3Z7");
        gstNumbers.add("25EDCRF4567T1Z2");
        gstNumbers.add("31TVGBY6789V2Z8");
        gstNumbers.add("18XSWED7890Y1Z6");
        gstNumbers.add("05NHYTR1234M3Z4");
        gstNumbers.add("24LPOIU3456B1Z1");
        gstNumbers.add("30LKJHG6789P2Z3");
	}
	
	public boolean checkGstNo(String gstNo) {
		if(verifyGst(gstNo))
			return true;
		else 
		{
			if(gstNumbers.contains(gstNo)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	public boolean verifyGst(String gstNumber) {
        try {
        	@SuppressWarnings("deprecation")
            URL url = new URL("https://api.gstverification.com/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"gstNumber\": \"" + gstNumber + "\"}";

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
