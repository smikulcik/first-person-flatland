/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.awt.*;
import java.awt.geom.Line2D;
/**
 *
 * @author Mikulcik
 */
public class PolygonD implements Drawable {
    private ArrayList<PointD> verts = new ArrayList<PointD>();
    private Color c = Color.black;

    public void setColor(Color c){this.c = c;}
    public Color getColor(){return c;}
    
    public PolygonD(){}
    public void add(PointD p){
        verts.add(p);
    }
    public PointD getVert(int index){return verts.get(index);}
    public int vertNum(){return verts.size();}
    public ArrayList<PointD> getVerts(){return verts;}
    @Override
    public void draw(Graphics2D g){
        if(!verts.isEmpty()){
            for(int i = 0; i<verts.size()-1;i++)
            {
                g.draw(new Line2D.Double(	verts.get(i).getX(),
                							verts.get(i).getY(),
                							verts.get(i+1).getX(),
                							verts.get(i+1).getY()
                							)
                		);
            }

            g.draw(new Line2D.Double(	verts.get(verts.size()-1).getX(),
										verts.get(verts.size()-1).getY(),
										verts.get(0).getX(),
										verts.get(0).getY()
										)
            		);
        }
    }
    public void rotateAround(PointD point, double degrees){
        for(PointD p: verts){
            p.set(p.rotatedAround(point, degrees));
        }
    }
    
    public void translate(Vector v){
        for(PointD p: verts){
            p.setX(p.getX()+v.getX());
            p.setY(p.getY()+v.getY());
        }
    }
}
