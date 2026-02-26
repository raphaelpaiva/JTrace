package org.jtrace.examples.io;

import org.jtrace.Scene;
import org.jtrace.examples.swing.App;
import org.jtrace.io.SceneIO;

public class YamlExample {

	public static void main(String[] args) {
	  Scene scene = App.createScene();
	  
	  SceneIO io = new SceneIO();
	  System.out.println(io.yaml().dump(scene));
  }
	
}
