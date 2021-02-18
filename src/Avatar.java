import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class Avatar implements Drawable
{
	private PolygonD boundary;
	private PointD center;
	private Vector orientation;
	private Vector velocity = new Vector(0,0);
	private double angularVelocity = 0;
	
	public Avatar(PointD center)
	{
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
	
	public Vector getVelocity(){return velocity;}
	public Vector getOrientation(){return orientation;}
	public double getAngularVelocity(){return angularVelocity;}
	public void setVelocity(Vector velocity){this.velocity = velocity;}
	public void setAngularVelocity(double angularVelocity){this.angularVelocity = angularVelocity;}
	
	public void translate(Vector v)
	{
		boundary.translate(v);
		center.translate(v);
		orientation.setTail(center);
	}
	
	public void rotate(double degrees)
	{
		boundary.rotateAround(center,degrees);
		orientation = orientation.rotate(degrees);
	}
	
	public void draw(Graphics2D g)
	{
		//orientation = orientation.rotate(.25);
		//boundary.rotateAround(center, .25);
		boundary.draw(g);
		center.draw(g);
		orientation.draw(g);
	}
	

	int counter = 0;
	public void drawFirstPerson(Graphics2D g, ArrayList<PolygonD> world)
	{
		Vector d = new Vector(1,1,orientation.getHead());
		d.setAngle(orientation.getAngle());
		d.setMag(50); //distance to image plane
		//d.draw(g);
		
		Vector x = new Vector(10,10,d.getHead());
		x.setAngle(d.getAngle() + 90);
		g.setColor(Color.black);
		double Xmin = -g.getClipBounds().width/2/20.;
		double mag = Xmin;
		x.setMag(-mag);
		x.draw(g);
		for(int pixel = 0; pixel<g.getClipBounds().width; pixel+=1)
		{
			Vector dx = (new Vector(x));
			dx.setMag(mag);
			mag += 1./20.;
			Vector ray = new Vector(orientation.getHead(), dx.getHead());
			
			double minDistance = Double.MAX_VALUE;
			PolygonD nearestObject = null;
			
			for(PolygonD object : world)
			{
				if(object.vertNum()>1)
				{
					PointD p1 = object.getVert(0);
					PointD p2;
					for(int i = 1; i <= object.vertNum(); i++)
					{
						if(i == object.vertNum())//once iterated through all verts, close polygon
							p2 = object.getVert(0);
						else
							p2 = object.getVert(i);
						PointD intersection = intersection(ray, p1, p2);
						if(intersection != null)
						{
							//intersection.draw(g);
							double distance = (new Vector(ray.getTail(), intersection)).getMag();
							if(distance < minDistance)
							{
								minDistance = distance;
								nearestObject = object;
							}
						}
						p1 = p2;
					}
				}
			}
			g.setColor(Color.black);
			ray.setMag(minDistance);
			
			if(pixel%200 == 0)//display every 200th ray
				ray.draw(g);
			
			Color surfaceColor;
			if(nearestObject !=null)
				surfaceColor = nearestObject.getColor();
			else
				surfaceColor = new Color(0,0,0);
			int greyColor = 255;
			
			if(minDistance != Double.MAX_VALUE)
			{
				double ratio = Math.pow(Math.E,-1*minDistance/300.);
				Color renderedColor = new Color( //fog - rgb components
						(int)(ratio*surfaceColor.getRed() + (1-ratio)*greyColor),
						(int)(ratio*surfaceColor.getGreen() + (1-ratio)*greyColor),
						(int)(ratio*surfaceColor.getBlue() + (1-ratio)*greyColor)
						);
				g.setColor(renderedColor);
				//g.setColor(Color.black);
			}
			else
			{
				g.setColor(Color.green);
			}
			g.drawLine(pixel, 400, pixel, g.getClipBounds().height);
				
		}
		counter+=2;
	}
	
	
	
	/**
	 * Finds the intersection of a vector and a line segment
	 * 
	 * @param v  the vector
	 * @param p1 the first point of the line segment
	 * @param p2 the second point of the line segment
	 * @return the point of intersection or null if they do not intersect
	 */
	private PointD intersection(Vector v, PointD p1, PointD p2)
	{
		//Basically we need to solve this system of equations:
		//at - es = b + f
		//ct - gs = d + h
		//where the line is defined by 
		//	y(t) = at + b
		//	x(t) = ct + d
		//and the line passing through the vector is defined by 
		//	y(s) = es + f
		//	x(s) = gs + h
		
		Vector line = new Vector(p1,p2);
		//line.setMag(1);//keep vector from being huge
		//v.setMag(1);
		
		double a = line.getY();
		double b = line.getTail().getY();
		double c = line.getX();
		double d = line.getTail().getX();
		
		double e = v.getY();
		double f = v.getTail().getY();
		double g = v.getX();
		double h = v.getTail().getX();
		 
		//check for parallel lines
		if(Math.abs(e*c - a*g) <= 0.000001)
			return null;
		if(Math.abs(e*c - a*g) == 1000)
			System.out.println(a + " \t "+e);
		//System.out.println(a + " " + c + " | "+ e + " " + g);
		//Using crammer's rule
		double s = (a*(h-d) - c*(f-b))/(e*c - a*g);
		double t = ((f-b)*(-1*g) - (h-d)*(-1*e))/(e*c - a*g);
		//negative s means the intersection is backwards
		if(s<0)
			return null;
		double x = g*s + h;
		double y = e*s + f;
		//System.out.println(x+" "+g+" "+s + " " + h);
		//System.out.println(y+" "+e+" "+s + " " + f);
		//System.out.println(-0.8590290258821459*297.4382452459837 + 255.50808607375217); //-2.8421709430404007E-14
		//System.out.println(-0.8590290*297.43824 + 255.508086); //-2.8421709430404007E-14
		//check bounds
		
		if(t <= 1 && t >= 0)//t is on line segment from [0,1]
			return new PointD(x,y);
		return null;
	}
}
