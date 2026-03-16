package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jtrace.geometry.TriangleMesh;
import org.jtrace.io.PlyReader;
import org.jtrace.material.Material;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TriangleMeshDeserializer extends JsonDeserializer<TriangleMesh> {

    private final Path basePath;

    public TriangleMeshDeserializer() {
        this.basePath = null;
    }

    public TriangleMeshDeserializer(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public TriangleMesh deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();

        String path = node.has("path") ? node.get("path").asText() : null;
        if (path == null || path.isEmpty()) {
            throw new IOException("TriangleMesh: 'path' is required");
        }

        Material material = null;
        if (node.has("material")) {
            JsonNode materialNode = node.get("material");
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            material = mapper.treeToValue(materialNode, Material.class);
        }

        InputStream is = resolveInputStream(path);

        if (is == null) {
            throw new IOException("TriangleMesh: could not find file: " + path);
        }

        return PlyReader.read(is, material);
    }

    private InputStream resolveInputStream(String path) throws IOException {
        if (basePath != null) {
            Path resolvedPath = basePath.resolve(path);
            File resolvedFile = resolvedPath.toFile();
            if (resolvedFile.exists()) {
                return Files.newInputStream(resolvedPath);
            }
        }

        File absoluteFile = new File(path);
        if (absoluteFile.exists()) {
            return Files.newInputStream(absoluteFile.toPath());
        }

        InputStream classpathStream = getClass().getResourceAsStream("/" + path);
        if (classpathStream != null) {
            return classpathStream;
        }

        return getClass().getResourceAsStream(path);
    }
}
