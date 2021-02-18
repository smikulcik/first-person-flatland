package flatlandCharacters;
import simonGraphics.PointD;
import simonGraphics.PolygonD;
import simonGraphics.Vector;


/**
 * Woman in Flatland
 * 
 * all women are lines
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Woman extends Person {

	public Woman(String name, PointD center, double size) {
		super(name);
		boundary = new PolygonD();
		boundary.add(new PointD(0,size/2));
		boundary.add(new PointD(0,-size/2));
		boundary.translate(new Vector(new PointD(0,0),center));

		this.center = center;
		orientation = new Vector(center, boundary.getVert(0));
	}
	
	public void updatePhysics() {
		this.rotate(this.getAngularVelocity());
		this.translate(this.getVelocity());
		
		this.angularVelocity = 2*Math.sin(System.nanoTime()/100000000.);
		//System.out.println(System.nanoTime()/1000000000/(2*Math.PI));
	}

}
