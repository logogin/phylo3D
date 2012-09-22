/*
 * Created on 10-Sep-2003
 *
 */
package phylo3D.trees;

import java.util.*;
import java.io.*;

import forester.tree.*;

import phylo3D.datafiles.*;
import phylo3D.utilities.*;
/**
 *  Class is a data structure for holding phylogenetic information that is to be output in NH format
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TreeNH extends Tree
{
	private boolean useTaxIDAsSeqName;
	
	public TreeNH()
	{}

	/**
	 * Constructor for instantiating with TreeNCBI
	 * @param treeNCBI TreeNCBI to be used for instantiating
	 * @param useTaxIDAsSeqName boolean setting whether taxIDs or species names should be used
	 * as sequence name in ouput
	 */
	public TreeNH(TreeNCBI treeNCBI, boolean useTaxIDAsSeqName)
	{
		this.useTaxIDAsSeqName = useTaxIDAsSeqName;
 		buildTree(treeNCBI);
	}
	
	/**
	 * Constructor for instantiating from a forester.tree.Tree
	 * @param foresterTree forester.tree.Tree to be used for instantiating
	 */
	public TreeNH(forester.tree.Tree foresterTree)
	{
		Node foresterRoot = foresterTree.getRoot();
		root = instantiateNode(foresterRoot);
		Vector allNonRootForesterNodes = foresterRoot.getAllChildren();//gets all descendent nodes but no pseudo nodes
		Iterator iter = allNonRootForesterNodes.iterator();
		while (iter.hasNext())
		{
			Node n = (Node) iter.next();
			//Determine parentID
			Node parentNode = n.getParent();
			while(parentNode.isPseudoNode())
			{
				parentNode = parentNode.getParent();
			}
			Integer parentID = new Integer(parentNode.getID());
			//Update hashtable
			Vector siblingGroup = (Vector)this.siblingGroups.get(parentID);
			if (siblingGroup == null)
			{
				siblingGroup = new Vector();
			}
			siblingGroup.add(instantiateNode(n));
			siblingGroups.put(parentID, siblingGroup);
		}
	}

		
	/* (non-Javadoc)
	 * @see phylo3D.trees.Tree#instantiateNode(java.lang.Object)
	 */
	protected NodeData instantiateNode(Object o)
	{
		NodeData result = null;
		if (o instanceof Node)
		{
			result = new NodeDataNH((Node) o);
		} 
		if (o instanceof NodeDataNCBI)
		{
			result = new NodeDataNH((NodeDataNCBI) o, isUseTaxIDAsSeqName());
		}
		return result;
	}
	
	/**
	 * Prints a description of the node
	 * @param ndnh The node for which a description is to be printed
	 * @return String containing the description
	 */
	protected String nodeDescription(NodeDataNH ndnh)
	{
		StringBuffer result = new StringBuffer();
		if (ndnh.getSequenceName() != null)
		{
			result.append(filterName(ndnh.getSequenceName()));	
		}
		if (ndnh.getBranchLengthToParent() != null)
		{
			result.append(":" + ndnh.getBranchLengthToParent());
		}
		return result.toString();				
	}
	
	/**
	 * Filters out characters that are not allowed in sequence names in NH format
	 * @param text String containing test to be filtered
	 * @return String containing the filtered text
	 */
	protected String filterName(String text)
	{
		text = text.replace('(', '{');
		text = text.replace(')', '}');
		text = text.replace('[', '{');
		text = text.replace(']', '}');
		text = text.replace(',', '*');
		text = text.replace(':', '*');
		text = text.replace(';', '*');		
		text = text.replace(' ', '_');
		return text;
	}	
	
	/**
	 * Prints a subtree beneath a specified node
	 * @param subtreeRootNode NodeDataNH specifying the root of the subtree
	 * @param outputInternalNodeData boolean determining whether internal node data should be included
	 * in the output
	 * @return String containing NH representation of the tree
	 */
	protected String printSubtree(NodeDataNH subtreeRootNode, boolean outputInternalNodeData)
	{
		StringBuffer result = new StringBuffer(); 
		Vector siblingGroup = getSiblingGroup(subtreeRootNode.getID());
		
		if (siblingGroup == null)
		{
			result.append(nodeDescription(subtreeRootNode));
		}
		else
		{
			//NHX cannot handle case where node has only one descendent
			if (siblingGroup.size() != 1)
			{
				result.append("(");
				Enumeration enum = siblingGroup.elements();
				while (enum.hasMoreElements())
				{
					NodeDataNH ndnh = (NodeDataNH) enum.nextElement();
					result.append(printSubtree(ndnh, outputInternalNodeData));
					if (enum.hasMoreElements())
					{
						result.append(",");
					}
				}
				result.append(")");
				if(outputInternalNodeData)
				{
					result.append(nodeDescription(subtreeRootNode));
				}
				
			}
			else
			{
				//In the case where only one descendent of node then jump over it
				NodeDataNH ndnh = (NodeDataNH)siblingGroup.get(0);
				result.append(printSubtree(ndnh, outputInternalNodeData));
							
			}
		}
		return result.toString();
	}


	/**
	 * Determines whether taxIDs are being used as sequence names or not
	 * @return boolean which is true if taxIDs are being used as sequence names
	 */
	public boolean isUseTaxIDAsSeqName()
	{
		return useTaxIDAsSeqName;
	}
	
	/**
	 * Produces a String representation of the whole tree 
	 * @param internalNodeData boolean which must be true if internal node data is going to be included 
	 * in the output
	 * @return String containing a representation of the tree in NH format
	 */
	public String toString(boolean internalNodeData)
	{
		return printSubtree((NodeDataNH)root, internalNodeData) + ";";//Must not forget semicolon at end of file
	}
		
	//Test methods
	private static void testInstantiatingFromNCBITree()
	throws FatalException
	{
		TreeNCBI treeNCBI = TreeNCBI.setupTestTree();
		TreeNH treeNH = new TreeNH(treeNCBI, true);//Modify parameter TreeNH(TreeNCBI treeNCBI, boolean useTaxIDAsSeqName)
		PrintWriter pw = FileOperations.getPrintWriter(DataFileNames.REL_PATH_TEST_OUTPUT_NH);	
		pw.write(treeNH.toString(true));//Modify parameter  toString(boolean internalNodeData)
		pw.close();
	}
	
	private static void testInstantiatingFromForesterTree()
	throws UserInputFileAccessException, OutputFileAccessException
	{
		forester.tree.Tree foresterTree = null;
		
		//Only run one of the following at a time
		//String inputFileName = DataFiles.ABS_PATH_SIMPLE_NH;
		//String inputFileName = DataFiles.ABS_PATH_MEDIUM_NH;
		String inputFileName = DataFileNames.REL_PATH_COMPLEX_NH;
		try
		{
			foresterTree = TreeHelper.readNHtree(new File(inputFileName));	
		}
		catch (Exception e)
		{
			throw new UserInputFileAccessException("Unable to read NH tree form file: " + inputFileName + "\n" + 
			e.getMessage());
		}
		
		TreeNH treeNH = new TreeNH(foresterTree);
		PrintWriter pw = FileOperations.getPrintWriter(DataFileNames.REL_PATH_TEST_OUTPUT_NH);
		pw.write(treeNH.toString(false));//Modify parameter:  toString(boolean internalNodeData)
		pw.close();		
	}
	
	public static void main(String[] args) throws FatalException
	{
		//Only run one of the following at a time
		testInstantiatingFromNCBITree();
		//testInstantiatingFromForesterTree();
	}
}
