package org.jtrace;

import java.awt.image.BufferedImage;

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
    }

    /**
     * Calculates the {@link ColorRGB} of the {@link Material}, given the
     * texture Coordinates u and v and the {@link Material}'s Texture.
     * 
     * @param u
     *            the longitudinal texture coordinate.
     * @param v
     *            the latitudinal texture coordinate.
     * @return the {@link ColorRGB} of the {@link Material} in the given u and v
     *         parameters
     * 
     * @see <a href="https://en.wikipedia.org/wiki/UV_Mapping">https://en.wikipedia.org/wiki/UV_Mapping</a>
     */
    public ColorRGB getColor(double u, double v) {
        if (texture == null) {
            return getColor();
        }
 
        int x = (int) Math.round((texture.getWidth() - 1) * u);
        int y = (int) Math.round((texture.getHeight() - 1) * v);
 
        int intColor = texture.getRGB(x, y);
 
        ColorRGB color = new ColorRGB(intColor);
 
        return color;
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

}
