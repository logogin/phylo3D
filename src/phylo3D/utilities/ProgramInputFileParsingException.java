/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Exception due to an error in the parsing of an input file that is part of the program.
 * This is an exception that should really not occur if the code has been properly tested.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class ProgramInputFileParsingException extends FatalException
{

	/**
	 * @param message
	 */
	public ProgramInputFileParsingException(String message)
	{
		super(message);
	}

}
