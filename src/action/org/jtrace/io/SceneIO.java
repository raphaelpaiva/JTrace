package org.jtrace.io;

import org.jtrace.io.YamlHelper.JTraceConstructor;
import org.jtrace.io.YamlHelper.JTraceRepresenter;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class SceneIO {

	private Yaml yaml;
	
	public SceneIO() {
		createYaml();
	}

	private void createYaml() {
		Representer representer = new JTraceRepresenter();
		representer.addClassTag(Point3D.class, new Tag("!pt"));
		representer.addClassTag(ColorRGB.class, new Tag("!color"));
		
		Constructor constructor = new JTraceConstructor();
		constructor.addTypeDescription(new TypeDescription(Point3D.class, new Tag("!pt")));
		constructor.addTypeDescription(new TypeDescription(ColorRGB.class, new Tag("!color")));
		
		yaml = new Yaml(constructor, representer);
		yaml.setBeanAccess(BeanAccess.FIELD);
	}
	
	public Yaml yaml() {
		return yaml;
	}
}