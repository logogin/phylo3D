/*
 * Created: 21-Aug-2003
 */
package phylo3D.trees;

import java.io.*;

/**
 * Class representing a node in the NCBI tree of life i.e. a combination of the data from the
 * siblingGroups table with the data from the names table. Class is essentially just a data holder.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NodeDataNCBI extends NodeData implements Serializable
{
	private Integer taxonomyID;
	private Integer parentTaxonomyID;
	private String rank;
	private String nameText;
	private String nameClass;
	
	//Added for lineage
	private String lineageText;
	private String lineageTaxID;



	/**
	 * Constructor which instantiates a NodeDataNCBI with the specified taxID.
	 * Useful since taxID is used for determining equality of NodeDataNCBI 
	 * @param ID Integer specifing the node
	 */
	public NodeDataNCBI(Integer ID)
	{
		super(ID);
	}
	
	public NodeDataNCBI(Integer ID, Integer parentID)
	{
		super(ID, parentID);
	}
	
	/**
	 * Produces a String representation of this object (taxID, parentTaxID, rank, nameText and nameClass)
	 * @return String representation
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String result = taxonomyID +" - " + parentTaxonomyID +" - " + rank +" - " + nameText +" - " + nameClass;
		return result;
	}

	public String getNameClass()
	{
		return nameClass;
	}

	public String getNameText()
	{
		return nameText;
	}
	
	public String getRank()
	{
		return rank;
	}

	public void setNameClass(String string)
	{
		nameClass = string;
	}

	public void setNameText(String string)
	{
		nameText = string;
	}

	public void setRank(String string)
	{
		rank = string;
	}

	public Integer getParentTaxonomyID()
	{
		return parentTaxonomyID;
	}

	public Integer getTaxonomyID()
	{
		return taxonomyID;
	}

	public void setParentTaxonomyID(Integer integer)
	{
		parentTaxonomyID = integer;
	}

	public void setTaxonomyID(Integer integer)
	{
		taxonomyID = integer;
	}

	public String getLineageTaxID()
	{
		return lineageTaxID;
	}

	public String getLineageText()
	{
		return lineageText;
	}

	public void setLineageTaxID(String string)
	{
		lineageTaxID = string;
	}

	public void setLineageText(String string)
	{
		lineageText = string;
	}

}
