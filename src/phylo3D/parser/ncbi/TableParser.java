/*
 * Created: 21-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.io.*;
import java.util.*;
import java.net.*;
 
import phylo3D.utilities.*;

/**
 * Class implementing functionality for parsing an NCBI tree of life table file dump.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public abstract class TableParser
{
	//Full file path to the file that this TableParser will parse
	private URL filenameURL = null;
	//Vector of records that have been parsed from the table file dump
	protected Vector records = new Vector();
	
	/**
	 * Constructor that reads a table file dump and parses it into records
	 * @param filenameURL URL to the file to be parsed
	 * @throws Exception
	 */
	public TableParser(URL filenameURL) 
	throws ProgramInputFileAccessException, ProgramInputFileParsingException
	{
		try
		{
			this.filenameURL = filenameURL;
			BufferedReader br = FileOperations.getBufferedReader(filenameURL);
			String line = null;
						
			while ((line = br.readLine()) != null)//does not return the line return characters
			{
				//remove the \t characters
				line = line.replaceAll("\t", " ");
				//Create a StringTokeniser and use as delimiter the string |
				StringTokenizer st = new StringTokenizer(line, "|");
				//Produce a tokenized record
				Vector tokenizedRecord = new Vector();
				while (st.hasMoreTokens())
				{
					tokenizedRecord.add(st.nextToken());
				}
				//Produce the record using the tokenized record
				Record record = instantiateRecord();
				record.setFields(tokenizedRecord);
				if (useRecord(record))
				{
					records.add(record);
				}
			}
		}
		catch(InputFileAccessException ifae)
		{
			throw new ProgramInputFileAccessException(ifae.getMessage());
		}		
		catch(IOException ioe)
		{
			throw new ProgramInputFileAccessException("Unable to read from file: " + filenameURL);
		}
	}

	/**
	 * Instantiates an object of a class that implements the Record interface
	 * @return An object of a class that implements the Record interface
	 * @throws Exception
	 */
	protected abstract Record instantiateRecord();
	
	
	/**
	 * Examines a record and determines whether the record is to be used e.g. a record may 
	 * not be used if the NameText is not the scientific name of the species
	 * @param record Record to be examined
	 * @return boolean true if the record can be used in the parsing
	 */
	protected abstract boolean useRecord(Record record);
	
	
	/**
	 * Gets the Vector of records that are encapsulated in this class
	 * @return Vector of Records
	 */
	public Vector getRecords()
	{
		return records;
	}
	

}
