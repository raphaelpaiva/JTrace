package org.jtrace.io;

import java.util.HashMap;
import java.util.Map;

import org.jtrace.primitives.Vector3D;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YamlHelper {

	static class JTraceConstructor extends Constructor {

		public JTraceConstructor() {
			this.yamlConstructors.put(new Tag("!vector"), new ConstructVector3D());
		}

		private class ConstructVector3D extends AbstractConstruct {
			public Object construct(Node node) {
				Map map = constructMapping((MappingNode) node);
				
				double x = (Double) map.get("x");
				double y = (Double) map.get("y");
				double z = (Double) map.get("z");
				
				return new Vector3D(x, y, z);
			}
		}
	}
	
	static class JTraceRepresenter extends Representer {
		public JTraceRepresenter() {
			this.representers.put(Vector3D.class, new RepresentVector3D());
		}

		private class RepresentVector3D implements Represent {
			public Node representData(Object data) {
				Vector3D vec = (Vector3D) data;
				
				Map<String, Double> map = new HashMap<String, Double>();
				map.put("x", vec.getX());
				map.put("y", vec.getY());
				map.put("z", vec.getZ());
				
				return representMapping(new Tag("!vector"), (Map) map, Boolean.TRUE);
			}
		}
	}

}
