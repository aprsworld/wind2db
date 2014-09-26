import java.io.*;
import java.text.*;
import java.util.*;


public class LogLocal { 
	protected String directory;

	/* constructor */
	public LogLocal(String d) {
		directory=d;
	}

	/* write a line to a file named yyyyMMdd in directory
	 * @param line String to write to file
	 * @param timeStamp Time of record
	 * @return true if write was sucessfull, false otherwise
	 */
	public boolean log(String line, Date timeStamp) {
		DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
		String filename= directory + "/" + dayFormat.format(timeStamp) + ".CSV";

		
		try { 
			/* check to see if the directory exists, otherwise try to create it */
			File f=new File(directory);
			if ( false == f.isDirectory() && false == f.mkdirs() ) {
				/* unable to create directory */
				System.err.println("# LogLocal error creating directory: " + directory);
				return false;
			}

			/* Open the file and append to it */
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));

			/* write line */
			writer.write(line + "\r\n");
			System.err.println("# LogLocal: " + filename + " - " + line); 

			/* close the file */
			writer.close();
		} catch ( Exception e ) {
			System.err.println("# LogLocal exception: " + e);
			return false;
		}

		return true;
	}

	/* write a line to a file named yyyyMMdd in directory
	 * @param line String to write to file
	 * @return true if write was sucessfull, false otherwise
	 */
	public boolean log(String line) {
		/* default to now */
		return log(line,new Date());
	}

}
