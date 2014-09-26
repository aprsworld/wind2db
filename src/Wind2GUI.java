import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Wind2GUI {
	protected AnemometerPanel anemometer[];
	protected UserADCPanel user[];
	protected WindDirectionPanel windDirection;
	protected JFrame f;
	protected String stationID, imageFileName;
	protected String anemometerType[];
	protected String anemometerUnits[];
	protected boolean hideAnemometer[],hideUser,hideWindDirection;
	protected JLabel statusLabel;
	protected javax.swing.Timer timer;
	protected Date recordDate;
	protected String recDate;
	protected UserChannel uc[];
	protected int maxAge; // seconds of history to plot
	protected int nPlots; // number of user plots
	protected int nColumns;
	protected int graphWidth, graphHeight;

	public void updateStatus() {
		/* calculate the difference between rec.rxDate and the current date and update status bar */
		Date d=new Date();
		long delta;

		delta=(d.getTime()-recordDate.getTime())/1000;
		
		statusLabel.setText("Last record received at " + recDate + " ( " + delta + " seconds ago)");

	}
	
	public void addHistoricalRecord(RecordWind2 rec) {
		for ( int i=0 ; i<3 ; i++ ) {
			if ( false == hideAnemometer[i] ) {
				anemometer[i].setWindHistory(rec.dlogDate,rec.windSpeed[i],rec.windGust[i]);
			}
		}
	
		/* we should really send only those we need to */
		for ( int j=0 ; j<nPlots ; j++ ) {
			for ( int i=0 ; i<8 ; i++ ) {
				user[j].setChannelHistory(rec.dlogDate,i,rec.adcVoltage[i]);
			}
		}
		windDirection.setWindDirectionHistory(rec.dlogDate,rec.windDirection);
	}

	public void updateDisplay(RecordWind2 rec) {
		/* timer for updating the status bar */
		if ( ! timer.isRunning() ) {
			timer.start();
		}

		recordDate=rec.rxDate;
		recDate=rec.date;
		updateStatus();
			
		
		for ( int i=0 ; i<3 ; i++ ) {
			if ( false == hideAnemometer[i] ) {
				anemometer[i].setWind(rec.windSpeed[i],rec.windGust[i],rec.windCount[i]);
			}
		}
		

		for ( int j=0 ; j<nPlots ; j++ ) {
			for ( int i=0 ; i<8 ; i++ ) {
				user[j].setChannel(i,rec.adcVoltage[i]);
			}
		}

		windDirection.setWindDirection(rec.windDirection);
		
		f.repaint();
	}

	public void updateDisplayG(RecordWind2G rec) {
		/* timer for updating the status bar */
		if ( ! timer.isRunning() ) {
			timer.start();
		}

		recordDate=rec.rxDate;
		recDate=rec.date;
		updateStatus();
			
		
		for ( int i=0 ; i<3 ; i++ ) {
			if ( false == hideAnemometer[i] ) {
				if ( rec.pulseCurrent[i] == -1.0 || rec.pulseGust[i] == -1.0 || rec.pulseCount[i] == -1 )
					anemometer[i].setWind(0.0,0.0,0);
				else
					anemometer[i].setWind(rec.pulseCurrent[i],rec.pulseGust[i],rec.pulseCount[i]);
			}
		}
		

		for ( int j=0 ; j<nPlots ; j++ ) {
			for ( int i=0 ; i<8 ; i++ ) {
				user[j].setChannelG(i,rec.analogValue[i]);
			}
		}

		windDirection.setWindDirection(rec.windDirection);
		
		f.repaint();
	}
	
	protected void readIni(IniFile ini) {
		double m,b;
		String name, label, format;
		boolean disabled=true;
		int plotNumber;
		stationID=ini.getValueSafe("GENERAL","stationID","DEFAULT");
		imageFileName=ini.getValueSafe("GUI","imageFileName","aprsworld_logo.250.png");
		maxAge=Integer.parseInt(ini.getValue("GUI","historySeconds"));
		nColumns=Integer.parseInt(ini.getValueSafe("GUI","nColumns","2"));
		graphWidth=Integer.parseInt(ini.getValueSafe("GUI","graphWidth","400"));
		graphHeight=Integer.parseInt(ini.getValueSafe("GUI","graphHeight","330"));

		//System.err.println("uc is " + uc);
		
		/* read adc channels */
		for ( int i=0 ; i<8 ; i++ ) {
			String subject="ADC" + i;

			try { 
				m=Double.parseDouble(ini.getValue(subject,"m"));
				b=Double.parseDouble(ini.getValue(subject,"b"));
				name=ini.getValue(subject,"name");
				label=ini.getValue(subject,"label");
				format=ini.getValue(subject,"format");
				disabled=ini.isTrue(subject,"disabled");
				plotNumber=Integer.parseInt(ini.getValue(subject,"plot"));
				
			} catch ( Exception e ) {
				System.err.println("# Error encountered when reading USER ADC channel from INI File " + e);
				m=0.0;
				b=0.0;
				name="Undefined";
				label="volts";
				format="0.00";
				disabled=true;
				plotNumber=0;
			}

			uc[i]=new UserChannel(i,m,b,label,name,format,false,disabled,plotNumber);
			
		}

		/* panels to show */
		hideAnemometer[0]=ini.isTrue("AN0","disabled");
		hideAnemometer[1]=ini.isTrue("AN1","disabled");
		hideAnemometer[2]=ini.isTrue("AN2","disabled");
		//hideAnalog=ini.isTrue("GUI","hideAnalog");
		hideUser=ini.isTrue("GUI","hideUser");
		hideWindDirection=ini.isTrue("WINDVANE","disabled");

		
		/* get anemometer labels */
		for ( int i=0 ; i<3 ; i++ ) {
			String subject="AN" + i;
			try { 
				anemometerType[i]=ini.getValueSafe(subject,"type","");
				anemometerUnits[i]=ini.getValueSafe(subject,"units","");
			} catch ( Exception e ) {
				System.err.println("# Error encountered when reading " + subject + " configuration from INI File " + e);
			}
		}
	}
	
	public void setVisible(boolean state) {
		f.setVisible(state);
	}

	public Wind2GUI(IniFile ini) {
		WindowUtilities.setNativeLookAndFeel();
	
		hideAnemometer=new boolean[3];
		anemometerType=new String[3];
		anemometerUnits=new String[3];
		uc=new UserChannel[8];
		
		readIni(ini);

		anemometer=new AnemometerPanel[3];

		int plots[]=new int[uc.length];
		for ( int i=0 ; i<uc.length ; i++ ) {
			plots[uc[i].getPlotNumber()]++;
		}
		for ( int i=0 ; i<plots.length ; i++ ) {
			if ( plots[i] > 0 )
				nPlots++;
			System.err.println("plot[" + i + "]=" + plots[i]);
			                                 
		}
		nPlots-=1;
		
		user=new UserADCPanel[nPlots];
		
		for ( int i=0 ; i<nPlots ; i++ ) {
			user[i]=new UserADCPanel("Analog Sensors (plot " + (i+1) + "):", uc, maxAge,i+1);
		}
		windDirection=new WindDirectionPanel("Wind Vane:",maxAge);

		
		f = new JFrame("Current Weather at " + stationID );
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		/* set the upper left icon */
		Image icon = Toolkit.getDefaultToolkit().getImage("small_sunny.gif");
		f.setIconImage(icon);

		/* Overall BorderLayout */
		Container cont = f.getContentPane();
		cont.setLayout(new BorderLayout());
		cont.setBackground(Color.white);
		
		/* Our body section */
		Container content=new Container();
		content.setLayout(new GridLayout(0,nColumns));
		content.setBackground(Color.white);		
		
		for ( int i=0 ; i<3 ; i++ ) {
			String anemometerTitle=new String("Anemometer " + i + " - " + anemometerType[i] + " - ");
			anemometer[i]=new AnemometerPanel(anemometerTitle,anemometerUnits[i],maxAge,graphWidth,graphHeight); //,width/2-20,height/2-65);
			if ( false == hideAnemometer[i] ) {
				content.add(anemometer[i]);	
			}
		}

		if ( false == hideWindDirection ) 
			content.add(windDirection);
		if ( false == hideUser ) { 
			for ( int i=0 ; i<nPlots ; i++ ) {
				content.add(user[i]);
			}
		}
	
		/* title text and photo */
		Container titleContainer=new Container();
		titleContainer.setLayout(new FlowLayout());
		
		if ( null != imageFileName && 0 != imageFileName.compareTo("") ) {
			/* big photo of datalogger */
			ImageIcon photo = new ImageIcon(imageFileName,"User Image");
			JLabel photoLabel = new JLabel(photo,JLabel.CENTER);
			titleContainer.add(photoLabel);
		}

		JLabel titleLabel = new JLabel("Current Weather at " + stationID);
		titleLabel.setBackground(Color.white);
		titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
		titleLabel.setForeground(Color.blue);
		titleContainer.setBackground(Color.white);
		titleContainer.add(titleLabel);
		
		/* add the title */
		titleContainer.validate();
		cont.add(titleContainer, BorderLayout.PAGE_START);
		
		/* Add the body */
		content.validate();
		cont.add(content, BorderLayout.CENTER);

		/* Add our status bar */
		statusLabel = new JLabel("No data received.",JLabel.CENTER);
		statusLabel.setOpaque(true);
		statusLabel.setBackground(Color.lightGray);
		cont.add(statusLabel, BorderLayout.PAGE_END);
	
		
		f.setLocationRelativeTo(null);
		f.addWindowListener(new ExitListener());
		cont.validate();
		f.pack();

		/* add a timer to keep our status bar updated */
		timer = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStatus();
			}
		});

	}
}
