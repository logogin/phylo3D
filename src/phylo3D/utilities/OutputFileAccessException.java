/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Exception to be thrown when the program is unable to access an output file for writing to.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class OutputFileAccessException extends FatalException
{

	/**
	 * @param message
	 */
	public OutputFileAccessException(String message)
	{
		super(message);
	}

}
