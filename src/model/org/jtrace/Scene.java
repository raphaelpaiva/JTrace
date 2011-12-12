package org.jtrace;

import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jtrace.geometry.GeometricObject;

public class Scene implements Iterable<GeometricObject> {
	private Set<GeometricObject> objects = new LinkedHashSet<GeometricObject>();

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

}
