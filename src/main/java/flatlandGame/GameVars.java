package flatlandGame;
import java.util.ArrayList;

import flatlandCharacters.Avatar;
import flatlandCharacters.Person;

/**
 * Global game variables to keep them organized
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class GameVars {
	public static boolean isRunning = true;//keeps game loop running
	
	//vars for the plotline
	public static boolean hasFoundSonYet = false;
	public static boolean hasWifeGivenMission = false;
	
	//reference to people in scene
	public static ArrayList<Person> people;//people who are not aSquare
	public static Avatar aSquare;
}
