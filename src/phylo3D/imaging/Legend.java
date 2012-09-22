/*
 * Created on 13-Oct-2003
 */
package phylo3D.imaging;

import java.awt.*;


/**
 *  Draws the legend for a selected colouring
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class Legend
{
	private Dimension colourBoxDimension = new Dimension(50,25);
	
	private Color backgroundColor = Color.BLACK;
	private Color textColor = Color.WHITE;
	
	private int fontSize = 12;
	private int widthOfFontInPixels = 6;
	private Font font = new Font("courier", Font.PLAIN, fontSize);
	
	private int rightMargin = 25;
	private int leftMargin = 25;
	private int topMargin = 25;
	private int bottomMargin = 25;
	
	private int gapBetweenBoxAndText = 25;
	private int gapBetweenColorBoxes = 15;
	
	private String [] texts = null;
	private Color [] colors = null;
	private Dimension dimension = null;

	/**
	 * Constructor
	 * @param texts String[] of texts 
	 * @param rgbColors Color[] of colours to be used in legend
	 * @throws Exception If the number of texts and colours are not equal
	 */
	public Legend(String [] texts, Color [] rgbColors) throws Exception
	{
		if (texts.length != rgbColors.length)
		{
			throw new Exception("The number of colours and texts are not equal.");
		}
		
	  this.texts = texts;
	  this.colors = new Color [rgbColors.length];
	  for(int i = 0; i < rgbColors.length; i++)
	  {
		this.colors = rgbColors;
	  }
	  
	  //Calculate the dimension
	  int height = colourBoxDimension.height * colors.length + gapBetweenColorBoxes*(colors.length - 1)  + topMargin + bottomMargin;
	  int width = leftMargin + colourBoxDimension.width + gapBetweenBoxAndText + maxTextLength() * widthOfFontInPixels + rightMargin;
	  dimension = new Dimension(width, height);
	}

	/**
	 * Computes the maximum length of the texts (in number of characters)
	 * @return length of longest text in number of characters
	 */
	private int maxTextLength()
	{
		int result = 0;
		for (int i = 0; i < texts.length; i++)
		{
			if(texts[i].length() > result)
			{
				result = texts[i].length();
			}
		}
		return result;
	}

	public int getHeight()
	{
		return (int)dimension.getHeight();
	} 

	public int getWidth()
	{
		return (int)dimension.getWidth();
	}

	public String toString()
	{
	   String result = "";
	   result += "The texts are: ";
	   for (int i = 0; i < texts.length; i++)
	   {
		  result += "\n" + texts[i];
	   }
	   result += "\nThe colors are: ";
	   for (int i = 0; i < colors.length; i++)
	   {
		  result += "\n" + "R" + colors[i].getRed();
		  result += "G" + colors[i].getGreen();
		  result += "B" + colors[i].getBlue();
	   }
	   result += "\nThe height is: " + dimension.getHeight();
	   result += "\nThe width is: " + dimension.getWidth();
	   return result;
	}

	/**
	 * Draws the legend onto a Graphics object
	 * @param g Graphics object onto which the legend is drawn
	 */
	public void draw(Graphics g)
	{
		//Set the background colour
		g.fillRect(0, 0, dimension.width, dimension.height);
		
		//Draw the rectangles
	  Rectangle [] rectangles = new Rectangle [colors.length];
	  Point nextPoint = new Point(leftMargin, topMargin);
	  for (int i = 0; i < rectangles.length; i++)
	  {
		rectangles[i] = new Rectangle(nextPoint, colourBoxDimension);

		g.setColor(colors[i]);
		g.fillRect((int)rectangles[i].getX(), (int)rectangles[i].getY(), (int)rectangles[i].getWidth(), (int)rectangles[i].getHeight());
		
		g.setColor(textColor);
		g.drawString(texts[i], nextPoint.x + colourBoxDimension.width + gapBetweenBoxAndText, 
			nextPoint.y +(colourBoxDimension.height/2 + fontSize/2));

		nextPoint = new Point (nextPoint.x, nextPoint.y + colourBoxDimension.height + gapBetweenColorBoxes);
	  }
	}

	//test method
	public static Legend instantiateTestLegend() throws Exception
	  {
		  String [] texts = {"One", "Two", "Three and half times the world and why not try adding even more this seems to be working but difficult to be sure", "four", "five", "six"};
		  Color [] colors = {new Color(0,0,255), new Color(255,0,0), new Color(50,125,145),new Color(120,120,120), new Color(0,255,0), new Color(0,60,0)};
		  Legend theLegend = new Legend(texts, colors);
		  return theLegend;
	  }

	public static void main (String args []) throws Exception
	{
	  System.out.println(Legend.instantiateTestLegend());
	}
}
