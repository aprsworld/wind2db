import gnu.io.CommPortIdentifier;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFrame;



public class Wind2DBInstall extends Thread {

	protected IniFile ini;
	JComboBox cbPorts;
	Vector<String> ports;
	
	Wind2DBInstall (String inifilename) {
		ini = new IniFile(inifilename);
		
		installNow();
	}

	protected static boolean copyFile(String source, String dest) {
		try {	
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(dest);
		
	
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) >= 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			in.close();
		} catch ( Exception e ) {
			System.err.println("Error copying file");
			return false;
		}
		
		return true;
	}
	
	protected void installNow() {

		String javaHome = System.getProperty("java.home");
		
		String fileSource, fileDest;

		/* rxtx.dll */
		fileSource="rxtxToInstall/rxtxSerial.dll";
		fileDest= javaHome + File.separator + "bin" + File.separator + "rxtxSerial.dll";
		/* check to see if our required RXTX files exist */
		//System.err.println("Copying " + fileSource + " to " + fileDest);
		File f = new File(fileSource);
		if ( f.exists()) {
			copyFile(fileSource,fileDest);
			System.err.println("Copying " + fileSource + " to " + fileDest);
		} else {
			System.err.println("RXTX Source file not found!");
		}
		
		/* RXTXcomm.jar */
		fileSource="rxtxToInstall/RXTXcomm.jar";
		fileDest= javaHome + File.separator + "lib" + File.separator + "ext" + File.separator + "RXTXcomm.jar";
		/* check to see if our required RXTX files exist */
		//System.err.println("Copying " + fileSource + " to " + fileDest);
		f = new File(fileSource);
		if ( f.exists()) {
			copyFile(fileSource,fileDest);
			System.err.println("Copying " + fileSource + " to " + fileDest);
		} else {
			System.err.println("RXTX Source file not found!");
		}
	
		
	}
	
	protected void writeConfigNow() {
		String serialPort=ports.elementAt(cbPorts.getSelectedIndex());
		System.err.println("Serial port " + serialPort + " selected.");
		
		ini.setValue("SERIAL","port",serialPort);
		ini.saveFile();

		
		System.exit(0);
	}
	
	public void run() {
		cbPorts=null;
		
		WindowUtilities.setNativeLookAndFeel();
		
		JFrame f = new JFrame("Wind2DB Software Install");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		f.setSize(250, 150);

		/* Overall BorderLayout */
		Container cont = f.getContentPane();
		cont.setBackground(Color.white);
		cont.setLayout(new BorderLayout());
		

		JLabel lTitle = new JLabel("Wind2DB Software Installation");
		lTitle.setFont(new Font("Serif", Font.BOLD, 18));
		lTitle.setForeground(Color.blue);
		f.add(lTitle,BorderLayout.PAGE_START);

		System.out.println("# Scanning serial ports: ");
		/* Get all available ports */
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();

		
		/* Find requested port */
		ports = new Vector<String>();
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier p = (CommPortIdentifier)portIdentifiers.nextElement();
			
			if (p.getPortType() == CommPortIdentifier.PORT_SERIAL ) { 
				System.out.println("Available port=" +p.getName());
				ports.add(p.getName());
			}
		}

	
		cbPorts = new JComboBox(ports);
		f.add(cbPorts,BorderLayout.CENTER);
	
		JButton bInstall = new JButton("Install Now");
		f.add(bInstall,BorderLayout.PAGE_END);
		bInstall.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						writeConfigNow();
					}
				}
		);
		
		
		f.setVisible(true);
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String s[]={ "java.home","java.class.path", "java.library.path", "java.ext.dirs", "file.separator", "path.separator", "line.separator"};
//		for ( int i=0 ; i<s.length ; i++ ) {
//			System.out.println(s[i] + "=" + System.getProperty(s[i]));
//		}

		
	
		
		
	
		
		String ini = null;

		if (args.length == 1) {
			ini = args[0];
		} else {
			ini="config_wind2db_default.ini";
		}

		(new Wind2DBInstall(ini)).start();
	}

}
