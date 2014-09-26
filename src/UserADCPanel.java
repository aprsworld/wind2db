import java.awt.*;
import java.util.Date;
import javax.swing.*;

import org.jfree.data.time.Second;

public class UserADCPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JLabel windSpeed, windGust, windCount;
	protected String speedUnits;
	
	protected UserChannel uc[];
	protected UserHistoryPanel history;
	protected UserADCTextPanel text;
	
	protected int chPlot[];
	protected int chd;
	
	public void setChannel(int ch, double v) {
		if ( chd == uc[ch].getPlotNumber() ) {
			history.addPoint(chPlot[ch],uc[ch].evaluate(v));
			text.setChannel(ch,v);
		}
		
	}
	public void setChannelG(int ch, double v) {
		if ( chd == uc[ch].getPlotNumber() ) {
			history.addPoint(chPlot[ch],v);
			text.setChannel(ch,v);
		}
		
	}

	
	public void setChannelHistory(Date dlogDate, int ch, double v) {
		Second s = new Second(dlogDate);
		
	//	System.err.println("go here, bitches");
		
		if ( chd == uc[ch].getPlotNumber() ) {
			history.addPoint(s,chPlot[ch],uc[ch].evaluate(v));
			
		}
	}
	
	
	
	
	public UserADCPanel(String title, UserChannel[] luc, int maxAge, int lch) {
		/* setup an overall BorderLayout to contain the anemometer title, history graph, and text information */
		super(new BorderLayout());
		uc=luc;
		chPlot=new int[uc.length];
		chd=lch;
		
		
		/* count number of channels that we will be plotting and grab their names*/
		String channelLabels[]=new String[uc.length];
		int nPlotting=0;
		int nc=0;
		for ( int i=0 ; i<uc.length ; i++ ) {
			if ( chd == uc[i].getPlotNumber() ) {
				channelLabels[nPlotting]=uc[i].getName();
				nPlotting++;
				chPlot[i]=nc;
				nc++;
			}
		}
		
		for ( int i=0 ; i<uc.length ; i++ ) {
			System.err.println( i + " says " + channelLabels[i]);
		}
		
	//	for ( int i=0 ; i<uc.length ; i++ ) {
	//		System.err.println("adc channel " + i + " corresponds to plot channel " + chPlot[i]);
	//	}
		
		history=new UserHistoryPanel(maxAge,nPlotting,channelLabels);
		text=new UserADCTextPanel(uc,chd,nPlotting);
		
		setBackground(Color.white);
		setBorder(BorderFactory.createTitledBorder(title));

		
		add(history, BorderLayout.LINE_START);		
		
		//text.setPreferredSize(new Dimension(250,200));
		add(text, BorderLayout.LINE_END);
		
	}
}
