import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;


public class FlatlandGame extends JPanel implements KeyListener, MouseMotionListener
{
	
	//http://stackoverflow.com/a/20769932 for fps counter
	int fpsCounter = 0;
	int fps = 0;
	private long lastTime;
	ArrayList<PolygonD>	objects = new ArrayList<PolygonD>();
	Avatar aSquare = new Avatar(new PointD(400,300));
	int screenWidth = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void setupWorld()
	{
		PolygonD triangle = new PolygonD();
		triangle.add(new PointD(305,325));
		triangle.add(new PointD(120,330));
		triangle.add(new PointD(100,220));
		triangle.setColor(Color.red);
		objects.add(triangle);
		
		PolygonD room = new PolygonD();
		room.add(new PointD(0,0));
		room.add(new PointD(800,0));
		room.add(new PointD(800,50));
		room.add(new PointD(50,50));
		room.add(new PointD(50,750));
		room.add(new PointD(750,750));
		room.add(new PointD(750,400));
		room.add(new PointD(800,400));
		room.add(new PointD(800,800));
		room.add(new PointD(0,800));
		room.translate(new Vector(100,100));
		//room.rotateAround(new PointD(400,400), 5);
		objects.add(room);
		
		PolygonD hex = generatePolygon(6,new PointD(300,500),30);
		hex.rotateAround(new PointD(300,500), 1);
		//objects.add(hex);
		
		PolygonD hugegon = generatePolygon(300,new PointD(600,500),50);
		//hugegon.rotateAround(new PointD(600,500), 1);
		objects.add(hugegon);

		PolygonD line = new PolygonD();
		line.add(new PointD(0,0));
		line.add(new PointD(1000,500));
		line.add(new PointD(0,1000));
		//line.add(new PointD(60,1000));
		//line.add(new PointD(60,0));
		//line.translate(new Vector(100,300));
		//objects.add(line);
		
	}
	
	public void start()
	{
		setupWorld();

		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		
		
		int delay = 16;
		lastTime = System.nanoTime();
		while(true)
		{
			//update fps once per second
			if(System.nanoTime()-lastTime > 1000000000)
			{
				//System.out.println(fps + "  " + fpsCounter + " "+(System.nanoTime() - lastTime)/1000000000.);
				fps = (int)(fpsCounter*1000000000./(System.nanoTime() - lastTime));
				lastTime = System.nanoTime();
				fpsCounter = 0;
			}
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.repaint();
		}
	}
	
	public void updateMotion(Graphics g)
	{
		aSquare.rotate(aSquare.getAngularVelocity());
		aSquare.translate(aSquare.getVelocity());
	}
	
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		screenWidth = graphics.getClipBounds().width;
		
		Graphics2D g = (Graphics2D)graphics; 
		/*g.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		*/
		updateMotion(graphics);
		
		aSquare.drawFirstPerson(g,objects);
		
		graphics.drawString("fps: "+fps,20,20);
		
		
		g.setColor(Color.black);
		aSquare.draw(g);

		g.setColor(Color.black);
		
		for(Drawable obj: objects)
			obj.draw(g);
			
		fpsCounter++;
		
		PolygonD tri = objects.get(0);
		tri.rotateAround(new PointD(180,250), .5);
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



	double forwardV = 0;
	double horizV = 0;
	@Override
	public void keyPressed(KeyEvent event) {
		
		switch(event.getKeyCode())
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
		if(horizV>2)
			horizV=2;
		if(forwardV>2)
			forwardV=2;
		if(horizV<-2)
			horizV=-2;
		if(forwardV<-2)
			forwardV=-2;
		
		Vector v = new Vector(forwardV, horizV);
		v = v.rotate(aSquare.getOrientation().getAngle());
		aSquare.setVelocity(v);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		System.out.println(event.getKeyCode());
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(screenWidth != 0)
		{
		//System.out.println(2*(e.getX() - screenWidth/2)/(screenWidth/2.));
		aSquare.setAngularVelocity(2*Math.pow((e.getX() - screenWidth/2)/(screenWidth/2.),3));
		}	
	}
}//end FlatlandGame
