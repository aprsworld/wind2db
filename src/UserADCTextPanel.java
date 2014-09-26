import java.awt.*;
import javax.swing.*;

public class UserADCTextPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel aLabel[];
	protected JLabel aValue[];
	protected JLabel aRaw[];
	protected UserChannel uc[];
	protected int ourChannel;
	protected int chMap[];
	
	public void setChannel(int ch, double voltage) {
		//NumberFormat dec = new DecimalFormat("0.00");
		
		if ( -1 == chMap[ch] )
			return;
		
		int box=chMap[ch];
		aValue[box].setText( uc[ch].evaluateString(voltage) );
		//aRaw[ch].setText(dec.format(voltage) + " volts");
	}
	
	public UserADCTextPanel(UserChannel luc[], int ch, int nPlotting) {
		super(new SpringLayout());
		int nEnabled=0;
		
		uc=luc;
		ourChannel=ch;
		
		chMap=new int[uc.length];
		aLabel=new JLabel[nPlotting];
		aValue=new JLabel[nPlotting];

		setBackground(Color.white);
		//setBorder(BorderFactory.createTitledBorder(title));

		int a=0;
		for ( int i=0 ; i< uc.length ; i++ ) {
			
			
			/* skip plots that aren't ours */
			if ( ch != uc[i].getPlotNumber() ) {
				System.err.println("Skipping " + uc[i].getPlotNumber() + " because it isn't equal to our plot ch " + ch);
				chMap[i]=-1;
				continue;
			} 
			chMap[i]=a;
			
			aLabel[a]=new JLabel(uc[i].getName() + ": ");
			aValue[a]=new JLabel("----" + uc[i].getUnits());
			
			/* only add to view if they are enabled */
			if ( uc[i].isEnabled() ) {
				add(aLabel[a]);
				add(aValue[a]);
				nEnabled++;
				a++;
			}
		}
		
		System.err.println("dumping channel map for UserADCTextPanel " + ourChannel);
		for ( int i=0 ; i<uc.length ; i++ ) {
			System.err.println("chmap[" + i + "]=" + chMap[i] );
		}
		
		SpringUtilities.makeCompactGrid(this,
                nEnabled*2, 1, //rows, cols
                20, 20, //initialX, initialY
                10, 10);//xPad, yPad
		
	}
}
