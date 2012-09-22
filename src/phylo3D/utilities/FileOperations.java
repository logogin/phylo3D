/*
 * Created on 17-Sep-2003
 */
package phylo3D.utilities;

import java.io.*;
import java.net.*;

/**
 *  Class implementing functionality for reading and writing to files
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class FileOperations
{
	/**
	 * Gets a BufferedReader for the specified file, prints message to standard out and exits if
	 * unable to find the specified file
	 * @param filePath String identifying the file for which a BufferedReader is required
	 * @return BufferedReader for the specified file
	 */
	public static synchronized BufferedReader getBufferedReader(String filePath)
	throws InputFileAccessException
	{
		File theFile = new File(filePath);
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(theFile));
		}
		catch (FileNotFoundException fnfe)
		{
			throw new InputFileAccessException("Cannot find file: " + theFile.toString());
		}
		return br;
	}
	
	/**
	 * Gets a PrintWriter to the specified file, prints message to standard out and exits if the file already
	 * exists or if unable to get PrintWriter to file.
	 * @param filePath String identifying file to which PrintWriter is needed
	 * @return PrinterWriter to spcified file with autoflushing switched on
	 */
	public static synchronized PrintWriter getPrintWriter(String filePath)
	throws OutputFileAccessException	
	{
		File theFile = new File(filePath);
		PrintWriter pw = null;
		
		try
		{
			pw = new PrintWriter(new FileWriter(theFile), false);	
		}
		catch(IOException ioe)
		{
			throw new OutputFileAccessException("Unable to get print writer to file:" + filePath);
		}
		
		return pw;
	}	
	
	
	/**
	 * Gets a BufferedReader for the specified file, prints message to standard out and exits if
	 * unable to find the specified file
	 * @param theURL URL identifying the file for which a BufferedReader is required
	 * @return BufferedReader for the specified file
	 */
	public static synchronized BufferedReader getBufferedReader(URL theURL)
	throws InputFileAccessException
	{
		InputStream is = null;
		try
		{
			is = theURL.openStream();
		}
		catch (IOException e)
		{
			throw new InputFileAccessException("IOException in trying to open InputStream to URL: " + theURL);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		return br;
	}

	/**
	 *Used for handling an exception where need to exit the runtime environment. Prints message to standard
	 *out and exits.
	 */
	public static synchronized void exit()
	{
		System.out.println("Exiting...");
		System.exit(1);	
	}

	public static synchronized void serializeObjectToFile(String path, Object o) 
	throws InputFileAccessException
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
		}
		catch (FileNotFoundException fnfe)
		{
			throw new InputFileAccessException("Unable to locate file for serialising object to: " + fnfe.getMessage());
		}
		catch (IOException ioe)
		{
			throw new InputFileAccessException("IOException while trying to serialize object to file: " + ioe.getMessage());
		}
	}

	public static synchronized Object loadFromFile(URL theURL) throws InputFileAccessException
	{
		Object  o = null;
		InputStream is = null;
		try
		{
			is = theURL.openStream();
		}
		catch (IOException ioe)
		{
			throw new InputFileAccessException("Unable to open input stream from URL: " + theURL);
		}
		
		try
		{
			ObjectInputStream in = new ObjectInputStream(is);
			o = (Object)in.readObject();
			in.close();
		}
		catch (IOException ioe)
		{
			throw new InputFileAccessException("IOException while attempting to read object from InputStream from URL " + theURL);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new InputFileAccessException("ClassNotFoundException while attempting to read object from InputStream from URL "+
			 theURL);
		}
		
		return o;
	}

}
