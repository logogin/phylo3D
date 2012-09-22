/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Exception thrown if an error occurs in the parsing of a user supplied file
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class UserInputFileParsingException extends FatalException
{
	public UserInputFileParsingException(String message)
	{
		super(message);
	}
}
