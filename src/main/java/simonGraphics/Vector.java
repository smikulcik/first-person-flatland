package simonGraphics;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.geom.*;
/**
 * Vector class
 * 
 * NOTE: This is more of a ray class since it has an x component, y component, and a tail
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 */
public class Vector implements Drawable
{
	public double x;
	public double y;
	public PointD tail;
		
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
		x = myHead.x - myTail.x;
		y = myHead.y - myTail.y;
		tail = myTail;
	}
	public Vector(Vector v){
		x = v.x;
		y = v.y;
		tail = v.tail;
	}
	
	/**
	 * Draw vector as an arrow
	 */
	@Override
	public void draw(Graphics2D g, PointD worldCenter){
		if(x==0&&y==0){x=60;y=60;}
		g.draw(new Line2D.Double(
				worldCenter.x + tail.x,
				worldCenter.y + tail.y, 
				worldCenter.x + tail.x + x, 
				worldCenter.y + tail.y + y));
		g.draw(new Line2D.Double(
				worldCenter.x + tail.x+x,
				worldCenter.y + tail.y+y, 
				worldCenter.x + tail.x+x + (getMag()/4)*Math.cos((getAngle()+135)*Math.PI/180.0),
				worldCenter.y + tail.y+y + (getMag()/4)*Math.sin((getAngle()+135)*Math.PI/180.0)));
		g.draw(new Line2D.Double(
				worldCenter.x + tail.x+x,
				worldCenter.y + tail.y+y, 
				worldCenter.x + tail.x+x + (getMag()/4)*Math.cos((getAngle()-135)*Math.PI/180.0),
				worldCenter.y + tail.y+y + (getMag()/4)*Math.sin((getAngle()-135)*Math.PI/180.0)));
	}
	
	/**
	 * Find the angle that this vector would have to rotate to point to point pt
	 * 
	 * @param pt point pt
	 * @return angle to point
	 */
	public double angleTo(PointD pt){
		double angle = Math.atan((pt.y-tail.y)/(pt.x-tail.x))*180/Math.PI;
		if(angle<0)angle+=360;
		if(tail.x-pt.x<0){angle+=180;}
		while(angle>360)angle-=360;
		return angle+180;
	}
	
	/**
	 * Finds the angle between vectors.
	 * 
	 * NOTE: This returns the signed angle between the vectors(positive is CCW) so that if 
	 * you rotate this, this number of degrees, you will be pointing towards vector v
	 * 
	 * @param v the other vector
	 * @return the angle from this to v
	 */
	public double angleBetween(Vector v){
		double value = Math.acos((v.x*x + v.y*y)/(getMag()*v.getMag()))*180/Math.PI;
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
	
	/**
	 * Find the rotated vector.
	 * 
	 * NOTE: this does not change the value of this vector
	 * 
	 * @param degrees  how much
	 * @return  the rotated vector
	 */
	public Vector rotate(double degrees){
		double oldTheata = Math.atan2(y,x);
		double newTheata = oldTheata + (degrees*Math.PI/180);
		double xComp = getMag()*Math.cos(newTheata);
		double yComp = getMag()*Math.sin(newTheata);
		return new Vector(xComp, yComp,tail);		
	}
	
	/**
	 * compute the dot product with this vector to another one
	 * @param v the other one
	 * @return the dot product
	 */
	public double dot(Vector v){return x*v.x + y*v.y;}
	
	/**
	 * Find the projected vector of this onto a target vector
	 * @param target the target vector
	 * @return the projected vector
	 */
	public Vector proj(Vector target){
		double multiplier = dot(target)/target.dot(target);
		return new Vector(target.x*multiplier, target.y*multiplier, target.getTail());
	}
	
	public void setAngle(double degrees){
			double mag = getMag();
			x = mag*Math.cos(degrees*Math.PI/180);
			y = mag*Math.sin(degrees*Math.PI/180);
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public PointD getTail(){return tail;}
	public PointD getHead(){return new PointD(tail.x + x, tail.y + y);}
	
	public void setMag(double newMag){
		double mag = getMag();
		x = newMag*x/mag;
		y = newMag*y/mag;
	}
	public void setX(double newX){x = newX;}
	public void setY(double newY){y = newY;}
	public void setTail(PointD newTail){tail = newTail;}
	
	/**
	 * add vector to this
	 * @param v vector to add
	 */
	public void add(Vector v)
	{
		x += v.x;
		y += v.y;
	}
	
	/**
	 * subtract another vector from this
	 * @param v vector to subtract from this
	 */
	public void subtract(Vector v)
	{
		x -= v.x;
		y -= v.y;
	}
	

	
	/**
	 * Finds the intersection of a vector and a line segment
	 * 
	 * @param v  the vector
	 * @param p1 the first point of the line segment
	 * @param p2 the second point of the line segment
	 * @return the point of intersection or null if they do not intersect
	 */
	public PointD intersection(PointD p1, PointD p2)
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
		
		Vector v = this;
		
		Vector line = new Vector(p1,p2);
		
		double a = line.y;
		double b = line.tail.y;
		double c = line.x;
		double d = line.tail.x;
		
		double e = v.y;
		double f = v.tail.y;
		double g = v.x;
		double h = v.tail.x;
		 
		//check for parallel lines
		if(Math.abs(e*c - a*g) <= 0.000001)
			return null;
		
		//Using crammer's rule
		double s = (a*(h-d) - c*(f-b))/(e*c - a*g);
		double t = ((f-b)*(-1*g) - (h-d)*(-1*e))/(e*c - a*g);
		
		//negative s means the intersection is backwards
		if(s<0)
			return null;
		double x = g*s + h;
		double y = e*s + f;
		//check bounds
		
		if(t <= 1 && t >= 0)//t is on line segment from [0,1]
			return new PointD(x,y);
		return null;
	}
	
	
	@Override
	public String toString(){return tail + " <" + (int)x + "," + (int)y + ">" + " Mag=" + getMag();}
}//end Vector
