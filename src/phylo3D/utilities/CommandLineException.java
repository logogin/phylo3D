/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Superclass for all command line related exceptions
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
abstract public class CommandLineException extends FatalException
{
	public CommandLineException(String msg)
	{
		super(msg);
	}
}
