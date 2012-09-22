/*
 * Created on 19-Sep-2003
 */
package phylo3D.tools;

import phylo3D.utilities.*;
import phylo3D.trees.*;

/**
 * Loads the NCBI tree from serialized file into TreeNCBI which can then be 
 * manipulated (narrowed) before it is printed to a NH or NHX file.
 * The options when calling the program are:
 * 	
 * --narrowTo=node where node is NCBI taxonomy ID (obligatory)
 * --sequenceName=T or S, where T: NCBI taxonomy IDs and S: NCBI scientific species names (obligatory)
 * --internalNodes=Y or N, where Y:internal node data is included in output and N: data not included (obligatory)
 * --outputFormat=NH or NHX (obligatory)
 * 
 * outputFile (obligatory)
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NCBItoNH extends ToolWithNHOutput
{
	//Data to be extracted from the command line
	private String taxIDOfNodeToNarrowTo = null;
	private String sequenceName = null;
	private String internalNodes = null;
	private String outputFormat = null;
	
	private String outputFilePath = null;
	
	public static void main(String[] args)
	{
		NCBItoNH tool = new NCBItoNH();
		try
		{
			tool.execute(args);
		}
		catch (FatalException e)
		{
			tool.handleException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see tools.Tool#execute(java.lang.String[])
	 */
	protected void execute(String[] args) throws FatalException
	
	{
		parseCommandLine(args);
		
		//Read the serialized NCBI tree in
		UserInfo.println("Loading the NCBI tree (please be patient).");
		TreeNCBI treeNCBI = TreeNCBI.loadFromFile();
				
		//Narrow to the correct subtree
		CommandLineParsing.narrowToSubtree(treeNCBI, taxIDOfNodeToNarrowTo);
				
		//Build the NH or NHX tree
		TreeNH treeNH = null;
		if (outputFormat.equals(CommandLineParsing.NH_OPTION_VALUE))
		{
			if (sequenceName.equals(CommandLineParsing.SEQUENCE_NAME_SPECIES))
			{
				treeNH = new TreeNH(treeNCBI, false);
			}
			else
			{
				treeNH = new TreeNH(treeNCBI,true);
			}
			
		}
		else
		{
			if (sequenceName.equals(CommandLineParsing.SEQUENCE_NAME_SPECIES))
			{
				treeNH = new TreeNHX(treeNCBI, false);
			}
			else
			{
				treeNH = new TreeNHX(treeNCBI,true);
			}
		}
		
		//Write the NH or NHX tree to file
		if (internalNodes.equals(CommandLineParsing.YES))
		{
			writeNHTreeToFile(treeNH, outputFilePath, true);
		}
		else
		{
			writeNHTreeToFile(treeNH, outputFilePath, false);
		}
		UserInfo.println("Finished.");
	}

	/* (non-Javadoc)
	 * @see phylo3D.tools.Tool#parseCommandLine(java.lang.String[])
	 */
	protected void parseCommandLine(String[] args)
	throws FatalException
	{
		//Parse the command line
			if (args.length == 5)
			{
				taxIDOfNodeToNarrowTo = CommandLineParsing.extractNodeToNarrowTo(args[0]);
				sequenceName = CommandLineParsing.extractSequenceName(args[1]);
				internalNodes = CommandLineParsing.extractInternalNodes(args[2]);
				outputFormat = CommandLineParsing.extractOutputFormat(args[3]);				
				outputFilePath = args[4];//extract the output file path
			}
			else
			{
				throw new CommandLineParsingException("Incorrect number of arguments");
			}
	}	
	
	/* (non-Javadoc)
	 * @see tools.Tool#errorInCommandLine(java.lang.String)
	 */
	protected String toolUsageText()
	{
		StringBuffer toolUsageText = new StringBuffer(); 
		String newLine = "\n";
		toolUsageText.append("Usage: \"NCBItoNH  [-options] <NH or NHX output file name>\"" + newLine);
		toolUsageText.append("" + newLine);		
		toolUsageText.append("Options (which must appear in the following order):" + newLine);
		toolUsageText.append("--narrowTo=node (obligatory) where node is an NCBI taxonomy ID" + newLine);		
		toolUsageText.append("--sequenceName=S or T (obligatory)" + newLine);
		toolUsageText.append("--internalNodes=Y or N (obligatory)" + newLine);
		toolUsageText.append("--outputFormat=NH or NHX (obligatory)" + newLine);
		toolUsageText.append("" + newLine);
		return toolUsageText.toString();
	}	

}
