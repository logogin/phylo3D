/*
 * Created on 19-Sep-2003
 */
package phylo3D.tools;

import phylo3D.utilities.*;

/**
 * Abstract class from which all Tool classes should inherit. Class primarily provides functionality for
 * parsing the commandline. Also contains definition of names of command line options. 
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public abstract class Tool
{

	/**
	 * Should be the only method called from the main method of a tool class. Implementation should
	 * consist of calls to other methods for carrying out the computation that the tool is designed for.
	 * @param args String array containing the arguments of the command line
	 */
	abstract protected void execute(String[] args)
	throws FatalException;
	
	
	/**
	 * Parses the arguments from the command line into the fields of the tool such that they are 
	 * later available to other methods specific to the tool
	 * @param args String array containing the command line arguments
	 */
	abstract protected void parseCommandLine(String[] args)
	throws FatalException;
		
	/**
	 * Prints to the screen a description of how to use the tool.
	 */
	abstract protected String toolUsageText();

	protected void handleException(FatalException fe)
	{
		UserInfo.println("");
		UserInfo.println(fe.getClass().getName());
		UserInfo.println(fe.getMessage());		
		UserInfo.println("");
				
		if (fe instanceof CommandLineException)
		{
			UserInfo.println(toolUsageText());
		}
		
		if (fe instanceof ObjectIntrospectionException || fe instanceof ProgramInputFileAccessException
		|| fe instanceof ProgramInputFileParsingException)
		{
			UserInfo.println("This type of exception is most likely to be caused by errors in the code or associated files" + "\n" + 
			"Please report the bug.");
		}
		
		FileOperations.exit();
		
	}

}
