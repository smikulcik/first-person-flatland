package flatlandCharacters;
import simonGraphics.PointD;
import simonGraphics.PolygonD;
import simonGraphics.Vector;



/**
 * Class for the character that the user manipulates
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Avatar extends Person
{
	
	/**
	 * Our avatar is aSquare, so make boundary a square
	 * @param name
	 * @param center
	 */
	public Avatar(String name, PointD center)
	{
		super(name);
		boundary = new PolygonD();
		boundary.add(new PointD(50,50));
		boundary.add(new PointD(50,-50));
		boundary.add(new PointD(-50,-50));
		boundary.add(new PointD(-50,50));
		
		//shift all points to the center
		boundary.translate(new Vector(new PointD(0,0), center));
		
		this.center = center;
		orientation = new Vector(-50,0,center);
	}

	
	
}
