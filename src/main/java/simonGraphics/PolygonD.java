package simonGraphics;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Polygon with double precision
 * 
 * NOTE: Uses algorithm from blackpawn, see isInTriangle()
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class PolygonD implements Drawable {
    public ArrayList<PointD> verts = new ArrayList<PointD>();
    private Color c = new Color(230,230,230);

    public void setColor(Color c){this.c = c;}
    public Color getColor(){return c;}
    
    public void add(PointD p){
        verts.add(p);
    }
    public PointD getVert(int index){return verts.get(index);}
    public int vertNum(){return verts.size();}
    public ArrayList<PointD> getVerts(){return verts;}
    @Override
    public void draw(Graphics2D g, PointD worldCenter){
        if(!verts.isEmpty()){
            for(int i = 0; i<verts.size()-1;i++)
            {
                g.draw(new Line2D.Double(	worldCenter.getX() + verts.get(i).getX(),
					                		worldCenter.getY() + verts.get(i).getY(),
					                		worldCenter.getX() + verts.get(i+1).getX(),
					                		worldCenter.getY() + verts.get(i+1).getY()
                							)
                		);
            }

            g.draw(new Line2D.Double(	worldCenter.getX() + verts.get(verts.size()-1).getX(),
					            		worldCenter.getY() + verts.get(verts.size()-1).getY(),
					            		worldCenter.getX() + verts.get(0).getX(),
					            		worldCenter.getY() + verts.get(0).getY()
										)
            		);
        }
    }
    
    /**
     * Rotate polygon ccw around a point
     * @param point center of rot
     * @param degrees degs of rot
     */
    public void rotateAround(PointD point, double degrees){
        for(PointD p: verts){
            p.set(p.rotatedAround(point, degrees));
        }
    }
    
    /**
     * Translage polygon by a vector
     * @param v  how far to translate
     */
    public void translate(Vector v){
        for(PointD p: verts){
            p.setX(p.getX()+v.getX());
            p.setY(p.getY()+v.getY());
        }
    }
    
    /**
     * Checks to see if a point is inside this polygon
     * 
     * NOTE: the polygon must be convex and non-self-intersecting
     *  for this to always work right because the triangulation
     *  algorithm is lame.
     * 
     * @param p
     * @return
     */
    public boolean contains(PointD p)
    {
    	//triangulate polygon
    	PointD base = verts.get(0); //base for triangulation
    	int vertsSize = verts.size(); //cache size of array
    	for(int i = 1; i < vertsSize-1; i++)
    	{
    		PointD p1 = verts.get(i);//get points fanning outward
    		PointD p2 = verts.get(i+1);
    		
    		if(isInTriangle(p, base, p1, p2))//if the point is in this polygon, we're done
    			return true;
    	}
    	return false;
    }
    
    /**
     * Checks to see if point p is in triangle ABC
     * 
     * most of this implementation is from blackpawn, I translated it to java
     * 
     * Author: blackpawn
     * Site: http://www.blackpawn.com/texts/pointinpoly/default.html
     * Accessed: 27 April 2014
     * 
     * @param p point to check if inside triangle
     * @param A vert 1 of triangle
     * @param B vert 2 of triangle
     * @param C vert 3 of triangle
     * @return true if p is in triangle ABC
     */
	private static boolean isInTriangle(PointD p, PointD A, PointD B, PointD C)
	{
		// Compute vectors        
		Vector3D v0 = new Vector3D(A, C);
		Vector3D v1 = new Vector3D(A, B);
		Vector3D v2 = new Vector3D(A, p);

		// Compute dot products
		double dot00 = v0.dot(v0);
		double dot01 = v0.dot(v1);
		double dot02 = v0.dot(v2);
		double dot11 = v1.dot(v1);
		double dot12 = v1.dot(v2);

		// Compute barycentric coordinates
		double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		// Check if point is in triangle
		return (u >= 0) && (v >= 0) && (u + v < 1);
	}
	
}
