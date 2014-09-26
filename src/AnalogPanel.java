import java.text.*;
import java.awt.*;
import javax.swing.*;

public class AnalogPanel extends JPanel {
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
	
	public AnalogPanel(int nChannels, String title, int gray) {
		super(new GridLayout(nChannels, 2));

		aLabel=new JLabel[nChannels];
		aValue=new JLabel[nChannels];

		setBackground(Color.white);
		setBorder(BorderFactory.createTitledBorder(title));

		for ( int i=0 ; i<nChannels ; i++ ) {
			aLabel[i]=new JLabel("Analog " + i + ": ");
			aValue[i]=new JLabel("---- ---- volts");

			/* gray out the first four channels */
			if ( i < gray ) {
				aLabel[i].setForeground(Color.gray);
				aValue[i].setForeground(Color.gray);
			}
			
			add(aLabel[i]);
			add(aValue[i]);
		}
	}
}
