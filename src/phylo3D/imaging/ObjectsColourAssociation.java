/*
 * Created on 17-Dec-2003
 */
package phylo3D.imaging;

import java.util.*;
import java.awt.*;

/**
 *  Class holding objects and colour associations
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class ObjectsColourAssociation
{
	private Vector objects;
	private Color colour;
	
	public ObjectsColourAssociation()
	{
		super();
		objects = new Vector();
	}
	
	public ObjectsColourAssociation(Vector nodes, Color colour)
	{
		super();
		this.objects = nodes;
		this.colour = colour;
	}
	
	public boolean isJustOneObjectInAssociation()
	{
		return objects.size() == 1;
	}
	
	public Object getFirstObject()
	{
		return objects.get(0);
	}
	
	public Object getSecondObject()
	{
		return objects.get(1);
	}
	
	public Object getThirdObject()
	{
		return objects.get(2);
	}		
	
	public void setObjects(Vector objects)
	{
		this.objects = objects;
	}
	
	public Iterator getObjectsIterator()
	{
		return objects.iterator();
	}

	public void addObject(Object o)
	{
		objects.add(o);
	}

	/**
	 * Gets the colour associated with the nodes held in this class
	 * @return Color of nodes
	 */
	public Color getColour()
	{
		return colour;
	}

	/**
	 * Sets the colour associated with the nodes held in this class
	 * @param colour Color of the nodes
	 */
	public void setColour(Color colour)
	{
		this.colour = colour;
	}

}
