package flatlandCharacters;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;

import flatlandGame.GameVars;

import simonGraphics.Drawable;
import simonGraphics.PointD;
import simonGraphics.PolygonD;
import simonGraphics.Vector;


/**
 * Generic person class.
 * 
 * Each person can render his own viewport of what he 'sees'
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Person implements Drawable
{
	public String name;
	public PolygonD boundary;
	protected PointD center;
	protected Vector orientation = new Vector(0,1);
	protected Vector velocity = new Vector(0,0);
	protected double angularVelocity = 0;
	protected ArrayList<Msg> messages = new ArrayList<Msg>();
	
	//getters/setters
	public PolygonD getBoundary(){return boundary;}
	public PointD getCenter(){return center;}
	public Vector getVelocity(){return velocity;}
	public Vector getOrientation(){return orientation;}
	public double getAngularVelocity(){return angularVelocity;}
	public void setVelocity(Vector velocity){this.velocity = velocity;}
	public void setAngularVelocity(double angularVelocity){this.angularVelocity = angularVelocity;}

	public Person(String name)
	{
		this.name = name;
	}
	
	/**
	 * Move the person
	 */
	public void translate(Vector v)
	{
		boundary.translate(v);
		center.translate(v);
		orientation.setTail(center);
	}
	
	/**
	 * Rotate the person
	 * @param degrees degrees to rotate character ccw from top view
	 */
	public void rotate(double degrees)
	{
		boundary.rotateAround(center,degrees);
		orientation = orientation.rotate(degrees);
	}
	
	/**
	 * Interact with all characters in range
	 */
	public void checkForInteractions()
	{
		for(Person p : GameVars.people)
			if(p.getCenter().distanceTo(center) < 300)
			{
				p.interact(this);//interact with me
			}
	}
	
	/**
	 * Inetract with a specific person
	 * @param withWhom  person who you are going to interact with
	 */
	public void interact(Person withWhom){
		withWhom.sendMsg(new Msg(this, "Hi, It's good \nto see you."));
	}
	
	/**
	 * Send a message to another person.
	 * If a person already sent a message, that person's old message
	 * will be replace by the new message.
	 * 
	 * @param m  the message to send
	 */
	public void sendMsg(Msg m)
	{
		for(Msg msg : messages)
		{
			if(msg.from.equals(m.from))
			{
				msg.msg = m.msg;
				return;
			}
		}
		
		messages.add(m);
	}
	
	/**
	 * Remove old message from 2 seconds ago
	 */
	public void cleanUpMessages()
	{
		for(int i = messages.size()-1; i >= 0; i--)
		{
			Msg m = messages.get(i);
			if((new Date()).getTime() - m.when.getTime() > 2000)//remove after 2s
			{
				messages.remove(i);
			}
		}
	}

	/**
	 * Draw the person for the top-down view
	 */
	public void draw(Graphics2D g, PointD worldCenter)
	{
		//orientation = orientation.rotate(.25);
		//boundary.rotateAround(center, .25);
		boundary.draw(g, worldCenter);
		center.draw(g, worldCenter);
		orientation.draw(g, worldCenter);
	}
	
	/**
	 * Draw the person for first person perspective 
	 * 
	 * @param g  the graphics to draw the view on
	 * @param drawArea  the bounds for drawing the view
	 * @param world  the polygons in the world
	 * @param worldCenter  the center of the world
	 */
	public void drawFirstPerson(Graphics2D g, Rectangle drawArea, ArrayList<PolygonD> world, PointD worldCenter)
	{	
		//d is distance of image plane
		Vector d = new Vector(1,1,orientation.getHead());
		d.setAngle(orientation.getAngle());
		d.setMag(50); //distance to image plane
		//d.draw(g, worldCenter);
		
		//x is the x value that moves across the image plane as you ray trace
		Vector x = new Vector(10,10,d.getHead());
		x.setAngle(d.getAngle() + 90);
		g.setColor(Color.black);
		double Xmin = -drawArea.width/2/20.;	//bounds for image plane
		double mag = Xmin;//mag is the distance from center of view to head of x on image plane
		x.setMag(-mag);
		
		//iterate over each pixel in the drawArea
		for(int pixel = drawArea.x; pixel < drawArea.x + drawArea.width; pixel+=1)
		{
			Vector dx = (new Vector(x));//copy of x that actually does the moving
			dx.setMag(mag);
			mag += 1./20.;
			Vector ray = new Vector(orientation.getHead(), dx.getHead());//ray to trace
			
			//find the nearest intersection, and that is the point of terminating point of our ray
			double minDistance = Double.MAX_VALUE;
			PolygonD nearestObject = null;
			
			//check for intersections from each polygon in the world
			for(PolygonD object : world)
			{
				int vertNum = object.verts.size();//cache verts size
				if(vertNum > 1)//ignore points, they will through indexOutOfBounds and wont show up anyway
				{
					PointD p1 = object.getVert(0);
					PointD p2;
					//iterate over each line segment (p1 to p2) in each polygon in scene
					for(int i = 1; i <= vertNum; i++)
					{
						if(i == vertNum)//once iterated through all verts, close polygon with initial vert
							p2 = object.getVert(0);
						else
							p2 = object.getVert(i);
						
						//check for intersections
						PointD intersection = ray.intersection(p1, p2);
						if(intersection != null)
						{//found intersection
							double distance = (new Vector(ray.getTail(), intersection)).getMag();
							if(distance < minDistance)//is this intersection the closest yet?
							{
								minDistance = distance;
								nearestObject = object;
							}
						}
						p1 = p2;//go to next point in polygon
					}
				}
			}
			//uncomment to draw rays (debug)
			//g.setColor(Color.black);
			//ray.setMag(minDistance);
			//if(pixel%200 == 0)//display every 200th ray
			//	ray.draw(g, worldCenter);
			
			//draw point on screen
			Color surfaceColor;
			int greyColor = 30;
			int skyColor = 20;
			
			if(minDistance != Double.MAX_VALUE)
			{
				//draw object color
				surfaceColor = nearestObject.getColor();
				
				//calculate fog falloff (got constants from the images in the book
				double ratio = 1.68323*Math.pow(Math.E,-1*minDistance*minDistance*1.78822E-5);
				ratio = Math.min(ratio, 1.0);
				
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
				//draw sky color
				g.setColor(new Color(skyColor, skyColor, skyColor));
			}
			
			//draw the pixel (it's actually a line, but it's Flatland!)
			g.drawLine(pixel, drawArea.y, pixel, drawArea.y + drawArea.height);
				
		}
		

		//messages
		checkForInteractions();
		cleanUpMessages();
		
		double angOfView = Math.atan((drawArea.width/2/20.)/50.)*180/Math.PI;
		
		for(Msg m : messages)
		{
			if(!m.from.equals(this))
			{//message from someone else
				double angle = (new Vector(d.getTail(), m.from.getCenter())).angleBetween(d);
				if(Math.abs(angle) < angOfView)//if the center of the person is on the screen
				{
					int locOnScreen = (int)(50*Math.tan(angle*Math.PI/180.)*20 + drawArea.width/2);
					int top = 30;
					String msg = m.toString();
					drawString(msg, locOnScreen, top, g);
				}
			}
			else
			{//messages from self are statuses
				drawString(m.msg, drawArea.width/2, drawArea.height - 100, g);
			}
		}
		
	}
	
	/**
	 * Draw text on the viewport.
	 * 
	 * NOTE: text is drawn centered at x
	 * 
	 * References:
	 * http://stackoverflow.com/a/6416215 aioobe http://stackoverflow.com/users/276052/aioobe for font metrics
	 * http://stackoverflow.com/a/4413153 aioobe http://stackoverflow.com/users/276052/aioobe for multi-line
	 * 
	 * @param text  the text to draw
	 * @param x  the x loc. of where to draw
	 * @param y  the y location of where to draw
	 * @param g  the graphics to draw on
	 */
	public static void drawString(String text, int x, int y, Graphics2D g)
	{
		//draw text box

		g.setFont(new Font("Arial", Font.PLAIN, 25));
		FontMetrics fm = g.getFontMetrics();
		String[] lines = text.split("\n");

		Rectangle2D stringBounds = fm.getStringBounds(lines[0], g);
		
		//draw a box behind the text
		g.setColor(Color.white);
		g.fillRect(
				x-(int)stringBounds.getWidth()/2 - 10,
				y-5,
				(int)stringBounds.getWidth()+20,
				fm.getHeight()*lines.length+10
				);
		
		g.setColor(Color.black);
		g.drawRect(
				x-(int)stringBounds.getWidth()/2 - 10,
				y-5,
				(int)stringBounds.getWidth()+20,
				fm.getHeight()*lines.length+10
				);
		
		//draw each line on the box
	    for (String line : lines)
	    {
	    	stringBounds = fm.getStringBounds(line, g);
			g.drawString(line, x-(int)stringBounds.getWidth()/2, y + fm.getAscent());
			y += fm.getHeight();
	    }
	}
	
	/**
	 * Update any physics calculations in overridden method
	 */
	public void updatePhysics()
	{
		//implement physics here
		this.rotate(angularVelocity);
	}
}
