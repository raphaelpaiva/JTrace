package org.jtrace.io;

import java.util.HashMap;
import java.util.Map;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YamlHelper {

    private static final String REFLECTANCE_COEFFICIENT_TAG = "!reflect";
    private static final String COLORRGB_TAG = "!color";
    private static final String POINT3D_TAG = "!pt";
    private static final String VECTOR3D_TAG = "!vector";

    static class JTraceConstructor extends Constructor {

        public JTraceConstructor() {
            yamlConstructors.put(new Tag(VECTOR3D_TAG), new ConstructVector3D());

            addTypeDescription(new TypeDescription(Point3D.class, new Tag(POINT3D_TAG)));
            addTypeDescription(new TypeDescription(ColorRGB.class, new Tag(COLORRGB_TAG)));
            addTypeDescription(new TypeDescription(ReflectanceCoefficient.class, new Tag(REFLECTANCE_COEFFICIENT_TAG)));
        }

        private class ConstructVector3D extends AbstractConstruct {
            @SuppressWarnings("rawtypes")
            @Override
            public Object construct(final Node node) {
                final Map map = constructMapping((MappingNode) node);

                final double x = (Double) map.get("x");
                final double y = (Double) map.get("y");
                final double z = (Double) map.get("z");

                return new Vector3D(x, y, z);
            }
        }
    }

    static class JTraceRepresenter extends Representer {
        public JTraceRepresenter() {
            representers.put(Vector3D.class, new RepresentVector3D());

            addClassTag(Point3D.class, new Tag(POINT3D_TAG));
            addClassTag(ColorRGB.class, new Tag(COLORRGB_TAG));
            addClassTag(ReflectanceCoefficient.class, new Tag(REFLECTANCE_COEFFICIENT_TAG));
        }

        private class RepresentVector3D implements Represent {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Node representData(final Object data) {
                final Vector3D vec = (Vector3D) data;

                final Map<String, Double> map = new HashMap<String, Double>();
                map.put("x", vec.getX());
                map.put("y", vec.getY());
                map.put("z", vec.getZ());

                return representMapping(new Tag(VECTOR3D_TAG), (Map) map, Boolean.TRUE);
            }
        }
    }

}
