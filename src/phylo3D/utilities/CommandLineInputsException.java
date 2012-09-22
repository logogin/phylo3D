/*
 * Created on 17-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Command line exceptions due to errors in the inputs supplied e.g. supplying an option value that
 * is a String instead of an Integer
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class CommandLineInputsException extends CommandLineException
{
	public CommandLineInputsException(String message)
	{
		super(message);
	}
}
