/*
 * Created on 08-Sep-2003
 */
package phylo3D.trees;

import java.io.*;

import forester.tree.*;

import phylo3D.datafiles.*;
import phylo3D.utilities.*;

/**
 *  Class is a data structure for holding phylogenetic information that is to be output in NHX format
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TreeNHX extends TreeNH
{
	/**
	 * Constructor for instantiating with TreeNCBI
	 * @param treeNCBI TreeNCBI to be used for instantiating
	 * @param useTaxIDAsSeqName boolean setting whether taxIDs or species names should be used
	 * as sequence name in ouput
	 */
	public TreeNHX(TreeNCBI treeNCBI, boolean useTaxIDAsSeqName)
	{
		super(treeNCBI, useTaxIDAsSeqName);
	}
	
	/**
	 * Constructor for instantiating from a forester.tree.Tree
	 * @param foresterTree forester.tree.Tree to be used for instantiating
	 */	
	public TreeNHX(forester.tree.Tree foresterTree)
	{
		super(foresterTree);
	}
	
	/* (non-Javadoc)
	 * @see phylo3D.trees.Tree#instantiateNode(java.lang.Object)
	 */
	protected NodeData instantiateNode(Object o)
	{
		NodeData result = null;
		if (o instanceof Node)
		{
			result = new NodeDataNHX((Node) o);
		} 
		if (o instanceof NodeDataNCBI)
		{
			result = new NodeDataNHX((NodeDataNCBI) o, isUseTaxIDAsSeqName());
		}
		return result;
	}	
	
	
	/* (non-Javadoc)
	 * @see phylo3D.trees.TreeNH#nodeDescription(phylo3D.trees.NodeDataNH)
	 */
	protected String nodeDescription(NodeDataNH temp)
	{
		NodeDataNHX ndnhx = (NodeDataNHX) temp;
		
		StringBuffer result = new StringBuffer();
		//Cannot rely on superclass method because different filtering
		if (ndnhx.getSequenceName() != null) {result.append(filterName(ndnhx.getSequenceName()));}
		if (ndnhx.getBranchLengthToParent() != null) {result.append(":" + ndnhx.getBranchLengthToParent());}
		
		StringBuffer extraInfo = new StringBuffer("[&&NHX");
		if (ndnhx.getBootstrapValue() != null)	{extraInfo.append(":B=" +ndnhx.getBootstrapValue() );}
		if (ndnhx.getSpeciesName() != null) {extraInfo.append(":S=" + filterName(ndnhx.getSpeciesName()));}
		if (ndnhx.getTaxonomyID() != null) {extraInfo.append(":T=" + ndnhx.getTaxonomyID());}
		if (ndnhx.getEcNumber() != null) {extraInfo.append(":E=" + ndnhx.getEcNumber());}
		if (ndnhx.isDuplication() != null) {extraInfo.append(":D=" + (ndnhx.isDuplication().booleanValue()?"Y":"N"));}
		if (ndnhx.getOrthologous() != null) {extraInfo.append(":O=" + ndnhx.getOrthologous());}
		if (ndnhx.getSuperOrthologous() != null) {extraInfo.append(":SO=" + ndnhx.getSuperOrthologous());}
		if (ndnhx.getLogLikelihoodValueOnParentBranch() != null) {extraInfo.append(":L=" + ndnhx.getLogLikelihoodValueOnParentBranch());}
		extraInfo.append("]");
		
		result.append(extraInfo.toString());
		return result.toString();				
	}
	

	
	/**
	 * Filters out characters that are not allowed in sequence names in NHX format
	 * The following characters cannot be part of names in NHX: '(' ')' '[' ']' ',' ':' as well as white spaces.
	 * Replaces both types of parenthesis with {}, replaces , and : with * and white spaces with _
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
	
	/* (non-Javadoc)
	 * @see phylo3D.trees.TreeNH#toString(boolean)
	 */
	public String toString(boolean internalNodeData)
	{
		return printSubtree((NodeDataNHX)root, internalNodeData) + ";";//Must not forget semicolon at end of file
	}
		
	//Test methods
	private static void testInstantiatingFromNCBI()
	throws FatalException
	{
		TreeNCBI treeNCBI = TreeNCBI.setupTestTree();
		TreeNHX treeNHX = new TreeNHX(treeNCBI, false);//Modify parameter TreeNHX(TreeNCBI treeNCBI, boolean useTaxIDAsSeqName)
		PrintWriter pw = FileOperations.getPrintWriter(DataFileNames.REL_PATH_TEST_OUTPUT_NHX);		
		pw.write(treeNHX.toString(false));//Modify parameter:  toString(boolean internalNodeData)
		pw.close();
	}
	
	private static void testInstantiatingFromForesterTree()
	throws UserInputFileAccessException, OutputFileAccessException
	{
		forester.tree.Tree foresterTree = null;
		
		//Only run one of the following at a time
		//String inputFileName = DataFiles.ABS_PATH_SIMPLE_NHX;
		//String inputFileName = DataFiles.ABS_PATH_MEDIUM_NHX;
		String inputFileName = DataFileNames.REL_PATH_COMPLEX_NHX;
		try
		{
			foresterTree = TreeHelper.readNHtree(new File(inputFileName));	
		}
		catch (Exception e)
		{
			throw new UserInputFileAccessException("Unable to read NH tree from file: " + inputFileName + "\n" + 
			e.getMessage());
		}
		
		TreeNHX treeNHX = new TreeNHX(foresterTree);
		PrintWriter pw = FileOperations.getPrintWriter(DataFileNames.REL_PATH_TEST_OUTPUT_NHX);	
		pw.write(treeNHX.toString(true));//Modify parameter:  toString(boolean internalNodeData)
		pw.close();
	}
	
	public static void main(String[] args)
	throws FatalException
	{
		//Only run one of the following at a time
		//testInstantiatingFromNCBI();
		testInstantiatingFromForesterTree();
	}	
	


}
