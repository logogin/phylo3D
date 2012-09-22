/*
 * Created: 21-Aug-2003
 */
package phylo3D.parser.ncbi;

import java.util.*;

import phylo3D.utilities.ProgramInputFileParsingException;

/**
 * Class representing a record from the NCBI tree of life names table.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class RecordNames implements Record
{
	private Integer taxID;
	private String nameText;
	private String nameClass;
	
	public void setFields(Vector fields) throws ProgramInputFileParsingException
	{
		if (fields.size() != 4)
		{
			throw new ProgramInputFileParsingException("Not 4 fields in namesRecord");
		}
		else
		{
			String temp = (String)fields.get(0);
			taxID = new Integer(temp.trim());
			nameText = ((String)fields.get(1)).trim();
			nameClass = ((String)fields.get(3)).trim();
		}
	}
	
	/**
	 * Determines if two objects are equal.
	 * @return boolean true if the two objects have the same taxID.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		RecordNames rn = (RecordNames) obj;
		return rn.taxID.equals(taxID);
	}

	
	/**
	 * Produces a string representation.
	 * @return String containing taxID, nameText and nameClass (e.g. "scientific name")
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String result = new String();
		result += "taxID: " + taxID + " - nameText: " + nameText + " - nameClass: " + nameClass;
		return result;
	}

	
	/**
	 * Determines whether the nameClass for the nameText is "scientific name"
	 * @return boolean true if nameClass is "scientific name"
	 */
	public boolean isScientificName()
	{
		return nameClass.equals("scientific name");
	}
	
	public String getNameClass()
	{
		return nameClass;
	}

	public String getNameText()
	{
		return nameText;
	}

	public Integer getTaxID()
	{
		return taxID;
	}

	public void setNameClass(String string)
	{
		nameClass = string;
	}

	public void setNameText(String string)
	{
		nameText = string;
	}

	public void setTaxID(Integer i)
	{
		taxID = i;
	}

}
