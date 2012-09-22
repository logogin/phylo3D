/*
 * Created: 21-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.util.*;
import phylo3D.utilities.*;

/**
 * Interface which must be implemented by all classes that represent a record from an NCBI database
 * table file dump.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public interface Record
{
	public void setFields(Vector fields) throws ProgramInputFileParsingException;
}
