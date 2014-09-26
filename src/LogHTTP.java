import java.net.*;
import java.io.*;


public class LogHTTP { 
	protected String cred;
	protected String baseURL;

	public LogHTTP(String b) {
		baseURL=b;
	}

	public LogHTTP(IniFile ini) {
		baseURL=ini.getValue("LOGHTTP","baseURL");
	}

	public void sendRecord(String record) {
		try {
			String url = baseURL + URLEncoder.encode(record,"utf-8");

			System.err.println("# requesting: " + url);

			/* connect to the server */
			URL u = new URL(url);
			URLConnection uc = u.openConnection();


			HttpURLConnection connection = (HttpURLConnection) uc;
			connection.setDoInput(true); 
			connection.setRequestMethod("GET");
      
			/* Read the response */
			InputStream in = connection.getInputStream(); 
			String line; 

			BufferedReader is = new BufferedReader(new InputStreamReader(in));

			System.out.println("################## LogHTTP server responded ######################");
			while ( (line = is.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("########################### done #################################");
		
			in.close();
			connection.disconnect();
		} catch (IOException e) {
			System.err.println(e); 
		}
	}
}
