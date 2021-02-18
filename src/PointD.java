/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.geom.*;
/**
 *
 * @author Mikulcik
 */
public class PointD implements Drawable, Comparable<PointD>
{
	private double x;
	private double y;
	private Color c = Color.blue;
	
	public PointD(){
		x = 0;
		y = 0;
	}
	public PointD(double xPos, double yPos){
		x = xPos;
		y = yPos;
	}
	@Override
	public void draw(Graphics2D g){
		g.setColor(c);
		g.fill(new Ellipse2D.Double(x-2,y-2,4,4));
	}
	public double getX(){return x;}
	public double getY(){return y;}
	public void set(PointD point){x=point.getX(); y=point.getY();}
	public void setX(double newX){x = newX;}
	public void setY(double newY){y = newY;}
	public void setColor(Color c){this.c = c;}
	
	public PointD rotatedAround(PointD center, double degrees){
		double newX = center.getX() + ((x-center.getX())*Math.cos(degrees*Math.PI/180) - (y-center.getY())*Math.sin(degrees*Math.PI/180));
		double newY = center.getY() + ((x-center.getX())*Math.sin(degrees*Math.PI/180) + (y-center.getY())*Math.cos(degrees*Math.PI/180));
		
		return new PointD(newX,newY);
	}
	
	public double angleTo(PointD pt){
		double angle = Math.atan((pt.getY()-y)/(pt.getX()-x))*180/Math.PI;
		if(angle<0)angle+=360;
		if(pt.getX()-x<0){angle+=180;}
		while(angle>360)angle-=360;
		return angle;
   
	}
	
	public void translate(Vector v)
	{
        x = x+v.getX();
        y = y+v.getY();
	}
   
	@Override
	public int compareTo(PointD other){
		if(other.getX()==x &&other.getY() == y){
			return 0;
		}else{
			return -1;
		}
	}
	public boolean equals(PointD other){
		if(other.getX()==x &&other.getY() == y){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public String toString(){return "(" + (int)x + "," + (int)y + ")";}
		
}//end PointD
