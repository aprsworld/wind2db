import java.text.*;

class UserChannel {
	protected int ch;
	protected double m,b;
	protected String name,units,format;
	protected boolean inverse;
	protected boolean disabled;
	protected int plotNumber;

	public int getPlotNumber() {
		return plotNumber;
	}
	
	public boolean isEnabled() {
		return ! disabled;
	}
	public boolean isDisabled() { 
		return disabled;
	}
	
	public boolean getInverse() {
		return inverse;
	}

	public int getCh() {
		return ch;
	}
	
	public String getName() {
		return name;
	}
	public String getUnits() {
		return units;
	}
	public double getM() {
		return m;
	}
	public double getB() {
		return b;
	}

	public double evaluate(double d) {
		if ( d != 0.0 && inverse ) {
			d=1.0/d;
		}
		return m*d + b;
	}

	public String evaluateString(double d) {
      NumberFormat f = new DecimalFormat(format);

		return  f.format(evaluate(d)) + " " + units;
	}

	public UserChannel ( int ch, double lm, double lb, String lUnits, String lName, String fFormat, boolean lInverse, boolean lDisabled, int lPlotNumber ) {
		m=lm;
		b=lb;
		name=lName;
		units=lUnits;
		format=fFormat;
		inverse=lInverse;
		disabled=lDisabled;
		plotNumber=lPlotNumber;
	}
}
