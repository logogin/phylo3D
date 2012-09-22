/*
 * Created on 19-Sep-2003
 */
 
package phylo3D.tools;

import java.util.*;
import java.io.*;
import java.awt.*;

import forester.tree.*;

import phylo3D.parser.nexus.TreeBlockParser;
import phylo3D.trees.*;
import phylo3D.imaging.*;
import phylo3D.utilities.*;
import phylo3D.parser.taxonomy.*;


/**
 * Loads an NH or NHX file and transforms it into a Walrus representation, optionally colours the tree 
 * (both specific nodes and according to a taxonomy),
 * and writes the walrus tree to a file. If the tree was coloured (either specific nodes or with a taxonomy)
 *  produces a legend file for each colouring and writes this to a .jpg file in the same directory as the walrus tree file.
 *  
 * --inputFormat=NH, NHX or NEXUS (obligatory) specifies the format of the input tree
 * --colours=node,colour+node,colour+node,colour (optional)	where node is a sequencename in the tree
 * and colour is an RGB colour in Hex format
 * --taxonomyFile=path
 * --matchingColumn=digit (default is 1)
 * --colouringColumn=digit (default is 1)
 * --colouringNames=node,colour+node,colour+node,colour (can be used if the taxonomyFile option is used)
* --colourSubtreeRootBranch=Y or N, where Y:branch leading to the root of the subtree is coloured
 *  and N: branch leading to the root of the subtree is not coloured (default is N)
 * 
 * inputFile (obligatory)
 * outputFile (obligatory)
 * 
 * Carries out the colouring in the specified order so that if the user wishes to colour a subtree a 
 * particular colour then the root node of the supertree should be specified first followed by the root node
 *  of the subtree. If the opposite order is used then the supertree colour will overwrite the subtree colour.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NHtoWalrus extends ToolWithWalrusOutput
{
	//Data to be extracted from the command line
	String inputFormat = null;
	String inputFilePath = null;
	String outputFilePath = null;
	Vector objectsColourAssociations = new Vector();
	String colourSubtreeRootBranch = null;
	String taxonomyFilePath = null;
	int matchingColumn = 1;
	int colouringColumn = 1;
	Vector colouringNames = new Vector();
	
	public static void main(String[] args)
	{
		
		NHtoWalrus tool = new NHtoWalrus();
		try
		{
			tool.execute(args);
		}
		catch (FatalException e)
		{
			tool.handleException(e);
		}
		//test();
	}
	
	/* (non-Javadoc)
	 * @see phylo3D.tools.Tool#execute(java.lang.String[])
	 */
	protected void execute(String[] args)
	throws FatalException
	{
		parseCommandLine(args);
		
		//Read the input file containing the NH or NHX tree
		forester.tree.Tree foresterTree = null;
		try
		{
			UserInfo.println("Reading tree from file: " + inputFilePath);
			if (inputFormat.equals(CommandLineParsing.NH_OPTION_VALUE) ||
				 inputFormat.equals(CommandLineParsing.NHX_OPTION_VALUE))
			{
				foresterTree = TreeHelper.readNHtree(new File(inputFilePath));	
			}
			else
			{
				TreeBlockParser tbp = new TreeBlockParser(inputFilePath);
				foresterTree = tbp.getForesterTree();
			}
			
		}
		catch(Exception e)
		{
			throw new UserInputFileAccessException(e.getMessage());
		}
			
		//Build the TreeNH (needed for building the TreeWalrus)
		TreeNH treeNH = null;
		if(inputFormat.equals(CommandLineParsing.NH_OPTION_VALUE) || 
			inputFormat.equals(CommandLineParsing.NEXUS_OPTION_VALUE))
		{
			treeNH = new TreeNH(foresterTree);
		}
		else
		{
			treeNH = new TreeNHX(foresterTree);			
		}
		
		//Build the Walrus tree
		TreeWalrus treeWalrus = new TreeWalrus(treeNH);
		
		//If a taxonomy file is present use this to attempt to colour the tree
		Vector taxonomyObjectsColourAssociations = new Vector();
		if (taxonomyFilePath != null)
		{
			TaxonomyFileParser tfp = new TaxonomyFileParser(taxonomyFilePath);
			taxonomyObjectsColourAssociations = tfp.getStringsColourAssociations(matchingColumn, colouringColumn);
			//Mark that full lineage is available
			treeWalrus.setFullLineageAssigned(true);		
		}
		
		
		//Sort alphabetically in all cases
		boolean sortLegendAlphabetically = false;
		if(colouringNames.size() == 0)
		{
			sortLegendAlphabetically = true;
		}
		else
		{
			sortLegendAlphabetically = true;
		}
		
		//Taxonomy colouring
		//taxonomyColouredWalrusNodes are needed to colour the walrus tree
	  	//taxonomyObjectsColourAssociations are needed to produce the taxonomy legend
	  	Vector taxonomyColouredWalrusNodes = new Vector();
	  	if (taxonomyObjectsColourAssociations.size() != 0)
		{
			taxonomyColouredWalrusNodes = produceColouringDataFromTaxonomy(treeWalrus, 
													taxonomyObjectsColourAssociations, colouringNames);
		}	
		
		//Specific nodes colouring
		//Locate the nodes that are to be coloured i.e. the Walrus nodes that have matching sequence
		//names to the those specified in the command line
		modifyColouringData(treeWalrus, objectsColourAssociations);
		
		//Produces tree file and legend file (if there are nodes to colour)
		if (colourSubtreeRootBranch != null)
		{
			if (colourSubtreeRootBranch.equals(CommandLineParsing.YES))
			{
				produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, true, 
				taxonomyColouredWalrusNodes, taxonomyObjectsColourAssociations, sortLegendAlphabetically);	
			}
			else
			{
				produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, false, 
				taxonomyColouredWalrusNodes, taxonomyObjectsColourAssociations, sortLegendAlphabetically);
			}
			
		}
		else
		{
			produceOutputFiles(outputFilePath, treeWalrus, objectsColourAssociations, false, 
			taxonomyColouredWalrusNodes, taxonomyObjectsColourAssociations, sortLegendAlphabetically);
		}
		
		
		//Inform user that task is completed
		UserInfo.println("Finished");		
	}
	
	/* (non-Javadoc)
	 * @see phylo3D.tools.Tool#parseCommandLine(java.lang.String[])
	 */
	protected void parseCommandLine(String[] args) 
	throws FatalException
	{
		//Parse the command line

		if (args.length == 3)
		{
			inputFormat = CommandLineParsing.extractInputFormat(args[0]);
			inputFilePath = args[1];
			outputFilePath = args[2];
		}
		else
		{
			if (args.length == 5)
			{
				inputFormat = CommandLineParsing.extractInputFormat(args[0]);					
				objectsColourAssociations = computeObjectsColourAssociations(args[1]);
				colourSubtreeRootBranch = CommandLineParsing.extractColourSubtreeRootBranch(args[2]);
				inputFilePath = args[3];
				outputFilePath = args[4];
			}
			else
			{
				if (args.length == 7)
				{
					//The command line consists of
					//inputFormat, taxonomyFile, matchingColumn, colouringColumn, colourSubtreeRootBranch
					//inputFilePath and outputFilePath
					inputFormat = CommandLineParsing.extractInputFormat(args[0]);					
					taxonomyFilePath = CommandLineParsing.extractTaxonomyFile(args[1]);
					matchingColumn = CommandLineParsing.extractMatchingColumn(args[2]);
					colouringColumn = CommandLineParsing.extractColouringColumn(args[3]);
					colourSubtreeRootBranch = CommandLineParsing.extractColourSubtreeRootBranch(args[4]);
					inputFilePath = args[5];
					outputFilePath = args[6];						
				}
				else
				{
					if (args.length == 8)
					{
						//The command line consists of
						//inputFormat, taxonomyFile, matchingColumn, colouringColumn, colouringNames, colourSubtreeRootBranch
						//inputFilePath and outputFilePath
						inputFormat = CommandLineParsing.extractInputFormat(args[0]);
						taxonomyFilePath = CommandLineParsing.extractTaxonomyFile(args[1]);
						matchingColumn = CommandLineParsing.extractMatchingColumn(args[2]);
						colouringColumn = CommandLineParsing.extractColouringColumn(args[3]);
						colouringNames = CommandLineParsing.extractColouringNames(args[4]);
						colourSubtreeRootBranch = CommandLineParsing.extractColourSubtreeRootBranch(args[5]);
						inputFilePath = args[6];
						outputFilePath = args[7];												
					}
					else
					{
						if (args.length == 9)
						{
							//The command line consists of
							//inputFormat, colours,  taxonomyFile, matchingColumn, colouringColumn, colouringNames, colourSubtreeRootBranch
							//inputFilePath and outputFilePath		
							inputFormat = CommandLineParsing.extractInputFormat(args[0]);
							objectsColourAssociations = computeObjectsColourAssociations(args[1]);
							taxonomyFilePath = CommandLineParsing.extractTaxonomyFile(args[2]);
							matchingColumn = CommandLineParsing.extractMatchingColumn(args[3]);
							colouringColumn = CommandLineParsing.extractColouringColumn(args[4]);
							colouringNames = CommandLineParsing.extractColouringNames(args[5]);
							colourSubtreeRootBranch = CommandLineParsing.extractColourSubtreeRootBranch(args[6]);
							inputFilePath = args[7];
							outputFilePath = args[8];															
						}
						else
						{
							throw new CommandLineParsingException("Incorrect number of arguments");
						}
					}
				}
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
		toolUsageText.append("Usage: \"NHtoWalrus  [-options] <nh, nhx or nexus tree file name> <walrus tree file name>\"" + newLine);
		toolUsageText.append("" + newLine);		
		toolUsageText.append("Options (which must appear in the following order):" + newLine);
		toolUsageText.append("--inputFormat=NH, NHX or NEXUS (obligatory)" + newLine);		
		toolUsageText.append("--colours=node,colour+node,colour+node,colour (optional)" + newLine);
		toolUsageText.append("\twhere node is the sequence name of one of the nodes in the input file" + newLine);
		toolUsageText.append("\twhere colour is the hex representation of an RGB code" + newLine);
		toolUsageText.append("--taxonomyFile=fileName (optional)" + newLine);
		toolUsageText.append("\t where fileName is the path to the file containing the taxonomy" + newLine);
		toolUsageText.append("--matchingColumn=digit (obligatory if taxonomyFile option used)" + newLine);
		toolUsageText.append("\t where digit is the number of the column to be used for matching (first column is 1)" + newLine);
		toolUsageText.append("--colouringColumn=digit (obligatory if taxonomyFile option used)" + newLine);
		toolUsageText.append("\t where digit is the number of the column to be used for colouring (first column is 1)" + newLine);
		toolUsageText.append("--colouringNames=colouringString,colour+colouringString,colour (optional)" + newLine);
		toolUsageText.append("\t where colouringString is a string from the colouring column" + newLine);
		toolUsageText.append("\twhere colour is the hex representation of an RGB code" + newLine);		
		toolUsageText.append("--colourSubtreeRootBranch=Y or N (default is N, obligatory if colouring either specific " +
		"nodes or colouring using a taxonomy (or both)" + newLine);		
		toolUsageText.append("" + newLine);
		return toolUsageText.toString();				
	}

	
	/**
	 * Modifies the colouring data contained in the objectsColourAssociations by searching the Walrus 
	 * tree for nodes with matching sequence name. This NodeDataWalrus nodes then replace the 
	 * Strings that contained the sequence names. Note that there may be several NodeDataWalrus 
	 * nodes that have the same sequence name
	 * @param treeWalrus TreeWalrus to be searched
	 * @param objectsColourAssociations Vector of ObjectsColourAssociations that contain associations
	 * between Strings containing sequence names and colours
	 */
	private void modifyColouringData(TreeWalrus treeWalrus, Vector objectsColourAssociations)
	{
		Vector allNodes = treeWalrus.getAllNodes();
		Iterator iter1 = objectsColourAssociations.iterator();
		while (iter1.hasNext())
		{
			ObjectsColourAssociation objectsColourAssociation = (ObjectsColourAssociation) iter1.next();
			String nodeSeqName = (String)objectsColourAssociation.getFirstObject();
			
			Iterator iter2 = allNodes.iterator();
			Vector locatedNodes = new Vector();
			//Note that we look for all nots with matching sequence name
			while (iter2.hasNext())
			{
				NodeDataWalrus ndw = (NodeDataWalrus) iter2.next();
				NodeDataNH ndnh = (NodeDataNH)ndw.getNodeData();
				if (nodeSeqName.equals(ndnh.getSequenceName()))
				{
					locatedNodes.add(ndw);
				}
			}
			
			if(locatedNodes.size() == 0)
			{
				UserInfo.println("The node " + nodeSeqName + " cannot be located in the input file.");//Inform user
				iter1.remove();//Remove this objectsColourAssociation since cannot be located
			}
			else
			{
				//add the located nodes (there may be more than one node with the same sequence name)
				objectsColourAssociation.setObjects(locatedNodes);
			}	
		}	
	}

	/**
	 * Carries out two distinct functionalities connected to the colouring of a Walrus tree.
	 * First, runs through all the walrus nodes trying to give the nodes a full lineage and thus a colour
	 * Then attempts to colour the nodes according to their position in the taxonomy. This involves
	 * updating the taxonomyObjectsColourAssociations by removing unused Associations (this is necessary
	 * so that the legend does not contain irrelevant information) and creating the taxonomyColouredWalrusNodes
	 * @param treeWalrus TreeWalrus to be coloured
	 * @param taxonomyObjectsColourAssociations Vector of ObjectsColourAssociations where the first 
	 * object is the matching string, the second object is the colouring string and the third object is the full
	 * lineage string
	 * @param namesAndColours is a Vector of ObjectsColourAssociation where the object is a string
	 * containing a colouring string which should match with a colouring string from the taxonomy
	 * @return Vector of ObjectsColourAssociations where the object is a Vector of NodeDataWalrus from the TreeWalrus
	 */
	private Vector produceColouringDataFromTaxonomy(TreeWalrus treeWalrus, Vector taxonomyObjectsColourAssociations,
																																		Vector namesAndColours)
	{
		//Attempt to assign classification to all external walrus nodes
		//Inform user if node cannot be assigned classification
		//remove any unused taxonomyObjectsColourAssociations
		Hashtable colouringStringIndexedWalrusNodes = buildColouringStringIndexedWalrusNodes(treeWalrus,
		taxonomyObjectsColourAssociations);
		
		//Associate a colour with each set of Walrus nodes WalrusNodes
		Vector taxonomyColouredWalrusNodes = new Vector();
		//Also need to update the taxonomyObjectsColourAssociations
		//i.e.associate colours with the colouringString and remove any Associations that do not get a colour
		if (namesAndColours.size() != 0)
		{
				Iterator iterNamesAndColours = namesAndColours.iterator();
				//Keep track of the taxonomyObjectsColourAssociations that are used, so that can remove unused
				Vector taxonomyObjectsColourAssociationsWithColour = new Vector();
				while (iterNamesAndColours.hasNext())
				{
					ObjectsColourAssociation ocaFromCommandLine = ((ObjectsColourAssociation) iterNamesAndColours.next());
					String nameFromCommandLine = (String)ocaFromCommandLine.getFirstObject();
					Color colourFromCommandLine = (Color)ocaFromCommandLine.getColour();
					Vector walrusNodes = (Vector)colouringStringIndexedWalrusNodes.get(nameFromCommandLine);
					if (walrusNodes == null)
					{
						UserInfo.println("No external walrus nodes matching the colouring string (classification) " + nameFromCommandLine);
						//iterNamesAndColours.remove();
					}
					else
					{
						//Add to the taxonomyColouredWalrusNodes
						ObjectsColourAssociation oca = new ObjectsColourAssociation();
						oca.setObjects(walrusNodes);
						oca.setColour(colourFromCommandLine);
						taxonomyColouredWalrusNodes.add(oca);
						
						//Update the taxonomyObjectsColourAssociations with this colour
						Iterator iter2 = taxonomyObjectsColourAssociations.iterator();
						while (iter2.hasNext())
						{
							ObjectsColourAssociation taxonomyObjectColourAssociation = (ObjectsColourAssociation) iter2.next();
							if (taxonomyObjectColourAssociation.getSecondObject().equals(nameFromCommandLine))
							{
								taxonomyObjectColourAssociation.setColour(colourFromCommandLine);
								taxonomyObjectsColourAssociationsWithColour.add(taxonomyObjectColourAssociation);
							}
						}
					}					
				}
				//Update the taxonomyObjectsColourAssociation
				taxonomyObjectsColourAssociations.retainAll(taxonomyObjectsColourAssociationsWithColour);
		}
		else	//determine the colouring based on the number of keys in the colouringStringIndexedWalrusNodes
		{
			Vector colours = ColourUtilities.determineColours(colouringStringIndexedWalrusNodes.size(), true);
			Hashtable colouringStringsAndColours = new Hashtable();
			
			//create correspondence between the colours and the strings
			Iterator iter4 = colours.iterator();
			Enumeration enum1 = colouringStringIndexedWalrusNodes.keys();
			while (iter4.hasNext() && enum1.hasMoreElements())
			{
				Color colour = (Color) iter4.next();
				String colouringString = (String)  enum1.nextElement();
				colouringStringsAndColours.put(colouringString, colour);
			}
			
			//Update the objectColoursAssociations
			Enumeration enum2 = colouringStringsAndColours.keys();
			Vector taxonomyObjectsColourAssociationsWithColour = new Vector();
			while (enum2.hasMoreElements())
			{
				String colouringString = (String) enum2.nextElement();
				Vector walrusNodes = (Vector)colouringStringIndexedWalrusNodes.get(colouringString);
				ObjectsColourAssociation oca = new ObjectsColourAssociation();
				oca.setColour((Color)colouringStringsAndColours.get(colouringString));
				oca.setObjects(walrusNodes);
				taxonomyColouredWalrusNodes.add(oca);
				Iterator iter6 = taxonomyObjectsColourAssociations.iterator();
				while (iter6.hasNext())
				{
					ObjectsColourAssociation taxonomyOCA = (ObjectsColourAssociation) iter6.next();
					String tempColouringString = (String)taxonomyOCA.getSecondObject(); 
					if (tempColouringString.equals(colouringString))
					{
						taxonomyOCA.setColour((Color) colouringStringsAndColours.get(colouringString));
						taxonomyObjectsColourAssociationsWithColour.add(taxonomyOCA);
					}
				}
			}
			taxonomyObjectsColourAssociations.retainAll(taxonomyObjectsColourAssociationsWithColour);			
		}
		return taxonomyColouredWalrusNodes;
	}
	
	/**
	 * Builds a hashtable that establishes a correspondence between the colouring strings from the taxonomy
	 * and WalrusNodes that are associated with this colouring string because their sequence name contains
	 * the matching string
	 * @param treeWalrus TreeWalrus whose external nodes a searched for matching strings
	 * @param taxonomyObjectsColourAssociations Vector of ObjectsColourAssociations that result from 
	 * the parsing of the taxonomy file
	 * @return Hashtable where the keys are colouring strings and the values are Vectors of NodeDataWalrus
	 */
	private Hashtable buildColouringStringIndexedWalrusNodes(TreeWalrus treeWalrus,
																							 Vector taxonomyObjectsColourAssociations)
	{
		//For each matchingName/colouringName determine the Vector of NodeDataWalrus that it matches
		Hashtable colouringStringIndexedWalrusNodes = new Hashtable();
		Vector allExternalNodes = treeWalrus.getAllExternalNodes();
		Vector unusedExternalNodes = new Vector(allExternalNodes);
		
		Iterator iter1 = taxonomyObjectsColourAssociations.iterator();
		while (iter1.hasNext())
		{
			ObjectsColourAssociation taxonomyObjectsColourAssociation = (ObjectsColourAssociation) iter1.next();
			String matchingString = (String)taxonomyObjectsColourAssociation.getFirstObject();
			String colouringString = (String)taxonomyObjectsColourAssociation.getSecondObject();
			
			Iterator iter2 = allExternalNodes.iterator();
			Vector locatedNodes = new Vector();
			//Note that we look for external nodes that have a matching sequence name
			while (iter2.hasNext())
			{
				NodeDataWalrus ndw = (NodeDataWalrus) iter2.next();
				NodeDataNH ndnh = (NodeDataNH)ndw.getNodeData();
				if (ndnh.getSequenceName() != null && match(ndnh.getSequenceName(), matchingString) && 
				unusedExternalNodes.contains(ndw))
				{
					ndw.setFullLineageText((String)taxonomyObjectsColourAssociation.getThirdObject());//Set the full lineage of the ndw
					ndw.setTaxonomicClassification((String)taxonomyObjectsColourAssociation.getSecondObject());
					locatedNodes.add(ndw);
					//Ensures that a node is allocated a colouringString only once
					unusedExternalNodes.remove(ndw);//Need to avoid a node matching several taxonomy records
				}
			}
			
			if(locatedNodes.size() == 0)
			{
				//No need to include this in the legend since there are no matching nodes in the walrus tree
				iter1.remove();//Remove this objectsColourAssociation since cannot be located, and need not appear
										//in the legend
			}
			else
			{
				//add the located nodes (there may be more than one node with the same sequence name)
				Vector walrusNodes = (Vector) colouringStringIndexedWalrusNodes.get(colouringString);
				if (walrusNodes == null)
				{
					colouringStringIndexedWalrusNodes.put(colouringString, locatedNodes);
				}
				else
				{
					walrusNodes.addAll(locatedNodes);
				}				
			}	
		}
		
		//Inform user of nodes that do not have a taxonomy classification
		Iterator iterUnusedNodes = unusedExternalNodes.iterator();
		while (iterUnusedNodes.hasNext())
		{
			NodeDataWalrus ndw = (NodeDataWalrus) iterUnusedNodes.next();
			NodeDataNH ndnh = (NodeDataNH)ndw.getNodeData(); 
			UserInfo.println("Unable to taxonomically classify node with sequence name: " + ndnh.getSequenceName());
		}
		
		return colouringStringIndexedWalrusNodes;	
	}	
	
	/*Old match function that confused users
	public static boolean match(String sequenceName, String matchingString)
	{
		boolean match = false;
		StringTokenizer tokenizer = new StringTokenizer(sequenceName, " _");
		while (tokenizer.hasMoreTokens())
		{
			String token = (String) tokenizer.nextToken();
			if (token.equals(matchingString))
			{
				match = true;
			}	
		}
		return match;
	}
	*/
	
	public static boolean match(String sequenceName, String matchingString)
	{		
		return sequenceName.equals(matchingString);
	}
	
	public static void test()
	{
		
		match("one two_three", "two");
	}
	
	protected String textToUseInLegend(NodeData nd)
	{
		NodeDataNH ndnh = (NodeDataNH)nd;
		return ndnh.getSequenceName();
	}
}