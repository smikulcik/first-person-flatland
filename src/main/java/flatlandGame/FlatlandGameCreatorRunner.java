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
		//uncomment for menu at top for interactive mode
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
