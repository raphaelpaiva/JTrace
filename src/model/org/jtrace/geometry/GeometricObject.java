package org.jtrace.geometry;

import java.util.List;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
import org.jtrace.Scene;
import org.jtrace.Section;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

/**
 * Abstract class to be inserted in a {@link Scene}. <br>
 * 
 * All child classes must implement the {@link #hit(Jay)} method.
 * 
 * @author raphaelpaiva
 *
 */
public abstract class GeometricObject {
	
	/**
	 * The {@link GeometricObject}'s {@link Material}.
	 */
	private Material material;
	
	/**
	 * Basic constructor. Must be called by the child classe's constructor.
	 * 
	 * @param material the {@link GeometricObject}'s {@link Material}.
	 */
	public GeometricObject(Material material) {
		this.material = material;
	}

	/**
	 * This method must be implemented in order to the {@link GeometricObject} to be rendered.<br>
	 * 
	 * It must check the collision between the {@link GeometricObject} and a {@link Jay}.
	 * 
	 * @param jay the {@link Jay} being casted.
	 * @return a {@link Hit} object containing information about the collision. Or {@link NotHit} if there was no collision.
	 * @see {@link Hit}.
	 */
	public abstract Hit hit(Jay jay);
	
	/**
	 * This method is used to check the {@link Section} that the passed {@link Jay} goes through.
	 * 
	 * 
	 * @param jay
	 * @return
	 * 
	 * @see Section
	 */
	public abstract List<Section> sections(Jay jay);
	
	public Material getMaterial() {
		return material;
	}
	
	public ColorRGB getColor(Point3D hitPoint) { 
		return material.getColor();
	}

}
