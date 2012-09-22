/*
 * Created on 22-Dec-2003
 */
package phylo3D.tools;

import java.util.*;


import phylo3D.utilities.*;
import phylo3D.trees.*;
import phylo3D.imaging.*;

/**
 *  Replace this text with a general description of the class
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class CommandLineParsing
{
	//private static final String OPEN_PARENTHESIS = "[";
	//private static final String CLOSE_PARENTHESIS = "]";
	//private static final String OPEN_BRACKET = "{";
	//private static final String CLOSE_BRACKET = "}";	
	private static final String COMMA = ",";
	private static final String PLUS = "+";
	private static final String DOUBLE_HYPHEN = "--";
	private static final String EQUAL = "=";

	protected static final String COLOURS_OPTION = "colours";
	protected static final String NARROW_TO_OPTION = "narrowTo";
	protected static final String SEQUENCE_NAME_OPTION = "sequenceName";
	protected static final String INPUT_FORMAT_OPTION = "inputFormat";
	protected static final String OUTPUT_FORMAT_OPTION = "outputFormat";
	protected static final String INTERNAL_NODES_OPTION = "internalNodes";
	protected static final String COLOUR_SUBTREE_ROOT_BRANCH_OPTION = "colourSubtreeRootBranch";
	protected static final String TAXONOMY_FILE_OPTION = "taxonomyFile";
	protected static final String MATCHING_COLUMN_OPTION = "matchingColumn";
	protected static final String COLOURING_COLUMN_OPTION = "colouringColumn";
	protected static final String COLOURING_NAMES_OPTION = "colouringNames";
	
	protected static final String NH_OPTION_VALUE = "NH";
	protected static final String NHX_OPTION_VALUE = "NHX";
	protected static final String NEXUS_OPTION_VALUE = "NEXUS";
	protected static final String YES = "Y";
	protected static final String NO = "N";
	protected static final String SEQUENCE_NAME_SPECIES = "S";
	protected static final String SEQUENCE_NAME_TAXID = "T";
	
	/**
	 * Parses an option block i.e. a String containing an option key and an option value ("optionkey=optionvalue")
	 * @param optionBlock String containing the option block
	 * @return String array of length 2 with at index 0 the option key and at index 1 the option value
	 * @throws Exception Thrown if the option block is malformed and cannot be parsed
	 */
	protected static String[] parseOptionBlock(String optionBlock) throws CommandLineParsingException
	{
		String doubleHyphen = optionBlock.substring(0,2);
		if (!doubleHyphen.equals(DOUBLE_HYPHEN))
		{
			throw new CommandLineParsingException("Malformed option " + optionBlock);
		}
		String optionBlockWithoutDoubleHyphen = optionBlock.substring(2,optionBlock.length());
		int indexOfEqual = optionBlockWithoutDoubleHyphen.indexOf(EQUAL);
		if (indexOfEqual == -1 || indexOfEqual==optionBlockWithoutDoubleHyphen.length()-1)
		{
			throw new CommandLineParsingException ("Malformed option " + optionBlock);
		}
		String[] keyAndValue = new String [2];
		keyAndValue[0] = optionBlockWithoutDoubleHyphen.substring(0, indexOfEqual);
		keyAndValue[1] = optionBlockWithoutDoubleHyphen.substring(indexOfEqual+1);
		return keyAndValue;		
	}
	
	/**
	 * Gets the option key from an option block of the form "optionKey=optionValue"
	 * @param optionBlock String containing the option block
	 * @return String containing the option key
	 * @throws Exception Thrown if the option block is malformed
	 */
	protected static String getOptionKey(String optionBlock) throws CommandLineParsingException
	{
		return parseOptionBlock(optionBlock)[0];
	}
	
	/**
	 * Gets the option value from an option block of the form "optionKey=optionValue"
	 * @param optionBlock String containing the option block
	 * @return String containing the option key
	 * @throws Exception Thrown if the option block is malformed
	 */	
	protected static String getOptionValue(String optionBlock) throws CommandLineParsingException
	{
		return parseOptionBlock(optionBlock)[1];
	}
	
	/**
	 * Parses a list of elements each separated by a separator String
	 * @param list String containing the list to be parsed
	 * @param separator String representing the elements separator
	 * @return String array with the elements of the list
	 * @throws Exception Thrown if problem with the syntax of the list
	 */
	private static String[] parseList(String list, String separator)
	{
		Vector elements = new Vector();
		
		String listWithoutParenth = list;
		StringTokenizer st = new StringTokenizer(listWithoutParenth, separator);
		while (st.hasMoreElements())
		{
			String element = (String) st.nextElement();
			elements.add(element.trim());
		}
		return (String[])elements.toArray(new String[0]);
	}
	
	/**
	 * Parses a list of the form x,y+z,w
	 * @param list String containing the list
	 * @return String array of the list elements i.e. of the form x,y
	 * @throws Exception Thrown if there are no elements in the list
	 */
	protected static String[] parseBigList(String list) throws CommandLineInputsException
	{
		String[] elements = parseList(list, PLUS);
		if (elements.length == 0)
		{
			throw new CommandLineInputsException("No elements in list");
		}
		return elements;		
	}

	/**
	 * Parses a list of the form x,y,z,w
	 * @param list String containing the list
	 * @return String array of the list elements i.e. of the form x 
	 * @throws Exception Thrown if there are no elements in the list
	 */	
	protected static String[] parseLittleList(String list) throws CommandLineInputsException
	{
		String[] elements = parseList(list, COMMA);
		if (elements.length == 0)
		{
			throw new CommandLineInputsException("No elements in list");
		}
		return elements;	
	}
	
	/**
	 * Checks that the "narrowTo" block is well formed and extracts the taxID of the node to
	 *  narrow to
	 * @param optionBlock String containing the option block specifying the node to narrow to
	 * @return String containing taxID of node to narrow to
	 * @throws Exception
	 */
	protected static String extractNodeToNarrowTo(String optionBlock) 
		throws CommandLineParsingException, CommandLineInputsException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!key.equals(NARROW_TO_OPTION))
		{
			throw new CommandLineParsingException("Error in option name or order.");
		}
		else
		{
			try
			{
				Integer.valueOf(value);	
			}
			catch (Exception e)
			{
				throw new CommandLineInputsException("Node specified for narrowing to is not an integer");
			}
		}
		return value;
	}
	
	/**
	 * Extracts the value from the "inputFormat" option block: checks that the option is correctly named
	 * and that it has one of the allowed values
	 * @param optionBlock String containing the "inputFormat" option block
	 * @return String containing the value of the "inputFormat" option block
	 * @throws Exception Thrown if it is not possible to extract the input format information from this 
	 * option block
	 */
	public static String extractInputFormat(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(INPUT_FORMAT_OPTION) && 
		(value.equals(NH_OPTION_VALUE) || value.equals(NHX_OPTION_VALUE) || value.equals(NEXUS_OPTION_VALUE))))
		{
			throw new CommandLineParsingException("Error in option order, name, or value.");
		}
		return value;
	}	
	
	public static String extractSequenceName(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(SEQUENCE_NAME_OPTION) && 
		(value.equals(SEQUENCE_NAME_SPECIES) || value.equals(SEQUENCE_NAME_TAXID))))
		{
			throw new CommandLineParsingException("Error in option order, name or value.");
		}
		return value;
	}
	
	public static String extractInternalNodes(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(INTERNAL_NODES_OPTION) && 
		(value.equals(YES) || value.equals(NO))))
		{
			throw new CommandLineParsingException("Error in option order, name or value.");
		}
		return value;
	}
	
	public static String extractColourSubtreeRootBranch(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(COLOUR_SUBTREE_ROOT_BRANCH_OPTION) && 
		(value.equals(YES) || value.equals(NO))))
		{
			throw new CommandLineParsingException("Error in option order, name or value.");
		}
		return value;
	}	
	
	public static String extractOutputFormat(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(OUTPUT_FORMAT_OPTION) && 
		(value.equals(NH_OPTION_VALUE) || value.equals(NHX_OPTION_VALUE))))
		{
			throw new CommandLineParsingException("Error in option order, name or value.");
		}
		return value;
	}

	public static String extractTaxonomyFile(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(TAXONOMY_FILE_OPTION)))
		{
			throw new CommandLineParsingException("Error in option name or order.");
		}
		
		return value;
	}
	
	public static int extractMatchingColumn(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(MATCHING_COLUMN_OPTION)))
		{
			throw new CommandLineParsingException("Error in option name or order.");
		}
		
		return Integer.parseInt(value);
	}		
	
	public static int extractColouringColumn(String optionBlock) throws CommandLineParsingException
	{
		String key = getOptionKey(optionBlock);
		String value = getOptionValue(optionBlock);
		if (!(key.equals(COLOURING_COLUMN_OPTION)))
		{
			throw new CommandLineParsingException("Error in option name or order.");
		}
		
		return Integer.parseInt(value);
	}
	
	/**
	 * Parses a colouring names block into objectsColourAssociations
	 * @param optionBlock String containing option block to be parsed
	 * @return Vector of objectsColourAssociations where the object is a String with the name of the nodes
	 * to use for colouring
	 */
	public static Vector extractColouringNames(String optionBlock) 
	throws CommandLineParsingException, CommandLineInputsException
	{
		String key = CommandLineParsing.getOptionKey(optionBlock);
		if (!key.equals(CommandLineParsing.COLOURING_NAMES_OPTION))
		{
			throw new CommandLineParsingException("Option does not exist");
		}
		String value = CommandLineParsing.getOptionValue(optionBlock);
		String[] objectColourPairs = CommandLineParsing.parseBigList(value);
		Vector objectsColourAssociations = new Vector();
		for(int i = 0; i < objectColourPairs.length; i++)
		{
			String[] nameAndColour = CommandLineParsing.parseLittleList(objectColourPairs[i]);
			if(nameAndColour.length != 2)
			{
				throw new CommandLineInputsException("Name and colour pairs are incorrectly defined.");
			}
			
			ObjectsColourAssociation objectsColourAssociation = new ObjectsColourAssociation();
			objectsColourAssociation.addObject(nameAndColour[0]);
			objectsColourAssociation.setColour(ColourUtilities.decodeHexColour(nameAndColour[1]));
			objectsColourAssociations.add(objectsColourAssociation);
			
		}
		return objectsColourAssociations;//String representing the node sequence name and a Color		
	}		
	
	
	/**
	 * Modifies the NCBI tree by narrowing the tree to the subtree rooted at the specified taxonomy ID.
	 * @param treeNCBI TreeNCBI upon which the narrowing operation must be carried out
	 * @param taxIDOfNodeToNarrowTo String containing the taxonomy ID the tree is to be narrowed to
	 */
	protected static void narrowToSubtree(TreeNCBI treeNCBI, String taxIDOfNodeToNarrowTo)
	throws InvalidInputsException
	{
		NodeDataNCBI ndnToNarrowTo = null;
		Vector allNCBINodes = treeNCBI.getAllNodes();
		Iterator iter  = allNCBINodes.iterator();
		while (iter.hasNext())
		{
			NodeDataNCBI ndn = (NodeDataNCBI) iter.next();
			if(ndn.getTaxonomyID().toString().equals(taxIDOfNodeToNarrowTo))
			{
				ndnToNarrowTo = ndn;
			}
		}

		//Narrow to the subtree
		if(ndnToNarrowTo == null)
		{
			throw new InvalidInputsException(
			"Unable to locate a node with the taxonomy ID " + taxIDOfNodeToNarrowTo + " in the NCBI tree" + "\n" +
			"Unable to carry out the narrowing operation.");
		}
		else
		{
			UserInfo.println("Narrowing to subtree rooted at node with taxonomy ID: " + ndnToNarrowTo.getTaxonomyID());
			treeNCBI.retainOnlySubtree(ndnToNarrowTo);
		}		
	}		
	
	private static void test() throws FatalException
	{
		String option = "--nodes(sfsdf,sfsdf,sfsdfsf)";
		Debug.println(getOptionKey(option));
	}
	
	public static void main(String[] args) throws FatalException
	{
		test();
	}
}
