/*
 * Created: 21-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.util.*;

import phylo3D.utilities.ProgramInputFileParsingException;


/**
 *  Class representing a record from the NCBI tree of life siblingGroups table
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class RecordNodes implements Record
{
	private Integer taxID;
	private Integer parentTaxID;
	private String rank;
	
	public void setFields(Vector fields) throws ProgramInputFileParsingException
	{
		if (fields.size() != 13)
		{
			throw new ProgramInputFileParsingException("Not 13 fields in namesRecord");
		}
		else
		{
			taxID = new Integer(((String)fields.get(0)).trim());
			parentTaxID = new Integer(((String)fields.get(1)).trim());
			rank = ((String)fields.get(2)).trim();
		}
	}
	
	public Integer getParentTaxID()
	{
		return parentTaxID;
	}

	public String getRank()
	{
		return rank;
	}

	public Integer getTaxID()
	{
		return taxID;
	}

	public void setParentTaxID(Integer i)
	{
		parentTaxID = i;
	}

	public void setRank(String string)
	{
		rank = string;
	}

	public void setTaxID(Integer i)
	{
		taxID = i;
	}

}
