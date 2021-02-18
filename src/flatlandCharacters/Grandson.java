package flatlandCharacters;
import simonGraphics.PointD;
import simonGraphics.Vector;
import flatlandGame.GameVars;

/**
 * Grandson of a Square -- a hexagon
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 *
 */
public class Grandson extends Man {

	public Grandson(String name, PointD center) {
		super("Grandson", 6, center, 50);
		this.velocity = new Vector(10,0);
		this.angularVelocity = 1.0;
	}
	
	/**
	 * Grandson's messages to aSquare for various situations
	 */
	public void interact(Person withWhom){
		if(!GameVars.hasWifeGivenMission)
			withWhom.sendMsg(new Msg(this, "Hey grandad, I found this cool number:\n 3.14159265359548124856328462268\n41348562184318568715843058546\n518643518418645186454848"));
		else
			withWhom.sendMsg(new Msg(this, "Where's my brother?!?!"));
	}

}
