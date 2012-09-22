/*
 * Created on 16-Sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package phylo3D.datafiles;
import phylo3D.utilities.*;

import java.net.*;
import java.io.*;

/**
 *  Class holding data file names and providing methods for locating these resources
 *
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class DataFileNames
{
    private static final String sep = "/";//this is the separator for URLs

    //Directories
    private static final String DIR_DATA = "data";
    private static final String DIR_NCBI = "ncbi";
    private static final String DIR_TEST = "test";
    private static final String DIR_INPUT = "input";
    private static final String DIR_OUTPUT = "output";

    //File names
    private static final String FILE_NCBI_NODES = "nodesSep2012.dmp"; //"nodesSep2003.dmp"
    private static final String FILE_NCBI_NAMES = "namesSep2012.dmp";//"namesSep2003.dmp"

    private static final String FILE_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED = "allNCBIDataInNCBITree.ser";
    private static final String FILE_ALL_NCBI_DATA_IN_FORESTER_TREE_SERIALIZED = "allNCBIDataInForesterTree.ser";
    private static final String FILE_ALL_NCBI_DATA_WITH_SPECIES_IN_NH = "allNCBIDataWithSpeciesNames.nh";
    private static final String FILE_ALL_NCBI_DATA_WITH_TAXIDS_IN_NH = "allNCBIDataWithTaxIDs.nh";
    private static final String FILE_ALL_NCBI_DATA_IN_NHX = "allNCBIData.nhx";
    private static final String FILE_CELLULAR_NCBI_DATA_IN_WALRUS = "cellularNCBIData.graph";
    private static final String FILE_EUTHERIA_NCBI_DATA_WITH_SPECIES_IN_NH = "eutheriaNCBIDataWithSpeciesNames.nh";
    private static final String FILE_EUTHERIA_NCBI_DATA_WITH_TAXIDS_IN_NH = "eutheriaNCBIDataWithTaxIDs.nh";
    private static final String FILE_EUTHERIA_DATA_IN_NHX = "eutheriaNCBIData.nhx";
    private static final String FILE_EUTHERIA_NCBI_DATA_IN_WALRUS = "eutheriaNCBIData.graph";
    private static final String FILE_CETACEA_NCBI_DATA_WITH_SPECIES_IN_NH = "cetaceaNCBIDataWithSpeciesNames.nh";
    private static final String FILE_CETACEA_NCBI_DATA_WITH_TAXIDS_IN_NH = "cetaceaNCBIDataWithTaxIDs.nh";
    private static final String FILE_CETACEA_DATA_IN_NHX = "cetaceaNCBIData.nhx";
    private static final String FILE_CETACEA_NCBI_DATA_IN_WALRUS = "cetaceaNCBIData.graph";
    private static final String FILE_PRIMATES_NCBI_DATA_WITH_SPECIES_IN_NH = "primatesNCBIDataWithSpeciesNames.nh";
    private static final String FILE_PRIMATES_NCBI_DATA_WITH_TAXIDS_IN_NH = "primatesNCBIDataWithTaxIDs.nh";
    private static final String FILE_PRIMATES_DATA_IN_NHX = "primatesNCBIData.nhx";
    private static final String FILE_PRIMATES_NCBI_DATA_IN_WALRUS = "primatesNCBIData.graph";

    private static final String FILE_SIMPLE_NH = "simple.nh";
    private static final String FILE_SIMPLE_NHX = "simple.nhx";
    private static final String FILE_MEDIUM_NH = "medium.nh";
    private static final String FILE_MEDIUM_NHX = "medium.nhx";
    private static final String FILE_COMPLEX_NH = "complex.nh";
    private static final String FILE_COMPLEX_NHX = "complex.nhx";

    private static final String FILE_TEST_OUTPUT_WALRUS = "testOutput.graph";
    private static final String FILE_TEST_OUTPUT_NH = "testOutput.nh";
    private static final String FILE_TEST_OUTPUT_NHX = "testOutput.nhx";

    //Relative paths
    private static final String temp1 = DIR_DATA + sep + DIR_NCBI + sep + DIR_INPUT + sep;
    public static final String REL_PATH_NCBI_NODES =  temp1 + FILE_NCBI_NODES;
    public static final String REL_PATH_NCBI_NAMES = temp1 + FILE_NCBI_NAMES;

    private static final String temp2 = DIR_DATA + sep + DIR_NCBI + sep + DIR_OUTPUT + sep;
    public static final String REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED =temp2 + FILE_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED;
    public  static final String REL_PATH_ALL_NCBI_DATA_IN_FORESTER_TREE_SERIALIZED = temp2 + FILE_ALL_NCBI_DATA_IN_FORESTER_TREE_SERIALIZED;
    public static final String REL_PATH_ALL_NCBI_DATA_WITH_SPECIES_IN_NH = temp2 + FILE_ALL_NCBI_DATA_WITH_SPECIES_IN_NH;
    public static final String REL_PATH_ALL_NCBI_DATA_WITH_TAXIDS_IN_NH = temp2 + FILE_ALL_NCBI_DATA_WITH_TAXIDS_IN_NH;
    public static final String REL_PATH_ALL_NCBI_DATA_IN_NHX = temp2 + FILE_ALL_NCBI_DATA_IN_NHX;
    public static final String REL_PATH_CELLULAR_NCBI_DATA_IN_WALRUS = temp2 + FILE_CELLULAR_NCBI_DATA_IN_WALRUS;
    public static final String REL_PATH_EUTHERIA_NCBI_DATA_WITH_SPECIES_IN_NH = temp2 + FILE_EUTHERIA_NCBI_DATA_WITH_SPECIES_IN_NH;
    public static final String REL_PATH_EUTHERIA_NCBI_DATA_WITH_TAXIDS_IN_NH = temp2 + FILE_EUTHERIA_NCBI_DATA_WITH_TAXIDS_IN_NH;
    public static final String REL_PATH_EUTHERIA_DATA_IN_NHX = temp2 + FILE_EUTHERIA_DATA_IN_NHX;
    public static final String REL_PATH_EUTHERIA_NCBI_DATA_IN_WALRUS = temp2 + FILE_EUTHERIA_NCBI_DATA_IN_WALRUS;
    public static final String REL_PATH_CETACEA_NCBI_DATA_WITH_SPECIES_IN_NH = temp2 + FILE_CETACEA_NCBI_DATA_WITH_SPECIES_IN_NH;
    public static final String REL_PATH_CETACEA_NCBI_DATA_WITH_TAXIDS_IN_NH = temp2 + FILE_CETACEA_NCBI_DATA_WITH_TAXIDS_IN_NH;
    public static final String REL_PATH_CETACEA_DATA_IN_NHX = temp2 + FILE_CETACEA_DATA_IN_NHX;
    public static final String REL_PATH_CETACEA_NCBI_DATA_IN_WALRUS = temp2 + FILE_CETACEA_NCBI_DATA_IN_WALRUS;
    public static final String REL_PATH_PRIMATES_NCBI_DATA_WITH_SPECIES_IN_NH = temp2 + FILE_PRIMATES_NCBI_DATA_WITH_SPECIES_IN_NH;
    public static final String REL_PATH_PRIMATES_NCBI_DATA_WITH_TAXIDS_IN_NH = temp2 + FILE_PRIMATES_NCBI_DATA_WITH_TAXIDS_IN_NH;
    public static final String REL_PATH_PRIMATES_DATA_IN_NHX = temp2 + FILE_PRIMATES_DATA_IN_NHX;
    public static final String REL_PATH_PRIMATES_NCBI_DATA_IN_WALRUS = temp2 + FILE_PRIMATES_NCBI_DATA_IN_WALRUS;

    private static final String  temp3 = DIR_DATA + sep + DIR_TEST + sep + DIR_INPUT + sep;
    public static final String REL_PATH_SIMPLE_NH = temp3 + FILE_SIMPLE_NH;
    public static final String REL_PATH_SIMPLE_NHX = temp3 + FILE_SIMPLE_NHX;
    public static final String REL_PATH_MEDIUM_NH = temp3 + FILE_MEDIUM_NH;
    public static final String REL_PATH_MEDIUM_NHX = temp3 + FILE_MEDIUM_NHX;
    public static final String REL_PATH_COMPLEX_NH = temp3 + FILE_COMPLEX_NH;
    public static final String REL_PATH_COMPLEX_NHX = temp3 + FILE_COMPLEX_NHX;

    private static final String temp4 = DIR_DATA + sep + DIR_TEST + sep + DIR_OUTPUT + sep;
    public static final String REL_PATH_TEST_OUTPUT_WALRUS = temp4 + FILE_TEST_OUTPUT_WALRUS;
    public static final String REL_PATH_TEST_OUTPUT_NH = temp4 + FILE_TEST_OUTPUT_NH;
    public static final String REL_PATH_TEST_OUTPUT_NHX = temp4 + FILE_TEST_OUTPUT_NHX;

    /**
     * Attempts to locate the URL for the specified resource
     * @param filename A "/" separated path to the resource
     * @return URL for the specified resource
     */
    public static URL fileURL(String filename)
    {
        URL theURL = ClassLoader.getSystemResource(filename);
        return theURL;
    }

    /**
     * Attempts to locate the filePath to the specified resource
     * @param filename A "/" separated path to the resource
     * @return String representing absolute filepath to the resource
     */
    public static String filePath(String filename)
    {
        String  urlPath= fileURL(filename).toString();
        URI theURI = null;
        try
        {
            theURI = new URI (urlPath);
        }
        catch (URISyntaxException use)
        {
            Debug.println(use.getMessage());
        }

        File theFile = new File(theURI);
        return theFile.toString();
    }

    private static void test()
    {
        Debug.println(fileURL(REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED).toString());
        Debug.println("");
        Debug.println(filePath(REL_PATH_ALL_NCBI_DATA_IN_NCBI_TREE_SERIALIZED));
    }

    public static void main(String[] args)
    {
        test();
    }

}
