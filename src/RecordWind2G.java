import java.util.*;
import java.text.*;

class RecordWind2G {
	public String date;
	public double pulseCurrent[];
	public double pulseGust[];
	public int    pulseCount[];
	public double counter[];
	public int windDirection;
	public double analogValue[];
	public int logging;
	public String serialNumber;

	private int lCRC;
	private int rCRC;
	public String rawRecord;
	protected boolean isValid;

	public Date dlogDate;
	public Date rxDate;



	/** XOR CRC a string 
	 *
	 * @param line elemnts to be XOR'ed 
	 * @return CRC value 
	 */
	public int CRCString(String line) {
		int crc=0;

		for ( int i=0 ; i<line.length(); i++ ) {
			crc=calc_crc(crc,line.charAt(i));
		}

		return crc;
	}

	public int calc_crc(int old_crc, int newbyte) {
		boolean fbbit;

		for (int ctr=0 ; ctr<8 ; ctr++) {
			//fbbit=((newbyte & 0x80)==0x80)^old_crc;

			/* crap I can't figure out Java some days */
			boolean z = (old_crc&0x01)==0x01;
			if ( (newbyte & 0x80)==0x80 ) {
				fbbit = true^z;
			} else {
				fbbit = false^z;
			}


			newbyte<<=1;
			old_crc>>=1;
			if ( fbbit )
				old_crc^=0x83;
		}
		return(old_crc);
	}


	/** Determine if record is valid based on its CRC and parsing status
	 *
	 * @return true if valid 
	 */
	public boolean isValid() {
		return (isValid && lCRC==rCRC);
	}

	public RecordWind2G() {
		pulseCurrent=new double[3];
		pulseGust=new double[3];
		pulseCount=new int[3];
		counter=new double[3];
		
		for ( int i=0 ; i<pulseCurrent.length ; i++ ) {
			pulseCurrent[i]=-1.0;
			pulseGust[i]=-1.0;
			pulseCount[i]=-1;
			counter[i]=-1.0;
		}
			
		
		analogValue=new double[8];
		isValid=true;
		
		serialNumber=null;
		logging=-1;
	}

	public void parseRecord(String line) {

		/* complete record */
		rawRecord=line;
		
		rxDate=new Date();

		try {
			/* tokenize at commas */
			String rec[]=line.split(",");

//			for ( int i=0 ; i<rec.length ; i++ ) {
//				System.err.println("[" + i + "] " + rec[i]);
//			}

			if ( rec.length != 23 && rec.length != 27 ) {
				System.err.println("# Incorrect record length. We received " + rec.length + " fields. We should have received 23 or 27");

				for ( int i=0 ; i<rec.length ; i++ ) {
					System.err.println("[" + i + "] " + rec[i]);
				}

				return;
			}

			/* base record CRC element is rec[22] and previous comma */
			int crcPos=0;
			for ( int i=0 ; i< 22 ; i++ ) {
				crcPos += rec[i].length() + 1;
			}

			lCRC=CRCString( line.substring(0,crcPos) );
			rCRC=Integer.parseInt(rec[22]);

			//System.out.println("Calculating CRC on: " + line.substring(0,crcPos) );

			if ( lCRC != rCRC ) {
				System.err.println("# BAD CRC! Local CRC: " + lCRC + " Received CRC: " + rCRC );
			}

			for ( int i=0 ; i<rec.length ; i++ ) {
				rec[i]=rec[i].trim();
				//		System.out.println("[" + i + "] " + rec[i] );
			}

			/* put into our local data */
			date=rec[0];

			/* parse our MySQL format date (2006-09-25 18:37:43) into a Java date */
			DateFormat sqlDateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			dlogDate=sqlDateParser.parse(date);

			if ( rec[1].length() > 0 )
				pulseCurrent[0]=Double.parseDouble(rec[1]);
			if ( rec[2].length() > 0 )
				pulseGust[0] =Double.parseDouble(rec[2]);
			if ( rec[3].length() > 0 )
				pulseCount[0]=Integer.parseInt(rec[3]);

			if ( rec[4].length() > 0 )
				pulseCurrent[1]=Double.parseDouble(rec[4]);
			if ( rec[5].length() > 0 )
				pulseGust[1] =Double.parseDouble(rec[5]);
			if ( rec[6].length() > 0 )
				pulseCount[1]=Integer.parseInt(rec[6]);

			if ( rec[7].length() > 0 )
				pulseCurrent[2]=Double.parseDouble(rec[7]);
			if ( rec[8].length() > 0 )
				pulseGust[2] =Double.parseDouble(rec[8]);
			if ( rec[9].length() > 0 )
				pulseCount[2]=Integer.parseInt(rec[9]);

			if ( rec[10].length() > 0 )
				counter[0]=Double.parseDouble(rec[10]);
			if ( rec[11].length() > 0 )
				counter[1]=Double.parseDouble(rec[11]);
			if ( rec[12].length() > 0 )
				counter[2]=Double.parseDouble(rec[12]);

			if ( rec[13].length() > 0 )
				windDirection=Integer.parseInt(rec[13]);
			else
				windDirection=-1;

			for ( int i=0 ; i<8 ; i++ ) {
				if ( rec[14+i].length() > 0 ) {
					analogValue[i]=Double.parseDouble(rec[14+i]);
				} else {
					analogValue[i]=-1.0;
				}
			}
			
			if ( 27 == rec.length ) {
				serialNumber=rec[23];
				
				if ( 0==rec[24].compareTo("1")) {
					logging=1;
				} else {
					logging=0;
				}
			}			

		} catch ( Exception e ) {
			System.err.println("Error parsing line: " + line );
			System.err.println("Exception: " + e);
			e.printStackTrace();
			isValid=false;
		}
	}
}
