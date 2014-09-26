import java.awt.*;
import java.util.Date;

import javax.swing.*;

import org.jfree.data.time.Second;
import org.jfree.chart.axis.CompassFormat;

public class WindDirectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected WindDirectionHistoryPanel wdHistory;
	protected WindDirectionTextPanel wdTable;
	protected JLabel windNow;
	protected CompassFormat cf;
	
	
	public void setWindDirection(int dir) {
		wdTable.setWindDirection(dir);
		wdHistory.addWindDirection(dir);
		windNow.setText( "Wind Direction " + dir + "\u00b0 (" + cf.getDirectionCode(dir) + ")" );
	}
	
	public void setWindDirectionHistory(Date dlogDate, int dir) {
		Second s = new Second(dlogDate);
		wdHistory.addWindDirection(s,dir);
	}
	
	public WindDirectionPanel(String title, int maxAge) {
		/* setup an overall BorderLayout to contain the anemometer title, history graph, and text information */
		super(new BorderLayout());

		cf=new CompassFormat();
		
		wdHistory=new WindDirectionHistoryPanel(maxAge);
		wdTable=new WindDirectionTextPanel(title);

		setBackground(Color.white);
		setBorder(BorderFactory.createTitledBorder(title));

		windNow = new JLabel("");
		windNow.setFont(new Font("Serif", Font.BOLD, 18));

		
		add(windNow,BorderLayout.PAGE_START);
		add(wdHistory,BorderLayout.PAGE_END);

		
	//	add(wdHistory, BorderLayout.LINE_START);
	//	add(wdTable, BorderLayout.LINE_END);
		
	}
}
