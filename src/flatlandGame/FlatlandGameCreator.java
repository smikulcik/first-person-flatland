package flatlandGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import simonGraphics.Drawable;
import simonGraphics.PointD;
import simonGraphics.PolygonD;
import simonGraphics.Vector;
import flatlandCharacters.Avatar;
import flatlandCharacters.Grandson;
import flatlandCharacters.Man;
import flatlandCharacters.Msg;
import flatlandCharacters.Person;
import flatlandCharacters.RunawayGrandson;
import flatlandCharacters.Son;
import flatlandCharacters.Wife;


/**
 * This class defines all of the interface features for the program.
 * 
 * @author Simon Mikulcik
 *
 */
public class FlatlandGameCreator extends JPanel implements KeyListener, MouseMotionListener, MouseListener, ActionListener
{
	
	//fps vars
	int fpsCounter = 0;
	int fps = 0;
	private long lastTime;
	
	//graphics objects
	ArrayList<PolygonD>	objects = new ArrayList<PolygonD>();
	ArrayList<Drawable>	drawables = new ArrayList<Drawable>();
	ArrayList<Person> people = new ArrayList<Person>();
	
	
	//initialize characters
	Avatar aSquare = new Avatar("A Square", new PointD(0,30));
	Grandson grandson = new Grandson("Grandson", new PointD(-200,-300));
	Wife wife = new Wife("Wife", new PointD(300,300), 100);
	Man triangle = new Man("Triangle", 3, new PointD(300,-300), 50);
	Son son = new Son("Son", new PointD(-150,0));
	RunawayGrandson runaway = new RunawayGrandson("Runaway Grandson", new PointD(1000, 0));
	
	PolygonD wall1 = new PolygonD();	
	PolygonD wall2 = new PolygonD();	
	PolygonD wall3 = new PolygonD();	
	PolygonD wall4 = new PolygonD();

	//graphics globals
	int screenWidth = 0;
	PointD worldCenter = new PointD(400,400);

	//interactive mode
	//for adding new points in the interactive mode
	PolygonD currentShape = new PolygonD();
	//for adding new points to currentShape
	PointD closestPointToCursor = null;
	//for interactive mode
	JPopupMenu rightClickMenu;
	
	//true for interactive mode, false for game mode
	public boolean showDebug = false;
	
	/**
	 *  for the graphics view
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * add all of the objects and characters to the scene
	 */
	public void setupWorld()
	{
		//build walls
		wall1.add(new PointD(-500, 500));
		wall1.add(new PointD(-490, 500));
		wall1.add(new PointD(-490, -500));
		wall1.add(new PointD(-500, -500));

		wall2.add(new PointD(-500, 500));
		wall2.add(new PointD(2000, 500));
		wall2.add(new PointD(2000, 490));
		wall2.add(new PointD(-500, 490));
		
		wall3.add(new PointD(2000, 500));
		wall3.add(new PointD(1990, 500));
		wall3.add(new PointD(1990, -500));
		wall3.add(new PointD(2000, -500));

		wall4.add(new PointD(-500, -500));
		wall4.add(new PointD(2000, -500));
		wall4.add(new PointD(2000, -490));
		wall4.add(new PointD(-500, -490));

		objects.add(wall1);
		objects.add(wall2);
		objects.add(wall3);
		objects.add(wall4);
		
		
		//add people
		GameVars.people = people;
		GameVars.aSquare = aSquare;
		
		objects.add(grandson.boundary);
		people.add(grandson);
		
		objects.add(son.boundary);
		people.add(son);
		
		objects.add(triangle.boundary);
		people.add(triangle);

		objects.add(wife.boundary);
		people.add(wife);

		objects.add(runaway.boundary);
		people.add(runaway);
		
		
		//set aSquare's position
		aSquare.rotate(220);
		
	}
	
	/**
	 * Setup the world, ui, and begin the game loop
	 */
	public void start()
	{
		//build scene
		setupWorld();
	    
		//setup interfae
	    RightClickMenuListener rightClickMenuListener = new RightClickMenuListener();
		rightClickMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Delete Node");
	    menuItem.addActionListener(this);
	    rightClickMenu.add(menuItem);
	    
	    this.addMouseListener(rightClickMenuListener);
		this.addMouseListener(this);
	    this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		
		
		//start the game loop
		int delay = 16;//16ms = 60fps
		lastTime = System.nanoTime();
		while(GameVars.isRunning)
		{
			//update fps once per second
			//http://stackoverflow.com/a/20769932 for fps counter
			if(System.nanoTime()-lastTime > 1000000000)
			{
				fps = (int)(fpsCounter*1000000000./(System.nanoTime() - lastTime));
				lastTime = System.nanoTime();
				fpsCounter = 0;
				//delay = (int)(1000./fps - 1.);
			}
			
			//delay simulation to set fps
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			//update motion and interactions
			updateMotion();
			checkForCollisions();
			//aSquare.checkForInteractions();
				
			for(Person p:people)
				p.updatePhysics();
			
			//redraw image each frame
			this.repaint();
		}
	}
	
