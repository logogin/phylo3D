package phylo3D.imaging;

import java.io.*;
import java.awt.*;

/**
 *  Class implementing functinality for printing a Legend object to file
 * 
 * @author Tim Hughes (Computational Biology Unit - University of Bergen - Norway)
 */
public class Drawer
{

	/**
	 * Prints a legend to file in jpeg format
	 * @param theLegend Legend to be drawn
	 * @param f File to draw legend to
	 * @throws FileNotFoundException if cannot find file to write to
	 * @throws IOException if cannot access file to write to
	 */
	public static void draw(Legend theLegend, File f)
		throws FileNotFoundException, IOException
	{
		FileOutputStream out = new FileOutputStream(f); // binary output!

		//Prepare to draw the legend
		Frame frame = null;
		Graphics g = null;

		try
		{
			frame = new Frame(); //Create an unshown frame
			frame.addNotify();
			Image image =
				frame.createImage(theLegend.getWidth(), theLegend.getHeight());
			//Get an image from the frame with same dimensions as bar
			g = image.getGraphics(); //Get a graphics object from the image
			theLegend.draw(g); //Draw the bar on the graphics object
			JpegEncoder jpg = new JpegEncoder(image, 50, out);
			//encode the image
			jpg.Compress(); //Compress the image
		} finally
		{
			// Clean up resources
			if (g != null)
				g.dispose();
			if (frame != null)
				frame.removeNotify();
		}
		out.close();

	}

	public static void test() throws Exception
	{
		File f =
			new File("/Home/strandfuru/tim/data/project_treeOfLife/junk/tester.jpg");

		Legend theLegend = Legend.instantiateTestLegend();
		draw(theLegend, f);


	}

	public static void main(String[] args) throws Exception
	{
		test();
	}

}
