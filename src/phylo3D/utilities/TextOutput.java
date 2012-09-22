/*
 * Created on 01-Sep-2003
 */
package phylo3D.utilities;

import java.io.*;

/**
 *  Class implementing textual output functionality. Enables textual output to be directed either
 * to the screen or to a file.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TextOutput
{
	private static PrintStream ps = System.out;
	
	/**
	 * Prints the specified message to the PrintStream
	 * @param debugMessage String message to be printed
	 */
	public static void print(String debugMessage)
	{
		ps.print(debugMessage);
	}
	
	/**
	 * Prints the specified message and a new line character to the PrintStream
	 * @param debugMessage String message to be printed
	 */
	public static void println(String debugMessage)
	{
		ps.println(debugMessage);
	}	
	
	/**
	 * Sets the PrintStream that Debug will write to. By default Debug writes to System.out
	 * @param stream PrintStream that Debug writes
	 */
	public static void setPs(PrintStream stream)
	{
		ps = stream;
	}

}
