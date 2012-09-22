/*
 * Created: 22-Aug-2003
 */
package phylo3D.trees;

import java.awt.*;

/**
 * Class representing a Node in Walrus tree. The Walrus tree has a file representation which
 * is very similar to the NCBI tree file representation so NodeDataWalrus is essential the same as 
 * NodeDataNCBI with a some extra fields relating to extra data needed in the Walrus tree: new 
 * IDs (siblingGroups need to be renumbered) and colouring data. The class is essentially just a data holder.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NodeDataWalrus extends NodeData
{
	//Need to be calculated before print the node to file
	private Integer walrusID;
	private Integer walrusParentID;
	//Determines the colour of the branch between this node and its parent
	private int branchColor = TreeWalrus.produceLibseaColourCoding(new Color(255, 255, 255));
	//Determines the colour of this node
	private int nodeColor = TreeWalrus.produceLibseaColourCoding(new Color(255, 255, 255));
	//The NodeData that this class encapsulates. Can be NodeDataNH, NodeDataNHX or NodeDataNCBI
	private NodeData nodeData;
	
	//Added data to allow full lineages to be present
	private String fullLineageText;
	private String taxonomicClassification;

	/**
	 * Constructor
	 * @param nd NodeData
	 */
	public NodeDataWalrus(NodeData nd)
	{
		super(nd.getID(), nd.getParentID());
		this.nodeData = nd;
	}
	
	public NodeDataWalrus(Integer ID)
	{
		super(ID);
	}

	public Integer getWalrusID()
	{
		return walrusID;
	}

	public Integer getWalrusParentID()
	{
		return walrusParentID;
	}

	public void setWalrusID(Integer i)
	{
		walrusID = i;
	}

	public void setWalrusParentID(Integer i)
	{
		walrusParentID = i;
	}

	public int getBranchColor()
	{
		return branchColor;
	}

	public void setBranchColor(int i)
	{
		branchColor = i;
	}

	public int getNodeColor()
	{
		return nodeColor;
	}

	public void setNodeColor(int i)
	{
		nodeColor = i;
	}

	public NodeData getNodeData()
	{
		return nodeData;
	}

	public void setNodeData(NodeData data)
	{
		nodeData = data;
	}

	public String getFullLineageText()
	{
		return fullLineageText;
	}

	public void setFullLineageText(String string)
	{
		fullLineageText = string;
	}

	public String getTaxonomicClassification()
	{
		return taxonomicClassification;
	}

	public void setTaxonomicClassification(String string)
	{
		taxonomicClassification = string;
	}

}
