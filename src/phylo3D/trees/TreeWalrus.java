/*
 * Created: 22-Aug-2003
 */
package phylo3D.trees;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.awt.*;

import phylo3D.utilities.*;

/**
 * Class implementing functionality for printing out a LibSea format representation of TreeNCBI
 * so that the TreeNCBI can be visualised using the hyperbolic tree visualisation program Walrus.
 * Most of the methods in this class are related to producing the LibSea format with the data supplied
 * by a TreeNCBI object. For details of the LibSea format see the Walrus website.
 *
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TreeWalrus extends Tree
{
    //Holds all nodes to avoid to avoid making repeated costly calls to getAllNodes()
    private Vector allNodes = null;
    private int nodesBaseColor = produceLibseaColourCoding(new Color(255,0,0));
    boolean fullLineageAssigned = false;

    public TreeWalrus(Tree tree)
    {
        buildTree(tree);
    }

    protected NodeData instantiateNode(Object o)
    {
        return new NodeDataWalrus((NodeData)o);
    }

    /**
     * Computes the walrusIDs for the all siblingGroups in this tree. These IDs are necessary because of the
     * particularities of the LibSea format. A call to this method is made just before creating a String representation
     * of the Walrus tree.
     */
    private void computeWalrusIDs()
    {
        Iterator iter = allNodes.iterator();
        int count = 0;
        Hashtable idTowalrusID = new Hashtable();
        while (iter.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iter.next();
            //Set the walrus IDs using count and store correspondence between IDs in hashtable
            ndw.setWalrusID(new Integer(count));
            idTowalrusID.put(ndw.getID(), ndw.getWalrusID());
            //Update count for numbering
            count++;
        }

        //Setting all the walrusParentIDs
        Iterator iterator = allNodes.iterator();
        while (iterator.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator.next();
            Integer parentID = ndw.getParentID();
            Integer walrusParentID = ((Integer)idTowalrusID.get(parentID));
            ndw.setWalrusParentID(walrusParentID);
        }
    }

    /**
     * Produces a String representation of this class which is the LibSea format representation of a tree.
     * This method makes a call to the the computeWalrusIDs() method.
     * @see java.lang.Object#toString()
     */
    public String stringRepresentation()
    throws FatalException
    {
        //Compute the walrus IDs before producing string
        allNodes = getAllNodes();
        computeWalrusIDs();
        StringBuffer result = new StringBuffer();
        result.append("Graph" + "\n");
        result.append("{" + "\n");
        result.append(tabOut(metadata("NCBI Tree of Life", "NCBI Tree of Life in LibSea format", allNodes.size(), allNodes.size()-1, 0, 0),1));
        result.append("\n");
        result.append(tabOut(structuraldata(), 1));
        result.append("\n");
        result.append(tabOut(attributedata(), 1));
        result.append("\n");
        result.append(tabOut(visualizationHints(), 1));
        result.append("\n");
        result.append(tabOut(interfaceHints(), 1));
        result.append("}");
        return result.toString();
    }

    public void print(IndentPrintWriter pw) throws FatalException {
        //Compute the walrus IDs before producing string
        allNodes = getAllNodes();
        computeWalrusIDs();
        pw.println("Graph");
        pw.println("{");
        pw.println(tabOut(metadata("NCBI Tree of Life", "NCBI Tree of Life in LibSea format", allNodes.size(), allNodes.size()-1, 0, 0),1));
        pw.println(tabOut(structuraldata(), 1));
        new PrintFragment() {
            @Override
            public void fragment(IndentPrintWriter pw) {
                try {
                    printAttributedata(pw);
                } catch (FatalException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }.printIndented(pw);
        pw.println(tabOut(visualizationHints(), 1));
        pw.println(tabOut(interfaceHints(), 1));
        pw.println("}");
    }

    /**
     * Tabs out a piece of text by inserting \t a specified number of times
     *  at the begin of each new line.
     * @param text String to be tabed out
     * @param numberOfTabs int representing the number of tabs to insert
     * @return String the tabbed out text
     */
    private static String tabOut(String text, int numberOfTabs) {
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < numberOfTabs; i++) {
            temp.append("\t");
        }
        String[] lines = text.split("\n");
        //lines[0] = temp + lines[0];
        StringBuffer result = new StringBuffer();
        for ( int i = 0; i < lines.length; i++ ) {
            if ( lines[i].length() > 0 ) {
                result.append(temp);
            }
            result.append(lines[i]);
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Produces the metadata part of the LibSea file.
     * @param name String name of tree
     * @param description String description of the tree
     * @param numNodes int number of siblingGroups in the tree
     * @param numLinks int number of links in the tree
     * @param numPaths int number of paths in the tree (not used by Walrus)
     * @param numPathLinks int number of path links (not used by Walrus)
     * @return String holding metadata
     */
    private String metadata(String name, String description, int numNodes, int numLinks, int numPaths, int numPathLinks)
    {
        StringBuffer result = new StringBuffer();
        result.append("### metadata ###\n");
        result.append("@name=\"" + name + "\";\n");
        result.append("@description=\"" + description + "\";\n");
        result.append("@numNodes=" + numNodes + ";\n");
        result.append("@numLinks=" + numLinks + ";\n");
        result.append("@numPaths=" + numPaths + ";\n");
        result.append("@numPathLinks=" + numPathLinks + ";\n");
        return result.toString();
    }

    /**
     * Produces the structural data part of the LibSea file id est the list of links that the tree consists of (the siblingGroups are
     * not explicitly represented in the LibSea format).
     * @return String holding structural data
     */
    private String structuraldata()
    {
        StringBuffer result = new StringBuffer();
        result.append("### structural data ###\n");
        result.append("@links=\n");
        result.append("[\n");

        Iterator iterator = allNodes.iterator();
        StringBuffer links = new StringBuffer();
        while (iterator.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator.next();
            if (ndw.getWalrusID() != ndw.getWalrusParentID())//As long as this is not the root that has itself as parent
            {
                links.append("{ @source=" + ndw.getWalrusParentID() + "; @destination=" + ndw.getWalrusID() + "; } " );
                links.append(",\n");
            }
        }
        links.deleteCharAt(links.length()-2);
        result.append(tabOut(links.toString(), 1));

        result.append("];\n");
        result.append("@paths=;\n");
        return result.toString();
    }

    /**
     * Produces the attribute data section of the LibSea file. This includes spanning tree definition,
     * color attribute definition, node name definition and node rank definition.
     * @return String with attribute data
     */
    private String attributedata()
    throws FatalException
    {
        StringBuffer result = new StringBuffer();
        result.append("### attribute data ###\n");

        result.append("@enumerations=;\n");

        result.append("@attributeDefinitions=\n");
        result.append("[\n");
        result.append(tabOut(rootAttributeDefinition(), 1));
        result.append(tabOut(",\n", 1));
        result.append(tabOut(treeLinkAttributeDefinition(), 1));
        result.append(tabOut(",\n", 1));
        result.append(tabOut(subtreeColorAttributeDefinition(), 1));
        result.append(tabOut(",\n", 1));
        if (isFullLineageAssigned())
        {
            result.append(tabOut(taxClassAttributeDefinition(), 1));
            result.append(tabOut(",\n", 1));
        }
        result.append(tabOut(optionalAttributeDefinitions(), 1));
        result.append("];\n");//end of attribute definitions

        result.append("@qualifiers=\n");
        result.append("[\n");
        result.append(tabOut(qualifiersDefinition(), 1));
        result.append("];\n");//end of attribute definitions

        return result.toString();
    }

    private void printAttributedata(IndentPrintWriter pw) throws FatalException {
        pw.println("### attribute data ###");

        pw.println("@enumerations=;");

        pw.println("@attributeDefinitions=");
        pw.println("[");
        pw.print(tabOut(rootAttributeDefinition(), 1));
        pw.print(tabOut(",\n", 1));
        pw.print(tabOut(treeLinkAttributeDefinition(), 1));
        pw.print(tabOut(",\n", 1));
        pw.print(tabOut(subtreeColorAttributeDefinition(), 1));
        pw.print(tabOut(",\n", 1));
        if (isFullLineageAssigned())
        {
            pw.print(tabOut(taxClassAttributeDefinition(), 1));
            pw.print(tabOut(",\n", 1));
        }
        pw.print(tabOut(optionalAttributeDefinitions(), 1));
        pw.println("];");//end of attribute definitions

        pw.println("@qualifiers=");
        pw.println("[");
        pw.print(tabOut(qualifiersDefinition(), 1));
        pw.println("];");//end of attribute definitions
    }

    private String optionalAttributeDefinitions()
    throws FatalException
    {
        StringBuffer result = new StringBuffer();
        NodeData rootsNodeData =  ((NodeDataWalrus)root).getNodeData();
        if(rootsNodeData instanceof NodeDataNHX)
        {
            result.append(nhSequenceNameAttributeDefinition());
            result.append(",\n");
            result.append(nhBranchLengthToParentAttributeDefinition());
            result.append(",\n");
            result.append(nhxBootstrapValueAttributeDefinition());
            result.append(",\n");
            result.append(nhxSpeciesNameAttributeDefinition());
            result.append(",\n");
            result.append(nhxTaxonomyIDAttributeDefinition());
            result.append(",\n");
            result.append(nhxECNumberAttributeDefinition());
        }
        else
        {
            if (rootsNodeData instanceof NodeDataNH)
            {
                result.append(nhSequenceNameAttributeDefinition());
                result.append(",\n");
                result.append(nhBranchLengthToParentAttributeDefinition());
            }
            else
            {
                if (rootsNodeData instanceof NodeDataNCBI)
                {
                    result.append(ncbiNameAttributeDefinition());
                    result.append(",\n");
                    result.append(ncbiRankAttributeDefinition());
                    result.append(",\n");
                    result.append(ncbiTaxonomyIDAttributeDefinition());
                }
            }
        }
        return result.toString();
    }

    private String rootAttributeDefinition()
    {
        StringBuffer result = new StringBuffer();
        NodeDataWalrus root = (NodeDataWalrus)getRoot();
        result.append(attributeDefinition("$root", "bool", "|| false ||", "[ { @id=" +  root.getWalrusID() + "; @value=T; } ]", "",""));
        return result.toString();
    }

    private String treeLinkAttributeDefinition()
    {
        StringBuffer linkValues = new StringBuffer();

        //Number of links is record.size() - 1, linkIDs from 0 to record.size() - 2
        //But do not include the root
        int count = 0;
        Iterator iterator = allNodes.iterator();
        while (iterator.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator.next();
            if (ndw.getWalrusID() != ndw.getWalrusParentID())
            {
                linkValues.append("{ @id=" + (count) + "; @value=" + "T" + "; }");
                linkValues.append(",\n");
                count++;
            }
        }
        linkValues.deleteCharAt(linkValues.length() - 2);//Get rid of the last comma
        linkValues = new StringBuffer(tabOut(linkValues.toString(), 1));

        //Make call to attributeDefinition
        return attributeDefinition("$tree_link",  "bool", "|| false ||", "", putInSquareBrackets(linkValues.toString()), "");
    }


    private String subtreeColorAttributeDefinition()
    {
        StringBuffer linkValues = new StringBuffer();

        //Number of links is record.size() - 1, linkIDs from 0 to record.size() - 2
        //But do not include the root
        int count = 0;
        Iterator iterator1 = allNodes.iterator();
        while (iterator1.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator1.next();
            if (ndw.getWalrusID() != ndw.getWalrusParentID())
            {
                linkValues.append("{ @id=" + (count) + "; @value=" + ndw.getBranchColor() + "; }");
                linkValues.append(",\n");
                count++;
            }
        }
        linkValues.deleteCharAt(linkValues.length()-2);//Get rid of the last comma
        linkValues = new StringBuffer(tabOut(linkValues.toString(), 1));

        //Defining nodes color
        StringBuffer nodeValues = new StringBuffer();
        Iterator iterator2 = allNodes.iterator();
        while (iterator2.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator2.next();
            nodeValues.append("{ @id=" + ndw.getWalrusID() + "; @value=" + ndw.getNodeColor() + "; }");
            nodeValues.append(",\n");
        }
        nodeValues.deleteCharAt(nodeValues.length()-2);//Get rid of the last comma
        nodeValues = new StringBuffer(tabOut(nodeValues.toString(), 1));

        //Set default color to white
        return attributeDefinition("$subtree_color",  "int", "|| " + produceLibseaColourCoding(new Color(255, 255, 255)) + " ||", putInSquareBrackets(nodeValues.toString()),
             putInSquareBrackets(linkValues.toString()), "");
    }

    //full lineage output
    private String taxClassAttributeDefinition()
    {
        StringBuffer nodeValues = new StringBuffer();
        //Only attempt to assign lineage to external nodes
        Iterator iter = getAllExternalNodes().iterator();
        while (iter.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iter.next();
            String taxClass = ndw.getTaxonomicClassification();
            if (taxClass == null)
            {
                taxClass = "unknown";
            }
            nodeValues.append("{ @id=" + ndw.getWalrusID() + "; @value=\"" + "tax. classif.=" + taxClass + "\"; }");
            nodeValues.append(",\n");
        }
        nodeValues.deleteCharAt(nodeValues.length() - 2);
        nodeValues = new StringBuffer(tabOut(nodeValues.toString(), 1));
        return attributeDefinition("$tax_classif", "string", "|| \"tax.classif.=internal node\" ||", putInSquareBrackets(nodeValues.toString()), "", "" );
    }


    private String buildNodeValues(String methodDefiningNodeValue, String attributeName)
    throws ObjectIntrospectionException
    {
        StringBuffer nodeValues = new StringBuffer();

        Iterator iterator = allNodes.iterator();
        while (iterator.hasNext())
        {
            NodeDataWalrus ndw = (NodeDataWalrus) iterator.next();
            NodeData nd = ndw.getNodeData();

            Object o = null;;
            try
            {
                if (nd instanceof NodeDataNCBI)
                {
                    NodeDataNCBI ndncbi = (NodeDataNCBI) nd;
                    o = ndncbi.getClass().getMethod(methodDefiningNodeValue, null).invoke(ndncbi, null);
                }
                else
                {
                    if (nd instanceof NodeDataNHX)
                    {
                        NodeDataNHX ndnhx = (NodeDataNHX) nd;
                        Method meth = ndnhx.getClass().getMethod(methodDefiningNodeValue, null);
                        o = meth.invoke(ndnhx, null);
                    }
                    else
                    {
                        NodeDataNH ndnh = (NodeDataNH) nd;
                        o = ndnh.getClass().getMethod(methodDefiningNodeValue, null).invoke(ndnh, null);
                    }
                }
            } catch (Exception e)
            {
                throw new ObjectIntrospectionException("Unable to carry out introspection on object while trying to produce node value.");
            }

            String value = null;
            if (o != null)
            {
                value = o.toString();
            }
            else
            {
                value = "unknown";
            }

            nodeValues.append("{ @id=" + ndw.getWalrusID() + "; @value=\"" + attributeName + "=" + value + "\"; }");
            nodeValues.append(",\n");

        }

        nodeValues.deleteCharAt(nodeValues.length()-2);//Get rid of the last comma
        nodeValues = new StringBuffer(tabOut(nodeValues.toString(), 1));

        return nodeValues.toString();
    }

    private String putInSquareBrackets(String text)
    {
        StringBuffer result = new StringBuffer();
        result.append("[\n");
        result.append(text);
        result.append("]\n");
        return result.toString();
    }

    private String putInWobbleBrackets(String text)
    {
        StringBuffer result = new StringBuffer();
        result.append("{\n");
        result.append(text);
        result.append("}\n");
        return result.toString();
    }

    private String nhSequenceNameAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getSequenceName", "sequence name");
        return attributeDefinition("$sequence_name",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String nhBranchLengthToParentAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getBranchLengthToParent", "branch length to parent");
        return attributeDefinition("$branch_length_to_parent",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String ncbiTaxonomyIDAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getTaxonomyID", "taxonomy ID");
        return attributeDefinition("$taxonomy_id",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String ncbiNameAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getNameText", "name");
        return attributeDefinition("$name_text",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String ncbiRankAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getRank", "rank");
        return attributeDefinition("$rank_text",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String nhxBootstrapValueAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getBootstrapValue", "bootstrap value");
        return attributeDefinition("$bootstrap_value",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String nhxSpeciesNameAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getSpeciesName", "species name");
        return attributeDefinition("$species_name",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String nhxTaxonomyIDAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getTaxonomyID", "taxonomy ID");
        return attributeDefinition("$taxonomy_id",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String nhxECNumberAttributeDefinition()
    throws FatalException
    {
        String nodeValues = buildNodeValues("getEcNumber", "EC number");
        return attributeDefinition("$ec_number",  "string", "|| \"unknown\" ||", putInSquareBrackets(nodeValues.toString()), "", "");
    }

    private String attributeDefinition(String name, String type, String defolt, String nodeValues, String linkValues, String pathValues)
    {
        StringBuffer result = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        result.append("{\n");

        temp.append("@name=" + name + ";\n");
        temp.append("@type=" + type + ";\n");
        temp.append("@default=" + defolt + ";\n");
        temp.append("@nodeValues=" + nodeValues + ";\n");
        temp.append("@linkValues=" + linkValues + ";\n");
        temp.append("@pathValues=" + pathValues + ";\n");
        result.append(tabOut(temp.toString(), 1));

        result.append("}\n");
        return result.toString();
    }

    private String qualifiersDefinition()
    {
        StringBuffer result = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        result.append("{\n");

        temp.append("@type=$spanning_tree;\n");
        temp.append("@name=$sample_spanning_tree;\n");
        temp.append("@description=;\n");
        temp.append("@attributes=\n");
        temp.append("[\n");
        temp.append(tabOut("{@attribute=0;@alias=$root;},\n", 1));
        temp.append(tabOut("{@attribute=1;@alias=$tree_link;}\n", 1));
        temp.append("];\n");

        result.append(tabOut(temp.toString(), 1));
        result.append("}\n");
        return result.toString();
    }

    private String visualizationHints()
    {
        StringBuffer result = new StringBuffer();
        result.append("### visualization hints ###\n");
        result.append("@filters=;\n");
        result.append("@selectors=;\n");
        result.append("@displays=;\n");
        result.append("@presentations=;\n");
        return result.toString();
    }

    private String interfaceHints()
    {
        StringBuffer result = new StringBuffer();
        result.append("### interface hints ###\n");
        result.append("@presentationMenus=;\n");
        result.append("@displayMenus=;\n");
        result.append("@selectorMenus=;\n");
        result.append("@filterMenus=;\n");
        result.append("@attributeMenus=;\n");
        return result.toString();
    }

    /**
     * LibSea encodes coloring attribute values as int according to a formula implemented in this method
     * @param colour Color that needs to be encoded
     * @return int encoding the colour using LibSea's representation
     */
    public static int produceLibseaColourCoding(Color colour)
    {
        return ((colour.getRed()*256)+colour.getGreen())*256+colour.getBlue();
    }

    /**
     * Ensures that the color attribute value for all siblingGroups in the specified subtree have the specified value
     * @param nd NodeData node which is the root of the subtree to be coloured
     * @param colour Color to be used to colour the subtree
     * @param colourSubtreeRootBranch boolean indicating whether the to colour the branch leading
     * to the root of the subtree
     */
    public void colorSubtreeBranches(NodeData nd, Color colour, boolean colourSubtreeRootBranch)
    {
        if (colourSubtreeRootBranch)
        {
            ((NodeDataWalrus)nd).setBranchColor(produceLibseaColourCoding(colour));
        }
        Vector subtreeBranches = getAllDescendentNodes(nd);
        Iterator iterator = subtreeBranches.iterator();
        while (iterator.hasNext())
        {
            NodeDataWalrus subtreeBranch= (NodeDataWalrus) iterator.next();
            subtreeBranch.setBranchColor(produceLibseaColourCoding(colour));
        }
    }

    /**
     * Ensures that the color attribute value for all branches in the specified subtree have the specified value
     * @param nd NodeData node which is the root of the subtree to be coloured
     * @param colour Color to colour the subtree nodes with
     */
    public void colorSubtreeNodes(NodeData nd, Color colour)
    {
        Vector subtreeNodes = getSubtree(nd);
        Iterator iterator = subtreeNodes.iterator();
        while (iterator.hasNext())
        {
            NodeDataWalrus subtreeNode= (NodeDataWalrus) iterator.next();
            subtreeNode.setNodeColor(produceLibseaColourCoding(colour));
        }
    }

    /**
     * Ensures that the color attribute value for all siblingGroups and links in the specified subtree have the specified value
     * @param nd NodeData node which is the root of the subtree to be coloured
     * @param colour Color to be used for colouring the subtree
     */
    public void colorSubtree(NodeData nd, Color colour, boolean colourSubtreeRootBranch)
    {
        colorSubtreeNodes(nd, colour);
        colorSubtreeBranches(nd, colour, colourSubtreeRootBranch);
    }

    /**
     * Sets the base color for the siblingGroups in the tree.
     * @param colour Color to be used as the base color
     */
    public void setNodesBaseColor(Color colour)
    {
        nodesBaseColor = produceLibseaColourCoding(colour);
    }

    //Test method
    private static void test()
    throws FatalException
    {
        TreeNCBI treeNCBI = TreeNCBI.setupTestTree();

        NodeDataWalrus ndw = new NodeDataWalrus(new Integer(1));

        treeNCBI.retainOnlySubtree(ndw);
        //treeNCBI.removeSubtree(ndn);

        TreeWalrus treeWalrus = new TreeWalrus(treeNCBI);

        treeWalrus.colorSubtreeBranches(ndw, new Color(255, 0, 0), false);
        treeWalrus.colorSubtreeNodes(ndw, new Color(0, 0, 255));

        PrintWriter pw = FileOperations.getPrintWriter("/Home/strandfuru/tim/data/junk/libseatest15.graph");
        pw.write(treeWalrus.toString());
        pw.close();
    }

    public static void main(String[] args) throws FatalException
    {
        test();
    }

    public boolean isFullLineageAssigned()
    {
        return fullLineageAssigned;
    }

    public void setFullLineageAssigned(boolean b)
    {
        fullLineageAssigned = b;
    }

}
