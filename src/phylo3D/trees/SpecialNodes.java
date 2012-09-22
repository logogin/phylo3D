/*
 * Created on 27-Aug-2003
 */
package phylo3D.trees;

/**
 *  NCBI Taxonomy IDs for the special nodes in the tree of life 
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class SpecialNodes
{
	public static final NodeDataNCBI ROOT = new NodeDataNCBI(new Integer(1));
	
	//Children of root
	public static final NodeDataNCBI  VIRUSES_ROOT= new NodeDataNCBI(new Integer(10239));
	public static final NodeDataNCBI  VIROIDS_ROOT= new NodeDataNCBI(new Integer(12884));
	public static final NodeDataNCBI  UNCLASSIFIED_ROOT= new NodeDataNCBI(new Integer(12908));
	public static final NodeDataNCBI  OTHER_SEQUENCES_ROOT= new NodeDataNCBI(new Integer(28384));
	public static final NodeDataNCBI  CELLULAR_ORGANISMS_ROOT= new NodeDataNCBI(new Integer(131567));
	
	//Children of cellular organisms
	public static final NodeDataNCBI  BACTERIA_ROOT= new NodeDataNCBI(new Integer(2));
	public static final NodeDataNCBI  ARCHEA_ROOT= new NodeDataNCBI(new Integer(2157));
	public static final NodeDataNCBI  EUKARYOTA_ROOT= new NodeDataNCBI(new Integer(2759));
	
	//Eutheria (Placentals)
	public static final NodeDataNCBI  EUTHERIA_ROOT= new NodeDataNCBI(new Integer(9347));
	
	//Children of Eutheria
	public static final NodeDataNCBI  CARNIVORA_ROOT= new NodeDataNCBI(new Integer(33554));//Canidae, felidae, mongooses, hyenas
	public static final NodeDataNCBI  CETARTIODACTYLA_ROOT= new NodeDataNCBI(new Integer(91561));//whales, hippo
	public static final NodeDataNCBI  CHIROPTERA_ROOT= new NodeDataNCBI(new Integer(9397));//bats
	public static final NodeDataNCBI  DERMOPTERA_ROOT= new NodeDataNCBI(new Integer(30656));//flying lemurs
	public static final NodeDataNCBI  EDENTATA_ROOT= new NodeDataNCBI(new Integer(9348));//armadillos
	public static final NodeDataNCBI  HYRACOIDA_ROOT= new NodeDataNCBI(new Integer(9810));//hyraxes
	public static final NodeDataNCBI  INSECTIVORA_ROOT= new NodeDataNCBI(new Integer(9362));//moles, shrews, hedgehogs
	public static final NodeDataNCBI  LAGOMORPHA_ROOT= new NodeDataNCBI(new Integer(9975));//rabbits, hares
	public static final NodeDataNCBI  MACROSCELIDEA_ROOT= new NodeDataNCBI(new Integer(28734));//elephant shrews
	public static final NodeDataNCBI  PERISSODACTYLA_ROOT= new NodeDataNCBI(new Integer(9787));//horses
	public static final NodeDataNCBI  PHOLIDOTA_ROOT= new NodeDataNCBI(new Integer(9971));//pangolins
	public static final NodeDataNCBI  PRIMATES_ROOT= new NodeDataNCBI(new Integer(9443));//apes
	public static final NodeDataNCBI  PROBOSCIDEA_ROOT= new NodeDataNCBI(new Integer(9779));//elephants
	public static final NodeDataNCBI  RODENTIA_ROOT= new NodeDataNCBI(new Integer(9989));//rodents
	public static final NodeDataNCBI  SCANDENTIA_ROOT= new NodeDataNCBI(new Integer(9392));//tree shrews
	public static final NodeDataNCBI  SIRENIA_ROOT= new NodeDataNCBI(new Integer(9774));//sea cow
	public static final NodeDataNCBI  TUBULIDENTATA_ROOT= new NodeDataNCBI(new Integer(9815));//ardvaark
	
	//Man
	public static final NodeDataNCBI  HUMAN_Node= new NodeDataNCBI(new Integer(9606));//man
	
	//Cetacea
	public static final NodeDataNCBI  CETACEA_ROOT= new NodeDataNCBI(new Integer(9721));//whales
	public static final NodeDataNCBI  MYSTICETI_ROOT= new NodeDataNCBI(new Integer(9761));//baleen whales
	public static final NodeDataNCBI  ODONTOCETI_ROOT= new NodeDataNCBI(new Integer(9722));//toothed whales
	
	//Primates
	public static final NodeDataNCBI  CATARRHINI_ROOT= new NodeDataNCBI(new Integer(9526));
	public static final NodeDataNCBI  OLDWORLDMONKEYS_ROOT= new NodeDataNCBI(new Integer(9527));
	public static final NodeDataNCBI  HOMINIDAE_ROOT= new NodeDataNCBI(new Integer(9604));
	public static final NodeDataNCBI  HYLOBATIDAE_ROOT= new NodeDataNCBI(new Integer(9577));
	
	public static final NodeDataNCBI  PLATYRRHINI_ROOT= new NodeDataNCBI(new Integer(9479));
	public static final NodeDataNCBI  MARMOSETS_ROOT= new NodeDataNCBI(new Integer(9480));
	public static final NodeDataNCBI  CEBIDAE_ROOT= new NodeDataNCBI(new Integer(9498));
	
	public static final NodeDataNCBI  PROSIMIANS_ROOT= new NodeDataNCBI(new Integer(9444));
	public static final NodeDataNCBI  DWARFLEMURS_ROOT= new NodeDataNCBI(new Integer(30615));
	public static final NodeDataNCBI  AYESAYES_ROOT= new NodeDataNCBI(new Integer(30613));
	public static final NodeDataNCBI  GALAGOS_ROOT= new NodeDataNCBI(new Integer(40297));
	public static final NodeDataNCBI  INDRIDAE_ROOT= new NodeDataNCBI(new Integer(30599));
	public static final NodeDataNCBI  LEMURS_ROOT= new NodeDataNCBI(new Integer(9445));
	public static final NodeDataNCBI  LORISES_ROOT= new NodeDataNCBI(new Integer(9461));
	public static final NodeDataNCBI  MEGACADAPIDAE_ROOT= new NodeDataNCBI(new Integer(30614));
	
	public static final NodeDataNCBI  TARSIERS_ROOT= new NodeDataNCBI(new Integer(9474));
	
	public static final NodeDataNCBI  UNCLASSIFIED_PRIMATES_ROOT= new NodeDataNCBI(new Integer(57118));
	
		
}
