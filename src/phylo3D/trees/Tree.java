/*
 * Created: 22-Aug-2003
 */
package phylo3D.trees;

import java.util.*;
import java.io.*;

/**
 * Class representing a Tree implemented using a Hashtable.
 * 
 * The keys to the hashtable are the parentTaxID, the values of the hashtable are vectors of siblingGroups
 * that have as their parent node the node with parentTaxID.  
 * 
 * This implementation of the tree is an improvement on the vector implementation but still stays close to
 * the way the tree is represented in the NCBI databases and the way the tree is represented in the Walrus format.
 * The speed of tree manipulations could be further increased if the tree was implemented using a proper
 *  tree datastructure but this kind of data structure suffers from serialization problems (it is a known problem
 * that it is not possible to serialize tree structures beyond a certain size without getting a Stack Overflow error).
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public abstract class Tree implements Serializable
{
	//Vector of nodes that make up the tree
	protected NodeData root = null;
	protected Hashtable siblingGroups = new Hashtable();

	//Old methods with same signatures but modified to use the new implementation of the tree
	/**
	 * Determines which of the siblingGroups in the Vector of siblingGroups is the root node.
	 * The convention is that the node that is the root node has itself as its parent node.
	 * @return NodeData the root node of this Tree
	 */
	public NodeData getRoot()
	{
		return root;
	}
	
	protected void buildTree(Tree oldTree)
	{
		//Handle root
		this.root = instantiateNode((NodeData)oldTree.getRoot());
		//Handle sibling groups
		Hashtable siblingGroups = oldTree.getSiblingGroups();
		Enumeration enum = siblingGroups.keys();
		while (enum.hasMoreElements())
		{
			Integer  key = (Integer) enum.nextElement();
			Vector oldSiblingGroup = (Vector)siblingGroups.get(key);
			Vector newSiblingGroup = new Vector();
			Iterator iter = oldSiblingGroup.iterator();
			while (iter.hasNext())
			{
				NodeData nd = (NodeData) iter.next();
				newSiblingGroup.add(instantiateNode(nd));
			}
			this.siblingGroups.put(key, newSiblingGroup);
		}	
	}
	
	protected abstract NodeData instantiateNode(Object n);

	/**
	 * Prunes the tree by removing the subtree rooted at the specified node.
	 * @param ndn NodeData node specifying the root of the subtree to be removed
	 */
	public void removeSubtree(NodeData ndn)
	{
		Hashtable allDescendentSiblingGroups = getAllDescendentSiblingGroups(ndn.getID());
		Enumeration enum = allDescendentSiblingGroups.keys();
		while (enum.hasMoreElements())
		{
			Integer key = (Integer) enum.nextElement();
			siblingGroups.remove(key);
		}
	}
	
	/**
	 * Narrows the tree to the subtree rooted at the specified node
	 * @param ndn NodeData node which is the root of the subtree to be retained
	 */
	public void retainOnlySubtree(NodeData ndn)
	{
		//Locate all sibling groups to be retained
		Hashtable allDescendentSiblingGroups = getAllDescendentSiblingGroups(ndn.getID());
		//Locate the new root node and update the root reference
		Vector allNodes = getAllNodes();
		int index = allNodes.indexOf(ndn);
		root = (NodeData)allNodes.get(index);
		root.setParentID(root.getID());//Important to reset the parentTaxID of the root
		//Update the siblingGroups
		siblingGroups = allDescendentSiblingGroups;
	}
	
	/**
	 * Gets the subtree rooted at the specified node. The subtree means all the siblingGroups
	 * that are descendent of the specified node and the specified node itself.
	 * @param nd NodeData which is the root of the subtree
	 * @return Vector of NodeData representing the subtee.
	 */
	public Vector getSubtree(NodeData nd)
	{
		Vector subtreeNodes = new Vector();
		Hashtable allDescendentSiblingGroups = getAllDescendentSiblingGroups(nd.getID());
		subtreeNodes.addAll(convertSiblingGroupsToNodes(allDescendentSiblingGroups));
		//Also need to include the root of the subtree
		Vector allNodes = getAllNodes();
		int index = allNodes.indexOf(nd);
		subtreeNodes.add(allNodes.get(index));
		return subtreeNodes;
	}

	public Vector getAllDescendentNodes(NodeData ndn)
	{
		Hashtable descendentSiblingGroups = getAllDescendentSiblingGroups(ndn.getID());
		return convertSiblingGroupsToNodes(descendentSiblingGroups);
	}

	public Vector getAllChildrenNodes(NodeData ndn)
	{
		Vector children = getSiblingGroup(ndn.getID());
		return children;		
	}

	public Vector getAllNodes()
	{
		Vector result = new Vector();
		result.add(root);
		result.addAll(convertSiblingGroupsToNodes(siblingGroups));
		return result;
	}
	
	public Vector getAllExternalNodes()
	{
		Vector allNodes = getAllNodes();
		Vector allParentIDs = new Vector();
		Iterator iter1 = allNodes.iterator();
		while (iter1.hasNext())
		{
			NodeData nd = (NodeData) iter1.next();
			if (nd.getParentID() != null)
			{
				allParentIDs.add(nd.getParentID());
			}
		}
		
		Vector allExternalNodes = new Vector();
		Iterator iter2 = allNodes.iterator();
		while (iter2.hasNext())
		{
			NodeData nd = (NodeData) iter2.next();
			if(!allParentIDs.contains(nd.getID()))
			{
				allExternalNodes.add(nd);
			}
		}
		return allExternalNodes;
	}


	//New methods relating to the new implementation 
	/**
	 * Gets all descendents siblingGroups of the specified node
	 * @param ID Integer represening node for which descendents are to be found
	 * @return Vector of NodeData siblingGroups which are descendents of
	 * specified siblingGroups.
	 */
	private Hashtable getAllDescendentSiblingGroups(Integer ID)
	{
		Vector query = new Vector();
		
		query.add(ID);
		return getAllDescendentSiblingGroups(query);
	}

	/**
	 * Gets all descendents of the specified siblingGroups. If some of the specified siblingGroups are
	 * descendents of specified siblingGroups then the sets of their descendents will overlap and 
	 * these overlapping siblingGroups will be present twice in the Vector return by this method.
	 * @param parentIDs Vector of Integers for which descendents are to be found
	 * @return Vector of NodeDatas which are the descendents of the specified siblingGroups
	 */
	private Hashtable getAllDescendentSiblingGroups(Vector parentIDs)
	{
		Hashtable descendentSiblingGroups = getMultipleSiblingGroups(parentIDs);

		if (descendentSiblingGroups.size() == 0)
		{
			return new Hashtable();	
		}
		else
		{
			descendentSiblingGroups.putAll(getAllDescendentSiblingGroups(buildVectorOfTaxIDs(descendentSiblingGroups)));
			return descendentSiblingGroups;
		}
	}
	
	protected Vector buildVectorOfTaxIDs(Hashtable siblingGroups)
	{
		Vector vectorOfIDs = new Vector();		
		Enumeration enum = siblingGroups.elements();
		while (enum.hasMoreElements())
		{
			Vector siblingGroup = (Vector) enum.nextElement();
			vectorOfIDs.addAll(buildVectorOfIDs(siblingGroup));
		}
		return vectorOfIDs;
	}
	
	protected Vector buildVectorOfIDs(Vector siblingGroup)
	{
		Vector vectorOfIDs = new Vector();		
		Iterator iterator = siblingGroup.iterator();
		while (iterator.hasNext())
		{
			NodeData ndn = (NodeData) iterator.next();
			vectorOfIDs.add(ndn.getID());		
		}
		return vectorOfIDs;	
	}
	
	/**
	 * Gets all children siblingGroups for a Vector of Nodes.
	 * @param parentIDs Vector of Integers (parent's taxID)
	 * @return Vector of all children siblingGroups
	 */
	protected Hashtable getMultipleSiblingGroups(Vector parentIDs)
	{
		Hashtable result = new Hashtable();
				
		Iterator iter = parentIDs.iterator();
		while (iter.hasNext())
		{
			Integer parentID = (Integer) iter.next();
			Vector siblingGroup = getSiblingGroup(parentID);
			if (siblingGroup != null)
			{
				result.put(parentID, siblingGroup);	
			}		
		}
		return result;
	}
	
	/**
	 * Gets all children siblingGroups of the specified node i.e. all siblingGroups that have as their
	 * parent the node the specified node
	 * @param parentID Integer representing node for which children to be found
	 * @return Vector of all children siblingGroups
	 */
	protected Vector getSiblingGroup(Integer parentID)
	{
		return (Vector)siblingGroups.get(parentID);
	}

	public Hashtable getSiblingGroups()
	{
		return siblingGroups;
	}

	private Vector convertSiblingGroupsToNodes(Hashtable siblingGroups)
	{
		Vector result = new Vector();
		Enumeration enum = siblingGroups.elements();
		while (enum.hasMoreElements())
		{
			Vector vector  = (Vector) enum.nextElement();
			result.addAll(vector);
		}
		return result;	
	}

}
