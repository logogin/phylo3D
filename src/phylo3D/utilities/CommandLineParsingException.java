/*
 * Created on 17-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Command line exceptions due to errors in the parsing of the command line
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class CommandLineParsingException extends CommandLineException
{
	public CommandLineParsingException(String message)
	{
		super(message);
	}
}
