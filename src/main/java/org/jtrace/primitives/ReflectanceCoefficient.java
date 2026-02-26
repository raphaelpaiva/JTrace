package org.jtrace.primitives;

public class ReflectanceCoefficient {
	
	private double red;
	
	private double green;
	
	private double blue;

	private ReflectanceCoefficient() { }
	
	public ReflectanceCoefficient(double red, double green, double blue) {
		this();
		
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public double getRed() {
		return red;
	}

	public double getGreen() {
		return green;
	}

	public double getBlue() {
		return blue;
	}

	@Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  long temp;
	  temp = Double.doubleToLongBits(blue);
	  result = prime * result + (int) (temp ^ (temp >>> 32));
	  temp = Double.doubleToLongBits(green);
	  result = prime * result + (int) (temp ^ (temp >>> 32));
	  temp = Double.doubleToLongBits(red);
	  result = prime * result + (int) (temp ^ (temp >>> 32));
	  return result;
  }

	@Override
  public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  ReflectanceCoefficient other = (ReflectanceCoefficient) obj;
	  if (Double.doubleToLongBits(blue) != Double.doubleToLongBits(other.blue))
		  return false;
	  if (Double.doubleToLongBits(green) != Double.doubleToLongBits(other.green))
		  return false;
	  if (Double.doubleToLongBits(red) != Double.doubleToLongBits(other.red))
		  return false;
	  return true;
  }
	
	
	
}