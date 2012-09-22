/*
 * Created on 21-Jul-2004
 */
package phylo3D.parser.nexus;

import java.io.*;
import java.util.*;


import phylo3D.utilities.*;

/**
 *  Parses the tree block in the NEXUS format as output by PAUP. Assumes that there is both a translate
 * section and a tree definition in the tree section.
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TreeBlockParser
{
	public static final String BEGIN_TREE_BLOCK = "Begin trees;";
	public static final String BEGIN_TRANSLATE_SECTION = "Translate";
	public static final String END_TRANSLATE_SECTION = ";";
	public static final String TREE_DEF = "tree";
	public static final String EQUAL_SIGN = "=";
	
	String inputFilePath = null;
	forester.tree.Tree foresterTree = null;
	
	public TreeBlockParser(String inputFilePath)
	throws UserInputFileAccessException, UserInputFileParsingException
	{

		try
		{
			this.inputFilePath = inputFilePath;
			//get reader to file
			BufferedReader br = FileOperations.getBufferedReader(inputFilePath);			
			//locate the tree block
			moveBufferToBeginTreeBlock(br);
			//see if there is a translate section and read the translate section
			Hashtable names  = extractTranslation(br);
			//read the tree
			String nhTree = extractNHTree(br);
			//put the names in the tree
			String nhTreeWithNames = placeNamesInTree(names, nhTree);
			//parse the labelled tree using the ATV parser
			foresterTree = convertToForesterTree(nhTreeWithNames);			
		}
		catch(InputFileAccessException ifae)
		{
			//Specify this to user
			throw new UserInputFileAccessException(ifae.getMessage());
		}
		catch (UserInputFileParsingException uifpe)
		{
			String message = "";
			message += "Unable to parse the nexus file" + "\n";
			message += uifpe.getMessage() + "\n";
			message += "The nexus parser is very primitive. If you are having problems get your tree into NH format" +				" and use this as the input format instead.";
			throw new UserInputFileParsingException(message);
		}
	}
	
	public forester.tree.Tree getForesterTree()
	{
		return foresterTree;
	}
	
	private void moveBufferToBeginTreeBlock(BufferedReader br)
	throws UserInputFileAccessException, UserInputFileParsingException
	{
		boolean foundBeginTreeBlock = false;
		String line = null;
		try
		{
			while ((line = br.readLine()) != null)
			{
				if(line.indexOf(BEGIN_TREE_BLOCK) != -1)
				{
					foundBeginTreeBlock = true;
					break;
				}
			}
		}
		catch (IOException ioe)
		{
			throw new UserInputFileAccessException("Unable to read from file " + inputFilePath);
		}
		
		if (foundBeginTreeBlock == false)
		{
			throw new UserInputFileParsingException("Unable to locate beginning of tree block.");
		}
	}

	private Hashtable extractTranslation(BufferedReader br) 
	throws UserInputFileAccessException, UserInputFileParsingException
	{
		Hashtable names = new Hashtable();
		String line = null;
		boolean locatedTranslateSection = false;
		try
		{
			while ((line = br.readLine()) != null)
			{
				if(line.indexOf(BEGIN_TRANSLATE_SECTION) != -1)
				{
					locatedTranslateSection = true;
					break;
				}
			}			
		}
		catch(IOException ioe)
		{
			throw new UserInputFileAccessException("Unable to read from file " + inputFilePath);
		}

		
		if (locatedTranslateSection == false)
		{
			throw new UserInputFileParsingException("Unable to locate Translate section");
		}
		else
		{
			try
			{
				while ((line = br.readLine()) != null)
				{
					if(line.indexOf(END_TRANSLATE_SECTION) == -1)
					{
						int firstQuotation = line.indexOf("'");
						int secondQuotation = line.lastIndexOf("'");
						String name = line.substring(firstQuotation + 1, secondQuotation);
						int lastTab = line.lastIndexOf("\t");
						String number = line.substring(lastTab+1, firstQuotation-1);
						names.put(number, name);
					}
					else
					{
						break;
					}
				}
			}
			catch(IOException ioe)
			{
				throw new UserInputFileAccessException("Unable to read from file " + inputFilePath);
			}			
		}
		return names;
	}

	private String extractNHTree(BufferedReader br)
	throws UserInputFileAccessException, UserInputFileParsingException
	{
		String nhTree = null;
		String line = null;
		try
		{
			while ((line = br.readLine()) != null)
			{
				if(line.indexOf(TREE_DEF) != -1)
				{
					int equalIndex = line.indexOf(EQUAL_SIGN);
					nhTree = line.substring(line.indexOf("(", equalIndex));
					break;
				}
			}
			if (nhTree == null)
			{
				throw new UserInputFileParsingException("Unable to locate tree in NEXUS file.");
				
			}
		}
		catch(IOException ioe)
		{
			throw new UserInputFileAccessException("Unable to read from file " + inputFilePath);
		}
		
		return nhTree;		
	}
	
	private String placeNamesInTree(Hashtable names, String tree)
	{
		StringBuffer treeWithNames = new StringBuffer();
		StringBuffer treeBuffer = new StringBuffer(tree);
		StringBuffer number = null;
		for (int i = 0; i < treeBuffer.length(); i++)
		{
			Character c = new Character(treeBuffer.charAt(i));
			if(!Character.isDigit(c.charValue()))
			{
				if (number != null)
				{
					String name = (String)names.get(number.toString());
					treeWithNames.append(name);
					//end
					number = null;
				}
				treeWithNames.append(c.charValue());
				//end
			}
			else
			{
				if(number == null)
				{
					number = new StringBuffer();
				}
				number.append(c);
			}
		}
		return treeWithNames.toString();
	}

	private forester.tree.Tree convertToForesterTree(String nhTreeWithNames)
	throws UserInputFileParsingException
	{
		forester.tree.Tree foresterTree = null;
		try
		{
			foresterTree = new forester.tree.Tree(nhTreeWithNames);
		}
		catch(Exception e)
		{
			String message = "Unable to parse the nexus tree" + "\n" + "Check that the file follows the NH syntax."; 
			throw new UserInputFileParsingException(message);
		}
		return foresterTree;			
	}
	
	public static void test() throws FatalException
	{
		TreeBlockParser tbp = new TreeBlockParser("/Home/strandfuru/tim/data/project_plantVisualisation/phylo3D/data/rbcl_rootambo.bionj");
		System.out.println(tbp.getForesterTree().toString());
	}
	
	public static void main(String[] args) throws Exception
	{
		test();
	}

}
