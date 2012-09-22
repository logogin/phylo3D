/*
 * Created on 11-Sep-2003
 */
package phylo3D.trees;

import forester.tree.*;

import phylo3D.utilities.*;

/**
 *  Class acting as a data holder for the nodes of an TreeNH.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NodeDataNH extends NodeData
{
	private String sequenceName;
	private Float branchLengthToParent;
	

	public NodeDataNH(Integer ID)
	{
		super(ID);
	}

	public NodeDataNH(Integer ID, Integer parentID)
	{
		super(ID, parentID);
	}
	
	public NodeDataNH(NodeDataNCBI ndn, boolean useTaxIDAsSeqName)
	{
		this(ndn.getID(), ndn.getParentID());
		if (useTaxIDAsSeqName)
		{
			sequenceName = ndn.getTaxonomyID().toString();
		}
		else
		{
			sequenceName = ndn.getNameText();
		}
	}
	
	public NodeDataNH(Node n)
	{
		this(new Integer(n.getID()));
		if(n.getParent() != null)
		{
			Node parent = n.getParent();
			while(parent.isPseudoNode())
			{
				parent = parent.getParent();
			}
			setParentID(new Integer(parent.getID()));
		}
		else
		{
			setParentID(new Integer(n.getID()));
		}
		
		if(n.getSeqName() != null && !n.getSeqName().equals(Constants.EMPTY_STRING))
		{
			sequenceName = n.getSeqName();
		}
		if (n.getDistanceToParent() != Node.DISTANCE_DEFAULT && n.getDistanceToParent() >= 0.0 )
		{
			branchLengthToParent = new Float(n.getDistanceToParent());	
		}
	}


	public String getSequenceName()
	{
		return sequenceName;
	}

	public void setSequenceName(String string)
	{
		sequenceName = string;
	}

	public Float getBranchLengthToParent()
	{
		return branchLengthToParent;
	}

	public void setBranchLengthToParent(Float float1)
	{
		branchLengthToParent = float1;
	}

}