	/**
	 * Move aSquare
	 */
	public void updateMotion()
	{
		aSquare.rotate(aSquare.getAngularVelocity());
		aSquare.translate(aSquare.getVelocity());
	}
	
	/**
	 * Check for collision detection
	 */
	public void checkForCollisions()
	{
		//collision detection
		for(PolygonD obj : objects)
		{
			//find points in character that intersected with the scene
			for(PointD p : aSquare.getBoundary().getVerts())
			{
				if(obj.contains(p)){
					//find side that intersected
					double minDist = Double.MAX_VALUE;
					PointD prev = obj.getVerts().get(obj.getVerts().size()-1);
					PointD closestPoint = null;
					for(PointD vert : obj.getVerts())
					{
						Double d = distanceToLine(new Vector(prev, vert), p);
						if(d != null && d < minDist)
						{
							minDist = d;
							closestPoint = getNearestPointOnLine(new Vector(prev, vert),p);
						}
						prev = vert;
					}
					Vector offset = new Vector(p, closestPoint);
					aSquare.translate(offset);
				}
			}
			
			//find points in scene that intersected with the character
			for(PointD p : obj.getVerts())
			{
				if(aSquare.getBoundary().contains(p)){
					//find side that intersected
					double minDist = Double.MAX_VALUE;
					PointD prev = aSquare.getBoundary().getVerts().get(aSquare.getBoundary().getVerts().size()-1);
					PointD closestPoint = null;
					for(PointD vert : aSquare.getBoundary().getVerts())
					{
						Double d = distanceToLine(new Vector(prev, vert), p);
						if(d != null && d < minDist)
						{
							minDist = d;
							closestPoint = getNearestPointOnLine(new Vector(prev, vert),p);
						}
						prev = vert;
					}
					
					//get the angle of object by 'feeling' it
					int numVerts = obj.verts.size();
					int objI = obj.verts.indexOf(p);
					int lastI = (objI - 1) % numVerts;
					if(lastI == -1)
						lastI = numVerts - 1;
					int nextI = (objI + 1) % numVerts;
					
					int angle = (int)Math.round((new Vector(obj.verts.get(objI), obj.verts.get(lastI))).angleBetween(new Vector(obj.verts.get(objI), obj.verts.get(nextI))));
					
					aSquare.sendMsg(new Msg(aSquare, "Felt Vertex: " + angle + "deg"));//null means status message
					
					
					//reverse direction to make aSquare move out of wall
					Vector offset = new Vector(closestPoint, p);
					aSquare.translate(offset);
				}
			}
		}
		aSquare.getCenter();
	}
	
