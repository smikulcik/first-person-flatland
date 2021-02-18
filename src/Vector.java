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
public class Vector implements Drawable
{
	private double x;
	private double y;
	private PointD tail;
		
	public Vector(){
		x = 10;
		y = 10;
		tail = new PointD(0,0);
	}
		
	public Vector(double xComp, double yComp){
		x = xComp;
		y = yComp;
		tail = new PointD(0,0);
	}
		
	public Vector(double xComp, double yComp, PointD myTail){
		x = xComp;
		y = yComp;
				tail = myTail;
	}
	public Vector(PointD myTail, PointD myHead){
		x = myHead.getX() - myTail.getX();
		y = myHead.getY() - myTail.getY();
		tail = myTail;
	}
	public Vector(Vector v){
		x = v.getX();
		y = v.getY();
		tail = v.getTail();
	}
	@Override
	public void draw(Graphics2D g){
		if(x==0&&y==0){x=60;y=60;}
		g.draw(new Line2D.Double(tail.getX(),tail.getY(), tail.getX() + x, tail.getY() + y));
		g.draw(new Line2D.Double(tail.getX()+x,tail.getY()+y, tail.getX()+x + (getMag()/4)*Math.cos((getAngle()+135)*Math.PI/180.0),tail.getY()+y + (getMag()/4)*Math.sin((getAngle()+135)*Math.PI/180.0)));
		g.draw(new Line2D.Double(tail.getX()+x,tail.getY()+y, tail.getX()+x + (getMag()/4)*Math.cos((getAngle()-135)*Math.PI/180.0),tail.getY()+y + (getMag()/4)*Math.sin((getAngle()-135)*Math.PI/180.0)));
	}
	
	public double angleTo(PointD pt){
		double angle = Math.atan((pt.getY()-tail.getY())/(pt.getX()-tail.getX()))*180/Math.PI;
		if(angle<0)angle+=360;
		if(tail.getX()-pt.getX()<0){angle+=180;}
		while(angle>360)angle-=360;
		return angle+180;
	}
	
	public double angleBetween(Vector v){
		double value = Math.acos((v.getX()*x + v.getY()*y)/(getMag()*v.getMag()))*180/Math.PI;
		int sign;
		
		if((int)((getAngle()+value)%360) == (int)v.getAngle()){sign=-1;}else{sign=1;}
		
		return sign*value;
	}
	
	public double getMag(){
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
	}
	
	public double getAngle(){
		double angle = Math.atan(y/x)*180/Math.PI;
		if(x<0){angle+=180;}
		if(angle<0)angle+=360;
		return angle;
	}
	public Vector rotate(double degrees){
		double oldTheata = Math.atan2(y,x);
		double newTheata = oldTheata + (degrees*Math.PI/180);
		double xComp = getMag()*Math.cos(newTheata);
		double yComp = getMag()*Math.sin(newTheata);
		return new Vector(xComp, yComp,tail);		
	}
	
	public double dot(Vector v){return x*v.getX() + y*v.getY();}
	public Vector proj(Vector target){
		double multiplier = dot(target)/target.dot(target);
		return new Vector(target.getX()*multiplier, target.getY()*multiplier, target.getTail());
	}
	public void setAngle(double degrees){
			double mag = getMag();
			x = mag*Math.cos(degrees*Math.PI/180);
			y = mag*Math.sin(degrees*Math.PI/180);
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public PointD getTail(){return tail;}
	public PointD getHead(){return new PointD(tail.getX() + x, tail.getY() + y);}
	
	public void setMag(double newMag){
		double mag = getMag();
		x = newMag*x/mag;
		y = newMag*y/mag;
	}
	public void setX(double newX){x = newX;}
	public void setY(double newY){y = newY;}
	public void setTail(PointD newTail){tail = newTail;}
	
	public void add(Vector v)
	{
		x += v.x;
		y += v.y;
	}
	
	public void subtract(Vector v)
	{
		x -= v.x;
		y -= v.y;
	}
	
	@Override
	public String toString(){return tail + " <" + (int)x + "," + (int)y + ">" + " Mag=" + getMag();}
}//end Vector
