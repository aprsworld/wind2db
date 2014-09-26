import java.net.*;
import java.io.*;


public class LogWorld { 
	protected String hostname;
	protected int port;


	public LogWorld(String h, int p) {
		hostname=h;
		port=p;
	}

	public LogWorld(IniFile ini) {
		hostname=ini.getValueSafe("LOGWORLD","hostname","data.aprsworld.com");
		port=Integer.parseInt(ini.getValueSafe("LOGWORLD","port","4010"));
	
	}

	public void sendRecord(String record) {
		Socket s = null;
		PrintWriter o = null;
		
		try {
			s = new Socket(hostname, port);
			o = new PrintWriter(s.getOutputStream(),true);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e);
			return;
		} catch (IOException e) {
			System.out.println("No I/O: " + e);
			return;
		}
		
		/* send our record */
		o.print(record);
		o.flush();
		
		/* disconnect */
		o.close();
		
		
	}
}
