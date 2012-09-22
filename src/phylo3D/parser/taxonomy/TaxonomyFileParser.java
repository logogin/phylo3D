/*
 * Created on 22-Jul-2004
 */
package phylo3D.parser.taxonomy;

import java.io.*;
import java.util.*;
import java.awt.*;


import phylo3D.imaging.ObjectsColourAssociation;
import phylo3D.utilities.*;

/**
 *  Class provides functionality for parsing a taxonomy file.
 * Each line in a taxonomy file is a taxonomic classification in tab dellimited format.
 * 
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class TaxonomyFileParser
{

	private static final String MISSING_VALUE = "NA";
	private Vector tokenizedLines = new Vector();
	private String inputFilePath = null;
	private int lineLength = 0;

	public TaxonomyFileParser(String inputFilePath) throws FatalException
	{
		//Get a buffered reader
		BufferedReader br = null;
		try
		{
			UserInfo.println("Reading taxonomy from file: " + inputFilePath);
			br = FileOperations.getBufferedReader(inputFilePath);
		}
		catch(InputFileAccessException ifae)
		{
			throw new UserInputFileAccessException(ifae.getMessage());
		}
		parseLines(br);
		
	}//end of method

	public Vector getTokenisedLines()
	{
		return tokenizedLines;
	}

	private void parseLines(BufferedReader br) 
	throws UserInputFileParsingException, UserInputFileAccessException
	{
		//Read each line
		String line = null;
		int numberOfTokens = 0;
		int lineCount = 0;

		try
		{
			while ((line = br.readLine()) != null)
			{
				lineCount++;
				StringTokenizer tokenizer = new StringTokenizer(line);
				if(tokenizer.countTokens() < 1)
				{
					throw new UserInputFileParsingException("Line " + lineCount + " in file that does not contain any elements." + 
					" Remove this line from the file or correct it.");
				}
				if (numberOfTokens != 0 && numberOfTokens != tokenizer.countTokens())
				{
					throw new UserInputFileParsingException("Line " + lineCount + " in file contains different number of elements.\n" + 
														" Make sure all lines have same number of elements");
				}
				numberOfTokens = tokenizer.countTokens();
				String[] lineTokens = new String [numberOfTokens];
				int tokenIndex = 0;
				while (tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					lineTokens[tokenIndex] = token;
					tokenIndex++;				
				}
				tokenizedLines.add(lineTokens);		
			}
		}
		catch (IOException ioe)
		{
			throw new UserInputFileAccessException("Unable to read from file " + inputFilePath);
		}
		lineLength = numberOfTokens;
		
	}//end of method

	/**
	 * Produces a Vector of ObjectsColourAssociations where the objects are in order the matching column
	 * string (first object), the colouring column string (second object) and a string containing the full lineage
	 * (third object). The associated colour is not set
	 * @param matchingCol int denoting the column to be used as the matching column. First col is 1
	 * @param colouringCol int denoting the column to be used as the colouring column (it is strings in this column
	 * that can be associated with colours in the namesAndColours vector). First col is 1
	 * @return Vector of ObjectsColourAssociations
	 */
	public Vector getStringsColourAssociations(int matchingCol, int colouringCol)
	throws InvalidInputsException
	{
		//Check the col indexes are within range 
		if (!(matchingCol > 0 && matchingCol <= getLineLength()))
		{
			throw new InvalidInputsException("Matching column index is out of the range of the columns in the taxonomy file" + "\n" +
			"Valid column index range is 1 to " + getLineLength());
		}

		if (!(colouringCol > 0 && colouringCol <= getLineLength()))
		{
			throw new InvalidInputsException("Colouring column index is out of the range of the columns in the taxonomy file" + "\n" +
			"Valid column index range is 1 to " + getLineLength());
		}

		//Set up the ObjectsColourAssociations
		Vector ocas = new Vector();
		Iterator iter = tokenizedLines.iterator();
		while (iter.hasNext())
		{
			String[] lineTokens = (String[]) iter.next();
			ObjectsColourAssociation oca = new ObjectsColourAssociation();
			oca.addObject(lineTokens[matchingCol - 1]);//First object is matching column string
			oca.addObject(lineTokens[colouringCol - 1]);//Second object is colouring column string
			String lineage = "";
			for (int i = 0; i < lineTokens.length; i++)
			{
				lineage += lineTokens[i] + " ";
			}
			oca.addObject(lineage);
			ocas.add(oca);
		}
				
		return ocas;
	}
	
	private static void test() throws FatalException
	{
		String testInputFile = "/Home/strandfuru/tim/data/project_treeOfLife/software/data/test/input/angioTaxo.txt";
		TaxonomyFileParser tfp = new TaxonomyFileParser(testInputFile);
		Vector lines = tfp.getTokenisedLines();
		String[] firstLine = (String[])lines.elementAt(1);
		for (int i = 0; i < firstLine.length; i++)
		{
			//System.out.println(firstLine[i]);
		}
		ObjectsColourAssociation oca = new ObjectsColourAssociation();
		oca.addObject("Orchidaceae");
		oca.setColour(new Color(125,125,125));
		Vector namesAndColours = new Vector();
		namesAndColours.add(oca);
		tfp.getStringsColourAssociations(1,4);
	}	
	
	public static void main (String[] args) throws FatalException
	{
		test();
	}

	/**
	 * @return int Number of tokens in a line
	 */
	public int getLineLength()
	{
		return lineLength;
	}

}
