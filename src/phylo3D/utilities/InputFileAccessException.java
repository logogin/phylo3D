/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Input file access exceptions due to the inability to access an input file whether it be user supplied or not.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class InputFileAccessException extends FatalException
{
	public InputFileAccessException(String msg)
	{
		super(msg);
	}
}
