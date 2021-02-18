package simonGraphics;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.geom.*;

/**
 * Point class with double precision
 * 
 * Can be drawn on the screen
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class PointD implements Drawable
{
	public double x;
	public  double y;
	public  Color c = Color.blue;
	
	public PointD(){
		x = 0;
		y = 0;
	}
	public PointD(double xPos, double yPos){
		x = xPos;
		y = yPos;
	}
	public PointD(Point p){
		x = p.x;
		y = p.y;
	}
	public PointD(PointD p){
		x = p.getX();
		y = p.getY();
		c = p.getColor();
	}
	
	/**
	 * draw point as a small circle
	 */
	@Override
	public void draw(Graphics2D g, PointD worldCenter){
		g.setColor(c);
		g.fill(new Ellipse2D.Double(worldCenter.getX() + x-2, worldCenter.getY()+y-2,4,4));
	}
	
	//getters and setters
	public double getX(){return x;}
	public double getY(){return y;}
	public Color getColor(){return c;}
	public void set(PointD point){x=point.getX(); y=point.getY();}
	public void setX(double newX){x = newX;}
	public void setY(double newY){y = newY;}
	public void setColor(Color c){this.c = c;}
	
	/**
	 * Find the location of the transformed point as it rotates ccw around another point
	 * @param center  center of rotation
	 * @param degrees  num of degs of rot.
	 * @return  the rotated point
	 */
	public PointD rotatedAround(PointD center, double degrees){
		double newX = center.getX() + ((x-center.getX())*Math.cos(degrees*Math.PI/180) - (y-center.getY())*Math.sin(degrees*Math.PI/180));
		double newY = center.getY() + ((x-center.getX())*Math.sin(degrees*Math.PI/180) + (y-center.getY())*Math.cos(degrees*Math.PI/180));
		
		return new PointD(newX,newY);
	}
	
	/**
	 * Find the angle of the standard vector from this point to the passed point
	 * @param pt  the passed point
	 * @return  the angle of the standard vector through these points
	 */
	public double angleTo(PointD pt){
		double angle = Math.atan((pt.getY()-y)/(pt.getX()-x))*180/Math.PI;
		if(angle<0)angle+=360;
		if(pt.getX()-x<0){angle+=180;}
		while(angle>360)angle-=360;
		return angle;
   
	}
	
	/**
	 * Move point
	 * @param v how much
	 */
	public void translate(Vector v)
	{
        x = x+v.getX();
        y = y+v.getY();
	}
   
	/**
	 * Find the distance from this point to point p
	 * @param p the other point
	 * @return the distance
	 */
	public double distanceTo(PointD p){return Math.sqrt((x-p.x)*(x-p.x) + (y-p.y)*(y-p.y));}
	
	/**
	 * Stringulate the point -- useful for debugging
	 */
	@Override
	public String toString(){return "(" + (int)x + "," + (int)y + ")";}
		
}//end PointD
