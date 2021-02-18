package flatlandCharacters;
import simonGraphics.PointD;
import simonGraphics.PolygonD;
import simonGraphics.Vector;


/**
 * Generic man class for Flatlanders
 * 
 * all men are polygons
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Man extends Person
{

	public Man(String name, int numSides, PointD center, double size)
	{
		super(name);
		boundary = generatePolygon(numSides, center, size);
		this.center = center;
		orientation = new Vector(center, boundary.getVert(0));
	}	

	/**
	 * 
	 * @param sides
	 * @param center
	 * @param size  px from center to vert
	 * @return
	 */
	public PolygonD generatePolygon(int sides, PointD center, double size)
	{
		
		
		PolygonD p = new PolygonD();
		Vector v = new Vector(0, size,center);
		p.add(v.getHead());
		double angle = (360./sides);	//external angle
		for(int i = 0; i<sides; i++)
		{
			v = v.rotate(angle);
			p.add(v.getHead());
		}
		return p;
	}
}
