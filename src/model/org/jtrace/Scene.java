package org.jtrace;

import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.ColorRGB;

public class Scene implements Iterable<GeometricObject> {
	private Set<GeometricObject> objects = new LinkedHashSet<GeometricObject>();
	private ColorRGB backgroundColor = ColorRGB.BLACK;

	public Scene withBackground(ColorRGB color) {
		backgroundColor = color;
		return this;
	}
	
	public Scene add(GeometricObject object) {
		objects.add(object);
		return this;
	}
	
	public Scene add(GeometricObject... paramObjects) {
		objects.addAll(asList(paramObjects));
		return this;
	}

	@Override
	public Iterator<GeometricObject> iterator() {
		return objects.iterator();
	}

	public ColorRGB getBackgroundColor() {
		return backgroundColor;
	}
	
}
