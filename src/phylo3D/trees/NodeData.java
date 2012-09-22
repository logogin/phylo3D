/*
 * Created on 10-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package phylo3D.trees;

import java.io.*;

/**
 *  Abstract class representing the data in a node
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public abstract class NodeData implements Serializable
{
	protected Integer ID;
	protected Integer parentID;
	
	NodeData(Integer ID)
	{
		this.ID = ID;
	}
	
	NodeData(Integer ID, Integer parentID)
	{
		this.ID = ID;
		this.parentID = parentID;
	}
	
	public Integer getID()
	{
		return ID;
	}

	public Integer getParentID()
	{
		return parentID;
	}

	public void setID(Integer integer)
	{
		ID = integer;
	}

	public void setParentID(Integer integer)
	{
		parentID = integer;
	}
	
	/**
	 * Determines if this object is equal the specified object. Two nodes are equal if they have the same ID.
	 * @param o Object that this is to be compared to
	 * @return true if the two objects have the same taxID
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		NodeData nd = (NodeData) o;
		return nd.getID().equals(getID());
	}	

}
