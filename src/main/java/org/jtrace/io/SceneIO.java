package org.jtrace.io;

import org.jtrace.io.YamlHelper.JTraceConstructor;
import org.jtrace.io.YamlHelper.JTraceRepresenter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

public class SceneIO {

	private Yaml yaml;
	
	public SceneIO() {
		createYaml();
	}

	private void createYaml() {
		Representer representer = new JTraceRepresenter();
		Constructor constructor = new JTraceConstructor();
		
		yaml = new Yaml(constructor, representer);
		yaml.setBeanAccess(BeanAccess.FIELD);
	}
	
	public Yaml yaml() {
		return yaml;
	}
}