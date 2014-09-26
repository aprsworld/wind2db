import java.awt.*;
import javax.swing.*;
import java.text.*;

public class AnemometerTextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JLabel windSpeed, windGust, windCount;
	protected String speedUnits;
	
	public void setWindSpeed(double ws) {
		NumberFormat f = new DecimalFormat("0.0");
		windSpeed.setText( f.format(ws) + " " + speedUnits);
	}
	
	public void setWindGust(double ws) {
		NumberFormat f = new DecimalFormat("0.0");
		windGust.setText( f.format(ws) + " " + speedUnits);
	}
	
	public void setWindCount(int count) {
		windCount.setText(count + "");
	}
	
	public void setWind(double ws, double wg, int count) {
		setWindSpeed(ws);
		setWindGust(wg);
		setWindCount(count);
	}
	
	public AnemometerTextPanel(String title, String sUnits) {
		//super(new GridLayout(0, 2));
		//JPanel panel = new JPanel(new SpringLayout());
		super(new SpringLayout());
		
		//for (int i = 0; i < 9; i++) {
		//    JTextField textField = new JTextField(Integer.toString(i));
		//    ...//when i==4, put long text in the text field...
		//    panel.add(textField);
		//}
		//...
		
		speedUnits=sUnits;
		setBackground(Color.white);
		//setBorder(BorderFactory.createTitledBorder(title));

		JLabel windSpeedLabel = new JLabel("Wind Speed: ");
		add(windSpeedLabel);
		windSpeed = new JLabel("---- MPH");
		add(windSpeed);

		JLabel windGustLabel = new JLabel("Wind Gust: ");
		add(windGustLabel);
		windGust = new JLabel("---- MPH");
		add(windGust);

		JLabel windCountLabel = new JLabel("Pulse Count: ");
		add(windCountLabel);
		windCount = new JLabel("----");
		add(windCount);

		SpringUtilities.makeCompactGrid(this,
                6, 1, //rows, cols
                20, 20, //initialX, initialY
                10, 10);//xPad, yPad

		
	}
}
