package org.jtrace.geometry;

import java.util.List;

import org.jtrace.tracer.Hit;
import org.jtrace.Jay;
import org.jtrace.material.Material;
import org.jtrace.tracer.NotHit;
import org.jtrace.Scene;
import org.jtrace.Section;
import org.jtrace.primitives.ColorRGB;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract class to be inserted in a {@link Scene}. <br>
 * 
 * All child classes must implement the {@link #hit(Jay)} method.
 * 
 * @author raphaelpaiva
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Sphere.class, name = "Sphere"),
    @JsonSubTypes.Type(value = Plane.class, name = "Plane"),
    @JsonSubTypes.Type(value = Triangle.class, name = "Triangle"),
    @JsonSubTypes.Type(value = TriangleMesh.class, name = "TriangleMesh"),
    @JsonSubTypes.Type(value = Quadrilateral.class, name = "Quadrilateral")
})
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
	
	public ColorRGB getColor(Hit hit) {
		return material.getColor(hit);
	}

}
