package org.jtrace.material;

import java.awt.image.BufferedImage;

import org.jtrace.tracer.Hit;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

/**
 * Class representing a Material with physical properties such as
 * {@link ReflectanceCoefficient} and texture information.
 * 
 * A Material describes a {@link GeometricObject}'s interaction with light and
 * thus it's color properties in a given point.
 * 
 * @author raphaelpaiva
 * 
 */
public class Material {

    private static ColorRGB DEFAULT_COLOR = ColorRGB.WHITE;
    
    /**
     * The {@link Material}'s basic color. Used when no texture is defined.
     */
    private ColorRGB color;

    /**
     * How much ambient light is reflected.
     * 
     * @see ReflectanceCoefficient
     */
    private ReflectanceCoefficient kAmbient;
    
    /**
     * How much diffuse light is reflected.
     * 
     * @see ReflectanceCoefficient
     */
    private ReflectanceCoefficient kDiffuse;
    
    /**
     * How much specular light is reflected.
     * 
     * @see ReflectanceCoefficient
     */
    private ReflectanceCoefficient kSpecular;

    /**
     * A {@link BufferedImage} representing the texture to be applied to the
     * object with this {@link Material}.
     */
    private BufferedImage texture;
    
    /**
     * Path to the texture file (used for YAML deserialization).
     */
    private String texturePath;

    private double textureScale = 1.0d; // TODO: implement texture scaling in YAML configuration

    private TextureMapper textureMapper = TextureMappers.SPHERICAL;

    /**
     * Default constructor for Jackson deserialization.
     */
    public Material() {
    }

    /**
     * Creates a {@link Material} with static color, no texture and no specular reflection.
     * 
     * Good for matte materials.
     * 
     * @param color the {@link Material}'s base color.
     * @param kAmbient how much ambient light is reflected.
     * @param kDiffuse how much diffuse light is reflected.
     */
    public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse) {
        this.color = color;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
    }
    
    /**
     * Creates a {@link Material} with texture and no specular reflection.
     * 
     * Good for textured matte materials.
     * 
     * @param kAmbient how much ambient light is reflected.
     * @param kDiffuse how much diffuse light is reflected.
     * @param texture a {@link BufferedImage} representing the {@link Material}'s texture.
     */
    public Material(ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, BufferedImage texture) {
        this.color = DEFAULT_COLOR;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
        this.texture = texture;
    }
    
    /**
     * Creates a {@link Material} with static color and no texture.
     * 
     * Good for glossy materials.
     * 
     * @param color the {@link Material}'s base color.
     * @param kAmbient how much ambient light is reflected.
     * @param kDiffuse how much diffuse light is reflected.
     * @param kSpecular how much specular light is reflected.
     */
    public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, ReflectanceCoefficient kSpecular) {
        this.color = color;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
        this.kSpecular = kSpecular;
    }
    
    /**
     * Creates a texturized {@link Material}.
     * 
     * Good for textured glossy materials.
     * 
     * @param kAmbient how much ambient light is reflected.
     * @param kDiffuse how much diffuse light is reflected.
     * @param kSpecular how much specular light is reflected.
     * @param texture a {@link BufferedImage} representing the {@link Material}'s texture.
     */
    public Material(ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, ReflectanceCoefficient kSpecular, BufferedImage texture) {
        this.color = DEFAULT_COLOR;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
        this.kSpecular = kSpecular;
        this.texture = texture;
        this.textureMapper = new SphericalTextureMapper();
    }

  /**
     * Calculates the {@link ColorRGB} of the {@link Material}, given the
     * texture Coordinates u and v and the {@link Material}'s Texture.
     * 
     * @param hit the {@link Hit} containing the intersection information, including the point of intersection and
   *            the normal vector, which are used to calculate the texture coordinates.
     * @return the {@link ColorRGB} of the {@link Material} in the given u and v
     *         parameters
     * 
     * @see <a href="https://en.wikipedia.org/wiki/UV_Mapping">https://en.wikipedia.org/wiki/UV_Mapping</a>
     */
    public ColorRGB getColor(Hit hit) {
        if (texture == null) {
            return getColor();
        }

        UVMapping uvMapping = this.textureMapper.map(hit);

        double u = uvMapping.u() / textureScale;
        double v = uvMapping.v() / textureScale;

        double uWrapped = u - Math.floor(u);
        double vWrapped = v - Math.floor(v);

        int x = (int) (uWrapped * texture.getWidth());
        int y = (int) (vWrapped * texture.getHeight());

        x = Math.min(x, texture.getWidth() - 1);
        y = Math.min(y, texture.getHeight() - 1);

        int intColor = texture.getRGB(x, y);
        return new ColorRGB(intColor);
    }

    /**
     * @return the {@link Material}'s base color.
     */
    public ColorRGB getColor() {
        return color;
    }

    public ReflectanceCoefficient getkAmbient() {
        return kAmbient;
    }

    public ReflectanceCoefficient getkDiffuse() {
        return kDiffuse;
    }

    public ReflectanceCoefficient getkSpecular() {
        return kSpecular;
    }

    public void setkSpecular(ReflectanceCoefficient kSpecular) {
        this.kSpecular = kSpecular;
    }

    /**
     * Sets the {@link Material}'s base color.
     * 
     * @param color the color to set
     */
    public void setColor(ColorRGB color) {
        this.color = color;
    }

    /**
     * Sets the ambient reflectance coefficient.
     * 
     * @param kAmbient the ambient reflectance to set
     */
    public void setkAmbient(ReflectanceCoefficient kAmbient) {
        this.kAmbient = kAmbient;
    }

    /**
     * Sets the diffuse reflectance coefficient.
     * 
     * @param kDiffuse the diffuse reflectance to set
     */
    public void setkDiffuse(ReflectanceCoefficient kDiffuse) {
        this.kDiffuse = kDiffuse;
    }

    /**
     * Sets the texture for this material.
     * 
     * @param texture the texture to set
     */
    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }
    
    public BufferedImage getTexture() {
        return texture;
    }
    
    public String getTexturePath() {
        return texturePath;
    }
    
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

  public double getTextureScale() {
    return textureScale;
  }

  public void setTextureScale(double textureScale) {
    this.textureScale = textureScale;
  }

  public TextureMapper getTextureMapper() {
    return textureMapper;
  }

  public void setTextureMapper(TextureMapper textureMapper) {
    this.textureMapper = textureMapper;
  }
}
