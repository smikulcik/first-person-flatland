package flatlandCharacters;
import flatlandGame.GameVars;
import simonGraphics.PointD;


/**
 * Wife of a square
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */
public class Wife extends Woman {
	
	public Wife(String name, PointD center, double size) {
		super(name, center, size);
	}

	/**
	 * Gives aSquare the mission in the beginning of the plotline
	 */
	public void interact(Person withWhom){
		if(!GameVars.hasFoundSonYet)
		{
			withWhom.sendMsg(new Msg(this, "Thank goodness you are here! One of our grandsons, \n" +
										"a hexagon, ran away to the council of circles.\n " +
										"Go find him before he gets himself in \n" + 
										"trouble?"));
			GameVars.hasWifeGivenMission = true;
		}
		else
			withWhom.sendMsg(new Msg(this, "I'm so glad you found him!"));
			
	}

}
