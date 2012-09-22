/*
 * Created on 11-Sep-2003
 */
package phylo3D.trees;

import forester.tree.*;

import phylo3D.utilities.*;

/**
 *  Class acting as a data holder for the nodes of TreeNHX
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class NodeDataNHX extends NodeDataNH
{
	private Integer bootstrapValue;
	private String speciesName;
	private Integer taxonomyID;
	private String ecNumber;
	private Boolean duplication;
	private Integer orthologous;
	private Integer superOrthologous;
	private Float logLikelihoodValueOnParentBranch;  

	public NodeDataNHX(Integer ID, Integer parentID)
	{
		super(ID, parentID);
	}
	
	public NodeDataNHX(NodeDataNCBI ndn, boolean useTaxIDAsSeqName)
	{
		super(ndn, useTaxIDAsSeqName);
		speciesName = ndn.getNameText();
		taxonomyID = ndn.getTaxonomyID();
	}
	
	public NodeDataNHX(Node n)
	{
		super(n);
		if(n.getSpecies() != null && !n.getSpecies().equals(Constants.EMPTY_STRING))
		{
			speciesName = n.getSpecies();
		}
		if (n.getTaxonomyID() != Node.TAXO_ID_DEFAULT && n.getTaxonomyID() > 0)
		{
			taxonomyID = new Integer(n.getTaxonomyID());	
		}
		if(n.getECnumber() != null && !n.getECnumber().equals(Constants.EMPTY_STRING))
		{
			ecNumber = n.getECnumber();
		}
		if (n.isLnLonParentBranchAssigned())
		{
			logLikelihoodValueOnParentBranch = new Float(n.getLnLonParentBranch());	
		}
		
		if (!n.isExternal())
		{
			if (n.isDuplicationOrSpecAssigned())
			{
				duplication = new Boolean(n.isDuplication());
			}
			if(n.getBootstrap() != Node.BOOTSTRAP_DEFAULT)
			{
				bootstrapValue = new Integer(n.getBootstrap());
			}
		}
		else
		{
			if (n.getOrthologous() != 0)
			{
				orthologous = new Integer(n.getOrthologous());
			}
			if (n.getSuperOrthologous() != 0)
			{
				superOrthologous = new Integer(n.getSuperOrthologous());
			}
		}			
	}	

	public Integer getBootstrapValue()
	{
		return bootstrapValue;
	}

	public Boolean isDuplication()
	{
		return duplication;
	}

	public String getEcNumber()
	{
		return ecNumber;
	}

	public Float getLogLikelihoodValueOnParentBranch()
	{
		return logLikelihoodValueOnParentBranch;
	}

	public Integer getOrthologous()
	{
		return orthologous;
	}

	public void setBootstrapValue(Integer integer)
	{
		bootstrapValue = integer;
	}

	public void setDuplication(Boolean b)
	{
		duplication = b;
	}

	public void setEcNumber(String string)
	{
		ecNumber = string;
	}

	public void setLogLikelihoodValueOnParentBranch(Float float1)
	{
		logLikelihoodValueOnParentBranch = float1;
	}

	public void setOrthologous(Integer integer)
	{
		orthologous = integer;
	}

	public String getSpeciesName()
	{
		return speciesName;
	}

	public Integer getSuperOrthologous()
	{
		return superOrthologous;
	}

	public Integer getTaxonomyID()
	{
		return taxonomyID;
	}

	public void setSpeciesName(String string)
	{
		speciesName = string;
	}

	public void setSuperOrthologous(Integer integer)
	{
		superOrthologous = integer;
	}

	public void setTaxonomyID(Integer integer)
	{
		taxonomyID = integer;
	}

}
