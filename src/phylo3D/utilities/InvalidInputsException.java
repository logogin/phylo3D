/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Invalid inputs exception due to errors in the inputs to the program that can occur at another level than
 * at the command line
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class InvalidInputsException extends FatalException
{
	public InvalidInputsException(String msg)
	{
		super(msg);
	}
}
