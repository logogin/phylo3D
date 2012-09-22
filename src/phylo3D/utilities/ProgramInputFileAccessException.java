/*
 * Created on 18-Aug-2004
 */
package phylo3D.utilities;

/**
 *  Exception covering the special case where the access problem relaties to an input file that is part of 
 * the program, as opposed to supplied by the user.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class ProgramInputFileAccessException extends InputFileAccessException
{
	public ProgramInputFileAccessException(String msg)
	{
		super(msg);
	}	
}
