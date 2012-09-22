/*
 * Created: 22-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.net.*;

import phylo3D.utilities.*;


/**
 * TableParser for the siblingGroups table flat file
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TableParserNodes extends TableParser
{

	/**
	 * Constructor
	 * @param filenameURL URL to a flat file of the siblingGroups table
	 * @throws Exception
	 */
	public TableParserNodes(URL filenameURL) throws FatalException
	{
		super(filenameURL);
	}

	/**
	 * Instantiates a RecordNodes
	 * @return RecordNodes
	 * @see TableParser#instantiateRecord()
	 */
	protected Record instantiateRecord()
	{
		return new RecordNodes();
	}

	/**
	 * Curently always returns true since we include all siblingGroups in the parsing of the siblingGroups flat file
	 * @return true
	 * @see TableParser#useRecord(Record)
	 */
	protected boolean useRecord(Record record)
	{
		return true;
	}

}
