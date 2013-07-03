package org.jtrace.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jtrace.Material;
import org.jtrace.geometry.Triangle;
import org.jtrace.geometry.TriangleMesh;
import org.jtrace.primitives.Point3D;
import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReaderFile;
import org.smurn.jply.util.NormalMode;
import org.smurn.jply.util.NormalizingPlyReader;
import org.smurn.jply.util.TesselationMode;
import org.smurn.jply.util.TextureMode;

public class PlyReader {

    public static TriangleMesh read(InputStream is, Material material) throws IOException {

        org.smurn.jply.PlyReader plyReader = new PlyReaderFile(is);

        plyReader = new NormalizingPlyReader(plyReader, TesselationMode.TRIANGLES,
                NormalMode.PASS_THROUGH,
                TextureMode.PASS_THROUGH);

        ElementReader elementReader = plyReader.nextElementReader();

        List<Point3D>  vertices  = null;
        TriangleMesh triangles = null;

        while (elementReader != null) {
            if (elementReader.getElementType().getName().equals("vertex")) {
                vertices = readVertices(elementReader);
            }

            if (elementReader.getElementType().getName().equals("face")) {
                triangles = readTriangles(elementReader, vertices, material);
            }

            elementReader = plyReader.nextElementReader();
        }

        return triangles;
    }

    private static TriangleMesh readTriangles(ElementReader elementReader, List<Point3D> vertices, Material material) throws IOException {
        Element faceElement = elementReader.readElement();

        TriangleMesh mesh = new TriangleMesh(material);

        while (faceElement != null) {
            int[] indexes = faceElement.getIntList("vertex_index");

            if (indexes.length == 3) {
                int a = indexes[0];
                int b = indexes[1];
                int c = indexes[2];

                mesh.add(new Triangle(vertices.get(a), vertices.get(b), vertices.get(c), material));
            }

            faceElement = elementReader.readElement();
        }

        return mesh;
    }

    private static List<Point3D> readVertices(ElementReader elementReader) throws IOException {
        List<Point3D> vertices = new ArrayList<Point3D>(elementReader.getCount());

        Element vertexElement = elementReader.readElement();

        while (vertexElement != null) {

            Point3D vertex = new Point3D(vertexElement.getDouble("x"), vertexElement.getDouble("y"), vertexElement.getDouble("z"));

            vertices.add(vertex);

            vertexElement = elementReader.readElement();
        }

        return vertices;
    }
}
