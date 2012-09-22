/*
 * Created on 22-Dec-2003
 */
package phylo3D.tools;

import java.io.*;


import phylo3D.utilities.*;
import phylo3D.trees.*;

/**
 *  Replace this text with a general description of the class
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
abstract public class ToolWithNHOutput extends Tool
{

	protected void writeNHTreeToFile(TreeNH treeNH, String filePath, boolean includeInternalNodes)
	throws FatalException
	{
		PrintWriter pw = FileOperations.getPrintWriter(filePath);
		UserInfo.println("Printing NH(X) tree to file: " + filePath);
		String temp = treeNH.toString(includeInternalNodes); 
		pw.write(temp);//Includes internal nodes
		pw.close();	
	}
}
