/*
 * Created on 19-Sep-2003
 */
package phylo3D.tools;

import java.util.*;

import phylo3D.utilities.*;
import phylo3D.imaging.*;
import phylo3D.trees.*;


/**
 *  Loads the NCBI tree from serialized file into TreeNCBI which can then be manipulated (narrowed)
 * and coloured before it is printed to a Walrus file.
 * The options when calling the program are:
 * --narrowTo=node (obligatory)
 * --colours=node,colour+node,colour+node,colour (optional) where node is NCBI taxonomy ID and colour
 * is an RGB colour in Hex format 
 * --colourSubtreeRootBranch=Y or N, where Y:branch leading to the root of the subtree is coloured
 *  and N: branch leading to the root of the subtree is not coloured (obligatory if the colours option is used)
 * 
 * outputFile (obligatory)
 * 
 * Carries out the colouring in the specified order so that if the user wishes to colour a subtree a 
 * particular colour then the root node of the supertree should be specified first followed by the root node
 *  of the subtree. If the opposite order is used then the supertree colour will overwrite the subtree colour.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NCBItoWalrus extends ToolWithWalrusOutput
{
	//Data to be extracted from the command line
	private String outputFilePath = null;
	private String taxIDOfNodeToNarrowTo = null;
	private Vector objectsColourAssociations = new Vector();
	private String colourSubtreeRootBranch = null;
		
	public static void main(String[] args)
	{
		NCBItoWalrus tool = new NCBItoWalrus();
		try
		{
			tool.execute(args);
		}
		catch (FatalException e)
		{
			tool.handleException(e);
		}
	}
	
	protected void execute(String[] args)
	throws FatalException
	{
		parseCommandLine(args);
		
		//Read the serialized NCBI tree in
		UserInfo.println("Loading the NCBI tree (please be patient).");
		TreeNCBI treeNCBI = TreeNCBI.loadFromFile();
		
		//Narrow to the correct subtree
		CommandLineParsing.narrowToSubtree(treeNCBI, taxIDOfNodeToNarrowTo);
		
		//Build the Walrus tree
		TreeWalrus treeWalrus = new TreeWalrus(treeNCBI);
		
		//Locate the nodes that are to be coloured i.e. the Walrus nodes that have matching taxIDs
		//to those specified in the command line
		modifyColouringData(treeWalrus, objectsColourAssociations);
		
		//Produce the output files: tree and legend
		if (colourSubtreeRootBranch != null)
		{
			if (colourSubtreeRootBranch.equals(CommandLineParsing.YES))
			{
				produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, true,
				new Vector(), new Vector(), false);
			}
			else
			{
				produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, false,
				new Vector(), new Vector(), false);
			}					
		}
		else
		{
			produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, false,
			new Vector(), new Vector(), false);
		}

		
		//Inform user that calculation is finished
		UserInfo.println("Finished.");
	}
	
	/* (non-Javadoc)
	 * @see tools.Tool#parseCommandLine(java.lang.String[])
	 */
	protected void parseCommandLine(String[] args) 
	throws FatalException
	{
		//Parse the command line

		if (args.length == 2)
		{
			taxIDOfNodeToNarrowTo= CommandLineParsing.extractNodeToNarrowTo(args[0]);
			outputFilePath = args[1];
		}
		else
		{
			if (args.length == 4)
			{
				taxIDOfNodeToNarrowTo = CommandLineParsing.extractNodeToNarrowTo(args[0]);
				objectsColourAssociations = computeObjectsColourAssociations(args[1]);
				colourSubtreeRootBranch = CommandLineParsing.extractColourSubtreeRootBranch(args[2]);
				outputFilePath = args[3];
			}
			else
			{
				throw new CommandLineParsingException("Incorrect number of arguments");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see tools.Tool#errorInCommandLine(java.lang.String)
	 */
	protected String toolUsageText()
	{
		StringBuffer toolUsageText = new StringBuffer();
		String newLine = "\n";
		toolUsageText.append("Usage: \"NCBItoWalrus  [-options] <walrus tree file name>\"" + newLine);
		toolUsageText.append("" + newLine);		
		toolUsageText.append("Options (which must appear in the following order):" + newLine);
		toolUsageText.append("--narrowTo=node where node is an NCBI taxonomy ID (obligatory)" + newLine);		
		toolUsageText.append("--colours=node,colour+node,colour+node,colour (optional)" + newLine);
		toolUsageText.append("where node is an NCBI taxonomy ID" + newLine);
		toolUsageText.append("where colour is the hex representation of an RGB code" + newLine);
		toolUsageText.append("--colourSubtreeRootBranch=Y or N (obligatory if the 'colours' option is used)" + newLine);
		toolUsageText.append("" + newLine);
		return toolUsageText.toString();
	}


	/* (non-Javadoc)
	 * @see tools.WalrusTool#textToUseInLegend(trees.NodeData)
	 */
	protected String textToUseInLegend(NodeData nd)
	{
		return ((NodeDataNCBI)nd).getNameText();
	}
	


	/**
 	 * Modifies the colouring data contained in the objectsColourAssociations by searching the Walrus 
	 * tree for the node with matching sequence name. This NodeDataWalrus node then replaces the 
	 * String that contained the taxID. Note that there will be only one node per taxID
	 * @param treeWalrus TreeWalrus to be searched
	 * @param objectsColourAssociations Vector of ObjectsColourAssociations that contain associations
	 * between Strings containing taxIDs and colours
	 */
	private void modifyColouringData(TreeWalrus treeWalrus, Vector objectsColourAssociations)
	{
		Vector allWalrusNodes = treeWalrus.getAllNodes();
		Iterator iter1 = objectsColourAssociations.iterator();
		while (iter1.hasNext())
		{
			ObjectsColourAssociation objectsColourAssociation = (ObjectsColourAssociation) iter1.next();
			String taxID = (String) objectsColourAssociation.getFirstObject();
			Iterator iter2 = allWalrusNodes.iterator();
			NodeDataWalrus locatedWalrusNode = null;
			while (iter2.hasNext())
			{
				NodeDataWalrus ndw = (NodeDataWalrus) iter2.next();
				NodeDataNCBI ndncbi = (NodeDataNCBI)ndw.getNodeData();
				if (taxID.equals(ndncbi.getTaxonomyID().toString()))
				{
					locatedWalrusNode = ndw;
					break;//Don't look for all nodes that match just the nodes
				}
			}
			
			if(locatedWalrusNode == null)
			{
				UserInfo.println("The node " + taxID + " cannot be located in the subtree that you have narrowed to.");//Inform user
				iter1.remove();//Remove the objectsColourAssociation since cannot be located
			}
			else
			{
				Vector temp = new Vector();
				temp.add(locatedWalrusNode);
				objectsColourAssociation.setObjects(temp);//add the located node
			}
		}		
	}
}
