/*
 * Created on 17-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Superclass for all exceptions that are considered to be fatal to the running of the program. This is
 * actual almost all types of exception.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
abstract public class FatalException extends Exception
{
	public FatalException(String message)
	{
		super(message);
	}
}
