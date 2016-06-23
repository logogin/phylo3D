/*
 * Created on 18-Dec-2003
 */
package phylo3D.tools;

import phylo3D.imaging.ObjectsColourAssociation;

import java.io.*;
import java.util.*;
import java.awt.*;


import phylo3D.trees.*;
import phylo3D.imaging.*;
import phylo3D.utilities.*;

/**
 *  Abstract class implementing functionality required by tools producing Walrus trees. This functionality
 * consists of methods for computing the legend and the tree colouring, and for printing the results to file.
 *
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public abstract class ToolWithWalrusOutput extends Tool
{
    /**
     * Takes an option block with node/colour pairs and produces a Vector of objectsColourAssociations
     * @param optionBlock String which is the option block to be parsed for the node/colour pairs
     * @return Vector of objectsColourAssociations
     * @throws Exception Thrown if node/colour pairs are improperly defined
     */
    protected Vector computeObjectsColourAssociations(String optionBlock)
    throws FatalException
    {
        String key = CommandLineParsing.getOptionKey(optionBlock);
        if (!key.equals(CommandLineParsing.COLOURS_OPTION))
        {
            throw new CommandLineParsingException("Option does not exist");
        }
        String value = CommandLineParsing.getOptionValue(optionBlock);
        String[] objectColourPairs = CommandLineParsing.parseBigList(value);
        Vector objectsColourAssociations = new Vector();
        for(int i = 0; i < objectColourPairs.length; i++)
        {
            String[] nodeAndColour = CommandLineParsing.parseLittleList(objectColourPairs[i]);
            if(nodeAndColour.length != 2)
            {
                throw new CommandLineInputsException("Node and colour pairs are incorrectly defined.");
            }

            ObjectsColourAssociation objectsColourAssociation = new ObjectsColourAssociation();
            objectsColourAssociation.addObject(nodeAndColour[0]);
            objectsColourAssociation.setColour(ColourUtilities.decodeHexColour(nodeAndColour[1]));
            objectsColourAssociations.add(objectsColourAssociation);

        }
        return objectsColourAssociations;//String representing the node sequence name and a Color
    }

    /**
     * Computes and prints to file the taxonomy legend, the legend, and the coloured Walrus tree
     * @param filePath Path to the file that will contain the (coloured) Walrus tree
     * @param treeWalrus TreeWalrus not yet coloured
     * @param objectsColourAssociations Vector of ObjectsColourAssociations needed for producing
     * the legend and the colouring of the TreeWalrus
     * @param colourSubtreeRootBranch boolean determines whether the branch leading to the node at
     * the root of the subtree should be coloured
     * @param taxonomyColouredWalrusNodes Vector of ObjectsColourAssociations where the objects are
     * NodeDataWalrus nodes
     * @param taxonomyObjectsColourAssociations Vector of ObjectsColourAssociations where the
     * first object is the matching column, the second object is the colouring column and the third object is
     * the full lineage string
     * @param sortLegendAlphabetically boolean indicating whether to sort the taxonomy legends alphabetically
     */
    protected void produceOutputFiles(String filePath, TreeWalrus treeWalrus,
    Vector objectsColourAssociations, boolean colourSubtreeRootBranch,
    Vector taxonomyColouredWalrusNodes, Vector taxonomyObjectsColourAssociations,
    boolean sortLegendAlphabetically)
    throws FatalException
    {
        //use taxonomyObjectsColourAssociations to produce the taxonomy legend
        produceTaxonomyLegend(taxonomyObjectsColourAssociations, filePath, sortLegendAlphabetically);
        //Produce the legend using the specific colourings
        produceLegend(objectsColourAssociations, filePath);

        //use objectsColourAssociation (specific nodes) and taxonomyColouredWalrusNodes
        // to produce the colouring of the Walrus tree
        produceWalrusTree(treeWalrus, objectsColourAssociations, filePath, colourSubtreeRootBranch,
        taxonomyColouredWalrusNodes);
    }

    /**
     * Prints the legend to file
     * @param objectsColourAssociations Vector of ObjectsColourAssociations
     * @param filePath String containing the output file path used for computing the path to the
     * legend file
     */
    private void produceLegend(Vector objectsColourAssociations, String filePath)
    throws InvalidInputsException, OutputFileAccessException
    {
        //Prepare input to the legend
        Vector texts = new Vector();//first main input to legend
        Vector colours = new Vector();//second main input to the legend
        computeInputsToLegend(texts, colours, objectsColourAssociations);

        //Determine name of file to save legend in
        File legendFile = determineLegendFile(filePath, false);

        //Print out the legend if colouring of nodes has been specified
        if (objectsColourAssociations.size() > 0)
        {
            Legend theLegend = null;
            try
            {
                theLegend = new Legend((String[])texts.toArray(new String[0]), (Color[])colours.toArray(new Color[0]));
            }
            catch(Exception e)
            {
                throw new InvalidInputsException("Error in inputs to legend for specific nodes, therefore unable to produce legend file.");
            }

            try
            {
                UserInfo.println("Printing specific nodes legend for walrus tree to file: "+ legendFile.getPath());
                Drawer.draw(theLegend, legendFile);
            }
            catch(FileNotFoundException fnfe)
            {
                throw new OutputFileAccessException("File not found exception while trying to write to legend file for specific nodes." + "\n"+
                                    fnfe.getMessage());
            }
            catch (IOException ioe)
            {
                throw new OutputFileAccessException("Input/output exception while trying to write to legend file for specific nodes." + "\n"+
                    ioe.getMessage());
            }
        }
        else //No objects to colour
        {
            UserInfo.println("No specific nodes to colour, therefore not producing legend file for specific nodes.");
        }
    }

    /**
     * Prints the taxonomy legend to file
     * @param taxonomyObjectsColourAssociations Vector of ObjectsColourAssociations
     * @param filePath String containing the output file path used for computing the path to the
     * legend file
     * @param sortAlphabetically boolean determining whether the taxonomy legend labels are to be
     * sorted alphabetically
     */
    private void produceTaxonomyLegend(Vector taxonomyObjectsColourAssociations, String filePath,
                                                                        boolean sortAlphabetically)
    throws InvalidInputsException, OutputFileAccessException
    {
        //Prepare input to the legend
        Vector texts = new Vector();//first main input to legend
        Vector colours = new Vector();//second main input to the legend
        //Ensures that only one colour is printed for each colouring string
        computeInputsToTaxonomyLegend(texts, colours, taxonomyObjectsColourAssociations, sortAlphabetically);

        //Determine name of file to save legend in
        File legendFile = determineLegendFile(filePath, true);

        //Print out the legend if colouring of nodes has been specified
        if (taxonomyObjectsColourAssociations.size() > 0)
        {
            Legend theLegend = null;
            try
            {
                theLegend = new Legend((String[])texts.toArray(new String[0]), (Color[])colours.toArray(new Color[0]));
            }
            catch(Exception e)
            {
                throw new InvalidInputsException("Error in inputs to taxonomy legend, therefore unable to produce legend file.");
            }

            try
            {
                UserInfo.println("Printing taxonomy legend for walrus tree to file: "+ legendFile.getPath());
                Drawer.draw(theLegend, legendFile);
            }
            catch (IOException ioe)
            {
                throw new OutputFileAccessException("Input/output exception while trying to write to taxonomy legend file." + "\n" +
                    ioe.getMessage());
            }
        }
        else //No objects to colour
        {
            UserInfo.println("No taxonomy nodes to colour, therefore not producing taxonomy legend file.");
        }
    }

    /**
     * Determines the file to write the legend to. Creates a file in the same directory as the file where the
     * Walrus tree is placed with the same name but the extension ".jpg"
     * @param outputFilePath String containing path to file which Walrus tree is to be written to
     * @return File to which the Legend will be written
     */
    private File determineLegendFile(String outputFilePath, boolean isTaxonomyLegend)
    {
        File legendFile = null;
        File outputFile = new File(outputFilePath);
        String outputFilename = outputFile.getName();

        String legendMarker = "Legend";
        if (isTaxonomyLegend)
        {
            legendMarker = "TaxonomyLegend";
        }

        String parentString = "";
        if (outputFile.getParent() != null)
        {
            parentString = outputFile.getParent() + File.separator;
        }

        if (outputFilename.indexOf(".") != -1)
        {
            legendFile = new File(parentString +  outputFilename.substring(0,outputFilename.indexOf(".")) +
                                                                                     legendMarker + ".jpg");
        }
        else
        {
            legendFile = new File(parentString + outputFilename + legendMarker + ".jpg");
        }
        return legendFile;
    }

    /**
     * Computes the inputs need to produce the legend namely the texts and the corresponding colours.
     * The texts and colours variables are modifi
     * @param texts String array of texts
     * @param colours String array of colours
     * @param objectsColourAssociations Vector of ObjectsColourAssociations where the objects are NodeDataWalrus
     */
    private void computeInputsToLegend(Vector texts, Vector colours, Vector objectsColourAssociations)
    {
        Iterator iter = objectsColourAssociations.iterator();
        while (iter.hasNext())
        {
            ObjectsColourAssociation objectColourAssociation = (ObjectsColourAssociation) iter.next();
            //All the NodeDataWalrus in one ObjectsColourAssociation have the same sequenceName
            NodeDataWalrus ndw = (NodeDataWalrus) objectColourAssociation.getFirstObject();

            texts.add(textToUseInLegend(ndw.getNodeData()));
            colours.add((Color) objectColourAssociation.getColour());
        }
    }


    /**
     * Computes the text and colour inputs to the Taxonomy legend by extracting from the
     * taxonomyObjectsColourAssociations a colour for each colouring string. TaxonomyObjectsColourAssociations
     * will usually contain multiple entries with the same colouring string.
     * @param texts String array to be updated by this function with the colouring strings
     * @param colours Color array of the colours associated with each colouring string
     * @param taxonomyObjectsColourAssociations Vector of ObjectsColourAssociations where first object
     * is matching string, second object is colouring string and third object is full lineage string
     * @param sortAlphabetically boolean indicating whether the legend labels should be ordered alphabetically
     */
    private void computeInputsToTaxonomyLegend(Vector texts, Vector colours,
    Vector taxonomyObjectsColourAssociations, boolean sortAlphabetically)
    {
        Hashtable colouringStringsAndColour = new Hashtable();
        Iterator iter1 = taxonomyObjectsColourAssociations.iterator();
        while (iter1.hasNext())
        {
             ObjectsColourAssociation oca = (ObjectsColourAssociation) iter1.next();
             Color colour = (Color)colouringStringsAndColour.get(oca.getSecondObject());
             if (colour == null)
            {
                colouringStringsAndColour.put(oca.getSecondObject(), oca.getColour());
            }

        }

        Iterator iter = null;
        if(sortAlphabetically)
        {
            Vector v = new Vector(colouringStringsAndColour.keySet());
            Collections.sort(v);
            iter = v.iterator();
        }
        else
        {
            iter = colouringStringsAndColour.keySet().iterator();
        }

        while (iter.hasNext())
        {
            String colouringString = (String)iter.next();
            Color colour = (Color)colouringStringsAndColour.get(colouringString);

            texts.add(colouringString);
            colours.add(colour);
        }
    }

    /**
     * Method that should be overridden by the subclass so that the data from the NodeData is used as
     * the text in the legend
     * @param nd NodeData which is extracted from the nodes of the TreeWalrus
     * @return String containing the desired data
     */
    abstract protected String textToUseInLegend(NodeData nd);

    /**
     * Produces the Walrus tree by first colouring it (with both taxonomy and specific nodes) and then
     *  printing it to file
     * @param treeWalrus TreeWalrus to be coloured and printed to file
     * @param objectsColourAssociations Vector of ObjectsColourAssociations specifying how to
     * colour the treeWalrus
     * @param filePath String specifying the path of the file to which the coloured treeWalrus is
     * to be written
     * @param colourSubtreeRootBranch determines whether the branch leading to the subtree root node
     * should be coloured.
     * @param taxonomyColouredWalrusNodes Vector of ObjectsColourAssociations where the Objects
     * NodeDataWalrus nodes from the TreeWalrus
     */
    public static void produceWalrusTree(TreeWalrus treeWalrus, Vector objectsColourAssociations,
                                     String filePath, boolean colourSubtreeRootBranch, Vector taxonomyColouredWalrusNodes)
                                     throws FatalException
    {
        //First colour using the taxonomy coloured nodes
        colourWalrusTree(treeWalrus, taxonomyColouredWalrusNodes, colourSubtreeRootBranch);
        //Second colour using the colouring for specific nodes
        colourWalrusTree(treeWalrus, objectsColourAssociations, colourSubtreeRootBranch);

        //Print to output file
        PrintWriter pw = FileOperations.getPrintWriter(filePath);
        UserInfo.println("Printing walrus tree to file: " + filePath);
        //pw.write(treeWalrus.stringRepresentation());
        treeWalrus.print(new IndentPrintWriter(pw));
        pw.close();
    }

    /**
     * Colours the Walrus tree
     * @param treeWalrus TreeWalrus to be coloured
     * @param objectsColourAssociations Vector of ObjectsColourAssociations containing the colouring
     * information. Note that the objects must be NodeDataWalrus objects that are to be coloured and that
     * this method takes into account the possibility that there may be several NodeDataWalrus objects
     * that are to be coloured in the same way.
     * @param colourSubtreeRootBranch determines whether the branch leading to the subtree root node
     * should be coloured.
     */
    private static void colourWalrusTree(TreeWalrus treeWalrus, Vector objectsColourAssociations,
                                    boolean colourSubtreeRootBranch)
    {
        Iterator iter = objectsColourAssociations.iterator();
        while (iter.hasNext())
        {
            ObjectsColourAssociation objectsColourAssociation = (ObjectsColourAssociation)iter.next();

            Iterator iterNodes = objectsColourAssociation.getObjectsIterator();
            while (iterNodes.hasNext())
            {
                NodeDataWalrus ndw = (NodeDataWalrus) iterNodes.next();
                //Colour tree
                treeWalrus.colorSubtree(ndw, objectsColourAssociation.getColour(), colourSubtreeRootBranch);
            }
        }
    }

}
