package simonGraphics;

/**
 * 3d vector class
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Vector3D {
	public double x;
	public double y;
	public double z;
	
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Create 3D Vector from points in xy plane
	 * @param a	tail (in xy plane)
	 * @param b head (in xy plane)
	 */
	public Vector3D(PointD a, PointD b)
	{
		x = b.x - a.x;
		y = b.y - a.y;
		z = 0;
	}
	
	public Vector3D(Vector v)
	{
		x = v.x;
		y = v.y;
		z = 0;
	}
	
	/**
	 * find the cross product between 2 3d vectors
	 * @param v the other vector
	 * @return the resultant
	 */
	public Vector3D cross( Vector3D v)
	{
		double xComp = y*v.z - v.y*z;
		double yComp = v.x*z - x*v.z;
		double zComp = x*v.y - v.x*y;
		return new Vector3D(xComp,yComp,zComp);
	}
	
	/**
	 * Find the dot product between this and another vector
	 * @param v  the other vector
	 * @return the dot product
	 */
	public double dot(Vector3D v)
	{
		return x*v.x + y*v.y + z*v.z;
	}
	
	@Override
	public String toString()
	{
		return "<"+x+", "+y+", "+z+">";
	}

}
