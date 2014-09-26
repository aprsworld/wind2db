import java.text.*;
import javax.swing.*;

public class AnalogUserPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel aLabel[];
	protected JLabel aValue[];
	
	public void setChannel(int ch, int raw, double voltage) {
		NumberFormat dec = new DecimalFormat("0.000");
		NumberFormat whole = new DecimalFormat("0000");

		aValue[ch].setText( whole.format(raw) + " " + dec.format(voltage) + " volts");
	}
	
	public AnalogUserPanel(int nChannels, String title, int gray) {
//		super(new GridLayout(nChannels, 5));
/*
		String[] columnNames = {"CH","Value"}; 
		Object[][] data; //	= { {"0","1 million"}, {"1","2 million friggin lasers"} };

		for ( int i=0 ; i<nChannels ; i++ ) {
			String row[]=new String[2];
			row[0]="ADC " + i;
			row[1]="Value goes here";
			data[i]=row;
		}


		JTable table = new JTable(data, columnNames);
		add(table);
*/
	}
}
