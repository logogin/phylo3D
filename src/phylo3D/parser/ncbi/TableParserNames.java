/*
 * Created: 21-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.net.*;

import phylo3D.utilities.*;


/**
 * TableParser for the names table flat file
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TableParserNames extends TableParser
{
	/**
	 * Constructor
	 * @param filenameURL  URL to a flat file of the siblingGroups table
	 * @throws Exception
	 */
	public TableParserNames(URL filenameURL) throws FatalException
	{
		super(filenameURL);
	}
	
	/**
	 * Instantiates a RecordNames
	 * @return RecordNames
	 * @see TableParser#instantiateRecord()
	 */
	protected Record instantiateRecord()
	{
		return new RecordNames();
	}
	
	/** 
	 * Returns true if the nameText of the record is the "scientific name"
	 * @see TableParser#useRecord(Record)
	 */
	protected boolean useRecord(Record record)
	{
		RecordNames rn = (RecordNames) record;
		return rn.isScientificName();
	}

}
