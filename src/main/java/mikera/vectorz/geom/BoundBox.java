package mikera.vectorz.geom;

import mikera.vectorz.AVector;
import mikera.vectorz.Vector3;

// TODO: migrate to vectorz main library once complete

/**
 * 3D Bounding box constructed from two Vector3 components
 * 
 * @author Mike
 *
 */
public final class BoundBox implements Cloneable {
	public final Vector3 lower;
	public final Vector3 upper;
	
	/**
	 * Construct a BoundBox as an exact deep copy of another BoundBox
	 */
	public BoundBox(BoundBox bb) {
		lower=bb.lower.clone();
		upper=bb.upper.clone();
	}
	
	/**
	 * Creates an empty BoundBox
	 */
	public BoundBox() {
		lower=new Vector3 (Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
		upper=new Vector3 (Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
	}
	
	/**
	 * Construct a BoundBox with a margin around an existing BoundBox
	 */
	public BoundBox(BoundBox bb, double margin) {
		Vector3 a=bb.lower;
		lower=new Vector3(a.x-margin,a.y-margin,a.z-margin);
		Vector3 b=bb.upper;
		upper=new Vector3(b.x+margin,b.y+margin,b.z+margin);
	}
	
	/**
	 * Creates a boundbox with a margin around a single point.
	 * Sufficient to bound a 
	 */
	public BoundBox(Vector3 a, double margin) {
		lower=new Vector3(a.x-margin,a.y-margin,a.z-margin);
		upper=new Vector3(a.x+margin,a.y+margin,a.z+margin);
	}
	
	/**
	 * Create a BoundBox including a Single point
	 */
	public BoundBox(AVector a) {
		if (a instanceof Vector3) {
			Vector3 v=(Vector3)a;
			lower=v.clone();
			upper=v.clone();
		} else {
			double x=a.get(0);
			double y=a.get(1);
			double z=a.get(2);
			lower=new Vector3(x,y,z);
			upper=new Vector3(x,y,z);
		}
	}
	
	public BoundBox(AVector a, AVector b) {
		this(a);
		include(b);
	}
	
	public void include(AVector a) {
		if (a instanceof Vector3) {
			include((Vector3) a);
			return;
		}
		include(a.get(0),a.get(1),a.get(2));
	}
	
	public void include(Vector3 a) {
		include(a.x,a.y,a.z);
	}

	public void include(double x, double y, double z) {
		if (x<lower.x) {lower.x=x;} 
		if (y<lower.y) {lower.y=y;} 
		if (z<lower.z) {lower.z=z;} 
		if (x>upper.x) {upper.x=x;}
		if (y>upper.y) {upper.y=y;}
		if (z>upper.z) {upper.z=z;}
	}
	
	public void include(BoundBox bb) {
		include(bb.upper);
		include(bb.lower);
	}
	
	public void setToPoint(Vector3 a) {
		lower.x=upper.x=a.x;
		lower.y=upper.y=a.y;
		lower.z=upper.z=a.z;
	}
	
	public BoundBox clone() {
		return new BoundBox(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof BoundBox) return equals((BoundBox) o);
		return false;
	}
	
	public boolean equals(BoundBox b) {
		return lower.equals(b.lower)&&upper.equals(b.upper);
	}

	public boolean contains(double x, double y, double z) {
		return	(x>=lower.x)&&(y>=lower.y)&&(z>=lower.z)
				&&(x<=upper.x)&&(y<=upper.y)&&(z<=upper.z);
	}
	
	public boolean contains(BoundBox bb) {
		return	contains(bb.lower)&&contains(bb.upper);
	}

	private boolean contains(Vector3 a) {
		return contains(a.x,a.y,a.z);
	}

	@Override
	public int hashCode() {
		return (31*lower.hashCode())+upper.hashCode();
	}
}