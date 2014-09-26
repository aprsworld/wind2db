import javax.swing.*;

/* master server */
class Wind2 extends Thread {
	protected String inifile;
	protected boolean useDlogClock;
	protected String stationID;
	protected boolean hideGUI;

	Wind2 (String inifilename) {
		inifile = inifilename;
	}



	public void run() {
		// Open configuration file
		IniFile ini = new IniFile(inifile);
		stationID = ini.getValue("GENERAL", "stationID").toUpperCase();
		useDlogClock=ini.isTrue("GENERAL", "useDlogClock");
		hideGUI=ini.isTrue("GENERAL","hideGUI");


		/* GUI screens */
		Wind2GUI disp=null;


		/* we can have one data source. Currently they are {tcp, serial} */
		Link link = null;
		if ("tcp".equals(ini.getValue("LINK", "type"))) {
			link = new LinkTCP(ini);
		} else if ("serial".equals(ini.getValue("LINK", "type"))) {
			link = new LinkSerial(ini);
		} else {
			System.err.println("Invalid Configuration File");
			System.exit(-1);
		}

		if ( ! hideGUI ) {
			/* Start the GUI */
			disp = new Wind2GUI(ini);
		}


		// Establish the Link
		if (!link.Connect()) {
			String message = "Invalid link type.";

			if ( "tcp".equals(ini.getValue("LINK","type")) ) {
				message = "Failed to establish " + ini.getValue("LINK","type").toUpperCase() + " link to " + ini.getValue("TCP","host") + ":" + ini.getValue("TCP","port");
			} else if ( "serial".equals(ini.getValue("LINK","type")) ) {
				message = "Failed to establish " + ini.getValue("LINK","type").toUpperCase() + " link to " + ini.getValue("SERIAL","port");
			}


			System.err.println("ABORT: Error Establishing Link\n" + message);
			if ( ! hideGUI ) {
				JOptionPane.showMessageDialog(disp.f, message, "Link Failure", JOptionPane.ERROR_MESSAGE);


				disp.f.dispose();
				System.exit(-1);
			}

			return;
		}

		LogHTTP http = null;
		if (ini.isTrue("LOGHTTP", "useLogHTTP")) {
			System.err.println("# Using LogHTTP");
			http = new LogHTTP(ini);
		}


		LogLocal log = null;
		if (ini.isTrue("LOGLOCAL", "useLogLocal")) {
			String directory = ini.getValue("LOGLOCAL","directory");
			System.err.println("# Using LogLocal");
			log = new LogLocal(directory);
		}

		LogWorld logWorld = null;
		if (ini.isTrue("LOGWORLD", "useLogWorld")) {
			logWorld = new LogWorld(ini);
		}
		
		disp.setVisible(true);
		
		String line;
		while ((line = link.getLine()) != null) { 
			System.out.println(line);

			/* figure out how many elements we have */
			String parts[]=line.split(",");
//			System.err.println("parts=" + parts.length );

			if ( 27 == parts.length ) {
				/* sdLoggerWind2g */
				RecordWind2G rec = new RecordWind2G();
				rec.parseRecord(line);
				
				if ( rec.isValid() ) {
					if ( ! hideGUI ) {
						disp.updateDisplayG(rec);
					}
				}
				
				/* wind2record may have modified the line (ie change the date), so we use it from now on */
				line=rec.rawRecord;

				/* log to text file, if requested */
				if ( null != log ) {
					log.log(line,rec.rxDate);
				}

				
			} else {
				/* presumably a wind2f or earlier */


				/* default: wind data logger */
				RecordWind2 rec = new RecordWind2(line,useDlogClock);

				if (rec.isValid()) {
					if ( ! hideGUI ) {
						disp.updateDisplay(rec);
					}

					/* wind2record may have modified the line (ie change the date), so we use it from now on */
					line=rec.wind2record;

					/* log to text file, if requested */
					if ( null != log ) {
						log.log(line,rec.rxDate);
					}


				}
			}

			/* everything gets here */
			/* send our line of data to the web */	
			if (http != null) {
				http.sendRecord(line);
			}
			
			if ( logWorld != null ) {
				logWorld.sendRecord(line);
			}
		}

		// Clean Up
		link.Disconnect();
	}

	public static void main (String args[]) {
		String ini = null;

		if (args.length == 0) {
			ini="config_wind2db_default.ini";
		} else if (args.length == 1) {
			ini = args[0];
		}

		System.err.println("# Using " + ini);
		System.err.println("# Invoke with -Dswing.aatext=true for anti-aliased fonts on *nix systems");

		(new Wind2(ini)).start();
	}
}
