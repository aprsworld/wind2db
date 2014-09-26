import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.*;
import org.jfree.data.time.Second;

public class AnemometerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JLabel windNow;
	protected String speedUnits;
	
	protected AnemometerHistoryPanel anHistory;
	protected AnemometerTextPanel anTable;
	
	
	public void setWind(double ws, double wg, int count) {
		anTable.setWind(ws,wg,count);
		anHistory.addWind(ws,wg);
		
		NumberFormat f = new DecimalFormat("0.0");
		windNow.setText(f.format(ws) + " " + speedUnits + " gusting to " + f.format(wg) + " " + speedUnits + " (" + count + " pulses)");
	}
	
	public void setWindHistory(Date dlogDate, double ws, double wg) {
		Second s = new Second(dlogDate);
		anHistory.addWind(s,ws,wg);
	}
	
	public AnemometerPanel(String title, String sUnits, int maxAge, int graphWidth, int graphHeight) {
		/* setup an overall BorderLayout to contain the anemometer title, history graph, and text information */
		super(new BorderLayout());
		
		speedUnits = sUnits;

		anHistory=new AnemometerHistoryPanel(maxAge,graphWidth,graphHeight);
		anTable=new AnemometerTextPanel(title,sUnits);

		setBackground(Color.white);
		setBorder(BorderFactory.createTitledBorder(title));

		windNow = new JLabel("");
		windNow.setFont(new Font("Serif", Font.BOLD, 18));

		
		add(windNow,BorderLayout.PAGE_START);
		add(anHistory,BorderLayout.PAGE_END);
		validate();
		
		
		/* graph to the left and text table to the right */
		//add(anHistory, BorderLayout.LINE_START);
		//add(anTable, BorderLayout.LINE_END);
		
	}
}
