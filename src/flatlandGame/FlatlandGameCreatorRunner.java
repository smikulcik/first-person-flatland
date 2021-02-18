package flatlandGame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Runner class for the Flatland Game.
 * 
 * @author Simon Mikulcik
 *
 */


//Raytracing http://www.cs.utah.edu/~shirley/books/fcg2/rt.pdf
//More on Raytracing http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-1-writing-a-simple-raytracer/implementing-the-raytracing-algorithm/
//Bounding box intersection http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-box-intersection/
public class FlatlandGameCreatorRunner {

	/**
	 * @param args  not used
	 */
	public static void main(String[] args) {

		FlatlandGameCreator game = new FlatlandGameCreator();
		JFrame frame = new JFrame();
		JButton newPolygon = new JButton("New Object");
		JButton save = new JButton("Save");
		
		JPanel menu = new JPanel();
		menu.add(newPolygon);
		menu.add(save);
		newPolygon.addActionListener(game);
		save.addActionListener(game);
		//frame.getContentPane().add(menu,  BorderLayout.NORTH);
		
		frame.setSize(800,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(game);
		frame.setVisible(true);

		//uncomment for fullscreen
 		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
			
		
		game.start();
	}

}
