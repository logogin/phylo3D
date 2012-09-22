/*
 * Created: 22-Aug-2003
 */
package phylo3D.trees;

import java.util.*;
import java.io.*;
import java.net.*;

import phylo3D.datafiles.*;


import phylo3D.parser.ncbi.*;
import phylo3D.utilities.*;


/**
 * Class is data structure for the NCBI taxonomy information
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TreeNCBI extends Tree implements Serializable
{
		
	/**
	 * Constructor that instantiates a TreeNCBI from the records from a Nodes flat file 
	 * and a Names flat file. 
	 * @param recordsNodes Records from a Nodes flat file
	 * @param recordsNames Records from a Names flat file
	 */
	public TreeNCBI(Vector recordsNodes, Vector recordsNames)
	{
		UserInfo.print("Started instantiating TreeNCBI");
		Iterator iter = recordsNodes.iterator();
		while (iter.hasNext())
		{
			RecordNodes recordNodes= (RecordNodes) iter.next();
			RecordNames recordNames = new RecordNames();
			recordNames.setTaxID(recordNodes.getTaxID());
			int index = recordsNames.indexOf(recordNames);
			recordNames = (RecordNames)recordsNames.get(index);
			recordsNames.remove(index);
			
			//Create a NodeDataNCBI
			NodeDataNCBI nd = new NodeDataNCBI(recordNodes.getTaxID(), recordNodes.getParentTaxID());
			nd.setTaxonomyID(recordNodes.getTaxID());
			nd.setParentTaxonomyID(recordNodes.getParentTaxID());
			nd.setRank(recordNodes.getRank());
			nd.setNameText(recordNames.getNameText());
			nd.setNameClass(recordNames.getNameClass());
			
			//Add node to tree
			if (nd.getID().equals(nd.getParentID()))
			{
				root = nd;
			}
			else
			{
				Vector siblings =  (Vector)siblingGroups.get(nd.getParentID());
				if (siblings != null)
				{
					siblings.add(nd);
				}
				else
				{
					Vector siblingGroup = new Vector();
					siblingGroup.add(nd);
					siblingGroups.put(nd.getParentID(), siblingGroup);
				}
			}
		}
		UserInfo.println(" - Finished");
		UserInfo.println("");
	}
	
	/**
	 * Constructor that instantiates a TreeNCBI from the full file path to a siblingGroups and names flat file
	 * @param nodesFileURL String path to siblingGroups file
	 * @param namesFileURL String path to the names file
	 * @throws Exception
	 */
	public TreeNCBI(URL nodesFileURL, URL namesFileURL) throws FatalException
	{
		this(loadNodes(nodesFileURL), loadNames(namesFileURL));
	}
	
	protected NodeData instantiateNode(Object o)
	{
		NodeData nd = (NodeData) o;
		return new NodeDataNCBI(nd.getID(), nd.getParentID());
	}
	
	
	/**
	 * Parses the records in a names flat file 
	 * @param namesFileURL String full path to the names flat file
	 * @return Vector of RecordNames
	 * @throws Exception
	 */
	private static Vector loadNames(URL namesFileURL)
	throws FatalException
	{
		UserInfo.print("Started parsing names from: " + namesFileURL);
		TableParserNames tpnames = null;
		
		tpnames = new TableParserNames(namesFileURL);
		
		System.gc();
		UserInfo.println(" - Finished");	
		return tpnames.getRecords();
	}
	
	/**
	 * Parses the records in a siblingGroups flat file 
	 * @param nodesFileURL String full path to the siblingGroups flat file
	 * @throws Exception
	 */	
	private static Vector loadNodes(URL nodesFileURL) 
	throws FatalException
	{
		UserInfo.print("Started parsing nodes from: " + nodesFileURL);
		TableParserNodes tpnodes = null;

		tpnodes = new TableParserNodes(nodesFileURL);	

		System.gc();
		UserInfo.println(" - Finished");			
		return tpnodes.getRecords();
	}
	
	
	public void saveToFile()
	throws FatalException
	{
		FileOperations.serializeObjectToFile(DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED), this);
	}
	
	//Most likely not in use 
	/*
	public void produceFullLineages()
	{
		Vector allNodes1 = this.getAllNodes();
		Vector allNodes2 = this.getAllNodes();
		Iterator iter1 = allNodes1.iterator();
		int count = 1;
		while (iter1.hasNext())
		{
			System.out.println(count);
			count++;
			NodeDataNCBI ndn = (NodeDataNCBI) iter1.next();
			StringBuffer fullLineageText = new StringBuffer(); 
			StringBuffer fullLineageID = new StringBuffer();
			Integer ID = ndn.getID();
			Integer parentID = ndn.getParentID();
			while(!ID.equals(parentID))
			{
				Iterator iter2 = allNodes2.iterator();
				while (iter2.hasNext())
				{
					NodeDataNCBI ndncbi = (NodeDataNCBI) iter2.next();
					if (ndncbi.getID().equals(parentID))
					{
						ID = ndncbi.getID();
						parentID = ndncbi.getParentID();
						fullLineageText.insert(0, ndncbi.getRank() + "=" + ndncbi.getNameText() + ",");
						fullLineageID.insert(0, ndncbi.getRank() + "=" + ndncbi.getTaxonomyID() + ",");
						System.out.println(fullLineageText);
						break;
					}
				}
			}
			//Removing the last colon
			//ndn.setLineageText(fullLineageText.substring(0,fullLineageText.length()-1));
			//ndn.setLineageTaxID(fullLineageID.substring(0,fullLineageID.length()-1));		
		}
	}*/
	
	public static TreeNCBI loadFromFile()
	throws FatalException
	{
		return (TreeNCBI)FileOperations.loadFromFile(DataFileNames.fileURL(DataFileNames.REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED));
	}
	
	/**
	 * Sets up a test tree: tree with 10 siblingGroups that can be manipulated for test purposes.
	 * @return TreeNCBI
	 */
	public static TreeNCBI setupTestTree()
	{
		Vector recordsNames = new Vector();
		Vector recordsNodes = new Vector();

		for (int i = 0; i < 10; i++)
		{
			Integer integ = new Integer(i);
			RecordNames recordNames = new RecordNames();	
			recordNames.setTaxID(integ);
			recordNames.setNameText("bird" + i);
			recordNames.setNameClass("scientific");
			recordsNames.add(recordNames);
			
			RecordNodes recordNodes = new RecordNodes();
			recordNodes.setTaxID(integ);
			recordNodes.setRank("species");
			recordsNodes.add(recordNodes);
		}
		
		//Set up structure before passing to constructor
		((RecordNodes)recordsNodes.get(0)).setParentTaxID(new Integer(0));
		((RecordNodes)recordsNodes.get(1)).setParentTaxID(new Integer(0));
		((RecordNodes)recordsNodes.get(2)).setParentTaxID(new Integer(0));
		((RecordNodes)recordsNodes.get(3)).setParentTaxID(new Integer(1));
		((RecordNodes)recordsNodes.get(4)).setParentTaxID(new Integer(1));
		((RecordNodes)recordsNodes.get(5)).setParentTaxID(new Integer(2));
		((RecordNodes)recordsNodes.get(6)).setParentTaxID(new Integer(2));
		((RecordNodes)recordsNodes.get(7)).setParentTaxID(new Integer(3));
		((RecordNodes)recordsNodes.get(8)).setParentTaxID(new Integer(3));
		((RecordNodes)recordsNodes.get(9)).setParentTaxID(new Integer(3));
				
		TreeNCBI treeNCBI = new TreeNCBI(recordsNodes, recordsNames);
			
		return treeNCBI;	
	}

	 public static void main (String[] args)
	 throws FatalException
	 {
	 	Debug.println(TreeNCBI.loadFromFile().getRoot().toString());
	 }
}
