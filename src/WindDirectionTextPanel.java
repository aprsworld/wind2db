import java.awt.*;
import javax.swing.*;

import org.jfree.chart.axis.CompassFormat;

public class WindDirectionTextPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel aLabel[];
	protected JLabel aValue[];
	protected CompassFormat cf;
	
	public void setWindDirection(int dir) {
		
		aValue[0].setText( dir + "\u00b0 (" + cf.getDirectionCode(dir) + ")" );
	}

	public WindDirectionTextPanel(String title) {
		super(new GridLayout(1, 2));

		cf=new CompassFormat();
		
		aLabel=new JLabel[1];
		aValue=new JLabel[1];

		setBackground(Color.white);
		setBorder(BorderFactory.createTitledBorder(title));

		aLabel[0]=new JLabel("Wind Direction: ");
		add(aLabel[0]);
		aValue[0]=new JLabel("---\u00b0 (Unknown)");
		add(aValue[0]);
	}
}
