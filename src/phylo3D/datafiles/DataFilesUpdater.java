/*
 * Created on 18-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package phylo3D.datafiles;

import phylo3D.tools.*;
import phylo3D.trees.*;
import phylo3D.utilities.*;

/**
 *  Contains functionality for updating the main data files and in particular the serialized NCBITree
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class DataFilesUpdater
{
	private static final String START = "Started updating: ";
	private static final String END = " - Finished";
	
	/**
	 * Update the serialized NCBITree
	 * @param treeNCBI TreeNCBI with the new data
	 */
	private static void updateAllNCBIDataInNCBITree(TreeNCBI treeNCBI) throws FatalException
	{
		UserInfo.print(START + DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED));
		treeNCBI.saveToFile();
		UserInfo.println(END);
	}
	

	//This method is not usable: it takes tool long to read the NHX file from disc and then throws an
	//exception when trying to serialize the Forrester tree (this is due to the fact that the tree is a nested
	//structure)
	/*
	private static void updateAllNCBIDataInForesterTree(TreeNCBI treeNCBI)
	{
		Debug.print(START + DataFileNames.ABS_PATH_ALL_NCBI_DATA_IN_FORESTER_TREE_SERIALIZED);
		//Make sure the allNCBIData.nhx has been updated
		updateAllNCBIDataInNHX();
		//Read this file in
		forester.tree.Tree foresterTree = null;
		try
		{
			foresterTree = TreeHelper.readNHtree(new File(DataFileNames.ABS_PATH_ALL_NCBI_DATA_IN_NHX));	
		}
		catch (Exception e)
		{
			Debug.println("Unable to read NH tree form file: " + DataFileNames.ABS_PATH_ALL_NCBI_DATA_IN_NHX);
			FileOperations.exit();
		}
		Debug.println("Getting ready to serialise Forrester tree");
		FileOperations.serializeObjectToFile(DataFileNames.ABS_PATH_ALL_NCBI_DATA_IN_FORESTER_TREE_SERIALIZED, foresterTree);
		Debug.println(END);
	}*/
	
	/**
	 * Updates the NCBI data in NH format 
	 */	
	private static void updateAllNCBIDataInNH()
	{
		//taxIDs
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_WITH_TAXIDS_IN_NH));
		String[] argsTax = {"--narrowTo=" + SpecialNodes.ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_WITH_TAXIDS_IN_NH)};
		NCBItoNH.main(argsTax);		
		UserInfo.println("");
		
				
		//species
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_WITH_SPECIES_IN_NH));
		String[] argsSpecies = {"--narrowTo=" + SpecialNodes.ROOT.getID().toString(),
		"--sequenceName=S", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_WITH_SPECIES_IN_NH)};
		NCBItoNH.main(argsSpecies);
		UserInfo.println("");
	}
	
	/**
	 * Updates the NCBI data in NHX format
	 */
	private static void updateAllNCBIDataInNHX()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_IN_NHX));
		String[] argsTax = {
		"--narrowTo=" + SpecialNodes.ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NHX",
		DataFileNames.filePath(DataFileNames.REL_PATH_ALL_NCBI_DATA_IN_NHX)
		};
		NCBItoNH.main(argsTax);
		UserInfo.println("");	
	}
	
	/**
	 * Updates the Cellular NCBI data in Walrus format
	 */
	private static void updateCellularNCBIDataInWalrus()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_CELLULAR_NCBI_DATA_IN_WALRUS));
		
		String[] args = 
		{
		"--narrowTo=" + SpecialNodes.CELLULAR_ORGANISMS_ROOT.getID().toString(),
		"--colours=" + 
		SpecialNodes.ARCHEA_ROOT.getID().toString() + "," + "ff0000" + "+" +
		SpecialNodes.BACTERIA_ROOT.getID().toString() + "," + "ff7d00" + "+" +
		SpecialNodes.EUKARYOTA_ROOT.getID().toString() + "," + "ffff00",
		"--colourSubtreeRootBranch=N",
		DataFileNames.filePath(DataFileNames.REL_PATH_CELLULAR_NCBI_DATA_IN_WALRUS)
		};
		NCBItoWalrus.main(args);
		UserInfo.println("");
	}
	
	
	/**
	 * Updates the eutheria NCBI data in Walrus format
	 */
	private static void updateEutheriaNCBIDataInWalrus()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_IN_WALRUS));
		
		String[] args = 
		{
		"--narrowTo=" + SpecialNodes.EUTHERIA_ROOT.getID().toString(),
		"--colours=" + 
		SpecialNodes.CARNIVORA_ROOT.getID().toString() + "," + "ff0000" + "+" +
		SpecialNodes.CETARTIODACTYLA_ROOT.getID().toString() + "," + "ff7d00" + "+" +
		SpecialNodes.CHIROPTERA_ROOT.getID().toString() + "," + "ffff00" + "+" +
		SpecialNodes.DERMOPTERA_ROOT.getID().toString() + "," + "00ff00" + "+" +
		SpecialNodes.EDENTATA_ROOT.getID().toString() + "," + "00ff7d" + "+" +
		SpecialNodes.HYRACOIDA_ROOT.getID().toString() + "," + "00ffff" + "+" +
		SpecialNodes.INSECTIVORA_ROOT.getID().toString() + "," + "0000ff" + "+" +
		SpecialNodes.LAGOMORPHA_ROOT.getID().toString() + "," + "007dff" + "+" +
		SpecialNodes.MACROSCELIDEA_ROOT.getID().toString() + "," + "ffff7d" + "+" +
		SpecialNodes.PERISSODACTYLA_ROOT.getID().toString() + "," + "00ffff" + "+" +
		SpecialNodes.PHOLIDOTA_ROOT.getID().toString() + "," + "ff007d" + "+" +
		SpecialNodes.PRIMATES_ROOT.getID().toString() + "," + "ff00ff" + "+" +
		SpecialNodes.PROBOSCIDEA_ROOT.getID().toString() + "," + "7dff00" + "+" +
		SpecialNodes.RODENTIA_ROOT.getID().toString() + "," + "7d00ff" + "+" +
		SpecialNodes.SCANDENTIA_ROOT.getID().toString() + "," + "7d00ff" + "+" +
		SpecialNodes.SIRENIA_ROOT.getID().toString() + "," + "ff7dff" + "+" +
		SpecialNodes.TUBULIDENTATA_ROOT.getID().toString() + "," + "7dffff" + "+" +
		SpecialNodes.HUMAN_Node.getID().toString() + "," + "f1e580",
		"--colourSubtreeRootBranch=N",
		DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_IN_WALRUS)
		};
		NCBItoWalrus.main(args);
		UserInfo.println("");
		
		
	}
	
	/**
	 * Updates the eutheria NCBI data in NHX format
	 */
	private static void updateEutheriaNCBIDataInNHX()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_DATA_IN_NHX));
		String[] argsTax = {
		"--narrowTo=" + SpecialNodes.EUTHERIA_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NHX",
		DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_DATA_IN_NHX)
		};
		NCBItoNH.main(argsTax);
		UserInfo.println("");
	}
	
	/**
	 * Updates the eutheria NCBI data in NH format
	 */
	private static void updateEutheriaNCBIDataInNH()
	{
		//taxIDs
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_WITH_TAXIDS_IN_NH));
		String[] argsTax = {"--narrowTo=" + SpecialNodes.EUTHERIA_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_WITH_TAXIDS_IN_NH)};
		NCBItoNH.main(argsTax);		
		UserInfo.println("");
		
				
		//species
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_WITH_SPECIES_IN_NH));
		String[] argsSpecies = {"--narrowTo=" + SpecialNodes.EUTHERIA_ROOT.getID().toString(),
		"--sequenceName=S", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_EUTHERIA_NCBI_DATA_WITH_SPECIES_IN_NH)};
		NCBItoNH.main(argsSpecies);
		UserInfo.println("");		
	}	
	
	/**
	 * Updates the cetacea NCBI data in Walrus format
	 */
	private static void updateCetaceaNCBIDataInWalrus()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_IN_WALRUS));
		
		String[] args = 
		{
		"--narrowTo=" + SpecialNodes.CETACEA_ROOT.getID().toString(),
		"--colours=" + 
		SpecialNodes.MYSTICETI_ROOT.getID().toString() + "," + "00ff00" + "+" +
		SpecialNodes.ODONTOCETI_ROOT.getID().toString() + "," + "0000ff",	
		"--colourSubtreeRootBranch=N",
		DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_IN_WALRUS)
		};
		NCBItoWalrus.main(args);
		UserInfo.println("");
		
		
	}
	
	/**
	 * Updates the cetacea NCBI data in NHX format
	 */
	private static void updateCetaceaNCBIDataInNHX()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_DATA_IN_NHX));
		String[] argsTax = {
		"--narrowTo=" + SpecialNodes.CETACEA_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NHX",
		DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_DATA_IN_NHX)
		};
		NCBItoNH.main(argsTax);
		UserInfo.println("");
	}
	
	/**
	 * Updates the cetacea NCBI data in NH format
	 */
	private static void updateCetaceaNCBIDataInNH()
	{
		//taxIDs
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_WITH_TAXIDS_IN_NH));
		String[] argsTax = {"--narrowTo=" + SpecialNodes.CETACEA_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_WITH_TAXIDS_IN_NH)};
		NCBItoNH.main(argsTax);		
		UserInfo.println("");
		
				
		//species
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_WITH_SPECIES_IN_NH));
		String[] argsSpecies = {"--narrowTo=" + SpecialNodes.CETACEA_ROOT.getID().toString(),
		"--sequenceName=S", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_CETACEA_NCBI_DATA_WITH_SPECIES_IN_NH)};
		NCBItoNH.main(argsSpecies);
		UserInfo.println("");		
	}		
	
	/**
	 * Updates the cetacea NCBI data in Walrus format
	 */
	private static void updatePrimatesNCBIDataInWalrus()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_IN_WALRUS));
		
		String[] args = 
		{
		"--narrowTo=" + SpecialNodes.PRIMATES_ROOT.getID().toString(),
		"--colours=" +
		SpecialNodes.CATARRHINI_ROOT.getID().toString() + "," + "0000ff" + "+" +
		SpecialNodes.OLDWORLDMONKEYS_ROOT.getID().toString() + "," + "3030ff" + "+" +
		SpecialNodes.HOMINIDAE_ROOT.getID().toString() + "," + "6060ff" + "+" +
		SpecialNodes.HYLOBATIDAE_ROOT.getID().toString() + "," + "9090ff" + "+" +
		SpecialNodes.HUMAN_Node.getID().toString() + "," + "f1e580" + "+" + 		
		SpecialNodes.PLATYRRHINI_ROOT.getID().toString() + "," + "00ff00" + "+" +
		SpecialNodes.MARMOSETS_ROOT.getID().toString() + "," + "50ff50" + "+" +
		SpecialNodes.CEBIDAE_ROOT.getID().toString() + "," + "40ff40" + "+" +
		SpecialNodes.PROSIMIANS_ROOT.getID().toString() + "," + "ff0000" + "+" +
		SpecialNodes.DWARFLEMURS_ROOT.getID().toString() + "," + "ff2020" + "+" +
		SpecialNodes.AYESAYES_ROOT.getID().toString() + "," + "ff4040" + "+" +
		SpecialNodes.GALAGOS_ROOT.getID().toString() + "," + "ff6060" + "+" +
		SpecialNodes.INDRIDAE_ROOT.getID().toString() + "," + "ff8080" + "+" +
		SpecialNodes.LEMURS_ROOT.getID().toString() + "," + "ffa0a0" + "+" +
		SpecialNodes.LORISES_ROOT.getID().toString() + "," + "ffc0c0" + "+" +
		SpecialNodes.MEGACADAPIDAE_ROOT.getID().toString() + "," + "ffe0e0" + "+" +
		SpecialNodes.TARSIERS_ROOT.getID().toString() + "," + "cccccc" + "+" +
		SpecialNodes.UNCLASSIFIED_PRIMATES_ROOT.getID().toString() + "," + "ffffff",
		"--colourSubtreeRootBranch=N",
		DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_IN_WALRUS)
		};
		NCBItoWalrus.main(args);
		UserInfo.println("");
		
		
	}
	
	/**
	 * Updates the cetacea NCBI data in NHX format
	 */
	private static void updatePrimatesNCBIDataInNHX()
	{
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_DATA_IN_NHX));
		String[] argsTax = {
		"--narrowTo=" + SpecialNodes.PRIMATES_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NHX",
		DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_DATA_IN_NHX)
		};
		NCBItoNH.main(argsTax);
		UserInfo.println("");
	}
	
	/**
	 * Updates the cetacea NCBI data in NH format
	 */
	private static void updatePrimatesNCBIDataInNH()
	{
		//taxIDs
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_WITH_TAXIDS_IN_NH));
		String[] argsTax = {"--narrowTo=" + SpecialNodes.PRIMATES_ROOT.getID().toString(),
		"--sequenceName=T", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_WITH_TAXIDS_IN_NH)};
		NCBItoNH.main(argsTax);		
		UserInfo.println("");
		
				
		//species
		UserInfo.println(START + DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_WITH_SPECIES_IN_NH));
		String[] argsSpecies = {"--narrowTo=" + SpecialNodes.PRIMATES_ROOT.getID().toString(),
		"--sequenceName=S", 
		"--internalNodes=Y",
		"--outputFormat=NH",
		DataFileNames.filePath(DataFileNames.REL_PATH_PRIMATES_NCBI_DATA_WITH_SPECIES_IN_NH)};
		NCBItoNH.main(argsSpecies);
		UserInfo.println("");		
	}			
	
	/**
	 * Updates all data by calling other methods
	 */
	private static void updateNCBITreeFiles() throws FatalException
	{
		
		TreeNCBI treeNCBI = new TreeNCBI(DataFileNames.fileURL(DataFileNames.REL_PATH_NCBI_NODES),
		DataFileNames.fileURL(DataFileNames.REL_PATH_NCBI_NAMES));
		updateAllNCBIDataInNCBITree(treeNCBI);//Only method on which programs are dependent
		
		//updateAllNCBIDataInForesterTree(treeNCBI);//Method not working, see method definition for explanation
		
		updateAllNCBIDataInNHX();//Not needed for program - useful for demonstrations
		updateAllNCBIDataInNH();//Not needed for program - useful for demonstrations
		
		updateCellularNCBIDataInWalrus();//Not needed for program - useful for demonstrations
		
		updateEutheriaNCBIDataInNH();//Not needed for program - useful for demonstrations
		updateEutheriaNCBIDataInNHX();//Not needed for program - useful for demonstrations
		updateEutheriaNCBIDataInWalrus();//Not needed for program - useful for demonstrations
		
		updateCetaceaNCBIDataInNH();//Not needed for program - useful for demonstrations
		updateCetaceaNCBIDataInNHX();//Not needed for program - useful for demonstrations
		updateCetaceaNCBIDataInWalrus();//Not needed for program - useful for demonstrations
			
		updatePrimatesNCBIDataInNH();//Not needed for program - useful for demonstrations
		updatePrimatesNCBIDataInNHX();//Not needed for program - useful for demonstrations
		updatePrimatesNCBIDataInWalrus();//Not needed for program - useful for demonstrations		
	}
	
	public static void main (String[] args) throws FatalException
	{
	   updateNCBITreeFiles();
	}	
}
