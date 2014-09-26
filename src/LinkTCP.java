import java.io.*;
import java.net.*;

public class LinkTCP extends Link
{
	protected Socket s = null;
	protected BufferedReader i = null;
	protected PrintWriter o = null;

	protected String host;
	protected int port;

	public LinkTCP (IniFile ini) {
		host = ini.getValue("TCP", "host");
		port = new Integer(ini.getValue("TCP", "port")).intValue();
	}
	public LinkTCP ( String h, int p ) {
		host=h;
		port=p;
	}
		
	public boolean Connect()
	{
		try {
			s = new Socket(host, port);
			o = new PrintWriter(s.getOutputStream(),
					true);
			i = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e);
			return false;
		} catch (IOException e) {
			System.out.println("No I/O: " + e);
			Disconnect();
			return false;
		}
		return true;
	}
	
	public String getLine()
	{
		String line = null;
		try {
			line = i.readLine();
		} catch (IOException e) {
			System.out.println("Read failed: " + e);
			return null;
		}
		return line;
	}
	
	public void sendLine(String s) {
		try {
			o.print(s);
			o.flush();
		} catch ( Exception e ) {
			System.err.println("# sendLine had an exception: " + e);
		}
	}
	public boolean dataReady ( ) {
		try {
			return i.ready();
		} catch ( Exception E ) {
			return false;
		}
	}
	
    public void emptyInputBuffer() {
    	try {
			while ( i.ready() ) {
				i.read();
				//System.err.print(".");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }   
	
	public boolean Disconnect()
	{
		if (i != null) {
			try {
				i.close();
			} catch (IOException e) {
				System.err.println("Error closing: " + e);
			}
			i = null;
		}
		if (o != null) {
			o.close();
			o = null;
		}
		if (s != null) {
			try {
				s.close();
			} catch (IOException e) {
				System.err.println("Error closing: " + e);
			}
			s = null;
		}

		return true;
	}


}
