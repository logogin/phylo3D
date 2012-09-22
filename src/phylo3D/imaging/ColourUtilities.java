/*
 * Created on 17-Dec-2003
 *
 */
package phylo3D.imaging;

import java.awt.*;
import java.util.*;

/**
 *  Uitilities for manipulating colours
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class ColourUtilities
{
	public static Color decodeHexColour(String colorRgbHex)
	{
	  Color result = new Color(extractRed(colorRgbHex), extractGreen(colorRgbHex), extractBlue(colorRgbHex));
	  return result;
	}

	private static int extractRed(String colorRgbHex)
	{
	  String temp = colorRgbHex.substring(0,2);
	  int result = convertHexToDec(temp);
	  return result;
	}

	private static int extractGreen(String colorRgbHex)
	{
	  String temp = colorRgbHex.substring(2,4);
	  int result = convertHexToDec(temp);
	  return result;
	}

	private static int extractBlue(String colorRgbHex)
	{
	  String temp = colorRgbHex.substring(4,6);
	  int result = convertHexToDec(temp);
	  return result;
	}

	private static int convertHexToDec (String hex)
	{
	   return Integer.decode("0x"+hex).intValue();
	}
	
	/**
	 * Given a number of colours needed, this function produces a set of such colours where the colours
	 * are as distinguishable as possible. The number of colours produced will usually be in excess of the 
	 * number needed, so it is possible to jumble the colours so that similar colours are not close together
	 * when one iterates through the vector.
	 * @param numberOfColours int number of colours required
	 * @param jumbled boolean indicating whether the colours should be jumbled
	 * @return Vector of colours which contains at least the number of colours required
	 */
	public static Vector determineColours(int numberOfColours, boolean jumbled)
	{
		Vector colours = new Vector();
		double third = 1.0/3.0;
		double cubeRoot = Math.pow(numberOfColours + 2, third);
		//System.out.println(cubeRoot + "");
		int divisor = (int)Math.ceil(cubeRoot - 1);
		//System.out.println(divisor + "");
		int increment = 255 / divisor;
		for (int i = 0; i <= divisor; i++)
		{
			for (int j = 0; j <= divisor; j++)
			{
				for (int k = 0; k <= divisor; k++)
				{
					Color newColour = new Color((int)Math.ceil(i*increment), (int)Math.ceil(j*increment), (int)Math.ceil(k*increment));
					//System.out.println(newColour);
					//Do not include black and white
					if (!(newColour.equals(Color.BLACK) || newColour.equals(Color.WHITE)))
					{
						colours.add(newColour);	
					}
				}
			}
		}
		
		Vector result = new Vector();
		
		if (jumbled)
		{
			//Take all even indexes
			for (int l = 0; l < colours.size(); l = l + 2)
			{
				result.add(colours.get(l));
			}
			//Take all odd indexes
			for (int m = 1; m < colours.size(); m = m + 2)
			{
				result.add(colours.get(m));
			}			
		}
		else
		{
			result = colours;
		}
		
		return result;
	}
	
	public static void main(String[] args)
	{
		System.out.println((determineColours(216, false).size()));
	}
}
