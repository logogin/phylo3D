/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Special case of the input file access exception where the input file is supplied by the user.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class UserInputFileAccessException extends InputFileAccessException
{
	public UserInputFileAccessException(String msg)
	{
		super(msg);
	}
}