	/**
	 * Draw a frame.
	 */
	@Override
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D)graphics; 
		
		//set global screenWidth for mouse controls
		screenWidth = g.getClipBounds().width;	
		
		//use anti-aliasing
		g.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		
		//actually draw the image now
		Rectangle drawArea = new Rectangle(g.getClipBounds());
		
		if(showDebug)//draw the interactive editor
		{
			g.setColor(Color.black);
			aSquare.draw(g, worldCenter);
	
			g.setColor(Color.black);
	
			for(Drawable obj: objects)
				obj.draw(g,worldCenter);
			for(Drawable p: drawables)
				p.draw(g,worldCenter);
	
			g.setColor(Color.green);
			currentShape.draw(g, worldCenter);
			for(PointD p : currentShape.getVerts())
				p.draw(g, worldCenter);
			
	
			if(closestPointToCursor != null)
				closestPointToCursor.draw(g, worldCenter);
				
			worldCenter.draw(g, new PointD(0,0));
			
			drawArea.y += .9*drawArea.height;
			drawArea.height /= 10;
		}
		
		//draw rendered view
		ArrayList<PolygonD> objectsInScene = new ArrayList<PolygonD>();
		objectsInScene.addAll(objects);
		objectsInScene.add(currentShape);
		
		//System.out.println(drawArea);
		aSquare.drawFirstPerson(g, drawArea, objectsInScene, worldCenter);
		
		if(!GameVars.isRunning)
			Person.drawString("You Won!!", drawArea.width/2, drawArea.height/2, g);
		
		g.setColor(Color.black);
		graphics.drawString("fps: "+fps,20,30);
		fpsCounter++;
	}

	double forwardV = 0;	//used for keyboard controls
	double horizV = 0;
	/**
	 * Handle keyboard user input.
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyCode())//iterate over buttons and dispatch command
		{
		case 38://up
		case 87://w
			forwardV += 2;
			break;
		case 40://down
		case 83://s
			forwardV -= 2;
			break;
		case 37://left
		case 65://a
			horizV -= 2;
			break;
		case 39://right
		case 68://d
			horizV += 2;
		}
		
		//set max and min for character speed
		if(horizV>2)
			horizV=2;
		if(forwardV>2)
			forwardV=2;
		if(horizV<-2)
			horizV=-2;
		if(forwardV<-2)
			forwardV=-2;
		
		//set character speed
		Vector v = new Vector(forwardV, horizV);
		v = v.rotate(aSquare.getOrientation().getAngle());
		aSquare.setVelocity(v);
	}

	/**
	 * more keyboard handling
	 * In general, make releasing a key make character stop moving
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		switch(event.getKeyCode())
		{
		case 38://up
		case 87://w
			forwardV -= 2;
			break;
		case 40://down
		case 83://s
			forwardV += 2;
			break;
		case 37://left
		case 65://a
			horizV += 2;
			break;
		case 39://right
		case 68://d
			horizV -= 2;
		}
		
		Vector v = new Vector(forwardV, horizV);
		v = v.rotate(aSquare.getOrientation().getAngle());
		aSquare.setVelocity(v);
	}

	@Override
	public void keyTyped(KeyEvent event) {
		
	}


	Point lastPointOnScreen;
	/**
	 * For interactive mode
	 * This method makes dragging move verticies of polygons.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0)
		{
			//for middle mouse button drags, pan across the scene
			Vector temp = new Vector(new PointD(lastPointOnScreen), new PointD(e.getPoint()));
			temp.setTail(worldCenter);
			worldCenter = temp.getHead();
			lastPointOnScreen  = e.getPoint();
		}
		else if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0)
		{
			//for left mouse button drags, move closest vert.
			if(closestPointToCursor != null)
			{
				Vector temp = new Vector(new PointD(lastPointOnScreen), new PointD(e.getPoint()));
				temp.setTail(closestPointToCursor);
				closestPointToCursor.set(temp.getHead());
				lastPointOnScreen  = e.getPoint();
			}
		}
		
	}

	/**
	 * Handle all mouse movements
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		lastPointOnScreen  = e.getPoint();			
		
		if(screenWidth != 0)//control looking around for first person
		{
		aSquare.setAngularVelocity(2*Math.pow((e.getX() - screenWidth/2)/(screenWidth/2.),3));
		}
		
		if(showDebug)//select closest point to cursor for global var
		{
			double minDistance = 100.0;
			closestPointToCursor = null;
			
			ArrayList<PolygonD> objectsInScene = new ArrayList<PolygonD>();
			objectsInScene.addAll(objects);
			objectsInScene.add(currentShape);

			//iterate over all of the verts in all of the objects in the scene
			for(PolygonD obj : objectsInScene)
				for(PointD p : obj.getVerts())
				{
					double distance = (double)(new Vector(
							p, 
							new PointD(
									e.getPoint().x - worldCenter.getX(),
									e.getPoint().y - worldCenter.getY()
									)
							)).getMag();
					if(distance < minDistance)
					{
						//set closest point to cursor
						minDistance = distance;
						closestPointToCursor = p;
					}
				}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch(e.getButton())
		{
		case 1: //left button
			if(showDebug)
				addNode();
			break;
		case 2://middle button
			break;
		case 3://right button
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(showDebug)//for interactive mode, 
			lastPointOnScreen  = e.getPoint();//for dragging and adding new nodes
		switch(e.getButton())
		{
		case 1: //left button
			break;
		case 2://middle button
			break;
		case 3://right button
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	
	/**
	 * for button clicks in interactive mode
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.requestFocus();
		String action = e.getActionCommand();
		if(action.equals("Save"))
		{
			System.out.println("save");
			//TODO: Implement a save format/feature
		}
		else if (action.equals("New Object"))
		{
			//for adding new shapes to the current scene
			if(currentShape.getVerts().size() > 0)
			{
				objects.add(currentShape);
				currentShape = new PolygonD();
			}
		}
		else if (action.equals("Delete Node"))
		{
			//delete closest vert to cursor from scene
			try
			{
			ArrayList<PolygonD> objectsInScene = new ArrayList<PolygonD>();
			objectsInScene.addAll(objects);
			objectsInScene.add(currentShape);
			for(PolygonD obj : objectsInScene)
				for(PointD p : obj.getVerts())
				{
					if(p.equals(closestPointToCursor))
					{
						obj.getVerts().remove(closestPointToCursor);
					}
				}
			closestPointToCursor = null;
			}
			catch(ConcurrentModificationException exception)
			{
				//do nothing, this just happens 
				//TODO: fix this so that it doesn't show up
				//its a problem of removing objects while rendering them
				//	at the same time
			}
		}
	}
	
	/**
	 * Adds a node in the nearest line segment
	 */
	public void addNode()
	{
		ArrayList<PolygonD> objectsInScene = new ArrayList<PolygonD>();
		objectsInScene.addAll(objects);
		objectsInScene.add(currentShape);
		
		PointD mousePoint = new PointD(lastPointOnScreen); 
		mousePoint.setX(mousePoint.getX() - worldCenter.getX());
		mousePoint.setY(mousePoint.getY() - worldCenter.getY());
		
		PolygonD nearestObject = null;
		Double minDistance = 100.;//threshold for adding points
		int pointIndex = 0;
		for(PolygonD obj : objectsInScene)
			if(obj.getVerts().size()>0){
				PointD p = obj.getVert(0);
				for(int i = 1; i < obj.getVerts().size(); i++)
				{
					System.out.println("i = "+i);
					Double d = distanceToLine(new Vector(p, obj.getVert(i)), mousePoint);
					System.out.println("d = "+d);
					if(d != null && d < minDistance)
					{
						System.out.println(d + "<"+ minDistance);
						nearestObject = obj;
						minDistance = d;
						pointIndex = i;
					}
					else
						System.out.println(d + ">="+ minDistance);
					p = obj.getVert(i);
				}
				//handle last side
				Double d = distanceToLine(new Vector(obj.getVert(0), obj.getVert(obj.getVerts().size()-1)), mousePoint);
				if(d != null && d < minDistance)
				{
					System.out.println(d + "<"+ minDistance);
					nearestObject = obj;
					minDistance = d;
					pointIndex = obj.getVerts().size();
					System.out.println("i = "+pointIndex);
				}
				else
					System.out.println(d + ">="+ minDistance);
			}
		System.out.println("minDistance "+minDistance +" pointIndex = "+pointIndex);

		if(nearestObject != null)
		{
			nearestObject.getVerts().add(pointIndex, mousePoint);
			System.out.println(nearestObject);
		}
		else
		{
			currentShape.add(mousePoint);
		}
	}
	
	/**
	 * Finds the distance from a point p, to a line passing through the vector v
	 * 
	 * @param v the vector defining the line
	 * @param p the point to measure the distance
	 * @return the shortest distance from the point to the line
	 */
	private Double distanceToLine(Vector v, PointD p)
	{
		PointD nearestPoint = getNearestPointOnLine(v,p);		
		
		double x1 = nearestPoint.getX();
		double y1 = nearestPoint.getY();
		
		double t;
		if(v.getX()>v.getY())//if one component is close to zero, use other one
			t = (x1 - v.getTail().getX()) / v.getX();
		else
			t = (y1 - v.getTail().getY()) / v.getY();
		
		if(t < 0 || t > 1)//then calculated point is not in line segment
			return null;
		
		return Math.sqrt((p.getX() - x1)*(p.getX() - x1) + (p.getY() - y1)*(p.getY() - y1));
	}
	
	/**
	 * Finds the location of the nearest point on a line from another point off of the line
	 * 
	 * @param v the vector through which the line passes
	 * @param p the point off of the line
	 * @return  the point on the line closest to the point off of the line
	 */
	private PointD getNearestPointOnLine(Vector v, PointD p)
	{
		//for line of form ax + by + c = 0 and point (x0, y0)
		double a = -1*v.getY();
		double b = v.getX();
		double c = v.getY()*v.getTail().getX() - v.getX()*v.getTail().getY();

		double x0 = p.getX();
		double y0 = p.getY();
		
		//nearest point on line is (x1,y1)
		double x1 = (b*(b*x0 - a*y0) - a*c )/(a*a + b*b);
		double y1 = (a*(-b*x0 + a*y0) - b*c )/(a*a + b*b);
		
		return new PointD(x1,y1);
	}
	

	/**
	 * For the interactive mode.
	 * 
	 * handles the right click menu
	 * 
	 * @author Simon Mikulcik
	 *
	 */
    class RightClickMenuListener extends MouseAdapter
    {
    	/**
    	 * show the right click menu when triggered
    	 */
    	@Override
    	public void mouseReleased(MouseEvent e)
    	{
	        if (showDebug && e.isPopupTrigger()) {
	            rightClickMenu.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
    	}
    	/**
    	 * Repeated methods for cross platform support
    	 */
    	@Override
    	public void mousePressed(MouseEvent e)
    	{
	        if (showDebug && e.isPopupTrigger()) {
	            rightClickMenu.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
    	}
    	
    }
	
}//end FlatlandGame
