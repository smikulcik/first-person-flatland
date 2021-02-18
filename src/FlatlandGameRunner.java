import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

//Raytracing http://www.cs.utah.edu/~shirley/books/fcg2/rt.pdf
//More on Raytracing http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-1-writing-a-simple-raytracer/implementing-the-raytracing-algorithm/
//Bounding box intersection http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-box-intersection/
public class FlatlandGameRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FlatlandGame game = new FlatlandGame();
		JFrame frame = new JFrame();
		frame.setSize(800,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(game);
		
		frame.setVisible(true);

 		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
 		
		game.start();
	}

}
